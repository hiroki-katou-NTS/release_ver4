/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.workrecord.closure;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class CloseName.
 */
@StringMaxLength(10)
public class CloseName extends StringPrimitiveValue<CloseName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4042037564670742671L;

	/**
	 * Instantiates a new close name.
	 *
	 * @param rawValue the raw value
	 */
	public CloseName(String rawValue) {
		super(rawValue);
	}

}
