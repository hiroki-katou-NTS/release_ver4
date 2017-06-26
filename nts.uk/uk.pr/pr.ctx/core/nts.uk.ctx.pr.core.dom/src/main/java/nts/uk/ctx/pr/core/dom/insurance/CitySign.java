/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class CitySign.
 */
@StringMaxLength(2)
public class CitySign extends StringPrimitiveValue<CitySign> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new city sign.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public CitySign(String rawValue) {
		super(rawValue);
	}

}
