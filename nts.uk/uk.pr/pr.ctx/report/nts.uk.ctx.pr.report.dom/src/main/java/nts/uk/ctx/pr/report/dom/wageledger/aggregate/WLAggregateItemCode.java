/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.arc.primitive.constraint.StringRegEx;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class WageLedgerAggregateItemCode.
 */
@StringCharType(CharType.NUMERIC)
@StringRegEx("\\d{4}")
@StringMaxLength(4)
public class WLAggregateItemCode extends CodePrimitiveValue<WLAggregateItemCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new output setting code.
	 *
	 * @param rawValue the raw value
	 */
	public WLAggregateItemCode(String rawValue) {
		super(rawValue);
	}

}
