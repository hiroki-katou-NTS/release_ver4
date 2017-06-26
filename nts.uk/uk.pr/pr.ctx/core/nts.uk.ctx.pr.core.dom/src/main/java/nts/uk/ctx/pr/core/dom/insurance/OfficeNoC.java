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
 * The Class OfficeNoC.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(1)
public class OfficeNoC extends StringPrimitiveValue<OfficeNoC> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new office no C.
	 *
	 * @param rawValue the raw value
	 */
	public OfficeNoC(String rawValue) {
		super(rawValue);
	}

}
