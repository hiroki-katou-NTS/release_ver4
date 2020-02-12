package nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime;

import static nts.arc.time.ClockHourMinute.now;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;

import nts.uk.ctx.at.record.dom.reservation.Helper;

public class BentoMenuByClosingTimeTest {

	@Test
	public void createForCurrent_frame1() {
		
		ReservationClosingTime time1 = Helper.ClosingTime.startEnd(
				now(),
				now().forwardByHours(1));
		
		ReservationClosingTime time2 = Helper.ClosingTime.startEnd(
				now().forwardByHours(2),
				now().forwardByHours(2));
		
		BentoMenuByClosingTime target = BentoMenuByClosingTime.createForCurrent(
				new BentoReservationClosingTime(time1, Optional.of(time2)),
				Collections.emptyList(),
				Collections.emptyList());
		
		assertThat(target.isReservationTime1Atr()).isTrue();
		assertThat(target.isReservationTime2Atr()).isFalse();
	}

	@Test
	public void createForCurrent_frame2() {
		
		ReservationClosingTime time1 = Helper.ClosingTime.startEnd(
				now().backByHours(2),
				now().backByHours(2));
		
		ReservationClosingTime time2 = Helper.ClosingTime.startEnd(
				now(),
				now().forwardByHours(1));
		
		BentoMenuByClosingTime target = BentoMenuByClosingTime.createForCurrent(
				new BentoReservationClosingTime(time1, Optional.of(time2)),
				Collections.emptyList(),
				Collections.emptyList());
		
		assertThat(target.isReservationTime1Atr()).isFalse();
		assertThat(target.isReservationTime2Atr()).isTrue();
	}

	@Test
	public void createForCurrent_both() {
		
		ReservationClosingTime time = Helper.ClosingTime.startEnd(
				now(),
				now().forwardByHours(1));
		
		BentoMenuByClosingTime target = BentoMenuByClosingTime.createForCurrent(
				new BentoReservationClosingTime(time, Optional.of(time)),
				Collections.emptyList(),
				Collections.emptyList());
		
		assertThat(target.isReservationTime1Atr()).isTrue();
		assertThat(target.isReservationTime2Atr()).isTrue();
	}

	@Test
	public void createForCurrent_neither() {
		
		ReservationClosingTime time = Helper.ClosingTime.startEnd(
				now().backByHours(2),
				now().backByHours(2));
		
		BentoMenuByClosingTime target = BentoMenuByClosingTime.createForCurrent(
				new BentoReservationClosingTime(time, Optional.of(time)),
				Collections.emptyList(),
				Collections.emptyList());
		
		assertThat(target.isReservationTime1Atr()).isFalse();
		assertThat(target.isReservationTime2Atr()).isFalse();
	}
	
}
