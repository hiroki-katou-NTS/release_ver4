/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SalaryPrintSettingDto.
 *
 */
@Setter
@Getter
public class SalaryPrintSettingDto {
    
    /** The show payment. */
    private Boolean showPayment;

    /** The sum person set. */
    private Boolean sumPersonSet;

    /** The sum month person set. */
    private Boolean sumMonthPersonSet;

    /** The sum each deprt set. */
    private Boolean sumEachDeprtSet;

    /** The sum month deprt set. */
    private Boolean sumMonthDeprtSet;

    /** The sum dep hrchy index set. */
    private Boolean sumDepHrchyIndexSet;

    /** The sum month dep hrchy set. */
    private Boolean sumMonthDepHrchySet;
    
    /** The selected levels. */
    private List<Integer> selectedLevels;
    
    /** The total set. */
    private Boolean totalSet;
    
    /** The month total set. */
    private Boolean monthTotalSet;
    
    // ===== OPTION REPORT =====
    /** The output format type. */
    private String outputFormatType;
    
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
