package nts.uk.file.at.app.export.bento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.app.command.reservation.bento.BentoMakeOrderCommand;
import nts.uk.ctx.at.record.app.command.reservation.bento.BentoMakeOrderCommandHandler;
import nts.uk.ctx.at.record.app.find.reservation.bento.dto.BentoReservationSearchConditionDto;
import nts.uk.ctx.at.record.app.find.reservation.bento.dto.OrderInfoDto;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTimeFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 予約確認一覧起動情報
 * @author tuan.ha1
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrderInfoDataSource {
    private List<String> workplaceIds;
    private String workLocationCodes;
    private DatePeriodDto period;
    private int totalExtractCondition;
    private int itemExtractCondition;
    private int frameNo;
    private String totalTitle;
    private String detailTitle;
    private int reservationClosingTimeFrame;
    private boolean isBreakPage;
    private boolean extractionConditionChecked;

    public OrderInfoDto getGeneratorData(CreateOrderInfoFileQuery createOrderInfoFileQuery, BentoMakeOrderCommandHandler commandHandler){
        Optional<BentoReservationSearchConditionDto> totalExtractCondition = this.getTotalExtractCondition() > -1
                ?  Optional.of(EnumAdaptor.valueOf(this.getTotalExtractCondition(), BentoReservationSearchConditionDto.class)) : Optional.empty();
        Optional<BentoReservationSearchConditionDto> itemExtractCondition = this.getItemExtractCondition() > -1
                ?  Optional.of(EnumAdaptor.valueOf(this.getItemExtractCondition(), BentoReservationSearchConditionDto.class)) : Optional.empty();
        Optional<Integer> frameNo = this.getFrameNo() > -1 ? Optional.of(this.getFrameNo()) : Optional.empty();
        Optional<String> totalTitle = this.getTotalTitle() == null | "".equals(this.getTotalTitle())
                ? Optional.empty() : Optional.of(this.getTotalTitle());
        Optional<String> detailTitle = this.getDetailTitle() == null | "".equals(this.getDetailTitle())
                ? Optional.empty() : Optional.of(this.getDetailTitle());
        ReservationClosingTimeFrame closingTimeFrame = EnumAdaptor.valueOf(this.getReservationClosingTimeFrame(), ReservationClosingTimeFrame.class);
        List<String> workLocationCode = new ArrayList<>();
        if(workLocationCodes != null & !"".equals(workLocationCodes))
            workLocationCode.add(workLocationCodes);

        OrderInfoDto result = createOrderInfoFileQuery.createOrderInfoFileQuery(this.getPeriod().convertToDate("yyyy/MM/dd"),this.getWorkplaceIds(), workLocationCode,
                totalExtractCondition, itemExtractCondition, frameNo, totalTitle,
                detailTitle, closingTimeFrame);
        if(this.extractionConditionChecked){
            List<BentoMakeOrderCommand> commands = new ArrayList<>();
            result.getTotalOrderInfoDtoList().forEach(item -> commands.add(new BentoMakeOrderCommand(
                    new ReservationDate(item.getReservationDate(),closingTimeFrame), new ReservationRegisterInfo(item.getReservationRegisInfo())
            )));
            commandHandler.handle(commands);
        }
        return result;
    }
}
