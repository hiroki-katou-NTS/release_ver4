/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class CertifyGroupName.
 */
@StringMaxLength(30)
public class CertifyGroupName extends StringPrimitiveValue<CertifyGroupName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new certify group name.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public CertifyGroupName(String rawValue) {
		super(rawValue);
	}

}
