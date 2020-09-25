package nts.uk.screen.at.app.kmk.kmk008.employment;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfEmploymentRepostitory;
import nts.uk.ctx.at.shared.dom.standardtime.AgreementTimeOfEmployment;
import nts.uk.ctx.at.shared.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class AgreeTimeOfEmploymentScreenProcessor {

    @Inject
    private AgreementTimeOfEmploymentRepostitory timeOfEmploymentRepostitory;

    public AgreementTimeOfEmploymentDto findAgreeTimeOfEmployment(RequestEmployment request) {

        Optional<AgreementTimeOfEmployment> data = timeOfEmploymentRepostitory.find(
                AppContexts.user().companyId(),request.getEmploymentCode(), EnumAdaptor.valueOf(request.getLaborSystemAtr(), LaborSystemtAtr.class));

        return AgreementTimeOfEmploymentDto.setData(data);
    }
}
