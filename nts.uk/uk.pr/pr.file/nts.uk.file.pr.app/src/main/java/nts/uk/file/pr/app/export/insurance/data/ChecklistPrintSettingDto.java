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
 * The Class ChecklistPrintSettingDto.
 *
 */

@Setter
@Getter
public class ChecklistPrintSettingDto {

    /** The show category insurance item. */
    private Boolean showCategoryInsuranceItem;

    /** The show delivery notice amount. */
    private Boolean showDeliveryNoticeAmount;

    /** The show detail. */
    private Boolean showDetail;

    /** The show office. */
    private Boolean showOffice;

    /** The show total. */
    private Boolean showTotal;
}
