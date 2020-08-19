package nts.uk.screen.at.app.reservation;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.Bento;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenuRepository;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.BentoReservationClosingTime;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.BentoReservationSetting;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.GetEmployeeReferenceRangeService;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.OperationDistinction;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 予約確認一覧
 *
 * @author 3si - Dang Huu Khai
 */
@Stateless
public class ReservationConfirmationListScreenQuery {

    @Inject
    private GetEmployeeReferenceRangeService domainService;

    @Inject
    private BentoMenuRepository bentoMenuRepo;

    public ReservationConfirmationListDto getReservationConfirmationListStartupInfo(String companyId) {
        ReservationConfirmationListDto dto = new ReservationConfirmationListDto();

        Optional<BentoReservationSetting> optBentoReservationSetting = domainService.getBentoReservationSetting(companyId);
        if (optBentoReservationSetting.isPresent()) {
            BentoReservationSetting bentoReservationSetting = optBentoReservationSetting.get();
            dto.setOperationDistinction(bentoReservationSetting.getOperationDistinction());
        }

        BentoMenu bentoMenuByEndDate = bentoMenuRepo.getBentoMenuByEndDate(companyId, GeneralDate.max());

        List<Bento> bentoList = bentoMenuByEndDate.getMenu();
        List<List<Bento>> partitions = new ArrayList<>(
                bentoList.stream()
                        .collect(Collectors.partitioningBy(item -> item.getWorkLocationCode().isPresent()))
                        .values()
        );

        List<Bento> bentoMenu;
        if (dto.getOperationDistinction() == OperationDistinction.BY_COMPANY) {
            bentoMenu = partitions.get(1);
        } else {
            bentoMenu = partitions.get(0);
        }
        List<BentoItemDto> bentoItemList = copyBentoItemList(bentoMenu);
        dto.setMenu(bentoItemList);

        BentoReservationClosingTime closingTime = bentoMenuByEndDate.getClosingTime();
        String reservationFrameName1 = closingTime.getClosingTime1().getReservationTimeName().v();
        int reservationStartTime1 = closingTime.getClosingTime1().getStart().get().v();
        int reservationEndTime1 = closingTime.getClosingTime1().getFinish().v();
        String reservationFrameName2 = closingTime.getClosingTime2().get().getReservationTimeName().v();
        int reservationStartTime2 = closingTime.getClosingTime2().get().getStart().get().v();
        int reservationEndTime2 = closingTime.getClosingTime2().get().getFinish().v();
        BentoMenuDto bentoMenuDtoClosingTime = new BentoMenuDto(
                reservationFrameName1,
                reservationStartTime1,
                reservationEndTime1,
                reservationFrameName2,
                reservationStartTime2,
                reservationEndTime2
        );
        dto.setClosingTime(bentoMenuDtoClosingTime);

        return dto;
    }

    private List<BentoItemDto> copyBentoItemList(List<Bento> bentoList){
        return bentoList.stream().map(item -> {
            BentoItemDto bentoItemDto = new BentoItemDto();
            bentoItemDto.setCode(item.getFrameNo());
            bentoItemDto.setName(item.getName().v());
            return bentoItemDto;
        }).collect(Collectors.toList());
    }
}