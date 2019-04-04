package nts.uk.ctx.bs.employee.infra.repository.employment.history;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employment.history.DateHistItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistory;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaEmploymentHistoryRepository extends JpaRepository implements EmploymentHistoryRepository {

	private static final String QUERY_BYEMPLOYEEID = "SELECT ep FROM BsymtEmploymentHist ep"
			+ " WHERE ep.sid = :sid and ep.companyId = :cid ORDER BY ep.strDate";

	private static final String QUERY_BYEMPLOYEEID_DESC = QUERY_BYEMPLOYEEID + " DESC";

//	private static final String GET_BY_EMPID_AND_STD = "SELECT h FROM BsymtEmploymentHist h"
//			+ " WHERE h.sid = :sid AND h.strDate <= :stdDate AND h.endDate >= :stdDate";

	private static final String SELECT_BY_LISTSID = "SELECT a FROM BsymtEmploymentHist a"
			+ " INNER JOIN BsymtEmploymentHistItem b on a.hisId = b.hisId" + " WHERE a.sid IN :listSid "
			+ " AND ( a.strDate <= :end AND a.endDate >= :start ) ";
	private static final String GET_BY_LSTSID_DATE = "SELECT c FROM BsymtEmploymentHist c where c.sid IN :lstSID" 
			+ " AND c.strDate <= :date and c.endDate >= :date";
	
	/**
	 * Convert from BsymtEmploymentHist to domain EmploymentHistory
	 * 
	 * @param sid
	 * @param listHist
	 * @return
	 */
	private EmploymentHistory toEmploymentHistory(List<BsymtEmploymentHist> listHist) {
		EmploymentHistory empment = new EmploymentHistory(listHist.get(0).companyId, listHist.get(0).sid,
				new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BsymtEmploymentHist item : listHist) {
			dateItem = new DateHistoryItem(item.hisId, new DatePeriod(item.strDate, item.endDate));
			empment.getHistoryItems().add(dateItem);
		}
		return empment;
	}

	@Override
	public Optional<EmploymentHistory> getByEmployeeId(String cid, String sid) {
		List<BsymtEmploymentHist> listHist = this.queryProxy().query(QUERY_BYEMPLOYEEID, BsymtEmploymentHist.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toEmploymentHistory(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<EmploymentHistory> getByEmployeeIdDesc(String cid, String sid) {
		List<BsymtEmploymentHist> listHist = this.queryProxy().query(QUERY_BYEMPLOYEEID_DESC, BsymtEmploymentHist.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toEmploymentHistory(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<DateHistoryItem> getByEmployeeIdAndStandardDate(String employeeId, GeneralDate standardDate) {
		try (val statement = this.connection().prepareStatement("select * FROM BSYMT_EMPLOYMENT_HIST where SID = ? and START_DATE <= ? and END_DATE >= ?")) {
			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(standardDate.localDate()));
			statement.setDate(3, Date.valueOf(standardDate.localDate()));
			Optional<BsymtEmploymentHist> optionData = new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				val entity = new BsymtEmploymentHist();
				entity.companyId = rec.getString("CID");
				entity.endDate = rec.getGeneralDate("END_DATE");
				entity.hisId = rec.getString("HIST_ID");
				entity.sid = employeeId;
				entity.strDate = rec.getGeneralDate("START_DATE");
				return entity;
			});
			if (optionData.isPresent()) {
				BsymtEmploymentHist entity = optionData.get();
				return Optional.of(new DateHistoryItem(entity.hisId, new DatePeriod(entity.strDate, entity.endDate)));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	@Override
	public Optional<DateHistoryItem> getByHistoryId(String historyId) {
		Optional<BsymtEmploymentHist> optionData = this.queryProxy().find(historyId, BsymtEmploymentHist.class);
		if (optionData.isPresent()) {
			BsymtEmploymentHist entity = optionData.get();
			return Optional.of(new DateHistoryItem(historyId, new DatePeriod(entity.strDate, entity.endDate)));
		}
		return Optional.empty();
	}

	@Override
	public void add(String sid, DateHistoryItem domain) {
		this.commandProxy().insert(toEntity(sid, domain));
	}

	@Override
	public void update(DateHistoryItem itemToBeUpdated) {
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(itemToBeUpdated.identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtEmploymentHist");
		}
		updateEntity(itemToBeUpdated, histItem.get());
		this.commandProxy().update(histItem.get());

	}

	@Override
	public void delete(String histId) {
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(histId, BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtEmploymentHist");
		}
		this.commandProxy().remove(BsymtEmploymentHist.class, histId);

	}

	/**
	 * Convert from domain to entity
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private BsymtEmploymentHist toEntity(String employeeID, DateHistoryItem item) {
		String companyId = AppContexts.user().companyId();
		return new BsymtEmploymentHist(item.identifier(), companyId, employeeID, item.start(), item.end());
	}

	/**
	 * Update entity from domain
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item, BsymtEmploymentHist entity) {
		entity.strDate = item.start();
		entity.endDate = item.end();
	}

	/**
	 * Update item before when updating or deleting
	 * 
	 * @param domain
	 * @param item
	 */
	@SuppressWarnings("unused")
	private void updateItemBefore(EmploymentHistory domain, DateHistoryItem item) {
		// Update item before
		Optional<DateHistoryItem> beforeItem = domain.immediatelyBefore(item);
		if (!beforeItem.isPresent()) {
			return;
		}
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(beforeItem.get().identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			return;
		}
		updateEntity(beforeItem.get(), histItem.get());
		this.commandProxy().update(histItem.get());
	}

	/**
	 * Update item after when updating or deleting
	 * 
	 * @param domain
	 * @param item
	 */
	@SuppressWarnings("unused")
	private void updateItemAfter(EmploymentHistory domain, DateHistoryItem item) {
		// Update item after
		Optional<DateHistoryItem> aferItem = domain.immediatelyAfter(item);
		if (!aferItem.isPresent()) {
			return;
		}
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(aferItem.get().identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			return;
		}
		updateEntity(aferItem.get(), histItem.get());
		this.commandProxy().update(histItem.get());
	}

	// convert to domain
	private EmploymentHistory toDomain(BsymtEmploymentHist entity) {
		EmploymentHistory domain = new EmploymentHistory(entity.companyId, entity.sid,
				new ArrayList<DateHistoryItem>());
		DateHistoryItem dateItem = new DateHistoryItem(entity.hisId, new DatePeriod(entity.strDate, entity.endDate));
		domain.getHistoryItems().add(dateItem);

		return domain;
	}

	@Override
	public List<EmploymentHistory> getByListSid(List<String> employeeIds, DatePeriod datePeriod) {

		// Split query.
		List<BsymtEmploymentHist> lstEmpHist = new ArrayList<>();

		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			
			String sql = "select * from BSYMT_EMPLOYMENT_HIST h"
					+ " inner join BSYMT_EMPLOYMENT_HIS_ITEM i"
					+ " on h.HIST_ID = i.HIST_ID"
					+ " where h.SID in (" + NtsStatement.In.createParamsString(subList) + ")"
					+ " and h.START_DATE <= ?"
					+ " and h.END_DATE >= ?";
			
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				
				int i = 0;
				for (; i < subList.size(); i++) {
					stmt.setString(1 + i, subList.get(i));
				}

				stmt.setDate(1 + i, Date.valueOf(datePeriod.end().localDate()));
				stmt.setDate(2 + i, Date.valueOf(datePeriod.start().localDate()));
				
				List<BsymtEmploymentHist> ents = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					BsymtEmploymentHist ent = new BsymtEmploymentHist();
					ent.hisId = rec.getString("HIST_ID");
					ent.companyId = rec.getString("CID");
					ent.sid = rec.getString("SID");
					ent.strDate = rec.getGeneralDate("START_DATE");
					ent.endDate = rec.getGeneralDate("END_DATE");
					return ent;
				});
				lstEmpHist.addAll(ents);
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});

		Map<String, List<BsymtEmploymentHist>> map = lstEmpHist.stream().collect(Collectors.groupingBy(x -> x.sid));

		List<EmploymentHistory> result = new ArrayList<>();

		for (Map.Entry<String, List<BsymtEmploymentHist>> info : map.entrySet()) {
			EmploymentHistory empHistory = new EmploymentHistory();
			empHistory.setEmployeeId(info.getKey());
			List<BsymtEmploymentHist> values = info.getValue();
			if (!values.isEmpty()) {
				empHistory.setHistoryItems(
						values.stream().map(x -> new DateHistoryItem(x.hisId, new DatePeriod(x.strDate, x.endDate)))
								.collect(Collectors.toList()));
			} else {
				empHistory.setHistoryItems(new ArrayList<>());
			}
			result.add(empHistory);
		}

		return result;

	}

	@Override
	@SneakyThrows
	public Optional<EmploymentHistory> getEmploymentHistory(String historyId, String employmentCode) {
		try (PreparedStatement statement = this.connection().prepareStatement(
				"SELECT DISTINCT b.* FROM BSYMT_EMPLOYMENT_HIST a INNER JOIN BSYMT_EMPLOYMENT_HIS_ITEM b ON a.HIST_ID = b.HIST_ID"
						  + " WHERE a.HIST_ID = ? AND  b.EMP_CD = ?")) {
			statement.setString(1, historyId);
			statement.setString(2, employmentCode);
			
			Optional<BsymtEmploymentHist>  optionData =  new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				BsymtEmploymentHist employmentHist = new BsymtEmploymentHist();
				employmentHist.companyId = rec.getString("CID");
				employmentHist.sid = rec.getString("SID");
				employmentHist.hisId = rec.getString("HIST_ID");
				employmentHist.endDate = rec.getGeneralDate("END_DATE");
				employmentHist.strDate = rec.getGeneralDate("START_DATE");
				return employmentHist;
			});
			if (optionData.isPresent()) {
				BsymtEmploymentHist entity = optionData.get();
				return Optional.of(toDomain(entity));
			}
		} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	return Optional.empty();
	}

	@Override
	public Map<String, DateHistItem> getBySIdAndate(List<String> lstSID, GeneralDate date) {
		List<DateHistItem> lst = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			lst.addAll(this.queryProxy().query(GET_BY_LSTSID_DATE, BsymtEmploymentHist.class)
					.setParameter("lstSID", splitData)
					.setParameter("date", date)
					.getList(c -> new DateHistItem(c.sid, c.hisId, new DatePeriod(c.strDate, c.endDate))));
		});
		Map<String, DateHistItem> mapResult = new HashMap<>();
		for(String sid : lstSID){
			List<DateHistItem> hist = lst.stream().filter(c -> c.getSid().equals(sid)).collect(Collectors.toList());
			if(hist.isEmpty()){
				continue;
			}
			mapResult.put(sid, hist.get(0));
		}
		return mapResult;
	}
}
