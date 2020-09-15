/**
 * 
 */
package nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal;

import static nts.arc.time.GeneralDate.today;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.task.tran.AtomTask;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal.EmpInfoTerminalBuilder;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.ReservationReceptionData;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.StampReceptionData;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.StampReceptionData.StampDataBuilder;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.service.ConvertTimeRecordReservationService;
import nts.uk.ctx.at.record.dom.reservation.Helper;
import nts.uk.ctx.at.record.dom.reservation.Helper.ClosingTime;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservation;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationCount;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReserveService;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.AuthcMethod;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.RefectActualResult;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Relieve;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeCalArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ReservationArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SetPreClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailywork.worktime.overtimedeclaration.OvertimeDeclaration;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author ThanhNX
 *
 *         就業情報端末Test
 */
@RunWith(JMockit.class)
public class EmpInfoTerminalTest {

	private EmpInfoTerminal empInfoTerminal;

	@Injectable
	private ConvertTimeRecordReservationService.Require require;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		empInfoTerminal = new EmpInfoTerminalBuilder(new IPAddress("192.168.1.1"), new MacAddress("AABBCCDD"),
				new EmpInfoTerminalCode(1), new EmpInfoTerSerialNo("1"), new EmpInfoTerminalName(""),
				new ContractCode("1"))
						.createStampInfo(new CreateStampInfo(new OutPlaceConvert(NotUseAtr.NOT_USE, Optional.empty()),
								new ConvertEmbossCategory(NotUseAtr.NOT_USE, NotUseAtr.NOT_USE), Optional.empty()))
						.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1))).build();
	}

	@Test
	public void testCreateReservRecord() {

		StampRecord recordExpect = new StampRecord(new StampNumber("1"),
				GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 01), false, ReservationArt.RESERVATION,
				Optional.of(new EmpInfoTerminalCode(1)));

		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Pair<StampRecord, AtomTask> resultActual = empInfoTerminal.createReservRecord(require, receptionData);

		BentoMenu menu = new BentoMenu("historyId", Arrays.asList(Helper.Menu.Item.bentoReserveFrame(1, true, true)),
				ClosingTime.UNLIMITED);
		ReservationRegisterInfo dummyRegInfo = Helper.Reservation.RegInfo.DUMMY;
		ReservationDate todayReserve = Helper.Reservation.Date.of(today());
		Map<Integer, BentoReservationCount> details = Collections.singletonMap(1, Helper.count(1));
		new Expectations() {
			{
				require.getBentoMenu((ReservationDate) any);
				result = menu;

				require.reserve((BentoReservation) any);

			}
		};

		assertThatFieldStamp(resultActual.getLeft(), recordExpect);

		NtsAssert
				.atomTask(
						() -> BentoReserveService.reserve(require, dummyRegInfo, todayReserve,
								GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 01), details),
						any -> require.reserve(any.get()));

	}

	@Test
	public void testCreateStamp() {

		StampRecord recordExpect = new StampRecord(new StampNumber("1"),
				GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), true, ReservationArt.NONE,
				Optional.of(new EmpInfoTerminalCode(1)));

		StampReceptionData dataNR = new StampDataBuilder("1", "A", "1", "A", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime("1201").build();

		RefectActualResult refActualResults = new RefectActualResult("01", null, new WorkTimeCode("1"),
				new OvertimeDeclaration(new AttendanceTime(661), new AttendanceTime(721)));
		// 打刻する方法
		Relieve relieve = new Relieve(AuthcMethod.ID_AUTHC, StampMeans.TIME_CLOCK);

		// 打刻種類
		Stamp stampExpect = new Stamp(new StampNumber("1"), GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), relieve,
				new StampType(false, null, SetPreClockArt.NONE, ChangeClockArt.GOING_TO_WORK, ChangeCalArt.NONE),
				refActualResults, false, null);

		Pair<Stamp, StampRecord> resultActual = empInfoTerminal.createStamp(dataNR);
		assertThatFieldStamp(resultActual.getRight(), recordExpect);
		assertThatFieldSR(resultActual.getLeft(), stampExpect);
	}

	@Test
	public void testCreateStamp2() {

		StampRecord recordExpect = new StampRecord(new StampNumber("1"),
				GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), true, ReservationArt.NONE,
				Optional.of(new EmpInfoTerminalCode(1)));

		StampReceptionData dataNR = new StampDataBuilder("1", "A", "1", "Q", "200303", "").time("0101")
				.overTimeHours("1101").midnightTime("").build();

		RefectActualResult refActualResults = new RefectActualResult(null, null, null, null);
		// 打刻する方法
		Relieve relieve = new Relieve(AuthcMethod.ID_AUTHC, StampMeans.TIME_CLOCK);

		// 打刻種類
		Stamp stampExpect = new Stamp(new StampNumber("1"), GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), relieve,
				new StampType(false, null, SetPreClockArt.NONE, ChangeClockArt.RETURN, ChangeCalArt.NONE),
				refActualResults, false, null);

		Pair<Stamp, StampRecord> resultActual = empInfoTerminal.createStamp(dataNR);
		assertThatFieldStamp(resultActual.getRight(), recordExpect);
		assertThatFieldSR(resultActual.getLeft(), stampExpect);
	}

	@Test
	public void testCreateStamp3() {

		StampRecord recordExpect = new StampRecord(new StampNumber("1"),
				GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), true, ReservationArt.NONE,
				Optional.of(new EmpInfoTerminalCode(1)));

		StampReceptionData dataNR = new StampDataBuilder("1", "A", "", "A", "200303", "01").time("0101")
				.overTimeHours("").midnightTime("1201").build();

		RefectActualResult refActualResults = new RefectActualResult("01", null, null, null);
		// 打刻する方法
		Relieve relieve = new Relieve(AuthcMethod.ID_AUTHC, StampMeans.TIME_CLOCK);

		// 打刻種類
		Stamp stampExpect = new Stamp(new StampNumber("1"), GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), relieve,
				new StampType(false, null, SetPreClockArt.NONE, ChangeClockArt.GOING_TO_WORK, ChangeCalArt.NONE),
				refActualResults, false, null);

		Pair<Stamp, StampRecord> resultActual = empInfoTerminal.createStamp(dataNR);
		assertThatFieldStamp(resultActual.getRight(), recordExpect);
		assertThatFieldSR(resultActual.getLeft(), stampExpect);
	}

	@Test
	public void testCreateStampGoout() {

		StampRecord recordExpect = new StampRecord(new StampNumber("1"),
				GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), true, ReservationArt.NONE,
				Optional.of(new EmpInfoTerminalCode(1)));

		StampReceptionData dataNR = new StampDataBuilder("1", "A", "1", "O", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime("1201").build();

		RefectActualResult refActualResults = new RefectActualResult("01", null, null,
				new OvertimeDeclaration(new AttendanceTime(661), new AttendanceTime(721)));
		// 打刻する方法
		Relieve relieve = new Relieve(AuthcMethod.ID_AUTHC, StampMeans.TIME_CLOCK);

		// 打刻種類
		Stamp stampExpect = new Stamp(
				new StampNumber("1"), GeneralDateTime.ymdhms(2020, 03, 03, 01, 01, 00), relieve, new StampType(false,
						GoingOutReason.PUBLIC, SetPreClockArt.NONE, ChangeClockArt.GO_OUT, ChangeCalArt.NONE),
				refActualResults, false, null);

		Pair<Stamp, StampRecord> resultActual = empInfoTerminal.createStamp(dataNR);
		assertThatFieldStamp(resultActual.getRight(), recordExpect);
		assertThatFieldSR(resultActual.getLeft(), stampExpect);
	}

	@Test
	public void getters() {

		EmpInfoTerminal target = new EmpInfoTerminalBuilder(new IPAddress("192.168.1.1"), new MacAddress("AABBCCDD"),
				new EmpInfoTerminalCode(1), new EmpInfoTerSerialNo("1"), new EmpInfoTerminalName(""),
				new ContractCode("1")).createStampInfo(null).modelEmpInfoTer(ModelEmpInfoTer.NRL_1)
						.intervalTime((new MonitorIntervalTime(1))).build();
		NtsAssert.invokeGetters(target);
	}

	private void assertThatFieldStamp(StampRecord resultActual, StampRecord recordExpect) {

		assertThat(resultActual.getStampNumber()).isEqualTo(recordExpect.getStampNumber());

		assertThat(resultActual.getStampDateTime()).isEqualTo(recordExpect.getStampDateTime());

		assertThat(resultActual.isStampArt()).isEqualTo(recordExpect.isStampArt());

		assertThat(resultActual.getRevervationAtr()).isEqualTo(recordExpect.getRevervationAtr());

	}

	private void assertThatFieldSR(Stamp resultActual, Stamp recordExpect) {

		assertThat(resultActual.getCardNumber()).isEqualTo(recordExpect.getCardNumber());

		assertThat(resultActual.getStampDateTime()).isEqualTo(recordExpect.getStampDateTime());

		assertThat(resultActual.getType().getChangeCalArt()).isEqualTo(recordExpect.getType().getChangeCalArt());
		assertThat(resultActual.getType().getChangeClockArt()).isEqualTo(recordExpect.getType().getChangeClockArt());
		assertThat(resultActual.getType().getGoOutArt()).isEqualTo(recordExpect.getType().getGoOutArt());
		assertThat(resultActual.getType().getSetPreClockArt()).isEqualTo(recordExpect.getType().getSetPreClockArt());

		assertThat(resultActual.getRelieve().getAuthcMethod()).isEqualTo(recordExpect.getRelieve().getAuthcMethod());
		assertThat(resultActual.getRelieve().getStampMeans()).isEqualTo(recordExpect.getRelieve().getStampMeans());

		assertThat(resultActual.getRefActualResults().getCardNumberSupport())
				.isEqualTo(recordExpect.getRefActualResults().getCardNumberSupport());
		if (!resultActual.getRefActualResults().getOvertimeDeclaration().isPresent()) {
			assertThat(resultActual.getRefActualResults().getOvertimeDeclaration())
					.isEqualTo(recordExpect.getRefActualResults().getOvertimeDeclaration());
		} else {
			assertThat(resultActual.getRefActualResults().getOvertimeDeclaration().get().getOverLateNightTime())
					.isEqualTo(
							recordExpect.getRefActualResults().getOvertimeDeclaration().get().getOverLateNightTime());
		}

		assertThat(resultActual.getRefActualResults().getWorkLocationCD().orElse(null))
				.isEqualTo(recordExpect.getRefActualResults().getWorkLocationCD().orElse(null));
		assertThat(resultActual.getRefActualResults().getWorkTimeCode())
				.isEqualTo(recordExpect.getRefActualResults().getWorkTimeCode());

		assertThat(resultActual.isReflectedCategory()).isEqualTo(recordExpect.isReflectedCategory());

		assertThat(resultActual.getLocationInfor()).isEqualTo(recordExpect.getLocationInfor());
	}
}
