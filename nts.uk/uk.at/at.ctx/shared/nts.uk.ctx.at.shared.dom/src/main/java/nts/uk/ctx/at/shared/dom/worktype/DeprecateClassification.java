/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktype;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;

/**
 * The Enum DeprecateClassification.
 */
// 廃止区分
@AllArgsConstructor
public enum DeprecateClassification {
	
	/** The Deprecated. */
	// 廃止する
	Deprecated(1), 

	/** The Not deprecated. */
	// 廃止しない
	NotDeprecated(0); 

	/** The value. */
	public final int value;
	
	/** The Constant values. */
	private final static DeprecateClassification[] values = DeprecateClassification.values();
	
	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the time day atr
	 */
	public static DeprecateClassification valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (DeprecateClassification val : DeprecateClassification.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
