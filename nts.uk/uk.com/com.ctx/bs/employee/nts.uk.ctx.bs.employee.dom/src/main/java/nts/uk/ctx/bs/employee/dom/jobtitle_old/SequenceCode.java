/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.jobtitle_old;

import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class SequenceCode.
 */
@StringMaxLength(5)
public class SequenceCode extends CodePrimitiveValue<SequenceCode> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sequence code.
	 *
	 * @param rawValue the raw value
	 */
	public SequenceCode(String rawValue) {
		super(rawValue);
	}

}
