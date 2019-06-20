/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.shortworktime;

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
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository;
import nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistory;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK_;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist_;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaSWorkTimeHistoryRepository.
 */
@Stateless
public class JpaSWorkTimeHistoryRepository extends JpaRepository
		implements SWorkTimeHistoryRepository {
	
	private static final String QUERY_GET_BYSID = "SELECT ad FROM BshmtWorktimeHist ad"
			+ " WHERE ad.bshmtWorktimeHistPK.sid = :sid and ad.cId = :cid ORDER BY ad.strYmd";
	
	private static final String QUERY_GET_BYSID_DESC = QUERY_GET_BYSID + " DESC";
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findByKey(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<ShortWorkTimeHistory> findByKey(String empId, String histId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
		Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);

		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.sid),
				empId));
		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.histId),
				histId));

		query.where(predicateList.toArray(new Predicate[] {}));

		List<BshmtWorktimeHist> result = em.createQuery(query).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(result)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findByBaseDate(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<ShortWorkTimeHistory> findByBaseDate(String empId, GeneralDate baseDate) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
		Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);

		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.sid),
				empId));
		predicateList.add(builder.lessThanOrEqualTo(root.get(BshmtWorktimeHist_.strYmd), baseDate));
		predicateList
				.add(builder.greaterThanOrEqualTo(root.get(BshmtWorktimeHist_.endYmd), baseDate));

		query.where(predicateList.toArray(new Predicate[] {}));

		List<BshmtWorktimeHist> result = em.createQuery(query).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(result)));
	}

	@Override
	public Optional<ShortWorkTimeHistory> getBySid(String cid, String sid) {
		List<BshmtWorktimeHist> listHist = this.queryProxy().query(QUERY_GET_BYSID,BshmtWorktimeHist.class)
				.setParameter("sid", sid)
				.setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()){
			return Optional.of(toWorkTime(listHist));
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<ShortWorkTimeHistory> getBySidDesc(String cid, String sid) {
		List<BshmtWorktimeHist> listHist = this.queryProxy().query(QUERY_GET_BYSID_DESC,BshmtWorktimeHist.class)
				.setParameter("sid", sid)
				.setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()){
			return Optional.of(toWorkTime(listHist));
		}
		return Optional.empty();
	}
	
	/**
	 * Convert to domain
	 * @param listHist
	 * @return
	 */
	private ShortWorkTimeHistory toWorkTime(List<BshmtWorktimeHist> listHist){
		ShortWorkTimeHistory affDepart = new ShortWorkTimeHistory(listHist.get(0).getCId(), listHist.get(0).getBshmtWorktimeHistPK().sid, new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BshmtWorktimeHist item : listHist){
			dateItem = new DateHistoryItem(item.getBshmtWorktimeHistPK().getHistId(), new DatePeriod(item.getStrYmd(), item.getEndYmd()));
			affDepart.getHistoryItems().add(dateItem);
		}
		return affDepart;
	}
	
	@Override
	public void add(String cid, String sid, DateHistoryItem histItem) {
		this.commandProxy().insert(toEntity(cid, sid, histItem));
	}

	@Override
	public void update(String sid, DateHistoryItem histItem) {
		BshmtWorktimeHistPK key = new BshmtWorktimeHistPK(sid, histItem.identifier());
		Optional<BshmtWorktimeHist> existItem = this.queryProxy().find(key, BshmtWorktimeHist.class);
		if (!existItem.isPresent()){
			throw new RuntimeException("Invalid BshmtWorktimeHist");
		}
		updateEntity(histItem, existItem.get());
		this.commandProxy().update(existItem.get());
	}

	@Override
	public void delete(String sid, String histId) {
		BshmtWorktimeHistPK key = new BshmtWorktimeHistPK(sid, histId);
		this.commandProxy().remove(BshmtWorktimeHist.class, key);
	}
	/**
	 * Convert from domain to entity
	 * @param cid
	 * @param sid
	 * @param dateItem
	 * @return
	 */
	private BshmtWorktimeHist toEntity(String cid, String sid, DateHistoryItem dateItem){
		BshmtWorktimeHistPK bshmtWorktimeHistPK = new BshmtWorktimeHistPK(sid, dateItem.identifier());
		return new BshmtWorktimeHist(bshmtWorktimeHistPK,cid,dateItem.start(),dateItem.end());
	}
	
	/**
	 * Update entity
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(DateHistoryItem domain,BshmtWorktimeHist entity){
		entity.setStrYmd(domain.start());
		entity.setEndYmd(domain.end());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findByEmpAndPeriod(java.util.List,
	 * nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public Map<String, ShortWorkTimeHistory> findByEmpAndPeriod(List<String> empIdList,
			DatePeriod period) {
		
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
		Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);
		
		List<BshmtWorktimeHist> result = new ArrayList<>();

		CollectionUtil.split(empIdList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<Predicate> predicateList = new ArrayList<>();

			predicateList.add(root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK)
					.get(BshmtWorktimeHistPK_.sid).in(splitData));

			predicateList.add(builder.not(builder.or(
					builder.lessThan(root.get(BshmtWorktimeHist_.endYmd), period.start()),
					builder.greaterThan(root.get(BshmtWorktimeHist_.strYmd), period.end()))));

			query.where(predicateList.toArray(new Predicate[] {}));

			result.addAll(em.createQuery(query).getResultList());
		});
		
		Map<String, List<BshmtWorktimeHist>> mapResult = result.stream()
				.collect(Collectors.groupingBy(item -> item.getBshmtWorktimeHistPK().getSid()));

		// Return
		return mapResult.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),
				e -> new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(e.getValue()))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findLstByEmpAndPeriod(java.util.List,
	 * nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public List<ShortWorkTimeHistory> findLstByEmpAndPeriod(List<String> empIdList,
			DatePeriod period) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
		Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);

		List<BshmtWorktimeHist> result = new ArrayList<>();
		
		CollectionUtil.split(empIdList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK)
					.get(BshmtWorktimeHistPK_.sid).in(splitData));

			predicateList.add(builder.not(
					builder.or(builder.lessThan(root.get(BshmtWorktimeHist_.endYmd), period.start()),
							builder.greaterThan(root.get(BshmtWorktimeHist_.strYmd), period.end()))));

			query.where(predicateList.toArray(new Predicate[] {}));
			result.addAll(em.createQuery(query).getResultList());
		});

		Map<String, List<BshmtWorktimeHist>> mapResult = result.stream()
				.collect(Collectors.groupingBy(item -> item.getBshmtWorktimeHistPK().getSid()));

		// Return
		return mapResult.values().stream()
				.map(e -> new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(e)))
				.collect(Collectors.toList());
	}

	@Override
	@SneakyThrows
	public List<DateHistoryItem> finsLstBySidsAndCidAndDate(String cid, List<String> sids, GeneralDate baseDate) {
		List<DateHistoryItem> result = new ArrayList<>();
		
		CollectionUtil.split(sids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = "SELECT * FROM BSHMT_WORKTIME_HIST WHERE CID = ? AND  STR_YMD <= ? AND END_YMD >= ? AND SID IN ("+ NtsStatement.In.createParamsString(subList) + ")";
		
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, cid);
				stmt.setDate(2, Date.valueOf(baseDate.localDate()));
				stmt.setDate(3, Date.valueOf(baseDate.localDate()));
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(4 + i, subList.get(i));
				}
				
				List<DateHistoryItem> lstObj = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					return new DateHistoryItem(rec.getString("HIST_ID"),  new DatePeriod(rec.getGeneralDate("STR_YMD"),  rec.getGeneralDate("END_YMD")));
				});
				result.addAll(lstObj);
			}catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});

		// Return
		return result;
	}

	@Override
	@SneakyThrows
	public List<ShortWorkTimeHistory> getBySidsAndCid(String cid, List<String> sids) {
		List<ShortWorkTimeHistory> shortWorkTimeHistory = new ArrayList<>();
		CollectionUtil.split(sids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			String sql = "SELECT * FROM BSHMT_WORKTIME_HIST" + " WHERE CID = ?  AND SID IN (" + NtsStatement.In.createParamsString(subList) + ")"
					+" ORDER BY STR_YMD";

			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, cid);
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(2 + i, subList.get(i));
				}

				List<Map<String, Object>> map = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					Map<String, Object> m = new HashMap<>();
					m.put("HIST_ID", rec.getString("HIST_ID"));
					m.put("SID", rec.getString("SID"));
					m.put("CID", rec.getString("CID"));
					m.put("STR_YMD", rec.getGeneralDate("STR_YMD"));
					m.put("END_YMD", rec.getGeneralDate("END_YMD"));
					return m;
				});
				map.stream().collect(Collectors.groupingBy(c -> c.get("SID"),
						Collectors.collectingAndThen(Collectors.toList(), list -> {
							ShortWorkTimeHistory his = new ShortWorkTimeHistory(list.get(0).get("CID").toString(),
									list.get(0).get("SID").toString(), list.stream().map(c -> {
										return new DateHistoryItem(c.get("HIST_ID").toString(), new DatePeriod(
												(GeneralDate) c.get("STR_YMD"), (GeneralDate) c.get("END_YMD")));
									}).collect(Collectors.toList()));
							shortWorkTimeHistory.add(his);
							return his;
						})));

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return shortWorkTimeHistory;
	}

	@Override
	public void addAll(Map<String, DateHistoryItem> histItem) {
		String cid = AppContexts.user().companyId();
		String INS_SQL = "INSERT INTO BSHMT_WORKTIME_HIST (INS_DATE, INS_CCD , INS_SCD , INS_PG,"
				+ " UPD_DATE , UPD_CCD , UPD_SCD , UPD_PG," 
				+ " HIST_ID, SID, CID,"
				+ " STR_YMD, END_YMD)"
				+ " VALUES (INS_DATE_VAL, INS_CCD_VAL, INS_SCD_VAL, INS_PG_VAL,"
				+ " UPD_DATE_VAL, UPD_CCD_VAL, UPD_SCD_VAL, UPD_PG_VAL,"
				+ " HIST_ID_VAL, SID_VAL, CID_VAL, STR_YMD_VAL, END_YMD_VAL); ";
		String insCcd = AppContexts.user().companyCode();
		String insScd = AppContexts.user().employeeCode();
		String insPg = AppContexts.programId();
		
		String updCcd = insCcd;
		String updScd = insScd;
		String updPg = insPg;
		StringBuilder sb = new StringBuilder();
		histItem.entrySet().stream().forEach(c ->{
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
			sql = sql.replace("STR_YMD_VAL", "'" + dateHistItem.start() + "'");
			sql = sql.replace("END_YMD_VAL","'" +  dateHistItem.end() + "'");
			
			sb.append(sql);
		});
		
		int records = this.getEntityManager().createNativeQuery(sb.toString()).executeUpdate();
		System.out.println(records);
		
	}

	@Override
	public void updateAll(Map<String, DateHistoryItem> histItem) {
		String UP_SQL = "UPDATE BSHMT_WORKTIME_HIST SET UPD_DATE = UPD_DATE_VAL, UPD_CCD = UPD_CCD_VAL, UPD_SCD = UPD_SCD_VAL, UPD_PG = UPD_PG_VAL,"
				+ " STR_YMD = STR_YMD_VAL, END_YMD = END_YMD_VAL"
				+ " WHERE HIST_ID = HIST_ID_VAL AND CID = CID_VAL AND SID = SID_VAL;";
		String cid = AppContexts.user().companyId();
		String updCcd = AppContexts.user().companyCode();
		String updScd = AppContexts.user().employeeCode();
		String updPg = AppContexts.programId();
		
		StringBuilder sb = new StringBuilder();
		histItem.entrySet().stream().forEach(c ->{
			String sql = UP_SQL;
			DateHistoryItem dateItem = c.getValue();
			sql = UP_SQL.replace("UPD_DATE_VAL", "'" + GeneralDateTime.now() +"'");
			sql = sql.replace("UPD_CCD_VAL", "'" + updCcd +"'");
			sql = sql.replace("UPD_SCD_VAL", "'" + updScd +"'");
			sql = sql.replace("UPD_PG_VAL", "'" + updPg +"'");
			
			sql = sql.replace("STR_YMD_VAL", "'" + dateItem.start() + "'");
			sql = sql.replace("END_YMD_VAL","'" +  dateItem.end() + "'");
			
			sql = sql.replace("HIST_ID_VAL", "'" + dateItem.identifier() +"'");
			sql = sql.replace("CID_VAL", "'" + cid +"'");
			sql = sql.replace("SID_VAL", "'" + c.getKey() +"'");
			sb.append(sql);
		});
		int  records = this.getEntityManager().createNativeQuery(sb.toString()).executeUpdate();
		System.out.println(records);
		
	}
}
