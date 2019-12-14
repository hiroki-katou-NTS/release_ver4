package nts.uk.ctx.at.record.app.command.reservation.bento;

import java.util.Arrays;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservation;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationRepository;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReserveModifyService;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenuRepository;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTimeFrame;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BentoReserveMofidyCommandHandler extends CommandHandler<BentoReserveCommand> {
	
	@Inject
	private StampCardRepository stampCardRepository;
	
	@Inject
	private BentoMenuRepository bentoMenuRepository;
	
	@Inject
	private BentoReservationRepository bentoReservationRepository;

	@Override
	protected void handle(CommandHandlerContext<BentoReserveCommand> context) {
		
		BentoReserveCommand command = context.getCommand();
		
		StampCard stampCard = stampCardRepository.getLstStampCardByLstSidAndContractCd(
				Arrays.asList(AppContexts.user().employeeId()),
				AppContexts.user().contractCode()).get(0);
		
		ReservationRegisterInfo reservationRegisterInfo = new ReservationRegisterInfo(stampCard.getStampNumber().toString());
		
		RequireImpl require = new RequireImpl(bentoMenuRepository, bentoReservationRepository);
		
		AtomTask persist1 = BentoReserveModifyService.reserve(
				require, 
				reservationRegisterInfo, 
				new ReservationDate(command.getDate(), ReservationClosingTimeFrame.FRAME1), 
				command.getFrame1Bentos());
		
		AtomTask persist2 = BentoReserveModifyService.reserve(
				require, 
				reservationRegisterInfo, 
				new ReservationDate(command.getDate(), ReservationClosingTimeFrame.FRAME2), 
				command.getFrame2Bentos());
		
		transaction.execute(() -> {
			persist1.run();
			persist2.run();
		});
		
	}
	
	@AllArgsConstructor
	private static class RequireImpl implements BentoReserveModifyService.Require {
		
		private final BentoMenuRepository bentoMenuRepository;
		
		private final BentoReservationRepository bentoReservationRepository;

		@Override
		public BentoMenu getBentoMenu(ReservationDate reservationDate) {
			String companyID = AppContexts.user().companyId();
			return bentoMenuRepository.getBentoMenu(companyID, reservationDate.getDate());
		}

		@Override
		public Optional<BentoReservation> getBefore(ReservationRegisterInfo registerInfor,
				ReservationDate reservationDate) {
			return bentoReservationRepository.find(registerInfor, reservationDate);
		}

		@Override
		public void reserve(BentoReservation bentoReservation) {
			bentoReservationRepository.add(bentoReservation);
		}

		@Override
		public void delete(BentoReservation bentoReservation) {
			bentoReservationRepository.delete(bentoReservation);
		}
		
	}
}
