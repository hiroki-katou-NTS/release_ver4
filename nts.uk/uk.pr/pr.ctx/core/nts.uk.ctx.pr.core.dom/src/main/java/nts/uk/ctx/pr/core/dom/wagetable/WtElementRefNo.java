/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class WageTableRefNo.
 */
@StringMaxLength(4)
public class WtElementRefNo extends StringPrimitiveValue<WtElementRefNo> {

	/**
	 * Instantiates a new wage table ref no.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public WtElementRefNo(String rawValue) {
		super(rawValue);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}