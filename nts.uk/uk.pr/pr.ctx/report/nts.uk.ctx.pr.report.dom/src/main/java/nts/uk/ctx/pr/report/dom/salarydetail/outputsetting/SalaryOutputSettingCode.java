/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class SalaryOutputSettingCode.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(2)
public class SalaryOutputSettingCode extends CodePrimitiveValue<SalaryOutputSettingCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new output setting code.
	 *
	 * @param rawValue the raw value
	 */
	public SalaryOutputSettingCode(String rawValue) {
		super(rawValue);
	}

}
