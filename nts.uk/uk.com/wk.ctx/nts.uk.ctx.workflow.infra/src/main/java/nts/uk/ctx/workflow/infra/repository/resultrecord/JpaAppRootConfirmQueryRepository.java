package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.ListHashMap;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootIntermForQuery;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmQueryRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootRecordConfirmForQuery;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaAppRootConfirmQueryRepository extends JpaRepository	implements AppRootConfirmQueryRepository {
	
	private static final String FIND_DAY_INSTANCE 
	= " select rt.ROOT_ID, rt.EMPLOYEE_ID, rt.RECORD_DATE, "
		+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR "
	+ " from WWFDT_DAY_APV_RT_INSTANCE as rt" 
	+ " left join WWFDT_DAY_APV_PH_INSTANCE as ph"
	+ " on rt.ROOT_ID = ph.ROOT_ID" ;

	@Override
	public AppRootIntermForQuery.List queryIntermDaily(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		sql.append(FIND_DAY_INSTANCE);
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");

		List<AppRootIntermForQuery> recordList = NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> {
						return new AppRootIntermForQuery(
							rec.getString("ROOT_ID"),
							rec.getString("EMPLOYEE_ID"),
							new DatePeriod(
								rec.getGeneralDate("START_DATE"),
								rec.getGeneralDate("END_DATE")),
							rec.getInt("FINAL_PHASE_ORDER"));
					});
		});
		return new AppRootIntermForQuery.List(recordList);
	}

	
	private static final String FIND_MON_INSTANCE 
	= " select rt.ROOT_ID, rt.EMPLOYEE_ID, rt.RECORD_DATE, "
		+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR "
	+ " from WWFDT_MON_APV_RT_INSTANCE as rt" 
	+ " left join WWFDT_MON_APV_PH_INSTANCE as ph"
	+ " on rt.ROOT_ID = ph.ROOT_ID" ;
	
	@Override
	public AppRootIntermForQuery.List queryIntermMonthly(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		sql.append(FIND_MON_INSTANCE);
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");

		List<AppRootIntermForQuery> recordList = NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> {
						return new AppRootIntermForQuery(
							rec.getString("ROOT_ID"),
							rec.getString("EMPLOYEE_ID"),
							new DatePeriod(
								rec.getGeneralDate("START_DATE"),
								rec.getGeneralDate("END_DATE")),
							rec.getInt("FINAL_PHASE_ORDER"));
					});
		});
		return new AppRootIntermForQuery.List(recordList);
	}


	private static final String FIND_DAY_CONFIRM 
			= " select rt.ROOT_ID, rt.EMPLOYEE_ID, rt.RECORD_DATE, "
				+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR "
			+ " from WWFDT_DAY_APV_RT_CONFIRM as rt" 
			+ " left join WWFDT_DAY_APV_PH_CONFIRM as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" ;

	@Override
	public AppRootRecordConfirmForQuery.List queryConfirmDaily(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		sql.append(FIND_DAY_CONFIRM);
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.RECORD_DATE between @startDate and @endDate");

		List<ConfirmRecord> recordList = NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> {
						ConfirmRecord r = new ConfirmRecord();
						r.rootId = rec.getString("ROOT_ID");
						r.sid = rec.getString("EMPLOYEE_ID");
						r.recordDate = rec.getGeneralDate("RECORD_DATE");
						r.phaseOrder = rec.getInt("PHASE_ORDER");
						r.appPhaseAtr = rec.getInt("APP_PHASE_ATR");
						return r;
					});			
		});
		ConfirmRecord.Set records = new ConfirmRecord.Set(recordList);
		return new AppRootRecordConfirmForQuery.List(records.aggregate());
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
