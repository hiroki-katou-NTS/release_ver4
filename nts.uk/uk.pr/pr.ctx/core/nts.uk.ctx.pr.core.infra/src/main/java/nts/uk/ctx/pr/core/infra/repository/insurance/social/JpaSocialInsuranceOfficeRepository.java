/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOffice;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOfficePK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOffice_;

/**
 * The Class JpaSocialInsuranceOfficeRepository.
 */
@Stateless
public class JpaSocialInsuranceOfficeRepository extends JpaRepository
		implements SocialInsuranceOfficeRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * add(nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice)
	 */
	@Override
	public void add(SocialInsuranceOffice office) {
		EntityManager em = this.getEntityManager();

		QismtSocialInsuOffice entity = new QismtSocialInsuOffice();
		office.saveToMemento(new JpaSocialInsuranceOfficeSetMemento(entity));

		em.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * update(nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice)
	 */
	@Override
	public void update(SocialInsuranceOffice office) {
		EntityManager em = this.getEntityManager();

		QismtSocialInsuOffice entity = new QismtSocialInsuOffice();
		office.saveToMemento(new JpaSocialInsuranceOfficeSetMemento(entity));

		em.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * remove(java.lang.String, java.lang.Long)
	 */
	@Override
	public void remove(String companyCode, OfficeCode officeCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtSocialInsuOffice> cq = cb.createQuery(QismtSocialInsuOffice.class);
		Root<QismtSocialInsuOffice> root = cq.from(QismtSocialInsuOffice.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.siOfficeCd), officeCode));
		cq.where(predicateList.toArray(new Predicate[] {}));
		List<QismtSocialInsuOffice> result = em.createQuery(cq).getResultList();
		
		// If have no record.
		if (!CollectionUtil.isEmpty(result)) {
			QismtSocialInsuOffice entity = new QismtSocialInsuOffice();
			entity = result.get(0);
			em.remove(entity);
		} else {
			throw new BusinessException("ER010");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * findAll(int)
	 */
	@Override
	public List<SocialInsuranceOffice> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtSocialInsuOffice> cq = cb.createQuery(QismtSocialInsuOffice.class);
		Root<QismtSocialInsuOffice> root = cq.from(QismtSocialInsuOffice.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.ccd), companyCode));
		cq.where(predicateList.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream().map(
				item -> new SocialInsuranceOffice(new JpaSocialInsuranceOfficeGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * findByOfficeCode(java.lang.String)
	 */
	@Override
	public Optional<SocialInsuranceOffice> findByOfficeCode(String companyCode,
			OfficeCode officeCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtSocialInsuOffice> cq = cb.createQuery(QismtSocialInsuOffice.class);
		Root<QismtSocialInsuOffice> root = cq.from(QismtSocialInsuOffice.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.siOfficeCd), officeCode));
		cq.where(predicateList.toArray(new Predicate[] {}));
		List<QismtSocialInsuOffice> result = em.createQuery(cq).getResultList();

		// If have no record.
		if (CollectionUtil.isEmpty(result)) {
			return null;
		}

		// Return
		SocialInsuranceOffice socialInsuranceOffice = new SocialInsuranceOffice(
				new JpaSocialInsuranceOfficeGetMemento(result.get(0)));
		return Optional.of(socialInsuranceOffice);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository#
	 * isDuplicateCode(nts.uk.ctx.pr.core.dom.insurance.OfficeCode)
	 */
	@Override
	public boolean isDuplicateCode(String companyCode, OfficeCode officeCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QismtSocialInsuOffice> cq = cb.createQuery(QismtSocialInsuOffice.class);
		Root<QismtSocialInsuOffice> root = cq.from(QismtSocialInsuOffice.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QismtSocialInsuOffice_.qismtSocialInsuOfficePK)
				.get(QismtSocialInsuOfficePK_.siOfficeCd), officeCode));
		cq.where(predicateList.toArray(new Predicate[] {}));
		List<QismtSocialInsuOffice> result = em.createQuery(cq).getResultList();

		// If have no record.
		if (CollectionUtil.isEmpty(result)) {
			return false;
		}

		return true;
	}
}
