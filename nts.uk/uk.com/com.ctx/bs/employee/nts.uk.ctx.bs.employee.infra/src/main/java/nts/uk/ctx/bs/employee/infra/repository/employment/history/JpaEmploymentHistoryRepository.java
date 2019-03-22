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
import nts.arc.time.GeneralDateTime;
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
			lstEmpHist.addAll(this.queryProxy().query(SELECT_BY_LISTSID, BsymtEmploymentHist.class)
					.setParameter("listSid", subList).setParameter("start", datePeriod.start())
					.setParameter("end", datePeriod.end()).getList());
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
		List<DateHistItem> lst =  this.queryProxy().query(GET_BY_LSTSID_DATE, BsymtEmploymentHist.class)
				.setParameter("lstSID", lstSID)
				.setParameter("date", date)
				.getList(c -> new DateHistItem(c.sid, c.hisId, new DatePeriod(c.strDate, c.endDate)));
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

	@Override
	public List<DateHistoryItem> getByEmployeeIdAndStandardDate(String cid, List<String> sids,
			GeneralDate standardDate) {
		List<DateHistoryItem> result = new ArrayList<>();
		CollectionUtil.split(sids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = "SELECT * FROM BSYMT_EMPLOYMENT_HIST WHERE CID =? and START_DATE < ? AND END_DATE > ? AND SID IN ( "+ NtsStatement.In.createParamsString(subList)+")"
					;
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				GeneralDate baseDate = standardDate.addDays(1);
				stmt.setString(1, cid);
				stmt.setDate(2, Date.valueOf(baseDate.localDate()));
				stmt.setDate(3, Date.valueOf(baseDate.localDate()));
				for (int i = 0 ; i < subList.size(); i++) {
					stmt.setString( 4 + i, subList.get(i));
				}

				new NtsResultSet(stmt.executeQuery()).forEach(rec -> {
					result.add(new DateHistoryItem(rec.getString("HIST_ID"), new DatePeriod(rec.getGeneralDate("START_DATE"),  rec.getGeneralDate("END_DATE"))));
				});
				
				
			}catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		});
		return result;
	}

	@Override
	public List<EmploymentHistory> getAllByCidAndSids(String cid, List<String> sids) {
		List<BsymtEmploymentHist> entities = new ArrayList<>();
		CollectionUtil.split(sids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = "SELECT * FROM BSYMT_EMPLOYMENT_HIST WHERE CID = ? AND SID IN ("
					+ NtsStatement.In.createParamsString(subList) + ")" + " ORDER BY START_DATE";

			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, cid);
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(2 + i, subList.get(i));
				}

				entities.addAll(new NtsResultSet(stmt.executeQuery()).getList(r -> {
					return new BsymtEmploymentHist(r.getString("HIST_ID"), r.getString("CID"), r.getString("SID"),
							r.getGeneralDate("START_DATE"), r.getGeneralDate("END_DATE"));
				}));

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		Map<String, List<BsymtEmploymentHist>> employmentHistMap = entities.parallelStream().collect(Collectors.groupingBy(c -> c.sid));
		List<EmploymentHistory> result = employmentHistMap.entrySet().parallelStream().map(c -> {return toEmploymentHistory(c.getValue());}).collect(Collectors.toList());
		return result;
	}

	@Override
	public void addAll(List<EmploymentHistory> employmentHistories) {
		String INS_SQL = "INSERT INTO BSYMT_EMPLOYMENT_HIST (INS_DATE, INS_CCD , INS_SCD , INS_PG,"
				+ " UPD_DATE , UPD_CCD , UPD_SCD , UPD_PG," 
				+ " HIST_ID, CID, SID,"
				+ " START_DATE, END_DATE)"
				+ " VALUES (INS_DATE_VAL, INS_CCD_VAL, INS_SCD_VAL, INS_PG_VAL,"
				+ " UPD_DATE_VAL, UPD_CCD_VAL, UPD_SCD_VAL, UPD_PG_VAL,"
				+ " HIST_ID_VAL, CID_VAL, SID_VAL, START_DATE_VAL, END_DATE_VAL); ";
		GeneralDateTime insertTime = GeneralDateTime.now();
		String insCcd = AppContexts.user().companyCode();
		String insScd = AppContexts.user().employeeCode();
		String insPg = AppContexts.programId();
		String updCcd = insCcd;
		String updScd = insScd;
		String updPg = insPg;
		StringBuilder sb = new StringBuilder();
		employmentHistories.parallelStream().forEach(c ->{
			String sql = INS_SQL;
			DateHistoryItem dateHistItem = c.getHistoryItems().get(0);
			sql = sql.replace("INS_DATE_VAL", "'" + insertTime + "'");
			sql = sql.replace("INS_CCD_VAL", "'" + insCcd + "'");
			sql = sql.replace("INS_SCD_VAL", "'" + insScd + "'");
			sql = sql.replace("INS_PG_VAL", "'" + insPg + "'");

			sql = sql.replace("UPD_DATE_VAL", "'" + insertTime + "'");
			sql = sql.replace("UPD_CCD_VAL", "'" + updCcd + "'");
			sql = sql.replace("UPD_SCD_VAL", "'" + updScd + "'");
			sql = sql.replace("UPD_PG_VAL", "'" + updPg + "'");
			
			sql = sql.replace("HIST_ID_VAL", "'" + dateHistItem.identifier() + "'");
			sql = sql.replace("CID_VAL", "'" + c.getCompanyId() + "'");
			sql = sql.replace("SID_VAL", "'" + c.getEmployeeId() + "'");
			sql = sql.replace("START_DATE_VAL", "'" + dateHistItem.start() + "'");
			sql = sql.replace("END_DATE_VAL","'" +  dateHistItem.end() + "'");
			
			sb.append(sql);
		});
		
		int records = this.getEntityManager().createNativeQuery(sb.toString()).executeUpdate();
		System.out.println(records);
		
	}

	@Override
	public void addAll(Map<String, DateHistoryItem> employmentHists) {
		String INS_SQL = "INSERT INTO BSYMT_EMPLOYMENT_HIST (INS_DATE, INS_CCD , INS_SCD , INS_PG,"
				+ " UPD_DATE , UPD_CCD , UPD_SCD , UPD_PG," 
				+ " HIST_ID, CID, SID,"
				+ " START_DATE, END_DATE)"
				+ " VALUES (INS_DATE_VAL, INS_CCD_VAL, INS_SCD_VAL, INS_PG_VAL,"
				+ " UPD_DATE_VAL, UPD_CCD_VAL, UPD_SCD_VAL, UPD_PG_VAL,"
				+ " HIST_ID_VAL, CID_VAL, SID_VAL, START_DATE_VAL, END_DATE_VAL); ";
		String cid = AppContexts.user().companyId();
		String insCcd = AppContexts.user().companyCode();
		String insScd = AppContexts.user().employeeCode();
		String insPg = AppContexts.programId();
		
		String updCcd = insCcd;
		String updScd = insScd;
		String updPg = insPg;
		StringBuilder sb = new StringBuilder();
		employmentHists.entrySet().parallelStream().forEach(c ->{
			String sql = INS_SQL;
			DateHistoryItem dateHistItem = c.getValue();
			sql = sql.replace("INS_DATE_VAL", "'" + GeneralDateTime.now() + "'");
			sql = sql.replace("INS_CCD_VAL", "'" + insCcd + "'");
			sql = sql.replace("INS_SCD_VAL", "'" + insScd + "'");
			sql = sql.replace("INS_PG_VAL", "'" + insPg + "'");

			sql = sql.replace("UPD_DATE_VAL", "'" + GeneralDateTime.now() + "'");
			sql = sql.replace("UPD_CCD_VAL", "'" + updCcd + "'");
			sql = sql.replace("UPD_SCD_VAL", "'" + updScd + "'");
			sql = sql.replace("UPD_PG_VAL", "'" + updPg + "'");
			
			sql = sql.replace("HIST_ID_VAL", "'" + dateHistItem.identifier() + "'");
			sql = sql.replace("CID_VAL", "'" + cid + "'");
			sql = sql.replace("SID_VAL", "'" + c.getKey() + "'");
			sql = sql.replace("START_DATE_VAL", "'" + dateHistItem.start() + "'");
			sql = sql.replace("END_DATE_VAL","'" +  dateHistItem.end() + "'");
			
			sb.append(sql);
		});
		
		int records = this.getEntityManager().createNativeQuery(sb.toString()).executeUpdate();
		System.out.println(records);
		
	}

	@Override
	public void updateAll(List<DateHistoryItem> itemToBeUpdateds) {	
		
		String UP_SQL = "UPDATE BSYMT_EMPLOYMENT_HIST SET UPD_DATE = UPD_DATE_VAL, UPD_CCD = UPD_CCD_VAL, UPD_SCD = UPD_SCD_VAL, UPD_PG = UPD_PG_VAL,"
				+ " START_DATE = START_DATE_VAL, END_DATE = END_DATE_VAL"
				+ " WHERE HIST_ID = HIST_ID_VAL AND CID = CID_VAL;";
		String cid = AppContexts.user().companyId();
		String updCcd = AppContexts.user().companyCode();
		String updScd = AppContexts.user().employeeCode();
		String updPg = AppContexts.programId();
		
		StringBuilder sb = new StringBuilder();
		itemToBeUpdateds.parallelStream().forEach(c ->{
			String sql = UP_SQL;
			sql = UP_SQL.replace("UPD_DATE_VAL", "'" + GeneralDateTime.now() +"'");
			sql = sql.replace("UPD_CCD_VAL", "'" + updCcd +"'");
			sql = sql.replace("UPD_SCD_VAL", "'" + updScd +"'");
			sql = sql.replace("UPD_PG_VAL", "'" + updPg +"'");
			
			sql = sql.replace("START_DATE_VAL", "'" + c.start() + "'");
			sql = sql.replace("END_DATE_VAL","'" +  c.end() + "'");
			
			sql = sql.replace("HIST_ID_VAL", "'" + c.identifier() +"'");
			sql = sql.replace("CID_VAL", "'" + cid +"'");
			sb.append(sql);
		});
		int  records = this.getEntityManager().createNativeQuery(sb.toString()).executeUpdate();
		System.out.println(records);
	}
}
