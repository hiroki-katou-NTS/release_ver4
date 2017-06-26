/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data.share;

import lombok.Builder;
import lombok.Getter;

/**
 * The Class HeaderReportData.
 */
@Builder
@Getter
public class HeaderReportData {
	
	/** The department code. */
	public String departmentCode;
	
	/** The department name. */
	public String departmentName;
	
	/** The employee code. */
	public String employeeCode;
	
	/** The employee name. */
	public String employeeName;
	
	/** The sex. */
	public String sex;
	
	/** The position. */
	public String position;
	
	/** The target year. */
	public int targetYear;
}
