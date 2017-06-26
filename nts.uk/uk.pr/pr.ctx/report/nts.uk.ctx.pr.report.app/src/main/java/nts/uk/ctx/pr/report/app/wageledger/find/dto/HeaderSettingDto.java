/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find.dto;

import lombok.Builder;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Class HeaderSettingDto.
 */
@Builder
public class HeaderSettingDto {
	
	/** The code. */
	public String code;
	
	/** The name. */
	public String name;
	
	/** The category. */
	public WLCategory category;
	
	/** The payment type. */
	public PaymentType paymentType;

}
