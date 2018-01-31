/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternCode;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondItem;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondItem_;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkingConditionItemRepository.
 */
@Stateless
public class JpaWorkingConditionItemRepository extends JpaRepository
		implements WorkingConditionItemRepository {

	/** The Constant FIRST_ITEM_INDEX. */
	private final static int FIRST_ITEM_INDEX = 0;
	
	/** The Constant FIND_WORKCONDITEM_MONTHLY_NOT_NULL. */
	private final static String FIND_WORKCONDITEM_MONTHLY_NOT_NULL = "SELECT wi FROM KshmtWorkingCondItem wi"
																	+ " WHERE wi.monthlyPattern IS NOT NULL"
																	+ " AND wi.sid IN :employeeIds";

	/** The Constant FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D. */
	private final static String FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D =
			"SELECT wi FROM KshmtWorkingCondItem wi "
			+ "WHERE wi.sid = :employeeId "
			+ "AND wi.kshmtWorkingCond.strD <= :endDate "
			+ "AND wi.kshmtWorkingCond.endD >= :startDate "
			+ "ORDER BY wi.kshmtWorkingCond.strD";
	
	/**
	 * Gets the by list sid and monthly pattern not null.
	 *
	 * @param employeeIds the employee ids
	 * @return the by list sid and monthly pattern not null
	 */
	public List<WorkingConditionItem> getByListSidAndMonthlyPatternNotNull(List<String> employeeIds){
		if (employeeIds.isEmpty()){
			return Collections.emptyList();
		}
		List<KshmtWorkingCondItem> entitys = this.queryProxy().query(FIND_WORKCONDITEM_MONTHLY_NOT_NULL, KshmtWorkingCondItem.class)
													.setParameter("employeeIds", employeeIds).getList();
		if(entitys.isEmpty()){
			return Collections.emptyList();
		}
		return entitys.stream().map(e -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(e))).collect(Collectors.toList());
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

	/**
	 * Gets the list by sid and date period
	 *
	 * @param employeeId the employee id
	 * @param datePeriod the date period
	 * @return the list
	 */
	// add 2018.1.31 shuichi_ishida
	@Override
	public List<WorkingConditionItem> getBySidAndPeriodOrderByStrD(String employeeId, DatePeriod datePeriod) {
		
		List<KshmtWorkingCondItem> entitys =
				this.queryProxy().query(FIND_BY_SID_AND_PERIOD_ORDER_BY_STR_D, KshmtWorkingCondItem.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end())
				.getList();
		
		if (entitys.isEmpty()) return Collections.emptyList();
		
		return entitys.stream()
				.map(e -> new WorkingConditionItem(new JpaWorkingConditionItemGetMemento(e)))
				.collect(Collectors.toList());
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
	public boolean copyLastMonthlyPatternSetting(String sourceSid, String destSid) {
		// Get items
		Optional<KshmtWorkingCondItem> optSourceItem = this.getLastWorkingCondItem(sourceSid);
		Optional<KshmtWorkingCondItem> optDestItem = this.getLastWorkingCondItem(destSid);

		// Check 
		if (!optSourceItem.isPresent() || !optDestItem.isPresent()) {
			// Copy fails
			return false;
		}
		
		// Copy data
		KshmtWorkingCondItem destItem = optDestItem.get();
		destItem.setMonthlyPattern(optSourceItem.get().getMonthlyPattern());

		// Update
		this.commandProxy().update(destItem);
		
		// Copy success
		return true;
	}
	
	/**
	 * Gets the last working cond item.
	 *
	 * @param employeeId the employee id
	 * @return the last working cond item
	 */
	private Optional<KshmtWorkingCondItem> getLastWorkingCondItem(String employeeId) {
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
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshmtWorkingCondItem_.kshmtWorkingCond).get(KshmtWorkingCond_.endD),
				GeneralDate.max()));

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
		return Optional.of(result.get(FIRST_ITEM_INDEX));
	}

}
