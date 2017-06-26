/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.wageledger.data.newlayout.BeforeEndYearData;
import nts.uk.file.pr.app.export.wageledger.data.newlayout.TotalData;
import nts.uk.file.pr.app.export.wageledger.data.share.HeaderReportData;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;

/**
 * The Class WLNewLayoutReportData.
 */
@Builder
public class WLNewLayoutReportData {
	
	/** The header data. */
	public HeaderReportData headerData;
	
	/** The salary total data. */
	public TotalData salaryTotalData;
	
	/** The bonus total data. */
	public TotalData bonusTotalData;
	
	/** The before end year data. */
	public BeforeEndYearData beforeEndYearData;
	
	/** The salary payment items. */
	public List<ReportItemDto> salaryPaymentItems;
	
	/** The salary deduction items. */
	public List<ReportItemDto> salaryDeductionItems;
	
	/** The salary attendance items. */
	public List<ReportItemDto> salaryAttendanceItems;
	
	/** The bonus payment items. */
	public List<ReportItemDto> bonusPaymentItems;
	
	/** The bonus deduction items. */
	public List<ReportItemDto> bonusDeductionItems;
	
	/** The bonus attendance items. */
	public List<ReportItemDto> bonusAttendanceItems;
	
	/** The salary payment date map. */
	public Map<Integer, GeneralDate> salaryPaymentDateMap;
	
	/** The bonus payment date map. */
	public Map<Integer, GeneralDate> bonusPaymentDateMap;
}
