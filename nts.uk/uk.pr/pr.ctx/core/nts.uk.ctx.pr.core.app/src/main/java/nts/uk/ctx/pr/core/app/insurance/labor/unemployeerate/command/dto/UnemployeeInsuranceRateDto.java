/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItem;

/**
 * The Class UnemployeeInsuranceRateDto.
 */

/**
 * Gets the rate items.
 *
 * @return the rate items
 */
@Getter

/**
 * Sets the rate items.
 *
 * @param rateItems
 *            the new rate items
 */
@Setter
public class UnemployeeInsuranceRateDto {

	/** The history insurance. */
	private UnemployeeInsuranceHistoryDto historyInsurance;

	/** The rate items. */
	private List<UnemployeeInsuranceRateItemDto> rateItems;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the unemployee insurance rate
	 */
	public UnemployeeInsuranceRate toDomain(String companyCode) {
		return new UnemployeeInsuranceRate(new UirGetMemento(companyCode, this));
	}

	/**
	 * The Class UirGetMemento.
	 */
	private class UirGetMemento implements UnemployeeInsuranceRateGetMemento {

		/** The company code. */
		private String companyCode;

		/** The dto. */
		private UnemployeeInsuranceRateDto dto;

		/**
		 * Instantiates a new uir get memento.
		 *
		 * @param companyCode
		 *            the company code
		 * @param dto
		 *            the dto
		 */
		public UirGetMemento(String companyCode, UnemployeeInsuranceRateDto dto) {
			super();
			this.companyCode = companyCode;
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
		 * UnemployeeInsuranceRateGetMemento#getRateItems()
		 */
		@Override
		public Set<UnemployeeInsuranceRateItem> getRateItems() {
			return dto.rateItems.stream().map(rateItem -> rateItem.toDomain(this.companyCode))
				.collect(Collectors.toSet());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
		 * UnemployeeInsuranceRateGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return historyInsurance.getHistoryId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
		 * UnemployeeInsuranceRateGetMemento#getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
		 * UnemployeeInsuranceRateGetMemento#getApplyRange()
		 */
		@Override
		public MonthRange getApplyRange() {
			return MonthRange.range(YearMonth.of(dto.getHistoryInsurance().getStartMonth()),
				YearMonth.of(dto.getHistoryInsurance().getEndMonth()));
		}

	}
}
