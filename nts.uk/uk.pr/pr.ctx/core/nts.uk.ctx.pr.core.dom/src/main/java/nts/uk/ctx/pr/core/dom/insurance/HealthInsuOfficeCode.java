/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * The Class HealthInsuOfficeCode.
 */
@DecimalRange(min = "0", max = "99999")
public class HealthInsuOfficeCode extends DecimalPrimitiveValue<HealthInsuOfficeCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new health insu office code.
	 *
	 * @param rawValue the raw value
	 */
	public HealthInsuOfficeCode(BigDecimal rawValue) {
		super(rawValue);
	}

}