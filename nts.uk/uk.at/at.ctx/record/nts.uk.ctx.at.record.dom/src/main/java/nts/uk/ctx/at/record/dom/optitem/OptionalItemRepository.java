/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem;

import java.util.List;

/**
 * The Interface OptionalItemRepository.
 */
public interface OptionalItemRepository {

	/**
	 * Update.
	 *
	 * @param dom the dom
	 */
	void update(OptionalItem dom);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param optionalItemNo the optional item no
	 * @return the optional item
	 */
	OptionalItem find(String companyId, String optionalItemNo);

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	List<OptionalItem> findAll(String companyId);

	/**
	 * Find by atr.
	 *
	 * @param companyId the company id
	 * @param atr the atr
	 * @return the list
	 */
	List<OptionalItem> findByAtr(String companyId, int atr);
}
