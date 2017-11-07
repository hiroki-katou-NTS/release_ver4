/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.affiliate;

import java.util.ArrayList;
import java.util.List;
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
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.KmnmtAffiliWorkplaceHist;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.KmnmtAffiliWorkplaceHistPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.KmnmtAffiliWorkplaceHistPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.KmnmtAffiliWorkplaceHist_;

/**
 * The Class JpaAffWorkplaceHistoryRepository.
 */
@Stateless
public class JpaAffWorkplaceHistoryRepository extends JpaRepository
		implements AffWorkplaceHistoryRepository {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.workplace.history.
	 * WorkplaceHistoryRepository#searchEmployee(nts.arc.time.GeneralDate,
	 * java.util.List)
	 */
	@Override
	public List<AffWorkplaceHistory> searchWorkplaceHistory(GeneralDate baseDate,
			List<String> workplaces) {

		// check exist data
		if (CollectionUtil.isEmpty(workplaces)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_WORKPLACE_HIST (KmnmtWorkplaceHist SQL)
		CriteriaQuery<KmnmtAffiliWorkplaceHist> cq = criteriaBuilder
				.createQuery(KmnmtAffiliWorkplaceHist.class);

		// root data
		Root<KmnmtAffiliWorkplaceHist> root = cq.from(KmnmtAffiliWorkplaceHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// work place in data work place
		lstpredicateWhere.add(
				criteriaBuilder.and(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.wkpId).in(workplaces)));

		// start date <= base date
		lstpredicateWhere.add(criteriaBuilder
				.lessThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.strD), baseDate));

		// endDate >= base date
		lstpredicateWhere.add(criteriaBuilder
				.greaterThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KmnmtAffiliWorkplaceHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the employee workplace history
	 */
	private AffWorkplaceHistory toDomain(KmnmtAffiliWorkplaceHist entity) {
		return new AffWorkplaceHistory(
				new JpaAffWorkplaceHistoryGetMemento(entity));
	}

	@Override
	public List<AffWorkplaceHistory> searchWorkplaceHistory(List<String> employeeIds,
			GeneralDate baseDate, List<String> workplaces) {

		// check exist data
		if (CollectionUtil.isEmpty(employeeIds) || CollectionUtil.isEmpty(workplaces)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_WORKPLACE_HIST (KmnmtWorkplaceHist SQL)
		CriteriaQuery<KmnmtAffiliWorkplaceHist> cq = criteriaBuilder
				.createQuery(KmnmtAffiliWorkplaceHist.class);

		// root data
		Root<KmnmtAffiliWorkplaceHist> root = cq.from(KmnmtAffiliWorkplaceHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// employee id in data employee id
		lstpredicateWhere.add(
				criteriaBuilder.and(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.empId).in(employeeIds)));

		// work place in data work place
		lstpredicateWhere.add(
				criteriaBuilder.and(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.wkpId).in(workplaces)));

		// start date <= base date
		lstpredicateWhere.add(criteriaBuilder
				.lessThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.strD), baseDate));

		// endDate >= base date
		lstpredicateWhere.add(criteriaBuilder
				.greaterThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KmnmtAffiliWorkplaceHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.employee.workplace.
	 * AffiliationWorkplaceHistoryRepository#searchWorkplaceHistoryByEmployee(
	 * java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<AffWorkplaceHistory> searchWorkplaceHistoryByEmployee(String employeeId, 
			GeneralDate baseDate) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_WORKPLACE_HIST (KmnmtWorkplaceHist SQL)
		CriteriaQuery<KmnmtAffiliWorkplaceHist> cq = criteriaBuilder
				.createQuery(KmnmtAffiliWorkplaceHist.class);

		// root data
		Root<KmnmtAffiliWorkplaceHist> root = cq.from(KmnmtAffiliWorkplaceHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// employee id equal employee id
		lstpredicateWhere.add(
				criteriaBuilder.equal(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.empId), employeeId));

		// start date <= base date
		lstpredicateWhere.add(criteriaBuilder
				.lessThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.strD), baseDate));

		// endDate >= base date
		lstpredicateWhere.add(criteriaBuilder
				.greaterThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KmnmtAffiliWorkplaceHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceHistory> searchWorkplaceOfCompanyId(List<String> employeeIds,
			GeneralDate baseDate) {
		
		// check exist data
		if(CollectionUtil.isEmpty(employeeIds)){
			return new ArrayList<>();
		}
		
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_WORKPLACE_HIST (KmnmtWorkplaceHist SQL)
		CriteriaQuery<KmnmtAffiliWorkplaceHist> cq = criteriaBuilder
				.createQuery(KmnmtAffiliWorkplaceHist.class);

		// root data
		Root<KmnmtAffiliWorkplaceHist> root = cq.from(KmnmtAffiliWorkplaceHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// employee id in employee id 
		lstpredicateWhere.add(
				criteriaBuilder.and(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.empId).in(employeeIds)));

		// start date <= base date
		lstpredicateWhere.add(criteriaBuilder
				.lessThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.kmnmtAffiliWorkplaceHistPK)
						.get(KmnmtAffiliWorkplaceHistPK_.strD), baseDate));

		// endDate >= base date
		lstpredicateWhere.add(criteriaBuilder
				.greaterThanOrEqualTo(root.get(KmnmtAffiliWorkplaceHist_.endD), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KmnmtAffiliWorkplaceHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}
	/**
	 * ドメインモデル「所属職場」を削除する
	 * @param domain
	 */
	@Override
	public void deleteAffWorkplaceHistory(AffWorkplaceHistory domain){
		KmnmtAffiliWorkplaceHistPK key = new KmnmtAffiliWorkplaceHistPK(domain.getEmployeeId(),domain.getWorkplaceId().v(),domain.getPeriod().start());
		
		this.commandProxy().remove(KmnmtAffiliWorkplaceHist.class,key);
	}
}
