/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionrate;

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

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate_;

/**
 * The Class JpaPensionRateRepository.
 */
@Stateless
public class JpaPensionRateRepository extends JpaRepository implements PensionRateRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #add(nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate)
	 */
	@Override
	public void add(PensionRate rate) {
		EntityManager em = this.getEntityManager();

		QismtPensionRate entity = new QismtPensionRate();
		rate.saveToMemento(new JpaPensionRateSetMemento(entity));

		em.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #update(nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate)
	 */
	@Override
	public void update(PensionRate rate) {

		EntityManager em = this.getEntityManager();

		QismtPensionRate entity = em.find(QismtPensionRate.class, rate.getHistoryId());

		rate.saveToMemento(new JpaPensionRateSetMemento(entity));

		em.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #remove(java.lang.String)
	 */
	@Override
	public void remove(String historyId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtPensionRate> cq = cb.createQuery(QismtPensionRate.class);
		Root<QismtPensionRate> root = cq.from(QismtPensionRate.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtPensionRate_.histId), historyId));

		cq.where(predicateList.toArray(new Predicate[] {}));

		List<QismtPensionRate> result = em.createQuery(cq).getResultList();

		// If have no record.
		if (CollectionUtil.isEmpty(result)) {
			// not found delete element
			throw new BusinessException("ER010");
		}

		QismtPensionRate entity = result.get(0);

		em.remove(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #removeByOfficeCode(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeByOfficeCode(String companyCode, String officeCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<QismtPensionRate> cq = cb.createCriteriaDelete(QismtPensionRate.class);
		Root<QismtPensionRate> root = cq.from(QismtPensionRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtPensionRate_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtPensionRate_.siOfficeCd), officeCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		// perform update
		em.createQuery(cq).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #findAll(int)
	 */
	@Override
	public List<PensionRate> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtPensionRate> cq = cb.createQuery(QismtPensionRate.class);
		Root<QismtPensionRate> root = cq.from(QismtPensionRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtPensionRate_.ccd), companyCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		cq.orderBy(cb.desc(root.get(QismtPensionRate_.strYm)));

		return em.createQuery(cq).getResultList().stream()
				.map(item -> new PensionRate(new JpaPensionRateGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #findById(java.lang.String)
	 */
	@Override
	public Optional<PensionRate> findById(String id) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtPensionRate> cq = cb.createQuery(QismtPensionRate.class);
		Root<QismtPensionRate> root = cq.from(QismtPensionRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtPensionRate_.histId), id));

		cq.where(predicateList.toArray(new Predicate[] {}));
		return Optional.of(em.createQuery(cq).getResultList().stream()
				.map(item -> new PensionRate(new JpaPensionRateGetMemento(item)))
				.collect(Collectors.toList()).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * deleteHistory(java.lang.String)
	 */
	// for history common
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
	public Optional<PensionRate> findLastestHistoryByMasterCode(String companyCode,
			String officeCode) {

		List<PensionRate> lstPensionRate = this.findAllOffice(companyCode, officeCode);

		// if create first his for office
		if (lstPensionRate.isEmpty()) {
			return Optional.empty();
		}

		// create more his
		return Optional.of(this.findAllOffice(companyCode, officeCode).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findAllHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PensionRate> findAllHistoryByMasterCode(String companyCode, String officeCode) {
		return this.findAllOffice(companyCode, officeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findHistoryByUuid(java.lang.String)
	 */
	@Override
	public Optional<PensionRate> findHistoryByUuid(String uuid) {
		return this.findById(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * addHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void addHistory(PensionRate history) {
		this.add(history);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * updateHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void updateHistory(PensionRate history) {
		this.update(history);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository
	 * #findAllOffice(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PensionRate> findAllOffice(String companyCode, String officeCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtPensionRate> cq = cb.createQuery(QismtPensionRate.class);
		Root<QismtPensionRate> root = cq.from(QismtPensionRate.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtPensionRate_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtPensionRate_.siOfficeCd), officeCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		cq.orderBy(cb.desc(root.get(QismtPensionRate_.strYm)));

		return em.createQuery(cq).getResultList().stream()
				.map(item -> new PensionRate(new JpaPensionRateGetMemento(item)))
				.collect(Collectors.toList());
	}

}
