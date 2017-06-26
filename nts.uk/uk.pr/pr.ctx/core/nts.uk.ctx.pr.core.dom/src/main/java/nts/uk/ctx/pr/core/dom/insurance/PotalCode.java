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
 * Name of Office.
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(10)
public class PotalCode extends StringPrimitiveValue<PotalCode> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PotalCode(String rawValue) {
		super(rawValue);
	}

}
