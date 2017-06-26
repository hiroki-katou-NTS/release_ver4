/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class OfficeNoB.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(6)
public class OfficeNoB extends StringPrimitiveValue<OfficeNoB> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office no B.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public OfficeNoB(String rawValue) {
		super(rawValue);
	}

}
