/**
 * 
 */
package nts.uk.ctx.bs.employee.infra.repository.classification.affiliate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistory;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.classification.affiliate.BsymtAffClassHistory;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author danpv
 * @author hop.nt
 *
 */
@Stateless
public class JpaAffClassHistoryRepository extends JpaRepository implements AffClassHistoryRepository {

	private static final String GET_BY_EID = "select h from BsymtAffClassHistory h"
			+ " where h.sid = :sid and h.cid = :cid ORDER BY h.startDate";
	
	private static final String GET_BY_EID_DESC = GET_BY_EID + " DESC";

//	private static final String GET_BY_SID_DATE = "select h from BsymtAffClassHistory h"
//			+ " where h.sid = :sid and h.startDate <= :standardDate and h.endDate >= :standardDate";
				
	@Override
	public Optional<DateHistoryItem> getByHistoryId(String historyId) {

		Optional<BsymtAffClassHistory> optionData = this.queryProxy().find(historyId,
				BsymtAffClassHistory.class);
		if (optionData.isPresent()) {
			BsymtAffClassHistory entity = optionData.get();
			return Optional.of(new DateHistoryItem(entity.historyId,
					new DatePeriod(entity.startDate, entity.endDate)));
		}
		return Optional.empty();

	}

	@Override
	@SneakyThrows
	public Optional<DateHistoryItem> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate) {
		
		try (PreparedStatement statement = this.connection().prepareStatement(
				"select * from BSYMT_AFF_CLASS_HISTORY"
				+ " where SID = ?"
				+ " and START_DATE <= ?"
				+ " and END_DATE >= ?")) {
			
			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(standardDate.localDate()));
			statement.setDate(3, Date.valueOf(standardDate.localDate()));
			
			return new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				BsymtAffClassHistory history = new BsymtAffClassHistory();
				history.historyId = rec.getString("HIST_ID");
				history.cid = rec.getString("CID");
				history.sid = rec.getString("SID");
				history.startDate = rec.getGeneralDate("START_DATE");
				history.endDate = rec.getGeneralDate("END_DATE");
				return new DateHistoryItem(
						history.historyId,
						new DatePeriod(history.startDate, history.endDate));
			});
		}
	}


	private Optional<AffClassHistory> toDomain(List<BsymtAffClassHistory> entities) {
		if (entities == null || entities.isEmpty()) {
			return Optional.empty();
		}
		AffClassHistory domain = new AffClassHistory(entities.get(0).cid, entities.get(0).sid,
				new ArrayList<DateHistoryItem>());
		entities.forEach(entity -> {
			DateHistoryItem dateItem = new DateHistoryItem(entity.historyId,
					new DatePeriod(entity.startDate, entity.endDate));
			domain.getPeriods().add(dateItem);
		});
		return Optional.of(domain);
	}

	@Override
	public Optional<AffClassHistory> getByEmployeeId(String cid, String employeeId) {
		List<BsymtAffClassHistory> entities = this.queryProxy().query(GET_BY_EID, BsymtAffClassHistory.class)
				.setParameter("sid", employeeId).setParameter("cid", cid).getList();
		return toDomain(entities);
	}

	@Override
	public Optional<AffClassHistory> getByEmployeeIdDesc(String cid, String employeeId) {
		List<BsymtAffClassHistory> entities = this.queryProxy()
				.query(GET_BY_EID_DESC, BsymtAffClassHistory.class).setParameter("sid", employeeId)
				.setParameter("cid", cid).getList();
		return toDomain(entities);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AffClassHistory> getByEmployeeListWithPeriod(List<String> employeeIds, DatePeriod period) {
		if (employeeIds.isEmpty()) {
			return new ArrayList<>();
		}
		List<DateHistoryItemTemp> result = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = "SELECT * FROM BSYMT_AFF_CLASS_HISTORY with(index(BSYMI_AFF_CLASS_HISTORY)) " 
					+ " WHERE START_DATE <= ?"
					+ " AND END_DATE >= ?" 
					+ " AND SID IN (" + NtsStatement.In.createParamsString(subList) + ")"
					+ " ORDER BY SID, START_DATE";
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				stmt.setDate(1, Date.valueOf(period.end().localDate()));
				stmt.setDate(2, Date.valueOf(period.start().localDate()));
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(3 + i, subList.get(i));
				}
				List<DateHistoryItemTemp> lstObj = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					BsymtAffClassHistory history = new BsymtAffClassHistory();
					history.historyId = rec.getString("HIST_ID");
					history.cid = rec.getString("CID");
					history.sid = rec.getString("SID");
					history.startDate = rec.getGeneralDate("START_DATE");
					history.endDate = rec.getGeneralDate("END_DATE");
					return new DateHistoryItemTemp(
							history.cid,
							history.sid,
							new DateHistoryItem(history.historyId, new DatePeriod(history.startDate, history.endDate))
							);
				}).stream().collect(Collectors.toList());
				result.addAll(lstObj);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		
		Map<String, List<DateHistoryItemTemp>> entitiesByEmployee = result.stream()
				.collect(Collectors.groupingBy(DateHistoryItemTemp::getEmployeeId));
		
		String companyId = AppContexts.user().companyId();
		List<AffClassHistory> resultList = new ArrayList<>();
		entitiesByEmployee.forEach((employeeId, entitiesOfEmp) -> {
			List<DateHistoryItem> historyItems = entitiesOfEmp.stream().map(e -> e.getDateItem()).collect(Collectors.toList());
			resultList.add(new AffClassHistory(companyId, employeeId, historyItems));
		});

		return resultList;
		
	}
	
	@Override
	public void add(String cid, String sid, DateHistoryItem itemToBeAdded) {
		BsymtAffClassHistory entity = new BsymtAffClassHistory(itemToBeAdded.identifier(), cid, sid,
				itemToBeAdded.start(), itemToBeAdded.end());
		this.commandProxy().insert(entity);
	}

	@Override
	public void update(DateHistoryItem item) {
		Optional<BsymtAffClassHistory> historyItemOpt = this.queryProxy().find(item.identifier(),
				BsymtAffClassHistory.class);
		if (!historyItemOpt.isPresent()) {
			throw new RuntimeException("Invalid KmnmtAffClassHistory");
		}
		BsymtAffClassHistory entity = historyItemOpt.get();
		entity.startDate = item.start();
		entity.endDate = item.end();
		this.commandProxy().update(entity);

	}

	@Override
	public void delete(String histId) {
		Optional<BsymtAffClassHistory> existItem = this.queryProxy().find(histId, BsymtAffClassHistory.class);
		if (!existItem.isPresent()) {
			throw new RuntimeException("Invalid KmnmtAffClassHistory");
		}
		this.commandProxy().remove(BsymtAffClassHistory.class, histId);
	}
	
	@Data
	@AllArgsConstructor
	class DateHistoryItemTemp {
		private String companyId;
		private String employeeId;
		private DateHistoryItem dateItem;
	}

}
