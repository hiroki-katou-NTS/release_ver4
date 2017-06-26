/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data.oldlayout;

import java.util.List;

import lombok.Builder;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;

/**
 * The Class PaymentData.
 */
@Builder
public class PaymentData {
	
	/** The aggregate item list. */
	public List<ReportItemDto> aggregateItemList;
	
	/** The total tax. */
	public ReportItemDto totalTax;
	
	/** The total tax exemption. */
	public ReportItemDto totalTaxExemption;
	
	/** The total subsidy. */
	public ReportItemDto totalSalaryPayment;

}
