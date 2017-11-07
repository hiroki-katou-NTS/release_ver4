/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.adapter.executionlog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.WorkplaceDto;

/**
 * The Interface ScWorkplaceAdapter.
 */
public interface ScWorkplaceAdapter {
    
    /**
     * Find wpk id list.
     *
     * @param companyId the company id
     * @param wpkCode the wpk code
     * @param baseDate the base date
     * @return the list
     */
    List<String> findWpkIdList(String companyId, String wpkCode, Date baseDate);
    
    
    /**
     * Find work place by id.
     *
     * @param employeeId the employee id
     * @param baseDate the base date
     * @return the optional
     */
    public Optional<WorkplaceDto> findWorkplaceById(String employeeId, GeneralDate baseDate);
}
