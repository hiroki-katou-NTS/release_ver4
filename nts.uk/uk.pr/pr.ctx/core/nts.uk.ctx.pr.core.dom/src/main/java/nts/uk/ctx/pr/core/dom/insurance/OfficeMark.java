/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class OfficeMark.
 */
@StringMaxLength(8)
public class OfficeMark extends StringPrimitiveValue<OfficeMark> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office mark.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public OfficeMark(String rawValue) {
		super(rawValue);
	}

}
