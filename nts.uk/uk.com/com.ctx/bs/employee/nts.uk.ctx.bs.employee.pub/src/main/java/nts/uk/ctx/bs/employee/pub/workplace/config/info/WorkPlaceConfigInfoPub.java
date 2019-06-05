package nts.uk.ctx.bs.employee.pub.workplace.config.info;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface WorkPlaceConfigInfoPub {
	/**
	 * Find by history ids and wpl ids.
	 *
	 * @param companyId
	 *            the company id
	 * @param historyIds
	 *            the history ids
	 * @param workplaceIds
	 *            the workplace ids -> if null or emplty, return all workplace
	 *            config info.
	 * @return the list
	 */
	List<WorkPlaceConfigInfoExport> findByHistoryIdsAndWplIds(String companyId, List<String> historyIds,
			List<String> workplaceIds);
	
	List<JobTitleExport> findAllById(String companyId,List<String> positionIds ,GeneralDate baseDate );
}
