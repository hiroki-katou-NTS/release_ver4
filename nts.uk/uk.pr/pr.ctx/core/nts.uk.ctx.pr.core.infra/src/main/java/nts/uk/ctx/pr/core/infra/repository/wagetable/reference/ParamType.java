/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.reference;

/**
 * The Enum OutputSetting.
 */
public enum ParamType {

	/** The not show. */
	COMPANY_CODE(":CCD"),

	/** The show. */
	BASEDATE(":BASEDATE_");

	/** The value. */
	public final String prefix;

	/** The bol. */

	/**
	 * Instantiates a new output setting.
	 *
	 * @param value
	 *            the value
	 */
	private ParamType(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Value of.
	 *
	 * @param prefix
	 *            the value
	 * @return the output setting
	 */
	public static ParamType prefixOf(String prefix) {
		// Invalid object.
		if (prefix == null) {
			return null;
		}

		// Find value.
		for (ParamType val : ParamType.values()) {
			if (val.prefix == prefix) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
