/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class Address2.
 */
@StringMaxLength(80)
public class Address2 extends StringPrimitiveValue<Address2> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
   /**
    * Instantiates a new address 2.
    *
    * @param rawValue the raw value
    */
	public Address2(String rawValue) {
		super(rawValue);
	}
}
