/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.certification;

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
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertification;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertificationPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertification_;

/**
 * The Class JpaCertificationRepository.
 */
@Stateless
public class JpaCertificationRepository extends JpaRepository implements CertificationRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationReponsitory#
	 * findAll(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	// Function get all Certification by companyCode
	@Override
	public List<Certification> findAll(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QCEMT_CERTIFICATION (QcemtCertification SQL)
		CriteriaQuery<QcemtCertification> cq = criteriaBuilder
				.createQuery(QcemtCertification.class);

		// root data
		Root<QcemtCertification> root = cq.from(QcemtCertification.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(QcemtCertification_.qcemtCertificationPK).get(QcemtCertificationPK_.ccd),
				companyCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<QcemtCertification> query = em.createQuery(cq);

		// exclude select
		List<Certification> lstCertification = query.getResultList().stream()
				.map(item -> new Certification(new JpaCertificationGetMemento(item)))
				.collect(Collectors.toList());

		return lstCertification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationReponsitory#
	 * findAllNoneOfGroup(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	// Function get all Certification none of Group
	@Override
	public List<Certification> findAllNoneOfGroup(String companyCode) {

		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QCEMT_CERTIFICATION (QcemtCertification SQL)
		CriteriaQuery<QcemtCertification> cq = criteriaBuilder
				.createQuery(QcemtCertification.class);

		// root data
		Root<QcemtCertification> root = cq.from(QcemtCertification.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode (where)
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(QcemtCertification_.qcemtCertificationPK).get(QcemtCertificationPK_.ccd),
				companyCode));
		lstpredicateWhere.add(
				criteriaBuilder.isEmpty(root.get(QcemtCertification_.qwtmtWagetableCertifyList)));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<QcemtCertification> query = em.createQuery(cq);

		// exclude select
		List<Certification> lstCertification = query.getResultList().stream()
				.map(item -> new Certification(new JpaCertificationGetMemento(item)))
				.collect(Collectors.toList());

		return lstCertification;
	}

}
