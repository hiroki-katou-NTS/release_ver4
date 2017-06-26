/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class SalaryAggregateItemCode.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(3)
public class SalaryAggregateItemCode extends CodePrimitiveValue<SalaryAggregateItemCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new salary aggregate item code.
	 *
	 * @param rawValue the raw value
	 */
	public SalaryAggregateItemCode(String rawValue) {
		super(rawValue);
	}

}
