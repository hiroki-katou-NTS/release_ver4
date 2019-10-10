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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondPK_;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkingConditionRepository.
 */
@Stateless
public class JpaWorkingConditionRepository extends JpaRepository implements WorkingConditionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#
	 * getAllWokingCondition(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkingCondition> getBySid(String companyId, String sId) {

		List<KshmtWorkingCond> result = this.findBy(companyId, sId, null);

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingCondition(new JpaWorkingConditionGetMemento(result)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#
	 * getBySid(java.lang.String)
	 */
	@Override
	public Optional<WorkingCondition> getBySid(String sId) {

		List<KshmtWorkingCond> result = this.findBy(null, sId, null);

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingCondition(new JpaWorkingConditionGetMemento(result)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#
	 * getByHistoryId(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<WorkingCondition> getByHistoryId(String historyId) {
		List<KshmtWorkingCond> result = this.findBy(null, null, historyId);

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// exclude select
		return Optional.of(new WorkingCondition(new JpaWorkingConditionGetMemento(result)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#
	 * getBySidAndStandardDate(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkingCondition> getBySidAndStandardDate(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCond> cq = criteriaBuilder.createQuery(KshmtWorkingCond.class);

		// root data
		Root<KshmtWorkingCond> root = cq.from(KshmtWorkingCond.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkingCond_.kshmtWorkingCondPK).get(KshmtWorkingCondPK_.sid), employeeId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(root.get(KshmtWorkingCond_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(root.get(KshmtWorkingCond_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<KshmtWorkingCond> query = em.createQuery(cq);

		List<KshmtWorkingCond> result = query.getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		return Optional.of(new WorkingCondition(new JpaWorkingConditionGetMemento(result)));
	}

	/**
	 * Adds the.
	 *
	 * @param entities
	 *            the entities
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#add(
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition)
	 */
	private void add(List<KshmtWorkingCond> entities) {
		this.commandProxy().insertAll(entities);
		this.getEntityManager().flush();
	}

	/**
	 * Delete all.
	 *
	 * @param entities
	 *            the entities
	 */
	private void deleteAll(List<KshmtWorkingCond> entities) {
		this.commandProxy().removeAll(entities);
		this.getEntityManager().flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#save
	 * (nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition)
	 */
	@Override
	@Transactional
	public void save(WorkingCondition workingCondition) {
		List<KshmtWorkingCond> entities = this.findBy(workingCondition.getCompanyId(), workingCondition.getEmployeeId(),
		null);
		
		List<KshmtWorkingCond> newWorkingCondition = new ArrayList<>(entities);

		workingCondition.saveToMemento(new JpaWorkingConditionSetMemento(newWorkingCondition));
		
		this.add(newWorkingCondition.stream()
				.filter(item -> {
					int index = entities.indexOf(item);
					if (index == -1) {
						return true;
					}				
					
					KshmtWorkingCond oldItem = entities.get(index);
					if (oldItem.getStrD().equals(item.getStrD()) && oldItem.getEndD().equals(item.getEndD())) {
						return false;
					}
					return true;
				})
				.collect(Collectors.toList()));
		
		this.deleteAll(entities.stream().filter(item ->  !newWorkingCondition.contains(item)).collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#
	 * remove(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String employeeId) {
		List<KshmtWorkingCond> entities = this.findBy(null, employeeId, null);
		this.commandProxy().removeAll(entities);
	}

	/**
	 * Find by.
	 *
	 * @param companyId
	 *            the company id
	 * @param sId
	 *            the s id
	 * @param historyId
	 *            the history id
	 * @return the list
	 */
	private List<KshmtWorkingCond> findBy(String companyId, String sId, String historyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCond> cq = criteriaBuilder.createQuery(KshmtWorkingCond.class);

		// root data
		Root<KshmtWorkingCond> root = cq.from(KshmtWorkingCond.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		if (companyId != null) {
			lstpredicateWhere.add(criteriaBuilder.equal(root.get(KshmtWorkingCond_.cid), companyId));
		}

		if (sId != null) {
			lstpredicateWhere.add(criteriaBuilder
					.equal(root.get(KshmtWorkingCond_.kshmtWorkingCondPK).get(KshmtWorkingCondPK_.sid), sId));
		}

		if (historyId != null) {
			lstpredicateWhere.add(criteriaBuilder.equal(
					root.get(KshmtWorkingCond_.kshmtWorkingCondPK).get(KshmtWorkingCondPK_.historyId), historyId));
		}

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Order by start date
		cq.orderBy(criteriaBuilder.desc(root.get(KshmtWorkingCond_.strD)));

		// creat query
		TypedQuery<KshmtWorkingCond> query = em.createQuery(cq);

		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository
	 * #getBySidAndStandardDate(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkingCondition> getBySidAndStandardDate(String companyId, String employeeId,
			GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCond> cq = criteriaBuilder.createQuery(KshmtWorkingCond.class);

		// root data
		Root<KshmtWorkingCond> root = cq.from(KshmtWorkingCond.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkingCond_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkingCond_.kshmtWorkingCondPK).get(KshmtWorkingCondPK_.sid), employeeId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(root.get(KshmtWorkingCond_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(root.get(KshmtWorkingCond_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<KshmtWorkingCond> query = em.createQuery(cq);

		List<KshmtWorkingCond> result = query.getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		return Optional.of(new WorkingCondition(new JpaWorkingConditionGetMemento(result)));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository#getBySids(java.util.List)
	 */
	@Override
	public List<WorkingCondition> getBySidsAndDatePeriod(List<String> employeeIds, DatePeriod datePeriod) {
		
		// Check exist
		if (CollectionUtil.isEmpty(employeeIds)) {
			return Collections.emptyList();
		}
				
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkingCond> cq = criteriaBuilder.createQuery(KshmtWorkingCond.class);

		// root data
		Root<KshmtWorkingCond> root = cq.from(KshmtWorkingCond.class);

		// select root
		cq.select(root);
		
		List<KshmtWorkingCond> result =  new ArrayList<>();
		
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			
			// eq company id
			lstpredicateWhere.add(root.get(KshmtWorkingCond_.kshmtWorkingCondPK)
					.get(KshmtWorkingCondPK_.sid).in(subList));
			lstpredicateWhere.add(criteriaBuilder.not(criteriaBuilder.or(
					criteriaBuilder.lessThan(root.get(KshmtWorkingCond_.endD), datePeriod.start()),
					criteriaBuilder.greaterThan(root.get(KshmtWorkingCond_.strD), datePeriod.end()))));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// creat query
			TypedQuery<KshmtWorkingCond> query = em.createQuery(cq);
			
			result.addAll(query.getResultList());
		});

		return result.parallelStream()
				.collect(Collectors.groupingBy(entity -> entity.getKshmtWorkingCondPK().getSid()))
				.values().parallelStream()
				.map(item -> new WorkingCondition(new JpaWorkingConditionGetMemento(item)))
				.collect(Collectors.toList());
	}

}
