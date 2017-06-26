/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.infra.repository.insurance.labor.unemployeerate;

import java.util.ArrayList;
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
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRate;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRatePK;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRatePK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRate_;

/**
 * The Class JpaUnemployeeInsuranceRateRepository.
 */
@Stateless
public class JpaUnemployeeInsuranceRateRepository extends JpaRepository
	implements UnemployeeInsuranceRateRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#add(nts.uk.ctx.pr.core.dom.insurance.
	 * labor.unemployeerate.UnemployeeInsuranceRate)
	 */
	@Override
	public void add(UnemployeeInsuranceRate rate) {
		this.commandProxy().insert(this.toEntity(rate));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#update(nts.uk.ctx.pr.core.dom.insurance
	 * .labor.unemployeerate.UnemployeeInsuranceRate)
	 */
	@Override
	public void update(UnemployeeInsuranceRate rate) {
		this.commandProxy().update(this.toEntity(rate));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#remove(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public void remove(String companyCode, String historyId, Long version) {
		this.commandProxy().remove(QismtEmpInsuRate.class,
			new QismtEmpInsuRatePK(companyCode, historyId));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#findById(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Optional<UnemployeeInsuranceRate> findById(String companyCode, String historyId) {
		return this.queryProxy()
			.find(new QismtEmpInsuRatePK(companyCode, historyId), QismtEmpInsuRate.class)
			.map(c -> this.toDomain(c));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#findAll(nts.uk.ctx.core.dom.company.
	 * CompanyCode)
	 */
	@Override
	public List<UnemployeeInsuranceRate> findAll(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_EMP_INSU_RATE (QismtEmpInsuRate SQL)
		CriteriaQuery<QismtEmpInsuRate> cq = criteriaBuilder.createQuery(QismtEmpInsuRate.class);

		// root data
		Root<QismtEmpInsuRate> root = cq.from(QismtEmpInsuRate.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere.add(criteriaBuilder.equal(
			root.get(QismtEmpInsuRate_.qismtEmpInsuRatePK).get(QismtEmpInsuRatePK_.ccd),
			companyCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order by
		cq.orderBy(criteriaBuilder.desc(root.get(QismtEmpInsuRate_.strYm)));

		// creat query
		TypedQuery<QismtEmpInsuRate> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item))
			.collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the unemployee insurance rate
	 */
	private UnemployeeInsuranceRate toDomain(QismtEmpInsuRate entity) {
		return new UnemployeeInsuranceRate(new JpaUnemployeeInsuranceRateGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param rate
	 *            the rate
	 * @return the qismt emp insu rate
	 */
	private QismtEmpInsuRate toEntity(UnemployeeInsuranceRate rate) {
		QismtEmpInsuRate entity = new QismtEmpInsuRate();
		rate.saveToMemento(new JpaUnemployeeInsuranceRateSetMemento(entity));
		return entity;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#findBetweenUpdate(nts.uk.ctx.core.dom.
	 * company.CompanyCode, nts.arc.time.YearMonth)
	 */
	@Override
	public Optional<UnemployeeInsuranceRate> findBetweenUpdate(String companyCode,
		YearMonth yearMonth, String historyId) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_EMP_INSU_RATE (QismtEmpInsuRate SQL)
		CriteriaQuery<QismtEmpInsuRate> cq = criteriaBuilder.createQuery(QismtEmpInsuRate.class);

		// root data
		Root<QismtEmpInsuRate> root = cq.from(QismtEmpInsuRate.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere.add(criteriaBuilder.equal(
			root.get(QismtEmpInsuRate_.qismtEmpInsuRatePK).get(QismtEmpInsuRatePK_.ccd),
			companyCode));

		// not eq historyId
		lstpredicateWhere.add(criteriaBuilder.notEqual(
			root.get(QismtEmpInsuRate_.qismtEmpInsuRatePK).get(QismtEmpInsuRatePK_.histId),
			historyId));

		// le end
		lstpredicateWhere.add(
			criteriaBuilder.le(root.get(QismtEmpInsuRate_.endYm), yearMonth.previousMonth().v()));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		cq.orderBy(criteriaBuilder.desc(root.get(QismtEmpInsuRate_.strYm)));

		// exclude select
		TypedQuery<QismtEmpInsuRate> query = em.createQuery(cq);
		List<QismtEmpInsuRate> lstQismtEmpInsuRate = query.getResultList();

		// check exist data
		if (CollectionUtil.isEmpty(lstQismtEmpInsuRate)) {
			return Optional.empty();
		}

		// get first data
		return Optional
			.ofNullable(this.toDomain(lstQismtEmpInsuRate.get(BusinessTypeEnum.Biz1St.index)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateRepository#findFirstData(java.lang.String)
	 */
	@Override
	public Optional<UnemployeeInsuranceRate> findFirstData(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_EMP_INSU_RATE (QismtEmpInsuRate SQL)
		CriteriaQuery<QismtEmpInsuRate> cq = criteriaBuilder.createQuery(QismtEmpInsuRate.class);

		// root data
		Root<QismtEmpInsuRate> root = cq.from(QismtEmpInsuRate.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere.add(criteriaBuilder.equal(
			root.get(QismtEmpInsuRate_.qismtEmpInsuRatePK).get(QismtEmpInsuRatePK_.ccd),
			companyCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		cq.orderBy(criteriaBuilder.desc(root.get(QismtEmpInsuRate_.strYm)));

		// exclude select
		TypedQuery<QismtEmpInsuRate> query = em.createQuery(cq);
		List<QismtEmpInsuRate> lstQismtEmpInsuRate = query.getResultList();

		// check exist data
		if (CollectionUtil.isEmpty(lstQismtEmpInsuRate)) {
			return Optional.empty();
		}

		// get first data
		return Optional
			.ofNullable(this.toDomain(lstQismtEmpInsuRate.get(BusinessTypeEnum.Biz1St.index)));
	}

}
