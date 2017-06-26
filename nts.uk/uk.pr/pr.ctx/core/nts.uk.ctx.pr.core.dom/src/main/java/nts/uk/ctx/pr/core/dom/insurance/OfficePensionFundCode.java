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
 * The Class OfficePensionFundCode.
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(6)
public class OfficePensionFundCode extends StringPrimitiveValue<OfficePensionFundCode> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office pension fund code.
	 *
	 * @param rawValue the raw value
	 */
	public OfficePensionFundCode(String rawValue) {
		super(rawValue);
	}

}
