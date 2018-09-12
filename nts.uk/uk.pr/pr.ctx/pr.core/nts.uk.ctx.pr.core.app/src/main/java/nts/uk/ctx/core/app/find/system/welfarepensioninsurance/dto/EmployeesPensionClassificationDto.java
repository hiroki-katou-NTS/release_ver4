package nts.uk.ctx.core.app.find.system.welfarepensioninsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.core.dom.socialinsurance.welfarepensioninsurance.EmployeesPensionClassification;
import nts.uk.ctx.core.dom.socialinsurance.welfarepensioninsurance.InsurancePremiumFractionClassification;

/**
 * 厚生年金端数区分
 */
@Getter
@AllArgsConstructor
public class EmployeesPensionClassificationDto{

    /**
     * 個人端数区分
     */
    private int personalFraction;

    /**
     * 事業主端数区分
     */
    private int businessOwnerFraction;

    /**
     * 厚生年金端数区分
     *
     * @param personalFraction      個人端数区分
     * @param businessOwnerFraction 事業主端数区分
     */
    public static EmployeesPensionClassificationDto fromDomainToDto(EmployeesPensionClassification domain) {
        return new EmployeesPensionClassificationDto(domain.getPersonalFraction().value, domain.getBusinessOwnerFraction().value);
    }
}
