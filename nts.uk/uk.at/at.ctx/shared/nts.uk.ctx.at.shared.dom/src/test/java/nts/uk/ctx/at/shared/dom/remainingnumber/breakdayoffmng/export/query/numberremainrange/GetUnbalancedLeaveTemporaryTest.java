package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.MngDataStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.BreakDayOffRemainMngRefactParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SubstituteHolidayAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.UnbalanceVacation;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.VacationDetails;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.SelectedAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;

@RunWith(JMockit.class)
public class GetUnbalancedLeaveTemporaryTest {

	private static String CID = "000000000000-0117";

	private static String SID = "292ae91c-508c-4c6e-8fe8-3e72277dec16";

	@Injectable
	private GetUnbalancedLeaveTemporary.Require require;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	/*
	 * テストしたい内容 暫定データから消化データを作成する
	 * 
	 * 準備するデータ 暫定代休管理データがある 
	 * →紐づけがなくて残ってるやつ 
	 * →紐づけしても残ってるやつ 
	 * 暫定休出代休紐付け管理がある 
	 * →最初から残ってない
	 * →紐づけしたら残ってない モード : 月次か
	 * 
	 */
	@Test
	public void testModeMonth() {
		List<InterimDayOffMng> dayOffMng = Arrays.asList(
				new InterimDayOffMng("876caf30-5a4d-47b7-8147-d646f74be08a", new RequiredTime(0), new RequiredDay(1.0),
						new UnOffsetTime(0), new UnOffsetDay(1.0)),
				new InterimDayOffMng("876caf30-5a4d-47b7-8147-d646f74be08b", new RequiredTime(0), new RequiredDay(0.0),
						new UnOffsetTime(0), new UnOffsetDay(1.0)),
				new InterimDayOffMng("077a8929-3df0-4fd6-859e-29e615a921ea", new RequiredTime(480),
						new RequiredDay(1.0), new UnOffsetTime(480), new UnOffsetDay(1.0)));

		List<InterimBreakMng> breakMng = Arrays.asList(
				new InterimBreakMng("adda6a46-2cbe-48c8-85f8-c04ca554e132", new AttendanceTime(480),
						GeneralDate.ymd(2020, 6, 6), new OccurrenceTime(480), new OccurrenceDay(1.0),
						new AttendanceTime(240), new UnUsedTime(480), new UnUsedDay(1.0)),
				new InterimBreakMng("adda6a46-2cbe-48c8-85f8-c04ca554e333", new AttendanceTime(480),
						GeneralDate.ymd(2019, 6, 6), new OccurrenceTime(480), new OccurrenceDay(1.0),
						new AttendanceTime(240), new UnUsedTime(480), new UnUsedDay(1.0)));

		List<InterimRemain> interimMng = Arrays.asList(
				new InterimRemain("adda6a46-2cbe-48c8-85f8-c04ca554e132", SID, GeneralDate.ymd(2019, 11, 5),
						CreateAtr.SCHEDULE, RemainType.BREAK, RemainAtr.SINGLE),
				new InterimRemain("adda6a46-2cbe-48c8-85f8-c04ca554e333", SID, GeneralDate.ymd(2019, 11, 6),
						CreateAtr.RECORD, RemainType.BREAK, RemainAtr.SINGLE),

				new InterimRemain("876caf30-5a4d-47b7-8147-d646f74be08a", SID, GeneralDate.ymd(2019, 11, 9),
						CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
				new InterimRemain("876caf30-5a4d-47b7-8147-d646f74be08b", SID, GeneralDate.ymd(2019, 11, 12),
						CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
				new InterimRemain("077a8929-3df0-4fd6-859e-29e615a921ea", SID, GeneralDate.ymd(2019, 11, 10),
						CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE));

		Optional<SubstituteHolidayAggrResult> holidayAggrResult = Optional.of(new SubstituteHolidayAggrResult(
				new VacationDetails(Collections.emptyList()), new ReserveLeaveRemainingDayNumber(0d),
				new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0),
				new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d),
				new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0),
				Collections.emptyList(), Finally.of(GeneralDate.ymd(2019, 12, 01)), Collections.emptyList()));

		BreakDayOffRemainMngRefactParam inputParam = new BreakDayOffRemainMngRefactParam(CID, SID,
				new DatePeriod(GeneralDate.ymd(2019, 11, 01), GeneralDate.ymd(2020, 10, 31)), true,
				GeneralDate.ymd(2019, 11, 30), false, interimMng, Optional.empty(), Optional.empty(), breakMng,
				dayOffMng, holidayAggrResult, new FixedManagementDataMonth(new ArrayList<>(), new ArrayList<>()));

		new Expectations() {
			{

				require.getBreakDayOffMng("077a8929-3df0-4fd6-859e-29e615a921ea", anyBoolean, (DataManagementAtr) any);
				result = Arrays.asList(
						new InterimBreakDayOffMng("", DataManagementAtr.INTERIM, "077a8929-3df0-4fd6-859e-29e615a921ea",
								DataManagementAtr.INTERIM, new UseTime(0), new UseDay(0d), SelectedAtr.AUTOMATIC));

			}

		};

		List<AccumulationAbsenceDetail> actualResult = GetUnbalancedLeaveTemporary.process(require, inputParam);

		assertThat(actualResult).extracting(x -> x.getManageId(), x -> x.getEmployeeId(), x -> x.getDataAtr(),
				x -> x.getDateOccur().isUnknownDate(), x -> x.getDateOccur().getDayoffDate(),
				x -> x.getNumberOccurren().getDay().v(), x -> x.getNumberOccurren().getTime(),
				x -> x.getOccurrentClass(), x -> x.getUnbalanceNumber().getDay().v(),
				x -> x.getUnbalanceNumber().getTime(),
				x -> x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE ? ((UnbalanceVacation) x).getDeadline()
						: Optional.empty())
				.containsExactly(Tuple.tuple("876caf30-5a4d-47b7-8147-d646f74be08a", SID, MngDataStatus.RECORD, false,
						Optional.of(GeneralDate.ymd(2019, 11, 9)), 1.0, Optional.of(new AttendanceTime(0)),
						OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(0)), Optional.empty()),
						Tuple.tuple("876caf30-5a4d-47b7-8147-d646f74be08b", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 12)), 0.0, Optional.of(new AttendanceTime(0)),
								OccurrenceDigClass.DIGESTION, 0.0, Optional.of(new AttendanceTime(0)),
								Optional.empty()),
						Tuple.tuple("077a8929-3df0-4fd6-859e-29e615a921ea", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 10)), 1.0, Optional.of(new AttendanceTime(480)),
								OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(480)),
								Optional.empty()));

	}

	/*
	 * テストしたい内容 暫定データから消化データを作成する
	 * 
	 * 準備するデータ 暫定代休管理データがある 
	 * →紐づけがなくて残ってるやつ 
	 * →紐づけしても残ってるやつ 
	 * 暫定休出代休紐付け管理がある 
	 * →最初から残ってない
	 * →紐づけしたら残ってない 
	 * モード : その他か
	 * 
	 */
	@Test
	public void testModeOther() {
		Optional<SubstituteHolidayAggrResult> holidayAggrResult = Optional.of(new SubstituteHolidayAggrResult(
				new VacationDetails(Collections.emptyList()), new ReserveLeaveRemainingDayNumber(0d),
				new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0),
				new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d),
				new RemainingMinutes(0), new ReserveLeaveRemainingDayNumber(0d), new RemainingMinutes(0),
				Collections.emptyList(), Finally.of(GeneralDate.ymd(2019, 12, 01)), Collections.emptyList()));

		BreakDayOffRemainMngRefactParam inputParam = new BreakDayOffRemainMngRefactParam(CID, SID,
				new DatePeriod(GeneralDate.ymd(2019, 11, 01), GeneralDate.ymd(2020, 10, 31)), false,
				GeneralDate.ymd(2019, 11, 30), false, new ArrayList<>(), Optional.empty(), Optional.empty(),
				new ArrayList<>(), new ArrayList<>(), holidayAggrResult,
				new FixedManagementDataMonth(new ArrayList<>(), new ArrayList<>()));

		new Expectations() {
			{

				require.getRemainBySidPriod(anyString, (DatePeriod) any, (RemainType) any);
				result = Arrays.asList(
						new InterimRemain("adda6a46-2cbe-48c8-85f8-c04ca554e132", SID, GeneralDate.ymd(2019, 11, 4),
								CreateAtr.SCHEDULE, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
						new InterimRemain("62d542c3-4b79-4bf3-bd39-7e7f06711c34", SID, GeneralDate.ymd(2019, 11, 5),
								CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
						new InterimRemain("077a8929-3df0-4fd6-859e-29e615a921ee", SID, GeneralDate.ymd(2019, 11, 8),
								CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
						new InterimRemain("876caf30-5a4d-47b7-8147-d646f74be08a", SID, GeneralDate.ymd(2019, 11, 9),
								CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE),
						new InterimRemain("876caf30-5a4d-47b7-8147-d646f74be08b", SID, GeneralDate.ymd(2019, 11, 10),
								CreateAtr.RECORD, RemainType.SUBHOLIDAY, RemainAtr.SINGLE));

				require.getDayOffBySidPeriod(anyString, (DatePeriod) any);
				result = Arrays.asList(
						new InterimDayOffMng("adda6a46-2cbe-48c8-85f8-c04ca554e132", new RequiredTime(480),
								new RequiredDay(1.0), new UnOffsetTime(480), new UnOffsetDay(1.0)),
						new InterimDayOffMng("62d542c3-4b79-4bf3-bd39-7e7f06711c34", new RequiredTime(480),
								new RequiredDay(1.0), new UnOffsetTime(480), new UnOffsetDay(1.0)),
						new InterimDayOffMng("077a8929-3df0-4fd6-859e-29e615a921ee", new RequiredTime(480),
								new RequiredDay(1.0), new UnOffsetTime(480), new UnOffsetDay(1.0)),
						new InterimDayOffMng("876caf30-5a4d-47b7-8147-d646f74be08a", new RequiredTime(480),
								new RequiredDay(1.0), new UnOffsetTime(480), new UnOffsetDay(1.0)),
						new InterimDayOffMng("876caf30-5a4d-47b7-8147-d646f74be08b", new RequiredTime(0),
								new RequiredDay(0.0), new UnOffsetTime(480), new UnOffsetDay(1.0)));

				require.getBreakDayOffMng("62d542c3-4b79-4bf3-bd39-7e7f06711c34", false, DataManagementAtr.INTERIM);
				result = Arrays.asList(
						new InterimBreakDayOffMng("", DataManagementAtr.INTERIM, "62d542c3-4b79-4bf3-bd39-7e7f06711c34",
								DataManagementAtr.INTERIM, new UseTime(0), new UseDay(0d), SelectedAtr.AUTOMATIC));

			}

		};

		List<AccumulationAbsenceDetail> actualResult = GetUnbalancedLeaveTemporary.process(require, inputParam);

		assertThat(actualResult).extracting(x -> x.getManageId(), x -> x.getEmployeeId(), x -> x.getDataAtr(),
				x -> x.getDateOccur().isUnknownDate(), x -> x.getDateOccur().getDayoffDate(),
				x -> x.getNumberOccurren().getDay().v(), x -> x.getNumberOccurren().getTime(),
				x -> x.getOccurrentClass(), x -> x.getUnbalanceNumber().getDay().v(),
				x -> x.getUnbalanceNumber().getTime(),
				x -> x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE ? ((UnbalanceVacation) x).getDeadline()
						: Optional.empty())
				.containsExactly(Tuple.tuple("adda6a46-2cbe-48c8-85f8-c04ca554e132", SID, MngDataStatus.SCHEDULE, false,
						Optional.of(GeneralDate.ymd(2019, 11, 4)), 1.0, Optional.of(new AttendanceTime(480)),
						OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(480)), Optional.empty()),

						Tuple.tuple("62d542c3-4b79-4bf3-bd39-7e7f06711c34", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 05)), 1.0, Optional.of(new AttendanceTime(480)),
								OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(480)),
								Optional.empty()),

						Tuple.tuple("077a8929-3df0-4fd6-859e-29e615a921ee", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 8)), 1.0, Optional.of(new AttendanceTime(480)),
								OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(480)),
								Optional.empty()),

						Tuple.tuple("876caf30-5a4d-47b7-8147-d646f74be08a", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 9)), 1.0, Optional.of(new AttendanceTime(480)),
								OccurrenceDigClass.DIGESTION, 1.0, Optional.of(new AttendanceTime(480)),
								Optional.empty()),

						Tuple.tuple("876caf30-5a4d-47b7-8147-d646f74be08b", SID, MngDataStatus.RECORD, false,
								Optional.of(GeneralDate.ymd(2019, 11, 10)), 0.0, Optional.of(new AttendanceTime(0)),
								OccurrenceDigClass.DIGESTION, 0.0, Optional.of(new AttendanceTime(0)),
								Optional.empty()

						));
	}

}
