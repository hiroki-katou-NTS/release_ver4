/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find.PensionAvgearnValueDto;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;

/**
 * The Class PensionAvgearnCommandDto.
 */
@Setter
@Getter
public class PensionAvgearnCommandDto {

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
	 * To domain.
	 *
	 * @param historyId
	 *            the history id
	 * @return the pension avgearn
	 */
	public PensionAvgearn toDomain(String historyId) {
		return new PensionAvgearn(new GetMemento(historyId, this));
	}

	/**
	 * The Class GetMemento.
	 */
	private class GetMemento implements PensionAvgearnGetMemento {

		/** The history id. */
		private String historyId;

		/** The type value. */
		private PensionAvgearnCommandDto typeValue;

		/**
		 * Instantiates a new gets the memento.
		 *
		 * @param historyId
		 *            the history id
		 * @param typeValue
		 *            the type value
		 */
		public GetMemento(String historyId, PensionAvgearnCommandDto typeValue) {
			this.historyId = historyId;
			this.typeValue = typeValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalPension()
		 */
		@Override
		public PensionAvgearnValue getPersonalPension() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.personalPension);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalFundExemption()
		 */
		@Override
		public PensionAvgearnValue getPersonalFundExemption() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.personalFundExemption);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalFund()
		 */
		@Override
		public PensionAvgearnValue getPersonalFund() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.personalFund);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getGrade()
		 */
		@Override
		public Integer getGrade() {
			return this.typeValue.grade;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return this.historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyPension()
		 */
		@Override
		public PensionAvgearnValue getCompanyPension() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.companyPension);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyFundExemption()
		 */
		@Override
		public PensionAvgearnValue getCompanyFundExemption() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.companyFundExemption);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyFund()
		 */
		@Override
		public PensionAvgearnValue getCompanyFund() {
			return PensionAvgearnValueDto.toDomain(this.typeValue.companyFund);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getChildContributionAmount()
		 */
		@Override
		public CommonAmount getChildContributionAmount() {
			return new CommonAmount(this.typeValue.childContributionAmount);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getAvgEarn()
		 */
		@Override
		public Long getAvgEarn() {
			return this.typeValue.avgEarn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getUpperLimit()
		 */
		@Override
		public Long getUpperLimit() {
			return this.typeValue.upperLimit;
		}

	}

}
