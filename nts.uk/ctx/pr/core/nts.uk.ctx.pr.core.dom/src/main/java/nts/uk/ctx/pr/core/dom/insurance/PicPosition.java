/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * Name of Office.
 */
@StringMaxLength(12)
public class PicPosition extends StringPrimitiveValue<PicPosition> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PicPosition(String rawValue) {
		super(rawValue);
	}

}
