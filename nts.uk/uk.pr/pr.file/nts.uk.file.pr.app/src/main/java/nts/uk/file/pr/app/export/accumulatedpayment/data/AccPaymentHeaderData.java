/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class AccPaymentHeaderData.
 */

@Builder


@Getter


@Setter
public class AccPaymentHeaderData {
	
	/** The department info. */
	private String departmentInfo;
	
	/** The emp type info. */
	private String empTypeInfo;
	
	/** The position info. */
	private String positionInfo;
	
	/** The year month info. */
	private String yearMonthInfo;
}
