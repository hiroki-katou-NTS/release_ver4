/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;

/**
 * The Class AccidentInsuranceRateDto.
 */
@Getter
@Setter
public class AccidentInsuranceRateDto {

	/** The history insurance. */
	private AccidentInsuranceRateHistoryDto historyInsurance;

	/** The rate items. */
	private List<InsuBizRateItemDto> rateItems;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the accident insurance rate
	 */
	public AccidentInsuranceRate toDomain(String companyCode) {
		return new AccidentInsuranceRate(new AccidentInsuranceRateGetMementoImpl(companyCode, this));
	}

	/**
	 * The Class AccidentInsuranceRateGetMementoImpl.
	 */
	public class AccidentInsuranceRateGetMementoImpl implements AccidentInsuranceRateGetMemento {

		/** The dto. */
		private AccidentInsuranceRateDto dto;

		/** The company code. */
		private String companyCode;

		/**
		 * Instantiates a new accident insurance rate get memento impl.
		 *
		 * @param companyCode
		 *            the company code
		 * @param dto
		 *            the dto
		 */
		public AccidentInsuranceRateGetMementoImpl(String companyCode, AccidentInsuranceRateDto dto) {
			super();
			this.dto = dto;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
		 * AccidentInsuranceRateGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return dto.historyInsurance.getHistoryId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
		 * AccidentInsuranceRateGetMemento#getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
		 * AccidentInsuranceRateGetMemento#getApplyRange()
		 */
		@Override
		public MonthRange getApplyRange() {
			return dto.historyInsurance.toDomain();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
		 * AccidentInsuranceRateGetMemento#getRateItems()
		 */
		@Override
		public Set<InsuBizRateItem> getRateItems() {
			return dto.rateItems.stream().map(rateDto -> rateDto.toDomain()).collect(Collectors.toSet());
		}

	}

}
