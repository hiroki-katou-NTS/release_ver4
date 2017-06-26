/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class SalaryAggregateItemName.
 */
@StringMaxLength(20)
public class SalaryAggregateItemName extends StringPrimitiveValue<SalaryAggregateItemName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new salary aggregate item name.
	 *
	 * @param rawValue the raw value
	 */
	public SalaryAggregateItemName(String rawValue) {
		super(rawValue);
	}
}
