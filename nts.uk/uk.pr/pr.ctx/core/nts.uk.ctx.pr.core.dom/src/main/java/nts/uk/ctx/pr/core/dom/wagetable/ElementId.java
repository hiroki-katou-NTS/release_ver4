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
@StringMaxLength(36)
public class ElementId extends StringPrimitiveValue<ElementId> {

	/** The default value. */
	public static ElementId DEFAULT_VALUE = new ElementId("-");

	/**
	 * Instantiates a new wage table ref no.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public ElementId(String rawValue) {
		super(rawValue);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}