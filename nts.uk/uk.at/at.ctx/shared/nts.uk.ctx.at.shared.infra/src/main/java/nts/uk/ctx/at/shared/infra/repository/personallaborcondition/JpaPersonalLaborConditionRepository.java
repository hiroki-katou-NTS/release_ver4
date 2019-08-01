/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.personallaborcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborConditionRepository;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerDayOfWeek;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerDayOfWeekPK_;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerDayOfWeek_;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerLaborCond;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerLaborCondPK_;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerLaborCond_;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerWorkCategory;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerWorkCategoryPK_;
import nts.uk.ctx.at.shared.infra.entity.personallaborcondition.KshmtPerWorkCategory_;

/**
 * The Class JpaPersonalLaborConditionRepository.
 */
@Stateless
public class JpaPersonalLaborConditionRepository extends JpaRepository
		implements
			PersonalLaborConditionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalLaborConditionRepository#findById(java.lang.String,
	 * nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public Optional<PersonalLaborCondition> findById(String employeeId, GeneralDate baseDate) {

		Optional<KshmtPerLaborCond> optionalEntityCondition = this.findByIdCondition(employeeId,
				baseDate);
		if (optionalEntityCondition.isPresent()) {
			return Optional.of(this.toDomain(optionalEntityCondition.get(),
					this.findAllDayOfWeek(employeeId, baseDate),
					this.findAllWorkCategory(employeeId, baseDate)));
		}
		return Optional.empty();
	}
	
	/**
	 * Find all day of week.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	private List<KshmtPerDayOfWeek> findAllDayOfWeek(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHMT_PER_DAY_OF_WEEK (KshmtPerDayOfWeek SQL)
		CriteriaQuery<KshmtPerDayOfWeek> cq = criteriaBuilder.createQuery(KshmtPerDayOfWeek.class);

		// root data
		Root<KshmtPerDayOfWeek> root = cq.from(KshmtPerDayOfWeek.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshmtPerDayOfWeek_.kshmtPerDayOfWeekPK).get(KshmtPerDayOfWeekPK_.sid),
				employeeId));

		// less than or equal start year month date
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KshmtPerDayOfWeek_.kshmtPerDayOfWeekPK).get(KshmtPerDayOfWeekPK_.startYmd),
				baseDate));

		// greater than or equal end year month date
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(KshmtPerDayOfWeek_.kshmtPerDayOfWeekPK).get(KshmtPerDayOfWeekPK_.endYmd),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

		// create query
		TypedQuery<KshmtPerDayOfWeek> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}
	
	/**
	 * Find all work category.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	private List<KshmtPerWorkCategory> findAllWorkCategory(String employeeId,
			GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHMT_PER_WORK_CATEGORY (KshmtPerWorkCategory SQL)
		CriteriaQuery<KshmtPerWorkCategory> cq = criteriaBuilder
				.createQuery(KshmtPerWorkCategory.class);

		// root data
		Root<KshmtPerWorkCategory> root = cq.from(KshmtPerWorkCategory.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KshmtPerWorkCategory_.kshmtPerWorkCategoryPK)
						.get(KshmtPerWorkCategoryPK_.sid), employeeId));

		// less than or equal start year month date
		lstpredicateWhere.add(criteriaBuilder
				.lessThanOrEqualTo(root.get(KshmtPerWorkCategory_.kshmtPerWorkCategoryPK)
						.get(KshmtPerWorkCategoryPK_.startYmd), baseDate));

		// greater than or equal end year month date
		lstpredicateWhere.add(criteriaBuilder
				.greaterThanOrEqualTo(root.get(KshmtPerWorkCategory_.kshmtPerWorkCategoryPK)
						.get(KshmtPerWorkCategoryPK_.endYmd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

		// create query
		TypedQuery<KshmtPerWorkCategory> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}
	
	
	/**
	 * Find by id condition.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	private Optional<KshmtPerLaborCond> findByIdCondition(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHMT_PER_LABOR_COND (KshmtPerLaborCond SQL)
		CriteriaQuery<KshmtPerLaborCond> cq = criteriaBuilder.createQuery(KshmtPerLaborCond.class);

		// root data
		Root<KshmtPerLaborCond> root = cq.from(KshmtPerLaborCond.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshmtPerLaborCond_.kshmtPerLaborCondPK).get(KshmtPerLaborCondPK_.sid),
				employeeId));

		// less than or equal start year month date
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KshmtPerLaborCond_.kshmtPerLaborCondPK).get(KshmtPerLaborCondPK_.startYmd),
				baseDate));

		// greater than or equal end year month date
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(KshmtPerLaborCond_.kshmtPerLaborCondPK).get(KshmtPerLaborCondPK_.endYmd),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

		// create query
		TypedQuery<KshmtPerLaborCond> query = em.createQuery(cq);

		try {
			// exclude select
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			//return Optional.empty();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * To domain.
	 *
	 * @param entityCondition the entity condition
	 * @param entityDayOfWeeks the entity day of weeks
	 * @param entityWorkCategorys the entity work categorys
	 * @return the personal labor condition
	 */
	private PersonalLaborCondition toDomain(KshmtPerLaborCond entityCondition,
			List<KshmtPerDayOfWeek> entityDayOfWeeks,
			List<KshmtPerWorkCategory> entityWorkCategorys) {
		return new PersonalLaborCondition(
				new JpaPersonalLaborConditionGetMemento(entityCondition, entityDayOfWeeks,
						entityWorkCategorys));
	}
}
