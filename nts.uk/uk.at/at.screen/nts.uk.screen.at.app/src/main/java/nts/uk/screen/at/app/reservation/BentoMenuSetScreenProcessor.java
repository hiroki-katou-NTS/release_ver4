package nts.uk.screen.at.app.reservation;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BentoMenuSetScreenProcessor {

    @Inject
    private BentoMenuScreenRepository bentoMenuScreenRepository;

    @Inject
    private BentoReservationScreenRepository bentoReservationScreenRepository;

    public BentoMenuJoinBentoSettingDto findDataBentoMenu() {
        String companyID = AppContexts.user().companyId();
        GeneralDate generalDate = GeneralDate.max();
        BentoMenuDto bentoMenuDto = bentoMenuScreenRepository.findDataBentoMenu(companyID,generalDate);

        BentoReservationSettingDto reservationSettingDto = bentoReservationScreenRepository.findDataBentoRervation(companyID);

        return BentoMenuJoinBentoSettingDto.setData(bentoMenuDto,reservationSettingDto);
    }

    public List<BentoJoinReservationSetting> getBentoMenuByHist(String histId) {
        String companyID = AppContexts.user().companyId();
        GeneralDate generalDate = GeneralDate.max();
        List<BentoDto> bentoDtos =  bentoMenuScreenRepository.findDataBento(companyID,generalDate,histId);
        BentoReservationSettingDto reservationSettingDto = bentoReservationScreenRepository.findDataBentoRervation(companyID);
        return BentoJoinReservationSetting.setData(bentoDtos,reservationSettingDto);
    }
}
