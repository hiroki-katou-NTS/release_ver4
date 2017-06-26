/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.certification;

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
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyG;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyGPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyGPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyG_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify_;

/**
 * The Class JpaCertifyGroupRepository.
 */
@Stateless
public class JpaCertifyGroupRepository extends JpaRepository implements CertifyGroupRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupRepository#add(nts.uk.ctx.pr
	 * .core.dom.wagetable.CertifyGroup)
	 */
	@Override
	public void add(CertifyGroup certifyGroup) {
		this.commandProxy().insert(toEntity(certifyGroup));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupRepository#update(nts.uk.ctx
	 * .pr.core.dom.wagetable.CertifyGroup)
	 */
	@Override
	public void update(CertifyGroup certifyGroup) {
		this.commandProxy().update(toEntity(certifyGroup));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupRepository#remove(nts.uk.ctx
	 * .core.dom.company.CompanyCode, java.lang.String, java.lang.Long)
	 */
	@Override
	public void remove(String companyCode, String groupCode) {
		this.commandProxy().remove(QwtmtWagetableCertifyG.class,
			new QwtmtWagetableCertifyGPK(companyCode, groupCode));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupRepository#findById(nts.uk.
	 * ctx.core.dom.company.CompanyCode, java.lang.String)
	 */
	@Override
	public Optional<CertifyGroup> findById(String companyCode, String code) {
		return this.queryProxy()
			.find(new QwtmtWagetableCertifyGPK(companyCode, code), QwtmtWagetableCertifyG.class)
			.map(c -> new CertifyGroup(new JpaCertifyGroupGetMemento(c)));
	}

	/**
	 * To entity.
	 *
	 * @param certifyGroup
	 *            the certify group
	 * @return the qwtmt wagetable certify G
	 */
	private QwtmtWagetableCertifyG toEntity(CertifyGroup certifyGroup) {
		QwtmtWagetableCertifyG entity = new QwtmtWagetableCertifyG();
		certifyGroup.saveToMemento(new JpaCertifyGroupSetMemento(entity));
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupRepository#findAll(nts.uk.
	 * ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public List<CertifyGroup> findAll(String companyCode) {

		// get entity manager
		EntityManager em = getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QWTMT_WAGETABLE_CERTIFY_G (QwtmtWagetableCertifyG SQL)
		CriteriaQuery<QwtmtWagetableCertifyG> cq = criteriaBuilder
			.createQuery(QwtmtWagetableCertifyG.class);

		// root data
		Root<QwtmtWagetableCertifyG> root = cq.from(QwtmtWagetableCertifyG.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QwtmtWagetableCertifyG_.qwtmtWagetableCertifyGPK)
				.get(QwtmtWagetableCertifyGPK_.ccd), companyCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<QwtmtWagetableCertifyG> query = em.createQuery(cq);

		// exclude select
		List<CertifyGroup> lstCertifyGroup = query.getResultList().stream()
			.map(item -> new CertifyGroup(new JpaCertifyGroupGetMemento(item)))
			.collect(Collectors.toList());
		return lstCertifyGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationReponsitory#
	 * findById(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean isBelongToExistGroup(String companyCode, String groupCode, String certifyCode) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// QWTMT_WAGETABLE_CERTIFY (QwtmtWagetableCertify) SQL
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);

		// root data QwtmtWagetableCertify
		Root<QwtmtWagetableCertify> root = cq.from(QwtmtWagetableCertify.class);

		// select count(*) root (SQL SELECT)
		cq.select(criteriaBuilder.count(root));

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode (where)
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QwtmtWagetableCertify_.qwtmtWagetableCertifyPK)
				.get(QwtmtWagetableCertifyPK_.ccd), companyCode));

		// eq CerticationCode (where)
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QwtmtWagetableCertify_.qwtmtWagetableCertifyPK)
				.get(QwtmtWagetableCertifyPK_.certifyCd), certifyCode));

		lstpredicateWhere
			.add(criteriaBuilder.notEqual(root.get(QwtmtWagetableCertify_.qwtmtWagetableCertifyPK)
				.get(QwtmtWagetableCertifyPK_.certifyGroupCd), groupCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		return (em.createQuery(cq).getSingleResult() > 0);
	}
}
