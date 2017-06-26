/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class CertifyGroupCode.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(3)
public class CertifyGroupCode extends CodePrimitiveValue<CertifyGroupCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new certify group code.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public CertifyGroupCode(String rawValue) {
		super(rawValue);
	}

}
