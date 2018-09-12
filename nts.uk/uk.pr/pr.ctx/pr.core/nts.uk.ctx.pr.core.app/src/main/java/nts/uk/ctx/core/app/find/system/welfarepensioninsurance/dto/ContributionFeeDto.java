package nts.uk.ctx.core.app.find.system.welfarepensioninsurance.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.core.dom.socialinsurance.welfarepensioninsurance.ContributionFee;
/**
 * 各負担料
 */
@Data
@AllArgsConstructor
public class ContributionFeeDto {
	/**
     * 女子保険料
     */
    private BigDecimal femaleInsurancePremium;

    /**
     * 男子保険料
     */
    private BigDecimal maleInsurancePremium;

    /**
     * 女子免除保険料
     */
    private BigDecimal femaleExemptionInsurance;

    /**
     * 男子免除保険料
     */
    private BigDecimal maleExemptionInsurance;
    
    public static ContributionFeeDto fromDomainToDto (ContributionFee domain) {
    	BigDecimal femaleExemptionInsurance = domain.getFemaleExemptionInsurance().isPresent() ? domain.getFemaleExemptionInsurance().get().v() : null;
    	BigDecimal maleExemptionInsurance = domain.getMaleExemptionInsurance().isPresent() ? domain.getMaleExemptionInsurance().get().v() : null;
    	return new ContributionFeeDto(domain.getFemaleInsurancePremium().v(), domain.getMaleInsurancePremium().v(), femaleExemptionInsurance, maleExemptionInsurance);
    }
}
