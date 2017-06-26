/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class AddressKana1.
 */
@StringMaxLength(240)
public class AddressKana1 extends StringPrimitiveValue<AddressKana1>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new address kana 1.
	 *
	 * @param rawValue the raw value
	 */
	public AddressKana1(String rawValue) {
		super(rawValue);
	}


}
