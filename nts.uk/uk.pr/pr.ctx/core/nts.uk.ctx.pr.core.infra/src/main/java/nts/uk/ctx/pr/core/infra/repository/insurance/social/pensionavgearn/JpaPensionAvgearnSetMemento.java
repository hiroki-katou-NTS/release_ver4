/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn;

import java.math.BigDecimal;

import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmountPK;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnDPK;

/**
 * The Class JpaPensionAvgearnSetMemento.
 */
public class JpaPensionAvgearnSetMemento implements PensionAvgearnSetMemento {

	/** The avgearn detail. */
	private QismtPensionAvgearnD avgearnDetail;

	/** The pension amount. */
	private QismtPensionAmount pensionAmount;

	/**
	 * Instantiates a new jpa pension avgearn set memento.
	 *
	 * @param ccd
	 *            the ccd
	 * @param officeCd
	 *            the office cd
	 * @param avgearnDetail
	 *            the avgearn detail
	 * @param pensionAmount
	 *            the pension amount
	 */
	public JpaPensionAvgearnSetMemento(String ccd, String officeCd,
			QismtPensionAvgearnD avgearnDetail, QismtPensionAmount pensionAmount) {
		this.avgearnDetail = avgearnDetail;
		this.pensionAmount = pensionAmount;

		this.avgearnDetail.setCcd(ccd);
		this.pensionAmount.setCcd(ccd);
		this.pensionAmount.setSiOfficeCd(officeCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		QismtPensionAvgearnDPK pensionAvgearnDPK = new QismtPensionAvgearnDPK();
		pensionAvgearnDPK.setHistId(historyId);
		this.avgearnDetail.setQismtPensionAvgearnDPK(pensionAvgearnDPK);

		QismtPensionAmountPK pensionAmountPK = new QismtPensionAmountPK();
		pensionAmountPK.setHistId(historyId);
		this.pensionAmount.setQismtPensionAmountPK(pensionAmountPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setGrade(java.lang.Integer)
	 */
	@Override
	public void setGrade(Integer grade) {
		QismtPensionAvgearnDPK pensionAvgearnDPK = this.avgearnDetail.getQismtPensionAvgearnDPK();
		pensionAvgearnDPK.setPensionGrade(BigDecimal.valueOf(grade));
		this.avgearnDetail.setQismtPensionAvgearnDPK(pensionAvgearnDPK);

		QismtPensionAmountPK pensionAmountPK = this.pensionAmount.getQismtPensionAmountPK();
		pensionAmountPK.setPensionGrade(BigDecimal.valueOf(grade));
		this.pensionAmount.setQismtPensionAmountPK(pensionAmountPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setChildContributionAmount(nts.uk.ctx.pr.core.
	 * dom.insurance.InsuranceAmount)
	 */
	@Override
	public void setChildContributionAmount(CommonAmount childContributionAmount) {
		this.pensionAmount.setChildContributionMny(childContributionAmount.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setCompanyFund(nts.uk.ctx.pr.core.dom.insurance.
	 * social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setCompanyFund(PensionAvgearnValue companyFund) {
		this.pensionAmount.setCFundFemMny(companyFund.getFemaleAmount().v());
		this.pensionAmount.setCFundMaleMny(companyFund.getMaleAmount().v());
		this.pensionAmount.setCFundMinerMny(companyFund.getUnknownAmount().v());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setCompanyFundExemption(nts.uk.ctx.pr.core.dom.
	 * insurance.social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setCompanyFundExemption(PensionAvgearnValue companyFundExemption) {
		this.pensionAmount.setCFundExemptFemMny(companyFundExemption.getFemaleAmount().v());
		this.pensionAmount.setCFundExemptMaleMny(companyFundExemption.getMaleAmount().v());
		this.pensionAmount.setCFundExemptMinerMny(companyFundExemption.getUnknownAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setCompanyPension(nts.uk.ctx.pr.core.dom.
	 * insurance.social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setCompanyPension(PensionAvgearnValue companyPension) {
		this.pensionAmount.setCPensionFemMny(companyPension.getFemaleAmount().v());
		this.pensionAmount.setCPensionMaleMny(companyPension.getMaleAmount().v());
		this.pensionAmount.setCPensionMinerMny(companyPension.getUnknownAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setPersonalFund(nts.uk.ctx.pr.core.dom.insurance
	 * .social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setPersonalFund(PensionAvgearnValue personalFund) {
		this.pensionAmount.setPFundFemMny(personalFund.getFemaleAmount().v());
		this.pensionAmount.setPFundMaleMny(personalFund.getMaleAmount().v());
		this.pensionAmount.setPFundMinerMny(personalFund.getUnknownAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setPersonalFundExemption(nts.uk.ctx.pr.core.dom.
	 * insurance.social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setPersonalFundExemption(PensionAvgearnValue personalFundExemption) {
		this.pensionAmount.setPFundExemptFemMny(personalFundExemption.getFemaleAmount().v());
		this.pensionAmount.setPFundExemptMaleMny(personalFundExemption.getMaleAmount().v());
		this.pensionAmount.setPFundExemptMinerMny(personalFundExemption.getUnknownAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setPersonalPension(nts.uk.ctx.pr.core.dom.
	 * insurance.social.pensionavgearn.PensionAvgearnValue)
	 */
	@Override
	public void setPersonalPension(PensionAvgearnValue personalPension) {
		this.pensionAmount.setPPensionFemMny(personalPension.getFemaleAmount().v());
		this.pensionAmount.setPPensionMaleMny(personalPension.getMaleAmount().v());
		this.pensionAmount.setPPensionMinerMny(personalPension.getUnknownAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setAvgEarn(java.lang.Long)
	 */
	@Override
	public void setAvgEarn(Long avgEarn) {
		this.avgearnDetail.setPensionAvgEarn(avgEarn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnSetMemento#setUpperLimit(java.lang.Long)
	 */
	@Override
	public void setUpperLimit(Long upperLimit) {
		this.avgearnDetail.setPensionUpperLimit(upperLimit);
	}

}
