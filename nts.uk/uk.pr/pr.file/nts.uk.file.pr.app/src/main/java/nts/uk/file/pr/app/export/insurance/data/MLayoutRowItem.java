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
 * The Class MLayoutRowItem.
 *
 */

@Setter
@Getter
public class MLayoutRowItem {
    
    /** The code. */
    private String code;
    
    /** The name. */
    private String name;
    
    /** The heal insu fee personal. */
    private double healInsuFeePersonal;
    
    /** The heal insu fee company. */
    private double healInsuFeeCompany;
    
    /** The deduction heal insu personal. */
    private double deductionHealInsuPersonal;
    
    /** The deduction heal insu company. */
    private double deductionHealInsuCompany;
    
    /** The welfare pen insu personal. */
    private double welfarePenInsuPersonal;
    
    /** The welfare pen insu company. */
    private double welfarePenInsuCompany;
    
    /** The deduction welfare pen insu personal. */
    private double deductionWelfarePenInsuPersonal;
    
    /** The deduction welfare pen insu company. */
    private double deductionWelfarePenInsuCompany;
    
    /** The welfare pen fund personal. */
    private double welfarePenFundPersonal;
    
    /** The welfare pen fund company. */
    private double welfarePenFundCompany;
    
    /** The deduction welfare pen fund personal. */
    private double deductionWelfarePenFundPersonal;
    
    /** The deduction welfare pen fund company. */
    private double deductionWelfarePenFundCompany;
    
    /** The child raising. */
    private double childRaising;
}
