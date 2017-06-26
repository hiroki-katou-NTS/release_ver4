/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthrate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate_;

/**
 * The Class JpaHealthInsuranceRateRepository.
 */
@Stateless
public class JpaHealthInsuranceRateRepository extends JpaRepository
		implements HealthInsuranceRateRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#add(nts.uk.ctx.pr.core.dom.insurance.social
	 * .healthrate.HealthInsuranceRate)
	 */
	@Override
	public void add(HealthInsuranceRate rate) {
		EntityManager em = this.getEntityManager();
		em.persist(this.toEntity(rate));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#update(nts.uk.ctx.pr.core.dom.insurance.
	 * social.healthrate.HealthInsuranceRate)
	 */
	@Override
	public void update(HealthInsuranceRate rate) {
		EntityManager em = this.getEntityManager();

		QismtHealthInsuRate entity = em.find(QismtHealthInsuRate.class, rate.getHistoryId());

		rate.saveToMemento(new JpaHealthInsuranceRateSetMemento(entity));

		em.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#remove(java.lang.String, java.lang.Long)
	 */
	@Override
	public void remove(String historyId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtHealthInsuRate> cq = cb.createQuery(QismtHealthInsuRate.class);
		Root<QismtHealthInsuRate> root = cq.from(QismtHealthInsuRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.histId), historyId));

		cq.where(predicateList.toArray(new Predicate[] {}));

		List<QismtHealthInsuRate> result = em.createQuery(cq).getResultList();

		// If have no record.
		if (CollectionUtil.isEmpty(result)) {
			return;
		}

		QismtHealthInsuRate entity = new QismtHealthInsuRate();

		entity = result.get(0);

		em.remove(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#removeByOfficeCode(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void removeByOfficeCode(String companyCode, String officeCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<QismtHealthInsuRate> cq = cb.createCriteriaDelete(QismtHealthInsuRate.class);
		Root<QismtHealthInsuRate> root = cq.from(QismtHealthInsuRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.siOfficeCd), officeCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		// perform update
		em.createQuery(cq).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#findAll(java.lang.String, java.lang.String)
	 */
	@Override
	public List<HealthInsuranceRate> findAll(String companyCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtHealthInsuRate> cq = cb.createQuery(QismtHealthInsuRate.class);
		Root<QismtHealthInsuRate> root = cq.from(QismtHealthInsuRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.ccd), companyCode));

		cq.where(predicateList.toArray(new Predicate[] {}));
		cq.orderBy(cb.desc(root.get(QismtHealthInsuRate_.strYm)));
		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#findById(java.lang.String)
	 */
	@Override
	public Optional<HealthInsuranceRate> findById(String historyId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtHealthInsuRate> cq = cb.createQuery(QismtHealthInsuRate.class);
		Root<QismtHealthInsuRate> root = cq.from(QismtHealthInsuRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.histId), historyId));

		cq.where(predicateList.toArray(new Predicate[] {}));
		return Optional.of(em.createQuery(cq).getResultList().stream()
				.map(item -> this.toDomain(item)).collect(Collectors.toList()).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateRepository#findAllOffice(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<HealthInsuranceRate> findAllOffice(String companyCode, String officeCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtHealthInsuRate> cq = cb.createQuery(QismtHealthInsuRate.class);
		Root<QismtHealthInsuRate> root = cq.from(QismtHealthInsuRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtHealthInsuRate_.siOfficeCd), officeCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		cq.orderBy(cb.desc(root.get(QismtHealthInsuRate_.strYm)));

		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * deleteHistory(java.lang.String)
	 */
	@Override
	public void deleteHistory(String uuid) {
		this.remove(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findLastestHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<HealthInsuranceRate> findLastestHistoryByMasterCode(String companyCode,
			String officeCode) {

		List<HealthInsuranceRate> lstHealthInsuranceRate = this.findAllOffice(companyCode,
				officeCode);

		// if create first his of office
		if (lstHealthInsuranceRate.isEmpty()) {
			return Optional.empty();
		}

		// add more his
		return Optional.of(this.findAllOffice(companyCode, officeCode).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findHistoryByUuid(java.lang.String)
	 */
	// for history common
	@Override
	public Optional<HealthInsuranceRate> findHistoryByUuid(String uuid) {
		return this.findById(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * addHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void addHistory(HealthInsuranceRate history) {
		this.add(history);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * updateHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void updateHistory(HealthInsuranceRate history) {
		this.update(history);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findAllHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<HealthInsuranceRate> findAllHistoryByMasterCode(String companyCode,
			String officeCode) {
		return this.findAllOffice(companyCode, officeCode);
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the health insurance rate
	 */
	private HealthInsuranceRate toDomain(QismtHealthInsuRate entity) {
		HealthInsuranceRate domain = new HealthInsuranceRate(
				new JpaHealthInsuranceRateGetMemento(entity));
		return domain;
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the qismt health insu rate
	 */
	private QismtHealthInsuRate toEntity(HealthInsuranceRate domain) {
		QismtHealthInsuRate entity = new QismtHealthInsuRate();
		domain.saveToMemento(new JpaHealthInsuranceRateSetMemento(entity));
		return entity;
	}
}
