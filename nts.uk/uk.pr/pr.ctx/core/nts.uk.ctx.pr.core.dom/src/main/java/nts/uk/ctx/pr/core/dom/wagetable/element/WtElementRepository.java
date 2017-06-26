/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element;

import java.util.Optional;

/**
 * The Interface WageTableElementRepository.
 */
public interface WtElementRepository {

	/**
	 * Find by history id.
	 *
	 * @param hitsoryId the hitsory id
	 * @return the optional
	 */
	Optional<WtElement> findByHistoryId(String historyId);

}
