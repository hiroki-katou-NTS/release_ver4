/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment.data;

import lombok.Data;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
public class AccPaymentDto {

	/** The target year. */
	private int targetYear;
	
	/** The is lower limit. */
	private boolean isLowerLimit;
	
	/** The is upper limit. */
	private boolean isUpperLimit;
	
	/** The lower limit value. */
	private int lowerLimitValue;	
	
	/** The upper limit value. */
	private int upperLimitValue;
	
	/**
	 * Instantiates a new acc payment dto.
	 *
	 * @param targetYear the target year
	 * @param isLowerLimit the is lower limit
	 * @param isUpperLimit the is upper limit
	 * @param lowerLimitValue the lower limit value
	 * @param upperLimitValue the upper limit value
	 */
	public AccPaymentDto(int targetYear, boolean isLowerLimit, 
			boolean isUpperLimit, int lowerLimitValue, int upperLimitValue) {
		this.targetYear = targetYear;
		this.isLowerLimit = isLowerLimit;
		this.isUpperLimit = isUpperLimit;
		this.lowerLimitValue = lowerLimitValue;
		this.upperLimitValue = upperLimitValue;
	}
}
