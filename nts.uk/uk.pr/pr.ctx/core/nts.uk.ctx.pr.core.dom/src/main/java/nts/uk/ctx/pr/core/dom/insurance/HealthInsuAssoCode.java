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
 * The Class HealthInsuAssoCode.
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(4)
public class HealthInsuAssoCode extends StringPrimitiveValue<HealthInsuAssoCode> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new health insu asso code.
	 *
	 * @param rawValue the raw value
	 */
	public HealthInsuAssoCode(String rawValue) {
		super(rawValue);
	}

}
