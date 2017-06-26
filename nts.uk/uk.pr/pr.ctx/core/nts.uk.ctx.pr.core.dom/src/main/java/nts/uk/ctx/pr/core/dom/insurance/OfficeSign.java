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
 * The Class OfficeSign.
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(4)
public class OfficeSign extends StringPrimitiveValue<OfficeSign> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office sign.
	 *
	 * @param rawValue the raw value
	 */
	public OfficeSign(String rawValue) {
		super(rawValue);
	}

}
