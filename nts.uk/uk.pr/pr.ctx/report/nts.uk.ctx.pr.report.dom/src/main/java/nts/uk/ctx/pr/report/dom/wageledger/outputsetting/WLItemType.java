/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

/**
 * The Enum WageLedgerItemType.
 */
public enum WLItemType {
	
	/** The Master. */
	Master(0),
	
	/** The Aggregate. */
	Aggregate(1);
	
	/** The value. */
	public final int value;
	
	/**
	 * Instantiates a new wage ledger item type.
	 *
	 * @param value the value
	 */
	private WLItemType(int value) {
		this.value = value;
	}
	
	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the element type
	 */
	public static WLItemType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (WLItemType val : WLItemType.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
