/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class BusinessName.
 */
@StringMaxLength(20)
public class BusinessName extends StringPrimitiveValue<BusinessName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new business name.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public BusinessName(String rawValue) {
		super(rawValue);
	}

}
