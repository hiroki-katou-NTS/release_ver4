package nts.uk.ctx.at.record.dom.reservation.bento;

import java.util.List;
import java.util.Optional;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface BentoReservationRepository {
	
	public Optional<BentoReservation> find(ReservationRegisterInfo registerInfor, ReservationDate reservationDate);
	
	public List<BentoReservation> findList(ReservationRegisterInfo registerInfor, ReservationDate reservationDate);
	
	public void add(BentoReservation bentoReservation);
	
	public void delete(BentoReservation bentoReservation);
	
	public List<BentoReservation> findByOrderedPeriodEmpLst(List<ReservationRegisterInfo> inforLst, DatePeriod period, boolean ordered);
	
}
