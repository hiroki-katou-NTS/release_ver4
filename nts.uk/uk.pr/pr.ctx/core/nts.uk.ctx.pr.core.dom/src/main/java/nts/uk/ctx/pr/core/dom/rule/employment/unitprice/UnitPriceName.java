/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class UnitPriceName.
 */
@StringMaxLength(20)
public class UnitPriceName extends StringPrimitiveValue<UnitPriceName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unit price name.
	 *
	 * @param rawValue the raw value
	 */
	public UnitPriceName(String rawValue) {
		super(rawValue);
	}

}
