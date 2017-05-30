/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.payment.data.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class PaymentEmployeeDto.
 */
@Getter
@Setter
public class PaymentEmployeeDto {

	/** The employee code. */
	private String employeeCode;

	/** The employee name. */
	private String employeeName;

	/** The postal code. */
	private String postalCode;

	/** The address one. */
	private String addressOne;

	/** The address two. */
	private String addressTwo;

	/** The tax total. */
	private BigDecimal taxTotal;

	/** The tax exemption total. */
	private BigDecimal taxExemptionTotal;

	/** The payment total. */
	private BigDecimal paymentTotal;

	/** The social insurance total. */
	private BigDecimal socialInsuranceTotal;

	/** The taxable amount. */
	private BigDecimal taxableAmount;

	/** The deduction total. */
	private BigDecimal deductionTotal;

	/** The subscription amount. */
	private BigDecimal subscriptionAmount;

	/** The taxable total. */
	private BigDecimal taxableTotal;

	/**
	 * Gets the preview data.
	 *
	 * @return the preview data
	 */
	public static PaymentEmployeeDto getPreviewData() {
		PaymentEmployeeDto dto = new PaymentEmployeeDto();
		dto.setEmployeeCode(PreviewData.EMPLOYEE_CODE);
		dto.setEmployeeName(PreviewData.EMPLOYEE_CODE);
		dto.setPostalCode(PreviewData.POSTAL_CODE);
		dto.setAddressOne(PreviewData.ADDRESS);
		dto.setAddressTwo(PreviewData.ADDRESS);
		dto.setTaxTotal(PreviewData.ITEM_VALUE);
		dto.setTaxExemptionTotal(PreviewData.ITEM_VALUE);
		dto.setPaymentTotal(PreviewData.ITEM_VALUE);
		dto.setSocialInsuranceTotal(PreviewData.ITEM_VALUE);
		dto.setTaxableAmount(PreviewData.ITEM_VALUE);
		dto.setDeductionTotal(PreviewData.ITEM_VALUE);
		dto.setSubscriptionAmount(PreviewData.ITEM_VALUE);
		dto.setTaxableTotal(PreviewData.ITEM_VALUE);
		return dto;
	}
}
