/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.timeitemmanagement;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * The Class OptionalItemNo.
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(3)
// 任意項目NO
// <<不変条件>> 001~100まで固定
public class OptionalItemNo extends CodePrimitiveValue<OptionalItemNo>{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new optional item no.
	 *
	 * @param rawValue the raw value
	 */
	public OptionalItemNo(String rawValue) {
		super(rawValue);
	}

}
