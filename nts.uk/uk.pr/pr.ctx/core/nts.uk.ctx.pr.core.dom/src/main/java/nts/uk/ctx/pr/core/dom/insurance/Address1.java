/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class Address1.
 */
@StringMaxLength(240)
public class Address1 extends StringPrimitiveValue<Address1> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new address 1.
	 *
	 * @param rawValue the raw value
	 */
	public Address1(String rawValue) {
		super(rawValue);
	}
	
}
