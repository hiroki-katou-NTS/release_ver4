/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data.newlayout;

import lombok.Builder;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;

/**
 * The Class TotalData.
 */
@Builder
public class TotalData {

	/** The total tax. */
	public ReportItemDto totalTax;
	
	/** The total tax exemption. */
	public ReportItemDto totalTaxExemption;
	
	/** The total payment. */
	public ReportItemDto totalPayment;
	
	/** The total social insurance. */
	public ReportItemDto totalSocialInsurance;
	
	/** The total taxable. */
	public ReportItemDto totalTaxable;
	
	/** The total income tax. */
	public ReportItemDto totalIncomeTax;
	
	/** The total inhabitant tax. */
	public ReportItemDto totalInhabitantTax;
	
	/** The total deduction. */
	public ReportItemDto totalDeduction;
	
	/** The total real. */
	public ReportItemDto totalReal;
}
