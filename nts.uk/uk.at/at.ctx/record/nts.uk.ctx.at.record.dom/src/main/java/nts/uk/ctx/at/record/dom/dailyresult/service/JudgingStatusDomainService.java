package nts.uk.ctx.at.record.dom.dailyresult.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.勤務実績.日別実績.実績データから在席状態を判断する.実績データから在席状態を判断する
 */
public class JudgingStatusDomainService {
	
	private JudgingStatusDomainService() {}
	
	//$出勤時刻
	private static Optional<Integer> attendanceTime = Optional.empty();
	
	//$退勤時刻
	private static Optional<Integer> leaveTime = Optional.empty();
	
	//$勤務区分
	private static Optional<String> workDivision = Optional.empty();
	
	//$直行区分
	private static Optional<Boolean> directDivision = Optional.empty();
	
	//出勤
	private static final String WORK = "出勤";
	
	//休み
	private static final String NOT_WORK = "休み";
	
	public static AttendanceAccordActualData JudgingStatus(Require rq, String sid) {

		GeneralDate baseDate = GeneralDate.today();

		// 日別実績の勤務情報を取得する
		Optional<WorkInfoOfDailyPerformance> dailyActualWorkInfo = rq.getDailyActualWorkInfo(sid, baseDate);

		// 日別実績の出退勤を取得する
		Optional<TimeLeavingOfDailyPerformance> dailyAttendanceAndDeparture = rq.getDailyAttendanceAndDeparture(sid,
				baseDate);
		Optional<String> workTypeCode = Optional.empty();

		// 日別勤務予定を取得する
		if (dailyActualWorkInfo.isPresent()) {
			workTypeCode = Optional.ofNullable(dailyActualWorkInfo.get().getWorkInformation().getRecordInfo().getWorkTypeCode().v());
		} else {
			workTypeCode = rq.getDailyWorkScheduleWorkTypeCode(sid, baseDate);
		}

		// 勤務種類を取得する
		Optional<Boolean> workingNow = Optional.empty();
		if (workTypeCode.isPresent()) {
			Optional<WorkType> workType = rq.getWorkType(workTypeCode.get());
			if (workType.isPresent()) {
				workingNow = Optional.ofNullable(judgePresenceStatus(workType.get().getDailyWork()));
			}
		}

		// 作成する
		if (dailyAttendanceAndDeparture.isPresent()) {
			Optional<TimeLeavingWork> time = dailyAttendanceAndDeparture.get().getAttendance().getTimeLeavingWorks()
					.stream().filter(fil -> fil.getWorkNo().v() == 1).findFirst();
			
			//$出勤時刻
			time.ifPresent(item -> item.getAttendanceStamp()
					.ifPresent(i -> i.getStamp()
							.ifPresent(it -> it.getTimeDay().getTimeWithDay()
									.ifPresent(m -> attendanceTime = Optional.ofNullable(m.v())))));
			//$退勤時刻
			time.ifPresent(item -> item.getLeaveStamp()
					.ifPresent(i -> i.getStamp()
							.ifPresent(it -> it.getTimeDay().getTimeWithDay()
									.ifPresent(m -> leaveTime = Optional.ofNullable(m.v())))));
			
			//$勤務区分
			workingNow.ifPresent(bool -> {
				workDivision = bool ? Optional.ofNullable(WORK) : Optional.ofNullable(NOT_WORK);
			});
			
			//$直行区分
			dailyActualWorkInfo.ifPresent(consumer -> {
				directDivision = Optional.ofNullable(consumer.getWorkInformation().getGoStraightAtr().value == 1);
			});
		}
		Integer now = GeneralDateTime.now().hours() * 100 + GeneralDateTime.now().minutes();
		if (leaveTime.isPresent() && leaveTime.get() <= now) {
			// case 1
			return AttendanceAccordActualData.builder()
					.attendanceState(StatusClassfication.GO_HOME)
					.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
					.build();
		} else if (leaveTime.isPresent() && leaveTime.get() > now) {
			if (attendanceTime.isPresent() && attendanceTime.get() >= now) {
				if (directDivision.isPresent() && directDivision.get().booleanValue()) {
					// case 2
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.GO_OUT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				} else {
					// case 3
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.PRESENT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				}
			} else if (attendanceTime.isPresent() && attendanceTime.get() < now) {
				// case 4
				return AttendanceAccordActualData.builder()
						.attendanceState(StatusClassfication.NOT_PRESENT)
						.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
						.build();
			} else {
				// case 5
				return AttendanceAccordActualData.builder()
						.attendanceState(StatusClassfication.NOT_PRESENT)
						.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
						.build();
			}
		} else {
			if (attendanceTime.isPresent() && attendanceTime.get() >= now) {
				if (directDivision.isPresent() && directDivision.get().booleanValue()) {
					// case 6
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.GO_OUT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				} else {
					// case 7
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.PRESENT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				}
			} else if (attendanceTime.isPresent() && attendanceTime.get() < now) {
				// case 8
				return AttendanceAccordActualData.builder()
						.attendanceState(StatusClassfication.NOT_PRESENT)
						.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
						.build();
			} else {
				if (!workDivision.isPresent()) {
					// case 9
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.NOT_PRESENT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				} else if (workDivision.get() == WORK) {
					// case 10
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.NOT_PRESENT)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				} else {
					// case 11
					return AttendanceAccordActualData.builder()
							.attendanceState(StatusClassfication.HOLIDAY)
							.workingNow(workingNow.map(m -> m.booleanValue()).orElse(false))
							.build();
				}
			}
		}
	}

	private static boolean judgePresenceStatus(DailyWork daily) {
		// 勤務区分の判断方法
		List<WorkTypeClassification> notIn = new ArrayList<>();
		notIn.add(WorkTypeClassification.Attendance);
		notIn.add(WorkTypeClassification.HolidayWork);
		notIn.add(WorkTypeClassification.Shooting);
		notIn.add(WorkTypeClassification.ContinuousWork);
		
		// 1日場合、「出勤、休日出勤、振出、連続勤務」 → 出勤 || その他 → 休み
		if (daily.getWorkTypeUnit() == WorkTypeUnit.OneDay && !notIn.stream().anyMatch(item -> item == daily.getOneDay())) {
			return false;
		}
		// 午前と午後の場合、午前が休み AND 午後が休み → 休み || その他 → 出勤
		if (daily.getWorkTypeUnit() == WorkTypeUnit.MonringAndAfternoon && !notIn.stream().anyMatch(item -> item == daily.getMorning())
				&& !notIn.stream().anyMatch(item -> item == daily.getAfternoon())) {
			return false;
		}
		return true;
	}
	
	public static void clearStaticVariable() {
		// $出勤時刻
		attendanceTime = Optional.empty();

		// $退勤時刻
		leaveTime = Optional.empty();

		// $勤務区分
		workDivision = Optional.empty();

		// $直行区分
		directDivision = Optional.empty();
	}

	public interface Require {

		/**
		 * [R-1]日別実績の勤務情報を取得する
		 * 
		 * 日別実績の勤務情報Repository.取得する(社員ID,年月日)
		 * 
		 * @param sid      社員ID
		 * @param baseDate 年月日
		 */
		public Optional<WorkInfoOfDailyPerformance> getDailyActualWorkInfo(String sid, GeneralDate baseDate);

		/**
		 * [R-2] 日別実績の出退勤を取得する
		 * 
		 * 日別実績の出退勤Repository.取得する(社員ID,年月日)
		 * 
		 * @param sid      社員ID
		 * @param baseDate 年月日
		 */
		public Optional<TimeLeavingOfDailyPerformance> getDailyAttendanceAndDeparture(String sid, GeneralDate baseDate);

		/**
		 * [R-3] 日別勤務予定を取得する
		 * 
		 * 日別勤務予定を取得するAdapter.取得する(社員ID,年月日)
		 * 
		 * @param sid      社員ID
		 * @param baseDate 年月日
		 */
		public Optional<String> getDailyWorkScheduleWorkTypeCode(String sid, GeneralDate baseDate);

		/**
		 * [R-4] 勤務種類を取得する
		 * 
		 * 勤務種類Repository.取得する(ログイン会社ID、コード)
		 * 
		 * @param loginCid ログイン会社ID
		 * @param code     コード
		 */
		public Optional<WorkType> getWorkType(String code);
	}
}
