/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn;

import java.math.BigDecimal;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnDPK;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmountPK;

/**
 * The Class JpaHealthInsuranceAvgearnSetMemento.
 */
public class JpaHealthInsuranceAvgearnSetMemento implements HealthInsuranceAvgearnSetMemento {

	/** The avgearn detail. */
	private QismtHealInsuAvgearnD avgearnDetail;

	/** The insu amount. */
	private QismtHealthInsuAmount insuAmount;

	/**
	 * Instantiates a new jpa health insurance avgearn set memento.
	 *
	 * @param companyCode
	 *            the company code
	 * @param siOfficeCd
	 *            the si office cd
	 * @param avgearnDetail
	 *            the avgearn detail
	 * @param insuAmount
	 *            the insu amount
	 */
	public JpaHealthInsuranceAvgearnSetMemento(String companyCode, String siOfficeCd,
			QismtHealInsuAvgearnD avgearnDetail, QismtHealthInsuAmount insuAmount) {
		this.avgearnDetail = avgearnDetail;
		this.insuAmount = insuAmount;
		this.avgearnDetail.setCcd(companyCode);
		this.insuAmount.setCcd(companyCode);
		this.insuAmount.setSiOfficeCd(siOfficeCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		QismtHealInsuAvgearnDPK healInsuAvgearnDPk = new QismtHealInsuAvgearnDPK();
		healInsuAvgearnDPk.setHistId(historyId);
		this.avgearnDetail.setQismtHealInsuAvgearnDPK(healInsuAvgearnDPk);

		QismtHealthInsuAmountPK healthInsuAvgearnPK = new QismtHealthInsuAmountPK();
		healthInsuAvgearnPK.setHistId(historyId);
		this.insuAmount.setQismtHealthInsuAmountPK(healthInsuAvgearnPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setGrade(java.lang.Integer)
	 */
	@Override
	public void setGrade(Integer grade) {
		QismtHealInsuAvgearnDPK healInsuAvgearnDPk = this.avgearnDetail
				.getQismtHealInsuAvgearnDPK();
		healInsuAvgearnDPk.setHealthInsuGrade(BigDecimal.valueOf(grade));
		this.avgearnDetail.setQismtHealInsuAvgearnDPK(healInsuAvgearnDPk);

		QismtHealthInsuAmountPK healthInsuAvgearnPK = this.insuAmount.getQismtHealthInsuAmountPK();
		healthInsuAvgearnPK.setHealthInsuGrade(BigDecimal.valueOf(grade));
		this.insuAmount.setQismtHealthInsuAmountPK(healthInsuAvgearnPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setCompanyAvg(nts.uk.ctx.pr.core.dom.
	 * insurance.social.healthavgearn.HealthInsuranceAvgearnValue)
	 */
	@Override
	public void setCompanyAvg(HealthInsuranceAvgearnValue companyAvg) {
		this.insuAmount.setCHealthBasicMny(companyAvg.getHealthBasicMny().v());
		this.insuAmount.setCHealthGeneralMny(companyAvg.getHealthGeneralMny().v());
		this.insuAmount.setCHealthNursingMny(companyAvg.getHealthNursingMny().v());
		this.insuAmount.setCHealthSpecificMny(companyAvg.getHealthSpecificMny().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setPersonalAvg(nts.uk.ctx.pr.core.dom.
	 * insurance.social.healthavgearn.HealthInsuranceAvgearnValue)
	 */
	@Override
	public void setPersonalAvg(HealthInsuranceAvgearnValue personalAvg) {
		this.insuAmount.setPHealthBasicMny(personalAvg.getHealthBasicMny().v());
		this.insuAmount.setPHealthGeneralMny(personalAvg.getHealthGeneralMny().v());
		this.insuAmount.setPHealthNursingMny(personalAvg.getHealthNursingMny().v());
		this.insuAmount.setPHealthSpecificMny(personalAvg.getHealthSpecificMny().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setAvgEarn(java.lang.Long)
	 */
	@Override
	public void setAvgEarn(Long avgEarn) {
		this.avgearnDetail.setHealthInsuAvgEarn(avgEarn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnSetMemento#setUpperLimit(java.lang.Long)
	 */
	@Override
	public void setUpperLimit(Long upperLimit) {
		this.avgearnDetail.setHealthInsuUpperLimit(upperLimit);
	}
}
