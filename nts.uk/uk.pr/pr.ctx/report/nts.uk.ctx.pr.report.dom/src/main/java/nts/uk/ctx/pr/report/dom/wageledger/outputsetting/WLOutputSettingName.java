/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class WageLedgerOutputSettingName.
 */
@StringMaxLength(40)
public class WLOutputSettingName extends StringPrimitiveValue<WLOutputSettingName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new output setting name.
	 *
	 * @param rawValue the raw value
	 */
	public WLOutputSettingName(String rawValue) {
		super(rawValue);
	}
}
