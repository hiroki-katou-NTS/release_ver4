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
@StringMaxLength(8)
public class OfficeRefCode2 extends StringPrimitiveValue<OfficeRefCode2> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office ref code 2.
	 *
	 * @param rawValue the raw value
	 */
	public OfficeRefCode2(String rawValue) {
		super(rawValue);
	}

}
