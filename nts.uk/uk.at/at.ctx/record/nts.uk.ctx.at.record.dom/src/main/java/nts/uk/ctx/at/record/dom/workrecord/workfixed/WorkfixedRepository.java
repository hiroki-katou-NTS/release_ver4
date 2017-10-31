/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.workrecord.workfixed;

import java.util.Optional;

/**
 * The Interface WorkfixedRepository.
 */
public interface WorkfixedRepository {
	 
	 /**
 	 * Removes the.
 	 *
 	 * @param workPlaceId the work place id
 	 * @param closureId the closure id
 	 */
 	void remove(String workPlaceId, Integer closureId); 
	 
	 /**
 	 * Adds the.
 	 *
 	 * @param workFixed the work fixed
 	 */
 	void add(WorkFixed workFixed);
	 
	 /**
 	 * Find by work place id and closure id.
 	 *
 	 * @param workPlaceId the work place id
 	 * @param closureId the closure id
 	 * @return the optional
 	 */
 	Optional<WorkFixed> findByWorkPlaceIdAndClosureId(String workPlaceId, Integer closureId);
	 
	 /**
 	 * Update.
 	 *
 	 * @param workFixed the work fixed
 	 */
 	void update(WorkFixed workFixed);
	 
	 
}
