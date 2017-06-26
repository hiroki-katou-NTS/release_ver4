/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class SalaryOutputSettingName.
 */
@StringMaxLength(20)
public class SalaryOutputSettingName extends StringPrimitiveValue<SalaryOutputSettingName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new salary output setting name.
	 *
	 * @param rawValue the raw value
	 */
	public SalaryOutputSettingName(String rawValue) {
		super(rawValue);
	}

}
