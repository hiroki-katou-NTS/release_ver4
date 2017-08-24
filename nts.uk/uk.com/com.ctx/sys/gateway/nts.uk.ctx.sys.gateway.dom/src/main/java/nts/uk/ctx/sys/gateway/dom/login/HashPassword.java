/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.login;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class HashPassword.
 */
@StringMaxLength(66)
@StringCharType(CharType.ANY_HALF_WIDTH)
public class HashPassword extends StringPrimitiveValue<HashPassword> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new hash password.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public HashPassword(String rawValue) {
		super(rawValue);
	}
}
