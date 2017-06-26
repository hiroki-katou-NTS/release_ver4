/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data;

import java.util.List;

import lombok.Builder;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.DeductionData;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.PaymentData;
import nts.uk.file.pr.app.export.wageledger.data.share.HeaderReportData;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;

/**
 * The Class WageLedgerReportData.
 */
@Builder
public class WLOldLayoutReportData {
	
	/** The header data. */
	public HeaderReportData headerData;
	
	/** The salary payment data. */
	public PaymentData salaryPaymentData;
	
	/** The bonus payment data. */
	public PaymentData bonusPaymentData;
	
	/** The net salary data. */
	public ReportItemDto netSalaryData;
	
	/** The attendance salary data. */
	public List<ReportItemDto> salaryAttendanceDatas;
	
	/** The salary deduction data. */
	public DeductionData salaryDeductionData;
	
	/** The bonus deduction data. */
	public DeductionData bonusDeductionData;
	
	/** The total bonus data. */
	public ReportItemDto totalBonusData;
	
	/** The attendance bonus data. */
	public List<ReportItemDto> bonusAttendanceDatas;
	
	/** The bonus month list. */
	public List<Integer> bonusMonthList;
	
	/** The salary month list. */
	public List<Integer> salaryMonthList;
}
