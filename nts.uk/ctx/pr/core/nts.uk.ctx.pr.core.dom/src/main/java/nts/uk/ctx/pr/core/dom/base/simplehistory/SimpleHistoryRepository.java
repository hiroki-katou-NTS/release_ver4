/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.base.simplehistory;

import java.util.Optional;

/**
 * The Interface SimpleHistoryRepository.
 */
public interface SimpleHistoryRepository<V extends Master, T extends History<T>> {

	/**
	 * Delete history.
	 *
	 * @param uuid the uuid
	 */
	void deleteHistory(String uuid);

	/**
	 * Find lastest history by master code.
	 *
	 * @param masterCode the master code
	 * @return the t
	 */
	Optional<T> findLastestHistoryByMasterCode(String companyCode, String masterCode);

	/**
	 * Find history by uuid.
	 *
	 * @param uuid the uuid
	 * @return the optional
	 */
	Optional<T> findHistoryByUuid(String uuid);

	/**
	 * Adds the history.
	 *
	 * @param history the history
	 */
	void addHistory(T history);

	/**
	 * Update history.
	 *
	 * @param history the history
	 */
	void updateHistory(T history);
}
