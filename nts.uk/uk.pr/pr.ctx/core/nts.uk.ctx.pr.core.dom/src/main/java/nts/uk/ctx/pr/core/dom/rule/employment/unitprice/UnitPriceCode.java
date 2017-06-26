/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class UnitPriceCode.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(2)
public class UnitPriceCode extends CodePrimitiveValue<UnitPriceCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unit price code.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public UnitPriceCode(String rawValue) {
		super(rawValue);
	}

}
