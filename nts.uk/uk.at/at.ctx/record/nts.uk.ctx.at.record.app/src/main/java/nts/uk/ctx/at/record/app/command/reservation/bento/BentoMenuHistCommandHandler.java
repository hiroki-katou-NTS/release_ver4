package nts.uk.ctx.at.record.app.command.reservation.bento;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.reservation.bento.*;
import nts.uk.ctx.at.record.dom.reservation.bento.rules.BentoReservationTimeName;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.*;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.BentoReservationClosingTime;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTime;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.BentoReservationSetting;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.BentoReservationSettingRepository;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.OperationDistinction;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.i18n.TextResource;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *  予約構成を追加する
 */
@Stateless
public class BentoMenuHistCommandHandler extends CommandHandler<BentoMenuHistCommand> {
    @Inject
    private IBentoMenuHistoryRepository bentoMenuHistotyRepository;

    @Inject
    private  BentomenuAdapter bentomenuAdapter;

    @Inject
    private  BentoMenuRepository bentoMenuRepository;

    @Inject
    private  BentoReservationSettingRepository reservationSettingRepository;

    final String bentoName = TextResource.localize("KMR001_83");
    final String unit = TextResource.localize("KMR001_84");

    @Override
    protected void handle(CommandHandlerContext<BentoMenuHistCommand> commandHandlerContext) {
        val command = commandHandlerContext.getCommand();
        RequireImpl require = new RequireImpl(bentoMenuHistotyRepository,bentoMenuRepository,reservationSettingRepository);
        val companyId = AppContexts.user().companyId();

        ReservationClosingTime closingTime1 = new ReservationClosingTime(new BentoReservationTimeName("1111"),
                new BentoReservationTime(120),
                Optional.of(new BentoReservationTime(0)));

        BentoReservationClosingTime bentoReservationClosingTime = new BentoReservationClosingTime(closingTime1,Optional.empty());
        AtomTask persist = BentoMenuHistService.register(require,
                new DatePeriod(command.getDate(), GeneralDate.max()), companyId,bentoReservationClosingTime);

        transaction.execute(() -> {
            persist.run();
        });
    }

    @AllArgsConstructor
    private class RequireImpl implements BentoMenuHistService.Require {

        private IBentoMenuHistoryRepository bentoMenuHistoryRepository;

        private BentoMenuRepository bentoMenuRepository;

        private BentoReservationSettingRepository reservationSettingRepository;

        @Override
        public BentoMenu getBentoMenu(String companyID, GeneralDate date) {
            return bentoMenuRepository.getBentoMenuByEndDate(companyID, date);
        }

        @Override
        public Optional<BentoMenuHistory> findByCompanyId(String companyId) {
            Optional<BentoMenuHistory> existHist = bentoMenuHistoryRepository.findByCompanyId(companyId);
            return existHist;
        }

        @Override
        public void update(DateHistoryItem item) {
            bentoMenuHistoryRepository.update(BentoMenuHistory.toDomain(AppContexts.user().companyId(), Arrays.asList(item)));
        }

        @Override
        public void add(DateHistoryItem item) {
            bentoMenuHistoryRepository.add(BentoMenuHistory.toDomain(AppContexts.user().companyId(),Arrays.asList(item)));
        }

        @Override
        public void addBentomenu(DateHistoryItem hist, BentoReservationClosingTime bentoReservationClosingTime,List<Bento> bentos) {

            Optional<BentoReservationSetting> bentoReservationSetting = reservationSettingRepository.findByCId(AppContexts.user().companyId());

            String workLocation = null;

            if (bentoReservationSetting.isPresent() && bentoReservationSetting.get().getOperationDistinction().value == OperationDistinction.BY_LOCATION.value){
                val data = bentomenuAdapter.findBySid(AppContexts.user().employeeId(),GeneralDate.today());
                workLocation = data.isPresent() ? data.get().getWorkLocationCd() : null;
            }
            Optional<WorkLocationCode> workLocationCode = workLocation == null ? Optional.empty() : Optional.of(new WorkLocationCode(workLocation));

            if (bentos.size() == 0){
                Bento bento = new Bento(1,new BentoName(bentoName),new BentoAmount(1000),
                        new BentoAmount(0),new BentoReservationUnitName(unit),
                        true,false,workLocationCode);
                bentoMenuRepository.add(new BentoMenu(hist.identifier() , Arrays.asList(bento),bentoReservationClosingTime));
            }else {
                bentoMenuRepository.add(new BentoMenu(hist.identifier() , bentos,bentoReservationClosingTime));
            }

        }
    }
}

