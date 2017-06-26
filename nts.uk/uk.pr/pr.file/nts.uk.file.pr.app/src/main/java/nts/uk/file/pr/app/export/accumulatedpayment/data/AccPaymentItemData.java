/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class AccPaymentItemData.
 */
@Builder
@Getter
@Setter
public class AccPaymentItemData {



	/** The emp designation. */
	private String empDesignation;

	/** The emp code. */
	private String empCode;

	/** The emp name. */
	private String empName;

	/** The tax amount. */
	private Double taxAmount;

	/** The social insurance amount. */
	private Double socialInsuranceAmount;

	/** The width holding tax amount. */
	private Double widthHoldingTaxAmount;

	/** The enrollment status. */
	private String enrollmentStatus;

	/** The directional status. */
	private String directionalStatus;

	/** The amount after tax deduction. */
	private Double amountAfterTaxDeduction;
}
