/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.detailpayment.query;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class PaymentSalaryQuery.
 */
@Setter
@Getter
public class PaymentSalaryQuery {

    /** The start date. */
    private Integer startDate;

    /** The end date. */
    private Integer endDate;
    
    /** The is normal month. */
    private Boolean isNormalMonth;
    
    /** The is preliminary month. */
    private Boolean isPreliminaryMonth;
    
    /** The employee codes. */
    private List<String> personIds;

    /** The output format type. */
    private String outputFormatType;

    /** The output setting code. */
    private String outputSettingCode;

    /** The is vertical line. */
    private Boolean isVerticalLine;

    /** The is horizontal line. */
    private Boolean isHorizontalLine;

    /** The page break setting. */
    private String pageBreakSetting;

    /** The hierarchy. */
    private String hierarchy;

    /** The output language. */
    private String outputLanguage;
}
