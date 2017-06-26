/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment.data;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class HeaderReportData.
 *
 */
@Setter
@Getter
public class HeaderReportData {
    
    /** The name company. */
    private String nameCompany;
    
    /** The tile report */
    private String titleReport;
    
    /** The department. */
    private String department;
    
    /** The category. */
    private String category;
    
    /** The position. */
    private String position;
    
    /** The year month. */
    private String targetYearMonth;
}
