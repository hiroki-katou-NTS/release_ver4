/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.base.simplehistory;

import nts.arc.primitive.PrimitiveValue;

/**
 * The Interface Master.
 */
public interface Master {

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	PrimitiveValue<String> getCode();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	PrimitiveValue<String> getName();
}
