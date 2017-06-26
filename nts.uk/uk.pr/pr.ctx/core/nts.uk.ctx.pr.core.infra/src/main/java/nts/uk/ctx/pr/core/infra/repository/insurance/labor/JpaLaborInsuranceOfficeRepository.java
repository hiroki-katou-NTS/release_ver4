/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor;

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
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOffice;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOfficePK;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOfficePK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOffice_;

/**
 * The Class this.
 */
@Stateless
public class JpaLaborInsuranceOfficeRepository extends JpaRepository
	implements LaborInsuranceOfficeRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository#add
	 * (nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice)
	 */
	@Override
	public void add(LaborInsuranceOffice office) {
		this.commandProxy().insert(this.toEntity(office));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository#
	 * update(nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice)
	 */
	@Override
	public void update(LaborInsuranceOffice office) {
		this.commandProxy().update(this.toEntity(office));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository#
	 * findAll(java.lang.String)
	 */
	@Override
	public List<LaborInsuranceOffice> findAll(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_LABOR_INSU_OFFICE (QismtLaborInsuOffice SQL)
		CriteriaQuery<QismtLaborInsuOffice> cq = criteriaBuilder.createQuery(QismtLaborInsuOffice.class);

		// root data
		Root<QismtLaborInsuOffice> root = cq.from(QismtLaborInsuOffice.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq CompanyCode
		lstpredicateWhere.add(criteriaBuilder.equal(
			root.get(QismtLaborInsuOffice_.qismtLaborInsuOfficePK).get(QismtLaborInsuOfficePK_.ccd),
			companyCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<QismtLaborInsuOffice> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository#
	 * findById(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<LaborInsuranceOffice> findById(String companyCode, String officeCode) {
		return this.queryProxy()
			.find(new QismtLaborInsuOfficePK(companyCode, officeCode), QismtLaborInsuOffice.class)
			.map(c -> this.toDomain(c));
	}

	/**
	 * To entity.
	 *
	 * @param laborInsuranceOffice
	 *            the labor insurance office
	 * @return the qismt labor insu office
	 */
	private QismtLaborInsuOffice toEntity(LaborInsuranceOffice laborInsuranceOffice) {
		QismtLaborInsuOffice entity = new QismtLaborInsuOffice();
		laborInsuranceOffice.saveToMemento(new JpaLaborInsuranceOfficeSetMemento(entity));
		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the labor insurance office
	 */
	private LaborInsuranceOffice toDomain(QismtLaborInsuOffice entity) {
		return new LaborInsuranceOffice(new JpaLaborInsuranceOfficeGetMemento(entity));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository#
	 * remove(nts.uk.ctx.core.dom.company.CompanyCode, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public void remove(String companyCode, String officeCode) {
		this.commandProxy().remove(QismtLaborInsuOffice.class,
			new QismtLaborInsuOfficePK(companyCode, officeCode));
	}
}
