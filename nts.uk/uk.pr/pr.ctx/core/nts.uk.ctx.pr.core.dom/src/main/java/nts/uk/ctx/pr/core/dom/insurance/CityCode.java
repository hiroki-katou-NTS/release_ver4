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
 * The Class CityCode.
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(2)
public class CityCode extends StringPrimitiveValue<CityCode> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new city code.
	 *
	 * @param rawValue the raw value
	 */
	public CityCode(String rawValue) {
		super(rawValue);
	}

}
