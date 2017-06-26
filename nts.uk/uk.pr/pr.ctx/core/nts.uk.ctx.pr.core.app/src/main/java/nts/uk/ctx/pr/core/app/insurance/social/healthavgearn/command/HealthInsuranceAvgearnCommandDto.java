/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsuranceAvgearnValueDto;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;

/**
 * The Class HealthInsuranceAvgearnCommandDto.
 */
@Getter
@Setter
public class HealthInsuranceAvgearnCommandDto {

	/** The grade. */
	private Integer grade;

	/** The avg earn. */
	private Long avgEarn;

	/** The upper limit. */
	private Long upperLimit;

	/** The company avg. */
	private HealthInsuranceAvgearnValueDto companyAvg;

	/** The personal avg. */
	private HealthInsuranceAvgearnValueDto personalAvg;

	/**
	 * To domain.
	 *
	 * @param historyId
	 *            the history id
	 * @return the health insurance avgearn
	 */
	public HealthInsuranceAvgearn toDomain(String historyId) {
		return new HealthInsuranceAvgearn(new GetMemento(historyId, this));
	}

	/**
	 * The Class GetMemento.
	 */
	private class GetMemento implements HealthInsuranceAvgearnGetMemento {

		/** The history id. */
		private String historyId;

		/** The type value. */
		private HealthInsuranceAvgearnCommandDto typeValue;

		/**
		 * Instantiates a new gets the memento.
		 *
		 * @param historyId
		 *            the history id
		 * @param typeValue
		 *            the type value
		 */
		public GetMemento(String historyId, HealthInsuranceAvgearnCommandDto typeValue) {
			this.historyId = historyId;
			this.typeValue = typeValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getPersonalAvg()
		 */
		@Override
		public HealthInsuranceAvgearnValue getPersonalAvg() {
			return HealthInsuranceAvgearnValueDto.toDomain(this.typeValue.personalAvg);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getGrade()
		 */
		@Override
		public Integer getGrade() {
			return this.typeValue.grade;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getAvgEarn()
		 */
		@Override
		public Long getAvgEarn() {
			return this.typeValue.avgEarn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getUpperLimit()
		 */
		@Override
		public Long getUpperLimit() {
			return this.typeValue.upperLimit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return this.historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getCompanyAvg()
		 */
		@Override
		public HealthInsuranceAvgearnValue getCompanyAvg() {
			return HealthInsuranceAvgearnValueDto.toDomain(this.typeValue.companyAvg);
		}

	}
}
