/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.budget.external.actualresult.export;

import java.util.List;

import lombok.Builder;
import nts.uk.ctx.at.schedule.app.find.budget.external.actualresult.dto.ExternalBudgetErrorDto;

/**
 * The Class ExportData.
 */
@Builder
public class ExportData {
    
    /** The employee id. */
    public String employeeId;
    
    /** The lst header. */
    public List<String> lstHeader;
    
    /** The lst error. */
    public List<ExternalBudgetErrorDto> lstError;
    
    
}
