/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * Name of Office.
 */
@StringMaxLength(6)
public class OfficeRefCode1 extends StringPrimitiveValue<OfficeRefCode1> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office ref code 1.
	 *
	 * @param rawValue the raw value
	 */
	public OfficeRefCode1(String rawValue) {
		super(rawValue);
	}

}
