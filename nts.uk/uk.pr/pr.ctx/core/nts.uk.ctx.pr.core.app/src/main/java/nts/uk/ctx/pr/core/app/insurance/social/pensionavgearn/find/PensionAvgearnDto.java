/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;

/**
 * The Class PensionAvgearnDto.
 */
@Builder
@Getter
public class PensionAvgearnDto {

	/** The grade. */
	private Integer grade;

	/** The avg earn. */
	private Long avgEarn;

	/** The upper limit. */
	private Long upperLimit;

	/** The child contribution amount. */
	private BigDecimal childContributionAmount;

	/** The company fund. */
	private PensionAvgearnValueDto companyFund;

	/** The company fund exemption. */
	private PensionAvgearnValueDto companyFundExemption;

	/** The company pension. */
	private PensionAvgearnValueDto companyPension;

	/** The personal fund. */
	private PensionAvgearnValueDto personalFund;

	/** The personal fund exemption. */
	private PensionAvgearnValueDto personalFundExemption;

	/** The personal pension. */
	private PensionAvgearnValueDto personalPension;

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the pension avgearn dto
	 */
	public PensionAvgearnDto fromDomain(PensionAvgearn domain) {
		PensionAvgearnDto dto = this;

		domain.saveToMemento(new SetMemento(dto));

		return dto;
	}

	/**
	 * The Class SetMemento.
	 */
	private class SetMemento implements PensionAvgearnSetMemento {

		/** The dto. */
		private PensionAvgearnDto dto;

		/**
		 * Instantiates a new sets the memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public SetMemento(PensionAvgearnDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setHistoryId(java.lang.String)
		 */
		@Override
		public void setHistoryId(String historyId) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setGrade(java.lang.Integer)
		 */
		@Override
		public void setGrade(Integer grade) {
			this.dto.grade = grade;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setChildContributionAmount(nts.uk.ctx.pr.
		 * core. dom.insurance.InsuranceAmount)
		 */
		@Override
		public void setChildContributionAmount(CommonAmount childContributionAmount) {
			this.dto.childContributionAmount = childContributionAmount.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setCompanyFund(nts.uk.ctx.pr.core.dom.
		 * insurance. social.pensionavgearn.PensionAvgearnValue)
		 */
		@Override
		public void setCompanyFund(PensionAvgearnValue companyFund) {
			this.dto.companyFund = PensionAvgearnValueDto.fromDomain(companyFund);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setCompanyFundExemption(nts.uk.ctx.pr.core.
		 * dom. insurance.social.pensionavgearn.PensionAvgearnValue)
		 */
		@Override
		public void setCompanyFundExemption(PensionAvgearnValue companyFundExemption) {
			this.dto.companyFundExemption = PensionAvgearnValueDto.fromDomain(companyFundExemption);
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
			this.dto.companyPension = PensionAvgearnValueDto.fromDomain(companyPension);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setPersonalFund(nts.uk.ctx.pr.core.dom.
		 * insurance .social.pensionavgearn.PensionAvgearnValue)
		 */
		@Override
		public void setPersonalFund(PensionAvgearnValue personalFund) {
			this.dto.personalFund = PensionAvgearnValueDto.fromDomain(personalFund);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setPersonalFundExemption(nts.uk.ctx.pr.core.
		 * dom. insurance.social.pensionavgearn.PensionAvgearnValue)
		 */
		@Override
		public void setPersonalFundExemption(PensionAvgearnValue personalFundExemption) {
			this.dto.personalFundExemption = PensionAvgearnValueDto
					.fromDomain(personalFundExemption);
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
			this.dto.personalPension = PensionAvgearnValueDto.fromDomain(personalPension);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setAvgEarn(java.lang.Long)
		 */
		@Override
		public void setAvgEarn(Long avgEarn) {
			this.dto.avgEarn = avgEarn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnSetMemento#setUpperLimit(java.lang.Long)
		 */
		@Override
		public void setUpperLimit(Long upperLimit) {
			this.dto.upperLimit = upperLimit;
		}

	}

}
