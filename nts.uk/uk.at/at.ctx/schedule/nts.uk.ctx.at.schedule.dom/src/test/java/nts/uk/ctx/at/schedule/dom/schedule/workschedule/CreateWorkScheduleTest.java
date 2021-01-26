package nts.uk.ctx.at.schedule.dom.schedule.workschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText.Builder;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.CreateWorkSchedule.WorkTimeZone;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule.Require;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.affiliationinfor.AffiliationInforOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.temporarytime.WorkNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.worktime.ChangeableWorkingTimeZonePerNo.ClockAreaAtr;
import nts.uk.ctx.at.shared.dom.worktime.ChangeableWorkingTimeZonePerNo.ContainsResult;
import nts.uk.shr.com.time.TimeWithDayAttr;

public class CreateWorkScheduleTest {
	
	@Injectable
	CreateWorkSchedule.Require require;
	
	@Test
	public void testGetErrorInfo_empty_case1(
			@Injectable WorkInformation workInformation
			) {
		
		Optional<ErrorInfoOfWorkSchedule> result = NtsAssert.Invoke.staticMethod(CreateWorkSchedule.class, "getErrorInfoWithWorkTimeZone", 
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				WorkTimeZone.START_TIME_1,
				new HashMap<>() // empty map
				);
		
		assertThat( result ).isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testGetErrorInfo_empty_case2(
			@Injectable WorkInformation workInformation
			) {

		new Expectations() {{
			workInformation.containsOnChangeableWorkingTime(require, ( ClockAreaAtr) any, (WorkNo) any, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		updateInfoMap.put(31, (T) new TimeWithDayAttr(31));
		
		Optional<ErrorInfoOfWorkSchedule> result = NtsAssert.Invoke.staticMethod(CreateWorkSchedule.class, "getErrorInfoWithWorkTimeZone", 
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				WorkTimeZone.START_TIME_1,
				updateInfoMap
				);
		
		assertThat( result ).isEmpty();
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testGetErrorInfo_successfully(
			@Injectable WorkInformation workInformation,
			@Mocked Builder builder
			) {
		new Expectations() {{
			
			workInformation.containsOnChangeableWorkingTime(require, ( ClockAreaAtr) any, (WorkNo) any, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			builder.build().buildMessage();
			result = "msg";
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		updateInfoMap.put( WS_AttendanceItem.StartTime1.ID, (T) new TimeWithDayAttr(31));
		Optional<ErrorInfoOfWorkSchedule> result = NtsAssert.Invoke.staticMethod(CreateWorkSchedule.class, "getErrorInfoWithWorkTimeZone", 
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				WorkTimeZone.START_TIME_1,
				updateInfoMap
				);
		
		assertThat( result.get().getEmployeeId() ).isEqualTo( "empId" );
		assertThat( result.get().getDate() ).isEqualTo( GeneralDate.ymd( 2020, 11, 1) );
		assertThat( result.get().getAttendanceItemId().get() ).isEqualTo( WS_AttendanceItem.StartTime1.ID );
		assertThat( result.get().getErrorMessage() ).isEqualTo( "msg");
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCheckTimeSpan(@Injectable WorkInformation workInformation, @Mocked Builder builder) {
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put( WS_AttendanceItem.EndTime1.ID, (T) new TimeWithDayAttr(34));
		updateInfoMap.put( WS_AttendanceItem.StartTime2.ID, (T) new TimeWithDayAttr(41));
		updateInfoMap.put( WS_AttendanceItem.EndTime2.ID, (T) new TimeWithDayAttr(44));
		
		new Expectations() {{
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_1.clockArea, WorkTimeZone.START_TIME_1.workNo, (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_1.clockArea, WorkTimeZone.END_TIME_1.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目41
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_2.clockArea, WorkTimeZone.START_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目44
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_2.clockArea, WorkTimeZone.END_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			builder.build().buildMessage();
			result = "msg";
		}};
		
		List<ErrorInfoOfWorkSchedule> result = NtsAssert.Invoke.staticMethod(CreateWorkSchedule.class, "checkTimeSpan", 
				require,
				"empId",
				GeneralDate.ymd(2020, 11, 1),
				workInformation,
				updateInfoMap);
		
		assertThat( result )
			.extracting( 
				e -> e.getEmployeeId(),
				e -> e.getDate(),
				e -> e.getAttendanceItemId().get(),
				e -> e.getErrorMessage())
			.containsExactly( 
					tuple("empId", GeneralDate.ymd(2020, 11, 1), WS_AttendanceItem.StartTime2.ID, "msg"),
					tuple("empId", GeneralDate.ymd(2020, 11, 1), WS_AttendanceItem.EndTime2.ID, "msg"));
	}
	
	@Test
	public <T> void testCreate_Exception_430(
			@Injectable WorkInformation workInformation,
			@Mocked BusinessException businessException,
			@Mocked Builder builder) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			//result = empty
			
			businessException.getMessageId();
			result = "Msg_430";
			
			builder.build().buildMessage();
			result = "msg";
		}};

		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				new HashMap<>());
		
		assertThat( result.getAtomTask() ).isEmpty();
		assertThat( result.getErrorInformation())
			.extracting( 
					e -> e.getEmployeeId(),
					e -> e.getDate(),
					e -> e.getAttendanceItemId(),
					e -> e.getErrorMessage())
			.containsExactly( 
				tuple(
					"empId", 
					GeneralDate.ymd(2020, 11, 1), 
					Optional.empty(), 
					"msg"));
		
	}
	
	@Test
	public <T> void testCreate_Exception_different430(@Injectable WorkInformation workInformation) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			//result = empty
		}};
		
		new MockUp<WorkSchedule>() {
			@Mock
			public WorkSchedule createByHandCorrectionWithWorkInformation(Require require,
					String employeeId,
					GeneralDate date,
					WorkInformation workInformation) {
				throw new BusinessException("Msg_x");
			}
		};
		
		NtsAssert.businessException("Msg_x", () -> CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				new HashMap<>()));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCreate_isNewRegister_hasError(
			@Injectable WorkInformation workInformation,
			@Injectable WorkInfoOfDailyAttendance workInfo,
			@Injectable AffiliationInforOfDailyAttd affInfo,
			@Mocked Builder builder) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			//result = empty
		}};
		
		WorkSchedule workSchedule = new WorkSchedule(
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				ConfirmedATR.UNSETTLED, 
				workInfo, 
				affInfo, 
				new BreakTimeOfDailyAttd(), 
				new ArrayList<>(), 
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty());
		
		new MockUp<WorkSchedule>() {
			@Mock
			public WorkSchedule createByHandCorrectionWithWorkInformation(Require require,
					String employeeId,
					GeneralDate date,
					WorkInformation workInformation) {
				
				return workSchedule;
			}
		};
		
		new Expectations() {{
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_1.clockArea, WorkTimeZone.START_TIME_1.workNo, (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_1.clockArea, WorkTimeZone.END_TIME_1.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目41
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_2.clockArea, WorkTimeZone.START_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目44
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_2.clockArea, WorkTimeZone.END_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			builder.build().buildMessage();
			result = "msg";
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put( WS_AttendanceItem.EndTime1.ID, (T) new TimeWithDayAttr(34));
		updateInfoMap.put( WS_AttendanceItem.StartTime2.ID, (T) new TimeWithDayAttr(41));
		updateInfoMap.put( WS_AttendanceItem.EndTime2.ID, (T) new TimeWithDayAttr(44));
		
		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				updateInfoMap);
		
		assertThat( result.getAtomTask() ).isEmpty();
		assertThat( result.getErrorInformation())
			.extracting( 
					e -> e.getEmployeeId(),
					e -> e.getDate(),
					e -> e.getAttendanceItemId(),
					e -> e.getErrorMessage())
			.containsExactly( 
				tuple(
					"empId", 
					GeneralDate.ymd(2020, 11, 1), 
					Optional.of(WS_AttendanceItem.StartTime2.ID), 
					"msg"),
				tuple(
					"empId",
					GeneralDate.ymd(2020, 11, 1),
					Optional.of(WS_AttendanceItem.EndTime2.ID),
					"msg"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCreate_isNotNewRegister_workInfoNotSame_hasError(
			@Injectable WorkInformation workInformation,
			@Mocked WorkSchedule workSchedule,
			@Mocked Builder builder) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			result = Optional.of(workSchedule);
			
			workSchedule.getWorkInfo().getRecordInfo().isSame( (WorkInformation) any );
			result = false;
		}};
		
		new Expectations() {{
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_1.clockArea, WorkTimeZone.START_TIME_1.workNo, (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_1.clockArea, WorkTimeZone.END_TIME_1.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目41
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_2.clockArea, WorkTimeZone.START_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目44
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_2.clockArea, WorkTimeZone.END_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			builder.build().buildMessage();
			result = "msg";
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put( WS_AttendanceItem.EndTime1.ID, (T) new TimeWithDayAttr(34));
		updateInfoMap.put( WS_AttendanceItem.StartTime2.ID, (T) new TimeWithDayAttr(41));
		updateInfoMap.put( WS_AttendanceItem.EndTime2.ID, (T) new TimeWithDayAttr(44));
		
		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				updateInfoMap);
		
		assertThat( result.getAtomTask() ).isEmpty();
		assertThat( result.getErrorInformation())
			.extracting( 
					e -> e.getEmployeeId(),
					e -> e.getDate(),
					e -> e.getAttendanceItemId(),
					e -> e.getErrorMessage())
			.containsExactly( 
				tuple(
					"empId", 
					GeneralDate.ymd(2020, 11, 1), 
					Optional.of(WS_AttendanceItem.StartTime2.ID), 
					"msg"),
				tuple(
					"empId",
					GeneralDate.ymd(2020, 11, 1),
					Optional.of(WS_AttendanceItem.EndTime2.ID),
					"msg"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCreate_isNotNewRegister_workInfoSame_hasError(
			@Injectable WorkInformation workInformation,
			@Injectable WorkSchedule workSchedule,
			@Mocked Builder builder) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			result = Optional.of(workSchedule);
			
			workSchedule.getWorkInfo().getRecordInfo().isSame( (WorkInformation) any );
			result = true;
		}};
		
		new Expectations() {{
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_1.clockArea, WorkTimeZone.START_TIME_1.workNo, (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_1.clockArea, WorkTimeZone.END_TIME_1.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目41
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_2.clockArea, WorkTimeZone.START_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			// 勤怠項目44
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_2.clockArea, WorkTimeZone.END_TIME_2.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( false, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			builder.build().buildMessage();
			result = "msg";
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put( WS_AttendanceItem.EndTime1.ID, (T) new TimeWithDayAttr(34));
		updateInfoMap.put( WS_AttendanceItem.StartTime2.ID, (T) new TimeWithDayAttr(41));
		updateInfoMap.put( WS_AttendanceItem.EndTime2.ID, (T) new TimeWithDayAttr(44));
		
		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				updateInfoMap);
		
		assertThat( result.getAtomTask() ).isEmpty();
		assertThat( result.getErrorInformation())
			.extracting( 
					e -> e.getEmployeeId(),
					e -> e.getDate(),
					e -> e.getAttendanceItemId(),
					e -> e.getErrorMessage())
			.containsExactly( 
				tuple(
					"empId", 
					GeneralDate.ymd(2020, 11, 1), 
					Optional.of(WS_AttendanceItem.StartTime2.ID), 
					"msg"),
				tuple(
					"empId",
					GeneralDate.ymd(2020, 11, 1),
					Optional.of(WS_AttendanceItem.EndTime2.ID),
					"msg"
						));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCreate_insert_successfull(
			@Injectable WorkInformation workInformation,
			@Mocked Builder builder,
			@Mocked WorkSchedule workSchedule) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			// result = empty
			
		}};
		
		new Expectations() {{
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.START_TIME_1.clockArea, WorkTimeZone.START_TIME_1.workNo, (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(
					require, WorkTimeZone.END_TIME_1.clockArea, WorkTimeZone.END_TIME_1.workNo, (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			workSchedule.changeAttendanceItemValueByHandCorrection(require, (Map<Integer, T>) any);
			}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put( WS_AttendanceItem.EndTime1.ID, (T) new TimeWithDayAttr(34));
		
		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				updateInfoMap);
		
		assertThat( result.getErrorInformation() ).isEmpty();
		NtsAssert.atomTask( () -> result.getAtomTask().get() , 
				any -> require.insertWorkSchedule(  any.get() ),
				any -> require.registerTemporaryData("emp", GeneralDate.ymd(2020, 11, 1))
				);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testCreate_update_successfull(
			@Injectable WorkInformation workInformation,
			@Mocked Builder builder,
			@Mocked WorkSchedule workSchedule
			) {
		
		new Expectations() {{
			require.getWorkSchedule(anyString, (GeneralDate) any);
			result = Optional.of(workSchedule);
			
			// 勤怠項目31
			workInformation.containsOnChangeableWorkingTime(require, ClockAreaAtr.START, new WorkNo(1), (TimeWithDayAttr) any);
			times = 0; 
			
			// 勤怠項目34
			workInformation.containsOnChangeableWorkingTime(require, ClockAreaAtr.END, new WorkNo(1), (TimeWithDayAttr) any);
			result = new ContainsResult( true, 
					Optional.of(new TimeSpanForCalc(
							new TimeWithDayAttr(1), 
							new TimeWithDayAttr(2))
							));
			
			workSchedule.changeAttendanceItemValueByHandCorrection(require, (Map<Integer, T>) any);
			
			require.correctWorkSchedule(workSchedule);
			result = workSchedule;
			
		}};
		
		Map<Integer, T> updateInfoMap = new HashMap<>();
		// updateInfoMap.put(31, value)
		updateInfoMap.put(34, (T) new TimeWithDayAttr(34));
		
		ResultOfRegisteringWorkSchedule result = CreateWorkSchedule.create(
				require, 
				"empId", 
				GeneralDate.ymd(2020, 11, 1), 
				workInformation, 
				new ArrayList<>(),
				updateInfoMap);
		
		assertThat( result.getErrorInformation() ).isEmpty();
		NtsAssert.atomTask( () -> result.getAtomTask().get() , 
				any -> require.updateWorkSchedule( any.get() ),
				any -> require.registerTemporaryData("emp", GeneralDate.ymd(2020, 11, 1))
				);
		
	}

}
