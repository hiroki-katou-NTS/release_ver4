/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;

/**
 * The Class HealthInsuranceAvgearnDto.
 */
@Data
public class HealthInsuranceAvgearnDto {

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
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the health insurance avgearn dto
	 */
	public HealthInsuranceAvgearnDto fromDomain(HealthInsuranceAvgearn domain) {
		HealthInsuranceAvgearnDto dto = this;

		domain.saveToMemento(new SetMemento(dto));

		return dto;
	}

	/**
	 * The Class SetMemento.
	 */
	private class SetMemento implements HealthInsuranceAvgearnSetMemento {

		/** The dto. */
		private HealthInsuranceAvgearnDto dto;

		/**
		 * Instantiates a new sets the memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public SetMemento(HealthInsuranceAvgearnDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setHistoryId(java.lang.String)
		 */
		@Override
		public void setHistoryId(String historyId) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setGrade(java.lang.Integer)
		 */
		@Override
		public void setGrade(Integer grade) {
			this.dto.grade = grade;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setCompanyAvg(nts.uk.ctx.pr.core.
		 * dom. insurance.social.healthavgearn.HealthInsuranceAvgearnValue)
		 */
		@Override
		public void setCompanyAvg(HealthInsuranceAvgearnValue companyAvg) {
			this.dto.companyAvg = HealthInsuranceAvgearnValueDto.fromDomain(companyAvg);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setPersonalAvg(nts.uk.ctx.pr.core.
		 * dom. insurance.social.healthavgearn.HealthInsuranceAvgearnValue)
		 */
		@Override
		public void setPersonalAvg(HealthInsuranceAvgearnValue personalAvg) {
			this.dto.personalAvg = HealthInsuranceAvgearnValueDto.fromDomain(personalAvg);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setAvgEarn(java.lang.Long)
		 */
		@Override
		public void setAvgEarn(Long avgEarn) {
			this.dto.avgEarn = avgEarn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnSetMemento#setUpperLimit(java.lang.Long)
		 */
		@Override
		public void setUpperLimit(Long upperLimit) {
			this.dto.upperLimit = upperLimit;
		}

	}

}
