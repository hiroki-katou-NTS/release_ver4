/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment.data;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class PaymentSalaryReportData.
 *
 */

@Setter
@Getter
public class PaymentSalaryReportData {
    
    /** The header data. */
    private HeaderReportData headerData;
    
    /** The employees. */
    private List<EmployeeDto> employees; // employees are sorted by department level.
    
    /** The map employee amount. */
    private Map<EmployeeKey, Double> mapEmployeeAmount;
    
    /** The configure. */
    private SalaryPrintSettingDto configure;
}
