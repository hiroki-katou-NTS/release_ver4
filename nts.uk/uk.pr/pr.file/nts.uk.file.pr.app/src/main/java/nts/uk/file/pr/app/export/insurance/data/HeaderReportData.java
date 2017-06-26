/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.insurance.data;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class HeaderReportData.
 *
 */

@Getter
@Setter
public class HeaderReportData {

    /** The name company. */
    private String nameCompany;
    
    /** The tile report */
    private String titleReport;
    
    /** The information office. */
    private String informationOffice;
    
    /** The target year month. */
    private String targetYearMonth;
    
    /** The condition. */
    private String condition;
    
    /** The formal calculation. */
    private String formalCalculation;
}