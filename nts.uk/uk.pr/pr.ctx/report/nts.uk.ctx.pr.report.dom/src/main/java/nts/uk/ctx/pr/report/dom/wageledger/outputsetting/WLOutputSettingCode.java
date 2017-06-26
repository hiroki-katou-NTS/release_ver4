/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.arc.primitive.constraint.StringRegEx;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class WageLedgerOutputSettingCode.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(3)
@StringRegEx("\\d{3}")
public class WLOutputSettingCode extends CodePrimitiveValue<WLOutputSettingCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new output setting code.
	 *
	 * @param rawValue the raw value
	 */
	public WLOutputSettingCode(String rawValue) {
		super(rawValue);
	}

}
