/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingRepository;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstPrintSet;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstPrintSet_;
import nts.uk.ctx.pr.report.infra.repository.salarydetail.memento.JpaSalaryPrintSettingGetMemento;
import nts.uk.ctx.pr.report.infra.repository.salarydetail.memento.JpaSalaryPrintSettingSetMemento;

/**
 * The Class JpaSalaryPrintSettingRepository.
 */
@Stateless
public class JpaSalaryPrintSettingRepository extends JpaRepository implements SalaryPrintSettingRepository {

	/**
	 * Find.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the salary print setting
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.report.dom.salarydetail.SalaryPrintSettingRepository#find(
	 * java.lang.String)
	 */
	@Override
	public SalaryPrintSetting find(String companyCode) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QlsptPaylstPrintSet> cq = cb.createQuery(QlsptPaylstPrintSet.class);
		Root<QlsptPaylstPrintSet> root = cq.from(QlsptPaylstPrintSet.class);

		// Construct condition.
		cq.where(cb.equal(root.get(QlsptPaylstPrintSet_.ccd), companyCode));

		List<QlsptPaylstPrintSet> result = em.createQuery(cq).getResultList();

		// Return default setting if result is empty.
		if (CollectionUtil.isEmpty(result)) {
			return SalaryPrintSetting.createWithIntial(companyCode);
		}

		return this.toDomain(result.get(0));
	}

	/**
	 * Save.
	 *
	 * @param salaryPrintSetting
	 *            the salary print setting
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.report.dom.salarydetail.SalaryPrintSettingRepository#save(
	 * nts.uk.ctx.pr.report.dom.salarydetail.SalaryPrintSetting)
	 */
	@Override
	public void save(SalaryPrintSetting salaryPrintSetting) {
		this.commandProxy().update(this.toEntity(salaryPrintSetting));
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the salary print setting
	 */
	private SalaryPrintSetting toDomain(QlsptPaylstPrintSet entity) {
		return new SalaryPrintSetting(new JpaSalaryPrintSettingGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the qlspt paylst print set
	 */
	private QlsptPaylstPrintSet toEntity(SalaryPrintSetting domain) {
		QlsptPaylstPrintSet entity = new QlsptPaylstPrintSet();
		domain.saveToMemento(new JpaSalaryPrintSettingSetMemento(entity));
		return entity;
	}

}
