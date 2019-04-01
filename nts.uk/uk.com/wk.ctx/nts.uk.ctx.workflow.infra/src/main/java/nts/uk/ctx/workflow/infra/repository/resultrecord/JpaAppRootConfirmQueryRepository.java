package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.collection.ListHashMap;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootIntermForQuery;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmQueryRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootRecordConfirmForQuery;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaAppRootConfirmQueryRepository
		extends JpaRepository
		implements AppRootConfirmQueryRepository {

	@Override
	public AppRootIntermForQuery.List queryInterm(
			String companyId,
			List<String> employeeIds,
			DatePeriod period,
			RecordRootType rootType) {
		
		List<AppRootIntermForQuery> results = new ArrayList<>();
		
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subEmpIds -> {
			
			String sql = "select r.ROOT_ID, r.EMPLOYEE_ID, r.START_DATE, r.END_DATE, MAX(p.PHASE_ORDER) as FINAL_PHASE_ORDER" 
					+ " from WWFDT_APP_ROOT_INSTANCE r"
					+ " inner join WWFDT_APP_PHASE_INSTANCE p"
					+ " on r.ROOT_ID = p.ROOT_ID"
					+ " where CID = ?"
					+ " and r.ROOT_TYPE = ?"
					+ " and r.EMPLOYEE_ID in (" + NtsStatement.In.createParamsString(subEmpIds) + ")"
					+ " and r.START_DATE <= ?"
					+ " and r.END_DATE >= ?"
					+ " group by r.ROOT_ID, r.EMPLOYEE_ID, r.START_DATE, r.END_DATE";
			
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				
				stmt.setString(1, companyId);
				stmt.setInt(2, rootType.value);
				
				for (int i = 0; i < subEmpIds.size(); i++) {
					stmt.setString(3 + i, subEmpIds.get(i));
				}
				
				stmt.setDate(3 + subEmpIds.size(), Date.valueOf(period.end().localDate()));
				stmt.setDate(4 + subEmpIds.size(), Date.valueOf(period.start().localDate()));
				
				List<AppRootIntermForQuery> subResults = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					
					return new AppRootIntermForQuery(
							rec.getString("ROOT_ID"),
							rec.getString("EMPLOYEE_ID"),
							new DatePeriod(
									rec.getGeneralDate("START_DATE"),
									rec.getGeneralDate("END_DATE")),
							rec.getInt("FINAL_PHASE_ORDER"));
				});
				
				results.addAll(subResults);
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		});
		
		return new AppRootIntermForQuery.List(results);
	}

	@Override
	public AppRootRecordConfirmForQuery.List queryConfirm(
			String companyId,
			List<String> employeeIds,
			DatePeriod period,
			RecordRootType rootType) {

		List<AppRootRecordConfirmForQuery> results = new ArrayList<>();

		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subEmpIds -> {
			
			String sql = "select r.ROOT_ID, r.EMPLOYEE_ID, r.RECORD_DATE, p.PHASE_ORDER, p.APP_PHASE_ATR"
					+ " from WWFDT_APP_ROOT_CONFIRM r"
					+ " left join WWFDT_APP_PHASE_CONFIRM p"
					+ " on r.ROOT_ID = p.ROOT_ID"
					+ " where r.CID = ?"
					+ " and r.ROOT_TYPE = ?"
					+ " and r.EMPLOYEE_ID in (" + NtsStatement.In.createParamsString(subEmpIds) + ")"
					+ " and r.RECORD_DATE between ? and ?";
			
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				
				stmt.setString(1, companyId);
				stmt.setInt(2, rootType.value);

				for (int i = 0; i < subEmpIds.size(); i++) {
					stmt.setString(3 + i, subEmpIds.get(i));
				}
				
				stmt.setDate(3 + subEmpIds.size(), Date.valueOf(period.start().localDate()));
				stmt.setDate(4 + subEmpIds.size(), Date.valueOf(period.end().localDate()));
				
				List<ConfirmRecord> recordList = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					
					ConfirmRecord r = new ConfirmRecord();
					r.rootId = rec.getString("ROOT_ID");
					r.sid = rec.getString("EMPLOYEE_ID");
					r.recordDate = rec.getGeneralDate("RECORD_DATE");
					r.phaseOrder = rec.getInt("PHASE_ORDER");
					r.appPhaseAtr = rec.getInt("APP_PHASE_ATR");
					
					return r;
				});
				
				ConfirmRecord.Set records = new ConfirmRecord.Set(recordList);
				results.addAll(records.aggregate());
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		
		return new AppRootRecordConfirmForQuery.List(results);
	}


	private static class ConfirmRecord {
		String rootId;
		String sid;
		GeneralDate recordDate;
		Integer phaseOrder;
		Integer appPhaseAtr;
		
		static class Set {
			
			private final Map<String, ListHashMap<GeneralDate, ConfirmRecord>> map;
			
			public Set(List<ConfirmRecord> records) {
				
				this.map = new HashMap<>();
				
				for (val record : records) {
					
					if (!map.containsKey(record.sid)) {
						map.put(record.sid, new ListHashMap<>());
					}
					
					val mapForOnePerson = map.get(record.sid);
					mapForOnePerson.addElement(record.recordDate, record);
				}
			}
			
			public List<AppRootRecordConfirmForQuery> aggregate() {
				
				val confirms = new ArrayList<AppRootRecordConfirmForQuery>();
				
				for (val esPerson : map.entrySet()) {
					String employeeId = esPerson.getKey();
					val mapForOnePerson = esPerson.getValue();
					
					for (val esDate : mapForOnePerson.entrySet()) {
						GeneralDate date = esDate.getKey();
						val records = esDate.getValue();
						
						Integer finalConfirmedPhase = records.stream()
								.filter(r -> r.appPhaseAtr != null && r.appPhaseAtr == 1)
								// appPhaseAtrがnullでないなら、phaseOrderもnullではないので、nullチェック不要
								.max((a, b) -> a.phaseOrder.compareTo(b.phaseOrder))
								.map(r -> r.phaseOrder)
								.orElse(null);
						
						val confirm = new AppRootRecordConfirmForQuery(
								records.get(0).rootId, // 少なくとも1レコードは必ずある
								employeeId,
								date,
								finalConfirmedPhase != null,
								finalConfirmedPhase);
						
						confirms.add(confirm);
					}
				}
				
				return confirms;
				
			}
			
		}
	}
}
