/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn;

import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.InsuranceAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount;

/**
 * The Class JpaHealthInsuranceAvgearnGetMemento.
 */
public class JpaHealthInsuranceAvgearnGetMemento implements HealthInsuranceAvgearnGetMemento {

	/** The insu amount. */
	private QismtHealthInsuAmount insuAmount;

	/** The avgearn detail. */
	private QismtHealInsuAvgearnD avgearnDetail;

	/**
	 * Instantiates a new jpa health insurance avgearn get memento.
	 *
	 * @param insuAmount
	 *            the insu amount
	 * @param avgearnDetail
	 *            the avgearn detail
	 */
	public JpaHealthInsuranceAvgearnGetMemento(QismtHealInsuAvgearnD avgearnDetail,
			QismtHealthInsuAmount insuAmount) {
		this.insuAmount = insuAmount;
		this.avgearnDetail = avgearnDetail;
	}

	/**
	 * Gets the personal avg.
	 *
	 * @return the personal avg
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnGetMemento#getPersonalAvg()
	 */
	@Override
	public HealthInsuranceAvgearnValue getPersonalAvg() {
		return new HealthInsuranceAvgearnValue(
				new InsuranceAmount(this.insuAmount.getPHealthBasicMny()),
				new CommonAmount(this.insuAmount.getPHealthGeneralMny()),
				new CommonAmount(this.insuAmount.getPHealthNursingMny()),
				new InsuranceAmount(this.insuAmount.getPHealthSpecificMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnGetMemento#getGrade()
	 */
	@Override
	public Integer getGrade() {
		return this.insuAmount.getQismtHealthInsuAmountPK().getHealthInsuGrade().intValue();
	}

	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.insuAmount.getQismtHealthInsuAmountPK().getHistId();
	}

	/**
	 * Gets the company avg.
	 *
	 * @return the company avg
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnGetMemento#getCompanyAvg()
	 */
	@Override
	public HealthInsuranceAvgearnValue getCompanyAvg() {
		return new HealthInsuranceAvgearnValue(
				new InsuranceAmount(this.insuAmount.getCHealthBasicMny()),
				new CommonAmount(this.insuAmount.getCHealthGeneralMny()),
				new CommonAmount(this.insuAmount.getCHealthNursingMny()),
				new InsuranceAmount(this.insuAmount.getCHealthSpecificMny()));
	}

	/**
	 * Gets the avg earn.
	 *
	 * @return the avg earn
	 */
	@Override
	public Long getAvgEarn() {
		return this.avgearnDetail.getHealthInsuAvgEarn();
	}

	/**
	 * Gets the upper limit.
	 *
	 * @return the upper limit
	 */
	@Override
	public Long getUpperLimit() {
		return this.avgearnDetail.getHealthInsuUpperLimit();
	}

}
