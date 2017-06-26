/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn;

import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnD;

/**
 * The Class JpaPensionAvgearnGetMemento.
 */
public class JpaPensionAvgearnGetMemento implements PensionAvgearnGetMemento {

	/** The avgearn detail. */
	private QismtPensionAvgearnD avgearnDetail;

	/** The pension amount. */
	private QismtPensionAmount pensionAmount;

	/**
	 * Instantiates a new jpa pension avgearn get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaPensionAvgearnGetMemento(QismtPensionAvgearnD avgearnDetail,
			QismtPensionAmount pensionAmount) {
		this.avgearnDetail = avgearnDetail;
		this.pensionAmount = pensionAmount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getPersonalPension()
	 */
	@Override
	public PensionAvgearnValue getPersonalPension() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getPPensionMaleMny()),
				new CommonAmount(this.pensionAmount.getPPensionFemMny()),
				new CommonAmount(this.pensionAmount.getPPensionMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getPersonalFundExemption()
	 */
	@Override
	public PensionAvgearnValue getPersonalFundExemption() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getPFundExemptMaleMny()),
				new CommonAmount(this.pensionAmount.getPFundExemptFemMny()),
				new CommonAmount(this.pensionAmount.getPFundExemptMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getPersonalFund()
	 */
	@Override
	public PensionAvgearnValue getPersonalFund() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getPFundMaleMny()),
				new CommonAmount(this.pensionAmount.getPFundFemMny()),
				new CommonAmount(this.pensionAmount.getPFundMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getGrade()
	 */
	@Override
	public Integer getGrade() {
		return this.pensionAmount.getQismtPensionAmountPK().getPensionGrade().intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.pensionAmount.getQismtPensionAmountPK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getCompanyPension()
	 */
	@Override
	public PensionAvgearnValue getCompanyPension() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getCPensionMaleMny()),
				new CommonAmount(this.pensionAmount.getCPensionFemMny()),
				new CommonAmount(this.pensionAmount.getCPensionMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getCompanyFundExemption()
	 */
	@Override
	public PensionAvgearnValue getCompanyFundExemption() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getCFundExemptMaleMny()),
				new CommonAmount(this.pensionAmount.getCFundExemptFemMny()),
				new CommonAmount(this.pensionAmount.getCFundExemptMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getCompanyFund()
	 */
	@Override
	public PensionAvgearnValue getCompanyFund() {
		return new PensionAvgearnValue(new CommonAmount(this.pensionAmount.getCFundMaleMny()),
				new CommonAmount(this.pensionAmount.getCFundFemMny()),
				new CommonAmount(this.pensionAmount.getCFundMinerMny()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getChildContributionAmount()
	 */
	@Override
	public CommonAmount getChildContributionAmount() {
		return new CommonAmount(this.pensionAmount.getChildContributionMny());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getAvgEarn()
	 */
	@Override
	public Long getAvgEarn() {
		return this.avgearnDetail.getPensionAvgEarn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnGetMemento#getUpperLimit()
	 */
	@Override
	public Long getUpperLimit() {
		return this.avgearnDetail.getPensionUpperLimit();
	}

}
