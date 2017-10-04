/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.executionlog;

import java.util.List;

/**
 * The Interface ScheduleCreatorRepository.
 */
public interface ScheduleCreatorRepository {
	
	/**
	 * Find all.
	 *
	 * @param executionId the execution id
	 * @return the list
	 */
	public List<ScheduleCreator> findAll(String executionId);
	
	/**
	 * Save.
	 *
	 * @param domain the domain
	 */
	public void save(ScheduleCreator domain);
	
	/**
	 * Save all.
	 *
	 * @param domains the domains
	 */
	public void saveAll(List<ScheduleCreator> domains);

}
