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
 * The Class OfficeNoA.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(4)
public class OfficeNoA extends StringPrimitiveValue<OfficeNoA> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office no A.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public OfficeNoA(String rawValue) {
		super(rawValue);
	}

}
