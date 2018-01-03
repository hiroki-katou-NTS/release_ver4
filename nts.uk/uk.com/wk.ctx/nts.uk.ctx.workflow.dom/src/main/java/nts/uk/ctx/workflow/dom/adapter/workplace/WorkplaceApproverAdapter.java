/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.workflow.dom.adapter.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
// 基準日時点の対象の職場IDに一致する職場コードと職場名称を取得する
public interface WorkplaceApproverAdapter {

	/**
	 * Find by wkp id.
	 *
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<WorkplaceImport> findByWkpId(String workplaceId, GeneralDate baseDate);
	
	WorkplaceImport findBySid(String employeeId, GeneralDate baseDate);

	List<String> findEmp(String workplaceId, DatePeriod period);
}
