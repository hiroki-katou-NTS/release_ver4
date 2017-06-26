/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.postcode.find;

import java.util.List;

/**
 * The Interface PostCodeFinder.
 */

public interface PostCodeFinder {

	/**
	 * Find address selection list.
	 *
	 * @param zipCode the zip code
	 * @return the list
	 */
	public List<PostCode> findPostCodeList(String zipCode);

}
