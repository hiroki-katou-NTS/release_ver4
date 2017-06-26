/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItem;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateSetMemento;

/**
 * The Class UnemployeeInsuranceRateDto.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateFindDto implements UnemployeeInsuranceRateSetMemento {

	/** The history insurance. */
	private UnemployeeInsuranceHistoryFindDto historyInsurance;

	/** The rate items. */
	private List<UnemployeeInsuranceRatetemFindDto> rateItems;

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		if (this.historyInsurance == null) {
			this.historyInsurance = new UnemployeeInsuranceHistoryFindDto();
		}
		this.historyInsurance.setHistoryId(historyId);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateSetMemento#setCompanyCode(nts.uk.ctx.core.dom.
	 * company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateSetMemento#setApplyRange(nts.uk.ctx.pr.core.dom.
	 * insurance.MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.historyInsurance.setEndMonth(applyRange.getEndMonth().v());
		this.historyInsurance.setStartMonth(applyRange.getStartMonth().v());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateSetMemento#setRateItems(java.util.Set)
	 */
	@Override
	public void setRateItems(Set<UnemployeeInsuranceRateItem> rateItems) {
		this.rateItems = rateItems.stream().map(rateItem -> {
			UnemployeeInsuranceRatetemFindDto dto = new UnemployeeInsuranceRatetemFindDto();
			rateItem.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}
