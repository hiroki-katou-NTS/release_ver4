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
 * The Class SocialInsuranceItemDto.
 *
 */

@Setter
@Getter
public class DataRowItem {

    /** The code. */
    private String code;
    
    /** The name. */
    private String name;
    
    /** The monthly health insurance normal. */
    private double monthlyHealthInsuranceNormal;
    
    /** The monthly general insurance normal. */
    private double monthlyGeneralInsuranceNormal;
    
    /** The monthly long term insurance normal. */
    private double monthlyLongTermInsuranceNormal;
    
    /** The monthly specific insurance normal. */
    private double monthlySpecificInsuranceNormal;
    
    /** The monthly basic insurance normal. */
    private double monthlyBasicInsuranceNormal;
    
    /** The monthly health insurance deduction. */
    private double monthlyHealthInsuranceDeduction;
    
    /** The monthly general insurance deduction. */
    private double monthlyGeneralInsuranceDeduction;
    
    /** The monthly long term insurance deduction. */
    private double monthlyLongTermInsuranceDeduction;
    
    /** The monthly specific insurance deduction. */
    private double monthlySpecificInsuranceDeduction;
    
    /** The monthly basic insurance deduction. */
    private double monthlyBasicInsuranceDeduction;
    
    /** The welfare pension insurance normal. */
    private double welfarePensionInsuranceNormal;
    
    /** The welfare pension insurance deduction. */
    private double welfarePensionInsuranceDeduction;
    
    /** The welfare pension fund normal. */
    private double welfarePensionFundNormal;
    
    /** The welfare pension fund deduction. */
    private double welfarePensionFundDeduction;
    
    /** The child raising contribution money.*/
    private double childRaisingContributionMoney;
    
}
