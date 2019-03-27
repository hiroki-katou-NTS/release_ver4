/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternCode;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemCustom;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionWithDataPeriod;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtDayofweekTimeZone;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtDayofweekTimeZonePK;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPerWorkCat;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPerWorkCatPK;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPersonalDayOfWeek;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPersonalDayOfWeekPK;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtScheduleMethod;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkCatTimeZone;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkCatTimeZonePK;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondItem;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondItem_;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond_;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkingConditionItemRepository.
 */
@Stateless
public class JpaWorkingConditionItemRepository extends JpaRepository
		implements WorkingConditionItemRepository {

	/** The Constant FIRST_ITEM_INDEX. */
	private final static int FIRST_ITEM_INDEX = 0;

	/** The Constant FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D. */
//	private final static String FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D =
//			"SELECT wi FROM KshmtWorkingCondItem wi "
//			+ "WHERE wi.sid = :employeeId "
//			+ "AND wi.kshmtWorkingCond.strD <= :endDate "
//			+ "AND wi.kshmtWorkingCond.endD >= :startDate "
//			+ "ORDER BY wi.kshmtWorkingCond.strD";
//	
//	private final static String FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D_FOR_MULTI =
//			"SELECT wi FROM KshmtWorkingCondItem wi "
//			+ "WHERE wi.sid IN :employeeId "
//			+ "AND wi.kshmtWorkingCond.strD <= :endDate "
//			+ "AND wi.kshmtWorkingCond.endD >= :startDate "
//			+ "ORDER BY wi.kshmtWorkingCond.strD";
	
	private final static String FIND_BY_SID_AND_PERIOD_WITH_JOIN = new StringBuilder("SELECT wi, c, m, wc, dw FROM KshmtWorkingCondItem wi ")
																						.append(" LEFT JOIN wi.kshmtWorkingCond c ")
																						.append(" LEFT JOIN wi.kshmtScheduleMethod m ")
																						.append(" LEFT JOIN wi.kshmtPerWorkCats wc ")
																						.append(" LEFT JOIN wi.kshmtPersonalDayOfWeeks dw ")
																						.append(" WHERE wi.sid IN :employeeId ")
																						.append(" AND c.strD <= :endDate ")
																						.append(" AND c.endD >= :startDate ")
																						.append(" ORDER BY c.strD").toString();
	
	/**
	 * Gets the by list sid and monthly pattern not null.
	 *
	 * @param employeeIds the employee ids
	 * @return the by list sid and monthly pattern not null
	 */
	public List<WorkingConditionItem> getByListSidAndMonthlyPatternNotNull(List<String> employeeIds, List<String> monthlyPatternCodes){
		if (CollectionUtil.isEmpty(employeeIds) || CollectionUtil.isEmpty(monthlyPatternCodes)) {
			return Collections.emptyList();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		List<KshmtWorkingCondItem> result = new ArrayList<>();
				
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT,
				subEmployeeList -> {
					CollectionUtil.split(monthlyPatternCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT,
							subPatternList -> {
								// add where
								List<Predicate> lstpredicateWhere = new ArrayList<>();

								// condition
								lstpredicateWhere.add(
										root.get(KshmtWorkingCondItem_.sid).in(subEmployeeList));
								lstpredicateWhere.add(root.get(KshmtWorkingCondItem_.monthlyPattern)
										.in(subPatternList));
								lstpredicateWhere
										.add(criteriaBuilder.equal(
												root.get(KshmtWorkingCondItem_.kshmtWorkingCond)
														.get(KshmtWorkingCond_.endD),
												GeneralDate.max()));

								// set where to SQL
								cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

								// create query
								TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

								result.addAll(query.getResultList());
							});
				});
		
		// exclude select
		return result.stream()
				.map(e -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(e)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * getByHistoryId(java.lang.String)
	 */
	@Override
	public Optional<WorkingConditionItem> getByHistoryId(String historyId) {
		// Query
		Optional<KshmtWorkingCondItem> optEntity = this.queryProxy().find(historyId,
				KshmtWorkingCondItem.class);

		// Check exist
		if (!optEntity.isPresent()) {
			return Optional.empty();
		}

		// Return
		return Optional.of(
				new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(optEntity.get())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * findWorkingConditionItemByPersWorkCat(java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId,
			GeneralDate baseDate) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KshmtWorkingCondItem_.sid), employeeId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.strD),
				baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

		List<KshmtWorkingCondItem> result = query.getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingConditionItem(
				new JpaWorkingConditionItemGetMemento(result.get(FIRST_ITEM_INDEX))));
	}

	@Override
	public List<WorkingConditionItemCustom> getBySidsAndStandardDate(List<String> employeeIds, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		List<KshmtWorkingCondItem> result = new ArrayList<>();

		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// equal
			lstpredicateWhere.add(root.get(KshmtWorkingCondItem_.sid).in(subList));
			lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
					root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.strD),
					baseDate));
			lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
					root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD),
					baseDate));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

			// creat query
			TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

			result.addAll(query.getResultList());
		});

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Collections.emptyList();
		}

		// exclude select
		return result.stream().map(entity -> new WorkingConditionItemCustom(entity.getSid(), entity.getLaborSys()))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the list by sid and date period
	 *
	 * @param employeeId the employee id
	 * @param datePeriod the date period
	 * @return the list
	 */
	// add 2018.1.31 shuichi_ishida
	@SneakyThrows
	@Override
	public List<WorkingConditionItem> getBySidAndPeriodOrderByStrD(String employeeId,
			DatePeriod datePeriod) {
		String sqlJdbc = "SELECT KWCI.*, KSM.* FROM KSHMT_WORKING_COND_ITEM KWCI "
				+ "LEFT JOIN KSHMT_SCHEDULE_METHOD KSM ON KWCI.HIST_ID = KSM.HIST_ID "
				+ "LEFT JOIN KSHMT_WORKING_COND KWC ON KWCI.HIST_ID = KWC.HIST_ID "
				+ "WHERE KWCI.SID = ? " + "AND KWC.START_DATE <= ? " + "AND KWC.END_DATE >= ? "
				+ "ORDER BY KWC.START_DATE";

		try (PreparedStatement stmt = this.connection().prepareStatement(sqlJdbc)) {

			stmt.setString(1, employeeId);
			stmt.setDate(2, Date.valueOf(datePeriod.end().toLocalDate()));
			stmt.setDate(3, Date.valueOf(datePeriod.start().toLocalDate()));

			List<KshmtWorkingCondItem> result = new NtsResultSet(stmt.executeQuery())
					.getList(rec -> {
						KshmtScheduleMethod kshmtScheduleMethod = new KshmtScheduleMethod();
						kshmtScheduleMethod.setHistoryId(rec.getString("HIST_ID"));
						kshmtScheduleMethod.setBasicCreateMethod(rec.getInt("BASIC_CREATE_METHOD"));
						kshmtScheduleMethod
								.setRefBusinessDayCalendar(rec.getInt("REF_BUSINESS_DAY_CALENDAR"));
						kshmtScheduleMethod.setRefBasicWork(rec.getInt("REF_BASIC_WORK"));
						kshmtScheduleMethod.setRefWorkingHours(rec.getInt("REF_WORKING_HOURS"));

						KshmtWorkingCondItem entity = new KshmtWorkingCondItem();
						entity.setHistoryId(rec.getString("HIST_ID"));
						entity.setSid(rec.getString("SID"));
						entity.setHourlyPayAtr(rec.getInt("HOURLY_PAY_ATR"));
						entity.setScheManagementAtr(rec.getInt("SCHE_MANAGEMENT_ATR"));
						entity.setAutoStampSetAtr(rec.getInt("AUTO_STAMP_SET_ATR"));
						entity.setAutoIntervalSetAtr(rec.getInt("AUTO_INTERVAL_SET_ATR"));
						entity.setVacationAddTimeAtr(rec.getInt("VACATION_ADD_TIME_ATR"));
						entity.setContractTime(rec.getInt("CONTRACT_TIME"));
						entity.setLaborSys(rec.getInt("LABOR_SYS"));
						entity.setHdAddTimeOneDay(rec.getInt("HD_ADD_TIME_ONE_DAY"));
						entity.setHdAddTimeMorning(rec.getInt("HD_ADD_TIME_MORNING"));
						entity.setHdAddTimeAfternoon(rec.getInt("HD_ADD_TIME_AFTERNOON"));
						entity.setTimeApply(rec.getString("TIME_APPLY"));
						entity.setMonthlyPattern(rec.getString("MONTHLY_PATTERN"));
						entity.setKshmtScheduleMethod(kshmtScheduleMethod);

						return entity;
					});

			if (result.isEmpty()) {
				return Collections.emptyList();
			}

			List<String> histIds = result.stream().map(KshmtWorkingCondItem::getHistoryId)
					.collect(Collectors.toList());

			List<KshmtWorkCatTimeZone> kshmtWorkCatTimeZones = new ArrayList<>();
			CollectionUtil.split(histIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				String sqlJdbcWc = "SELECT * FROM KSHMT_WORK_CAT_TIME_ZONE KWCTZ WHERE KWCTZ.HIST_ID IN ("
						+ NtsStatement.In.createParamsString(subList) + ")";
				
				try (PreparedStatement statement = this.connection().prepareStatement(sqlJdbcWc)) {
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(i + 1, subList.get(i));
					}
					kshmtWorkCatTimeZones
							.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
								KshmtWorkCatTimeZonePK kshmtWorkCatTimeZonePK = new KshmtWorkCatTimeZonePK();
								kshmtWorkCatTimeZonePK.setHistoryId(rec.getString("HIST_ID"));
								kshmtWorkCatTimeZonePK
										.setPerWorkCatAtr(rec.getInt("PER_WORK_CAT_ATR"));
								kshmtWorkCatTimeZonePK.setCnt(rec.getInt("CNT"));

								KshmtWorkCatTimeZone entity = new KshmtWorkCatTimeZone();
								entity.setKshmtWorkCatTimeZonePK(kshmtWorkCatTimeZonePK);

								return entity;
							}));
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				}
			});

			// Get KshmtPerWorkCats
			List<KshmtPerWorkCat> kshmtPerWorkCats = new ArrayList<>();
			CollectionUtil.split(histIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				String sqlJdbcWc = "SELECT * FROM KSHMT_PER_WORK_CAT KPWC WHERE KPWC.HIST_ID IN ("
						+ NtsStatement.In.createParamsString(subList) + ")";
				try (PreparedStatement statement = this.connection().prepareStatement(sqlJdbcWc)) {
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(i + 1, subList.get(i));
					}
					kshmtPerWorkCats
							.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
								KshmtPerWorkCatPK kshmtPerWorkCatPK = new KshmtPerWorkCatPK();
								kshmtPerWorkCatPK.setHistoryId(rec.getString("HIST_ID"));
								kshmtPerWorkCatPK.setPerWorkCatAtr(rec.getInt("PER_WORK_CAT_ATR"));

								KshmtPerWorkCat entity = new KshmtPerWorkCat();
								entity.setKshmtPerWorkCatPK(kshmtPerWorkCatPK);
								entity.setWorkTypeCode(rec.getString("WORK_TYPE_CODE"));
								entity.setWorkTimeCode(rec.getString("WORK_TIME_CODE"));

								return entity;
							}));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});

			List<KshmtDayofweekTimeZone> kshmtDayofweekTimeZones = new ArrayList<>();
			CollectionUtil.split(histIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				String sqlJdbcWc = "SELECT * FROM KSHMT_DAYOFWEEK_TIME_ZONE KDTZ WHERE KDTZ.HIST_ID IN ("
						+ NtsStatement.In.createParamsString(subList) + ")";
				;
				try (PreparedStatement statement = this.connection().prepareStatement(sqlJdbcWc)) {
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(i + 1, subList.get(i));
					}
					kshmtDayofweekTimeZones
							.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
								KshmtDayofweekTimeZonePK kshmtDayofweekTimeZonePK = new KshmtDayofweekTimeZonePK();
								kshmtDayofweekTimeZonePK.setHistoryId(rec.getString("HIST_ID"));
								kshmtDayofweekTimeZonePK
										.setPerWorkDayOffAtr(rec.getInt("PER_WORK_DAY_OFF_ATR"));
								kshmtDayofweekTimeZonePK.setCnt(rec.getInt("CNT"));

								KshmtDayofweekTimeZone entity = new KshmtDayofweekTimeZone();
								entity.setKshmtDayofweekTimeZonePK(kshmtDayofweekTimeZonePK);

								return entity;
							}));
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				}
			});

			// Get
			List<KshmtPersonalDayOfWeek> kshmtPersonalDayOfWeeks = new ArrayList<>();
			CollectionUtil.split(histIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				String sqlJdbcWc = "SELECT * FROM KSHMT_PERSONAL_DAY_OF_WEEK KPDW WHERE KPDW.HIST_ID IN ("
						+ NtsStatement.In.createParamsString(subList) + ")";
				
				try (PreparedStatement statement = this.connection().prepareStatement(sqlJdbcWc)) {
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(i + 1, subList.get(i));
					}
					kshmtPersonalDayOfWeeks
							.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
								KshmtPersonalDayOfWeekPK kshmtPersonalDayOfWeekPK = new KshmtPersonalDayOfWeekPK();
								kshmtPersonalDayOfWeekPK.setHistoryId(rec.getString("HIST_ID"));
								kshmtPersonalDayOfWeekPK
										.setPerWorkDayOffAtr(rec.getInt("PER_WORK_DAY_OFF_ATR"));

								KshmtPersonalDayOfWeek entity = new KshmtPersonalDayOfWeek();
								entity.setKshmtPersonalDayOfWeekPK(kshmtPersonalDayOfWeekPK);
								entity.setWorkTypeCode(rec.getString("WORK_TYPE_CODE"));
								entity.setWorkTimeCode(rec.getString("WORK_TIME_CODE"));

								return entity;
							}));
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				}
			});

			// Put value
			Map<String, Map<Integer, List<KshmtWorkCatTimeZone>>> kshmtWorkCatTimeZonesMap = kshmtWorkCatTimeZones
					.stream()
					.collect(Collectors.groupingBy(
							item -> item.getKshmtWorkCatTimeZonePK().getHistoryId(),
							Collectors.groupingBy(
									item -> item.getKshmtWorkCatTimeZonePK().getPerWorkCatAtr())));

			kshmtPerWorkCats.forEach(item -> {
				item.setKshmtWorkCatTimeZones(
						kshmtWorkCatTimeZonesMap.getOrDefault(item.getKshmtPerWorkCatPK().getHistoryId(), Collections.emptyMap())
								.getOrDefault(item.getKshmtPerWorkCatPK().getPerWorkCatAtr(), Collections.emptyList()));
			});

			Map<String, List<KshmtPerWorkCat>> kshmtPerWorkCatsMap = kshmtPerWorkCats.stream()
					.collect(Collectors
							.groupingBy(item -> item.getKshmtPerWorkCatPK().getHistoryId()));

			Map<String, Map<Integer, List<KshmtDayofweekTimeZone>>> kshmtDayofweekTimeZonesMap = kshmtDayofweekTimeZones
					.stream()
					.collect(Collectors.groupingBy(
							item -> item.getKshmtDayofweekTimeZonePK().getHistoryId(),
							Collectors.groupingBy(item -> item.getKshmtDayofweekTimeZonePK()
									.getPerWorkDayOffAtr())));

			kshmtPersonalDayOfWeeks.forEach(item -> {
				item.setKshmtDayofweekTimeZones(kshmtDayofweekTimeZonesMap
						.getOrDefault(item.getKshmtPersonalDayOfWeekPK().getHistoryId(), Collections.emptyMap())
						.getOrDefault(item.getKshmtPersonalDayOfWeekPK().getPerWorkDayOffAtr(), Collections.emptyList()));
			});

			Map<String, List<KshmtPersonalDayOfWeek>> kshmtPersonalDayOfWeeksMap = kshmtPersonalDayOfWeeks
					.stream().collect(Collectors
							.groupingBy(item -> item.getKshmtPersonalDayOfWeekPK().getHistoryId()));

			result.forEach(item -> {
				item.setKshmtPerWorkCats(kshmtPerWorkCatsMap.getOrDefault(item.getHistoryId(), Collections.emptyList()));
				item.setKshmtPersonalDayOfWeeks(
						kshmtPersonalDayOfWeeksMap.get(item.getHistoryId()));
			});

			return result.stream()
					.map(e -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(e)))
					.collect(Collectors.toList());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * add(nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem)
	 */
	@Override
	public void add(WorkingConditionItem item) {
		KshmtWorkingCondItem entity = new KshmtWorkingCondItem();
		item.saveToMemento(new JpaWorkingConditionItemSetMemento(entity));
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * update(nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem)
	 */
	@Override
	public void update(WorkingConditionItem item) {
		Optional<KshmtWorkingCondItem> optEntity = this.queryProxy().find(item.getHistoryId(),
				KshmtWorkingCondItem.class);

		KshmtWorkingCondItem entity = optEntity.get();

		item.saveToMemento(new JpaWorkingConditionItemSetMemento(entity));

		this.commandProxy().update(entity);
	}
	
	/**
	 *  Update WorkingConditionItem trong trường hợp category WorkingCondition chia đôi.
	 */
	@Override
	public void updateWorkCond2(WorkingConditionItem item) {
		Optional<KshmtWorkingCondItem> optEntity = this.queryProxy().find(item.getHistoryId(),
				KshmtWorkingCondItem.class);

		KshmtWorkingCondItem entity = optEntity.get();

		item.saveToMemento(new JpaWorkingConditionItem2SetMemento(entity));

		this.commandProxy().update(entity);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * delete(java.lang.String)
	 */
	@Override
	public void delete(String historyId) {
		this.commandProxy().remove(KshmtWorkingCondItem.class, historyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * getBySidAndHistId(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkingConditionItem> getBySid(String employeeId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KshmtWorkingCondItem_.sid), employeeId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

		List<KshmtWorkingCondItem> result = query.getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingConditionItem(
				new JpaWorkingConditionItemGetMemento(result.get(FIRST_ITEM_INDEX))));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * getBySidsAndBaseDate(java.util.List, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkingConditionItem> getBySidsAndDatePeriod(List<String> employeeIds,
			DatePeriod datePeriod) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		List<KshmtWorkingCondItem> result = new ArrayList<>();

		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// equal
			lstpredicateWhere.add(root.get(KshmtWorkingCondItem_.sid).in(subList));
			
			lstpredicateWhere.add(criteriaBuilder.not(criteriaBuilder.or(
					criteriaBuilder.lessThan(root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD), datePeriod.start()),
					criteriaBuilder.greaterThan(root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.strD), datePeriod.end()))));
			
			// TODO: Check & request update EAP with new condition
//			lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
//					root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.strD),
//					datePeriod.end()));
//			lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
//					root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD),
//					datePeriod.start()));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// creat query
			TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

			result.addAll(query.getResultList());
		});

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Collections.emptyList();
		}

		// exclude select
		return result.stream().map(
				entity -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(entity)))
				.collect(Collectors.toList());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * getBySidAndHistId(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkingConditionItem> getBySidAndHistId(String employeeId, String histId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KshmtWorkingCondItem_.sid), employeeId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KshmtWorkingCondItem_.historyId), histId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

		List<KshmtWorkingCondItem> result = query.getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingConditionItem(
				new JpaWorkingConditionItemGetMemento(result.get(FIRST_ITEM_INDEX))));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#deleteMonthlyPattern(java.lang.String)
	 */
	@Override
	public void deleteMonthlyPattern(String historyId) {
		Optional<KshmtWorkingCondItem> optEntity = this.queryProxy().find(historyId,
				KshmtWorkingCondItem.class);
		KshmtWorkingCondItem entity = optEntity.get();
		
		entity.setMonthlyPattern(null);
		
		this.commandProxy().update(entity);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#updateMonthlyPattern(java.lang.String, nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternCode)
	 */
	@Override
	public void updateMonthlyPattern(String historyId, MonthlyPatternCode monthlyPattern) {
		Optional<KshmtWorkingCondItem> optEntity = this.queryProxy().find(historyId,
				KshmtWorkingCondItem.class);
		KshmtWorkingCondItem entity = optEntity.get();
		
		entity.setMonthlyPattern(monthlyPattern.v());
		this.commandProxy().update(entity);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#copyLastMonthlyPatternSetting(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean copyLastMonthlyPatternSetting(String sourceSid, List<String> destSid) {
		// Get items
		Optional<WorkingConditionItem>  optSourceItem = this.getBySid(sourceSid);
		List<KshmtWorkingCondItem> optDestItem = this.getLastWorkingCondItemEntities(destSid);

		// Check 
		if (!optSourceItem.isPresent() || optDestItem.isEmpty()) {
			// Copy fails
			return false;
		}
		
		// Copy data
		optDestItem.stream().forEach(e -> {
			e.setMonthlyPattern(optSourceItem.get().getMonthlyPattern().get().v());
			// Update
			this.commandProxy().update(e);
		});
		
		// Copy success
		return true;
	}
	
	/**
	 * Gets the last working cond item.
	 *
	 * @param employeeId the employee id
	 * @return the last working cond item
	 */
	private List<KshmtWorkingCondItem> getLastWorkingCondItemEntities(List<String> employeeId) {
		
		// Check empty
		if (CollectionUtil.isEmpty(employeeId)) {
			return Collections.emptyList();
		}
				
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCondItem> cq = criteriaBuilder
				.createQuery(KshmtWorkingCondItem.class);

		// root data
		Root<KshmtWorkingCondItem> root = cq.from(KshmtWorkingCondItem.class);
		
		// select root
		cq.select(root);
		
		List<KshmtWorkingCondItem> result = new ArrayList<>();

		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// equal
			lstpredicateWhere
					.add(root.get(KshmtWorkingCondItem_.sid).in(subList));
			lstpredicateWhere.add(criteriaBuilder.equal(
					root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD),
					GeneralDate.max()));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
			
			// create query
			TypedQuery<KshmtWorkingCondItem> query = em.createQuery(cq);

			result.addAll(query.getResultList());
		});

		// exclude select
		return result;
	}

	@Override
	public Map<String, Map<GeneralDate, WorkingConditionItem>> getBySidAndPeriod(Map<String, Set<GeneralDate>> params) {
		//データの取得
		TypedQueryWrapper<Object[]> query = this.queryProxy().query(FIND_BY_SID_AND_PERIOD_WITH_JOIN, Object[].class);

		List<Object[]> entities = new ArrayList<>();
		CollectionUtil.split(params, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p ->{
			Set<GeneralDate> all = p.entrySet().stream().map(c -> c.getValue()).flatMap(Collection::stream).collect(Collectors.toSet());
			GeneralDate min = all.stream().min((d1, d2) -> d1.compareTo(d2)).orElse(null);
			GeneralDate max = all.stream().max((d1, d2) -> d1.compareTo(d2)).orElse(null);
			entities.addAll(query.setParameter("employeeId", p.keySet())
							.setParameter("startDate", min)
							.setParameter("endDate", max)
							.getList());
		});
		
		if (entities.isEmpty()) return new HashMap<>();
		return params.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(), p -> {
			List<Object[]> sub = entities.stream().filter(e -> ((KshmtWorkingCondItem) e[0]).getSid().equals(p.getKey())).collect(Collectors.toList());
			Map<GeneralDate, WorkingConditionItem> result = new HashMap<>();
			p.getValue().stream().forEach(d -> {
				List<Object[]> data = sub.stream().filter(wc -> {
					KshmtWorkingCond se = (KshmtWorkingCond) wc[1];
					return se.getStrD().compareTo(d) <= 0 && se.getEndD().compareTo(d) >= 0;
				}).collect(Collectors.toList());
				
				if(data.isEmpty()){
					return;
				}
				
				KshmtWorkingCondItem workCondItem = data.stream().filter(dt -> dt[0] != null).findFirst()
						.map(dt -> (KshmtWorkingCondItem) dt[0]).orElse(null);
				
				if(workCondItem == null){
					return;
				}
				result.put(d, createWorkConditionItem(data, workCondItem));
			});
			
			return result;
		}));
	}
	
	/**
	 * Gets the list with period by sid and date period  
	 * @param employeeId the employee id
	 * @param datePeriod the date period
	 * @return the list
	 */
	@Override
	public WorkingConditionWithDataPeriod getBySidAndPeriodOrderByStrDWithDatePeriod(Map<String,DatePeriod> param,GeneralDate max,GeneralDate min) {
		//データの取得
		TypedQueryWrapper<Object[]> query = this.queryProxy().query(FIND_BY_SID_AND_PERIOD_WITH_JOIN, Object[].class);

		List<Object[]> entities = new ArrayList<>();
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			entities.addAll(query.setParameter("employeeId", p.keySet())
							.setParameter("startDate", min)
							.setParameter("endDate", max)
							.getList());
		});

		Map<DateHistoryItem, WorkingConditionItem> result = new LinkedHashMap<>();
		
		entities.stream().filter(c -> c[0] != null).collect(Collectors.groupingBy(c -> (KshmtWorkingCondItem) c[0], Collectors.toList()))
			.entrySet().stream().forEach(c -> {
				DatePeriod period = param.get(c.getKey().getSid());
				c.getValue().stream().filter(wc -> (KshmtWorkingCond) wc[1] != null).findFirst()
					.map(wc -> (KshmtWorkingCond) wc[1]).ifPresent(wc -> {
					if(period.start().compareTo(wc.getEndD()) <= 0 && period.end().compareTo(wc.getStrD()) >= 0){
						WorkingConditionItem wcItem = createWorkConditionItem(c.getValue(), c.getKey());
						
						GeneralDate strDate = wc.getStrD().compareTo(period.start()) > 0 ? wc.getStrD() : period.start();
						GeneralDate endDate = wc.getEndD().compareTo(period.end()) > 0 ? period.end() : wc.getEndD();
						DateHistoryItem dateItem = new DateHistoryItem(wc.getKshmtWorkingCondPK().getHistoryId(), new DatePeriod(strDate, endDate));
						
						result.put(dateItem, wcItem);
					}
				}); 
			});
				
//		//データの取得
//		TypedQueryWrapper<KshmtWorkingCondItem> entitys = this.queryProxy().query(FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D_FOR_MULTI, KshmtWorkingCondItem.class);
//
//		List<KshmtWorkingCondItem> a = new ArrayList<>();
//		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p ->{
//			a.addAll(entitys.setParameter("employeeId", p.keySet())
//							.setParameter("startDate", min)
//							.setParameter("endDate", max)
//							.getList()
//							.stream()
//							.filter(tc ->  p.containsKey(tc.getSid())
//										&& p.get(tc.getSid()).start().compareTo(tc.getKshmtWorkingCond().getEndD()) <= 0
//										&& p.get(tc.getSid()).end().compareTo(tc.getKshmtWorkingCond().getStrD()) >= 0)
//							.collect(Collectors.toList()));
//		});
//		
//				
//		Map<DateHistoryItem, WorkingConditionItem> result = new LinkedHashMap<>();
//		//取得したデータの変換
//		a.forEach(c -> {
//			val strDate = c.getKshmtWorkingCond().getStrD().compareTo(param.get(c.getSid()).start())>0?c.getKshmtWorkingCond().getStrD():param.get(c.getSid()).start();
//			val endDate = c.getKshmtWorkingCond().getEndD().compareTo(param.get(c.getSid()).end())<0?param.get(c.getSid()).end():c.getKshmtWorkingCond().getEndD();
//			result.put(new DateHistoryItem(c.getKshmtWorkingCond().getKshmtWorkingCondPK().getHistoryId(), 
//											new DatePeriod(strDate, endDate)), 
//						new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(c)));
//		});
		return new WorkingConditionWithDataPeriod(result);
	}

	private WorkingConditionItem createWorkConditionItem(List<Object[]> source, KshmtWorkingCondItem main) {
		List<KshmtPerWorkCat> perWorkCat = source.stream().filter(dt -> dt[3] != null)
				.map(dt -> (KshmtPerWorkCat) dt[3]).distinct().collect(Collectors.toList());
		List<KshmtPersonalDayOfWeek> perDayWeek = source.stream().filter(dt -> dt[4] != null)
				.map(dt -> (KshmtPersonalDayOfWeek) dt[4]).distinct().collect(Collectors.toList());
		KshmtScheduleMethod method = source.stream().filter(dt -> dt[2] != null).findFirst()
				.map(dt -> (KshmtScheduleMethod) dt[2]).orElse(null);
		
		return new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(main, perWorkCat, perDayWeek, method));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository#
	 * getLastWorkingCondItem(java.util.List)
	 */
	@Override
	public List<WorkingConditionItem> getLastWorkingCondItem(List<String> employeeIds) {
		List<KshmtWorkingCondItem> result = this.getLastWorkingCondItemEntities(employeeIds);

		// exclude select
		return result.stream().map(
				entity -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(entity)))
				.collect(Collectors.toList());
	}

}

