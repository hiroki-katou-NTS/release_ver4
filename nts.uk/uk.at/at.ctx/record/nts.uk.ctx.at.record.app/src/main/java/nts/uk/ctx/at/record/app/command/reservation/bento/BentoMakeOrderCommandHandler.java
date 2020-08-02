package nts.uk.ctx.at.record.app.command.reservation.bento;

import lombok.AllArgsConstructor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoMakeOrderService;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservation;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationRepository;
import nts.uk.ctx.at.record.dom.reservation.bentoReservationSetting.BentoReservationSetting;
import nts.uk.ctx.at.record.dom.reservation.bentoReservationSetting.BentoReservationSettingRepository;
import nts.uk.ctx.at.record.dom.reservation.bentoReservationSetting.OrderDeadline;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 注文済みにする
 * @author Hoang Anh Tuan
 */
@Stateless
public class BentoMakeOrderCommandHandler extends CommandHandler<List<BentoMakeOrderCommand>> {

    private final boolean ORDERED = true;

    @Inject
    BentoReservationSettingRepository bentoReservationSettingRepository;

    @Inject
    BentoReservationRepository bentoReservationRepository;

    @Override
    protected void handle(CommandHandlerContext<List<BentoMakeOrderCommand>> context) {
        OrderDeadline orderDeadlineTemp = OrderDeadline.DURING_CLOSING_PERIOD;
        RequireImpl require = new RequireImpl(bentoReservationRepository);
        Optional<BentoReservation> temp;
        List<BentoReservation> result = new ArrayList<>();
        List<BentoMakeOrderCommand> commands = context.getCommand();
        Optional<BentoReservationSetting> bentoReservationSetting = bentoReservationSettingRepository.findByCId(AppContexts.user().companyId());

        if(bentoReservationSetting.isPresent())
            orderDeadlineTemp = bentoReservationSetting.get().getCorrectionContent().getOrderDeadline();

        if(orderDeadlineTemp == OrderDeadline.ALWAYS){
            for(BentoMakeOrderCommand command :commands ){
                temp = bentoReservationRepository.find(command.getReservationRegisterInfo(), command.getReservationDate());
                if(temp.isPresent()){
                    temp.get().setOrdered(ORDERED);
                    result.add(temp.get());
                    AtomTask persist = BentoMakeOrderService.update(require, temp.get());
                    transaction.execute(() -> {
                        persist.run();
                    });
                }
            }
        }
    }

    @AllArgsConstructor
    private class RequireImpl implements BentoMakeOrderService.Require{
        private BentoReservationRepository repository;
        @Override
        public void update(BentoReservation bentoReservation){
            this.repository.update(bentoReservation);
        }
    }
}
