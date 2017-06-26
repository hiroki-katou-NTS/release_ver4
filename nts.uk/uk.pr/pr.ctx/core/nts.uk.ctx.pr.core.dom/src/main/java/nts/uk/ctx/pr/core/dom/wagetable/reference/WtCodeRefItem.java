/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import lombok.Getter;

/**
 * The Class WtCodeRefItem.
 */
@Getter
public class WtCodeRefItem {

	/** The reference code. */
	private String referenceCode;

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates a new wt code ref item.
	 *
	 * @param referenceCode
	 *            the reference code
	 * @param displayName
	 *            the display name
	 */
	public WtCodeRefItem(String referenceCode, String displayName) {
		super();
		this.referenceCode = referenceCode;
		this.displayName = displayName;
	}

}
