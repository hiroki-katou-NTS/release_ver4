package nts.uk.ctx.core.dom.socialinsurance.healthinsurance;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.core.dom.socialinsurance.InsuranceFee;

/**
 * 各負担料
 */
@Getter
public class HealthInsuranceContributionFee extends DomainObject {

    /**
     * 健康保険料
     */
    private InsuranceFee healthInsurancePremium;

    /**
     * 介護保険料
     */
    private InsuranceFee nursingCare;

    /**
     * 特定保険料
     */
    private InsuranceFee specInsurancePremium;

    /**
     * 基本保険料
     */
    private InsuranceFee basicInsurancePremium;

    /**
     * 各負担料
     *
     * @param healthInsurancePremium 健康保険料
     * @param nursingCare            介護保険料
     * @param specInsurancePremium   特定保険料
     * @param basicInsurancePremium  基本保険料
     */
    public HealthInsuranceContributionFee(BigDecimal healthInsurancePremium, BigDecimal nursingCare, BigDecimal specInsurancePremium, BigDecimal basicInsurancePremium) {
        this.healthInsurancePremium = new InsuranceFee(healthInsurancePremium);
        this.nursingCare            = new InsuranceFee(nursingCare);
        this.specInsurancePremium   = new InsuranceFee(specInsurancePremium);
        this.basicInsurancePremium  = new InsuranceFee(basicInsurancePremium);
    }
}
