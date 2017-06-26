/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.insurance.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SocialInsuMLayoutReportData.
 *
 */

@Setter
@Getter
public class SocialInsuMLayoutReportData {

    /** The header data. */
    private HeaderReportData headerData;
    
    /** The report item. */
    private List<MLayoutInsuOfficeDto> officeItems;
    
    /** The total all office. */
    private MLayoutRowItem totalAllOffice;
    
    /** The delivery notice amount. */
    private double deliveryNoticeAmount;
    
    /** The insured collect amount. */
    private double insuredCollectAmount;
    
    /** The configure output. */
    private ChecklistPrintSettingDto configureOutput;
}
