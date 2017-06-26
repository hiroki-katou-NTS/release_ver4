/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data.newlayout;

import lombok.Builder;
import lombok.Getter;

/**
 * The Class BeforeEndYearData.
 */
@Builder
@Getter
public class BeforeEndYearData {
	
	/** The total tax previous position. */
	public Long totalTaxPreviousPosition;
	
	/** The total tax other money. */
	public Long totalTaxOtherMoney;
	
	/** The total social insurance previous position. */
	public Long totalSocialInsurancePreviousPosition;
	
	/** The total social insurance other money. */
	public Long totalSocialInsuranceOtherMoney;
	
	/** The Acquisition tax previous position. */
	public Long acquisitionTaxPreviousPosition;
	
	/** The Acquisition tax other money. */
	public Long acquisitionTaxOtherMoney;
}
