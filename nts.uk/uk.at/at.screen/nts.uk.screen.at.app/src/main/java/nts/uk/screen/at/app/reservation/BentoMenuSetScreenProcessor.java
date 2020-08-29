package nts.uk.screen.at.app.reservation;

import nts.arc.error.BusinessException;
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

    public BentoJoinReservationSetting getBentoMenuByHist(BentoRequest request) {
        String companyID = AppContexts.user().companyId();
        GeneralDate generalDate = GeneralDate.max();
        List<BentomenuJoinBentoDto> bentomenuJoinBentoDtos =  bentoMenuScreenRepository.findDataBento(companyID,generalDate,request);
        if (bentomenuJoinBentoDtos.size() == 0){
            throw new BusinessException("Msg_1848");
        }
        BentoReservationSettingDto reservationSettingDto = bentoReservationScreenRepository.findDataBentoRervation(companyID);
        if (reservationSettingDto == null){
            throw new BusinessException("Msg_1847");
        }
        return BentoJoinReservationSetting.setData(bentomenuJoinBentoDtos,reservationSettingDto);
    }

    public List<WorkLocationDto> getWorkLocationByCid() {
        String companyID = AppContexts.user().companyId();
        return bentoMenuScreenRepository.findDataWorkLocation(companyID);
    }
}
