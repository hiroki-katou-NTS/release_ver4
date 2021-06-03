package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.BreakTimeZoneSettingOutPut;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.caltimediff.CalculateTimeDiffService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeDeductTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexHalfDayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class ReflectBreakTimeOfDailyDomainServiceImpl implements ReflectBreakTimeOfDailyDomainService {

	@Inject
	private BasicScheduleService basicScheduleService;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepo;
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRep;
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepo;
	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepo;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingRepo;
	@Inject
	private RangeOfDayTimeZoneService rangeOfDayTimeZoneService;
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepo;
	@Inject
	private ErrMessageInfoRepository errRepo;
	@Inject
	private CalculateTimeDiffService calculateTimeDiffService;
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepo;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepo;
	
	@Inject
	private BreakTimeOfDailyPerformanceCorrectService correctService;
	@Inject
	private EditStateOfDailyPerformanceRepository editStateRepo;

	@Override
	public BreakTimeOfDailyPerformance correctBreakTimeByTimeLeaveAndWorkInfo(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, WorkInfoOfDailyPerformance WorkInfo) {
		
		Optional<BreakTimeOfDailyPerformance> breakOpt = this.breakTimeOfDailyPerformanceRepo.find(employeeID, processingDate, 0);
		List<EditStateOfDailyPerformance> editStates = this.editStateRepo.findByKey(employeeID, processingDate);
		
		return correctService.correct(companyId, WorkInfo, editStates, Optional.ofNullable(timeLeavingOfDailyPerformance), 
				breakOpt, Optional.empty()).orElse(null);
	}

	@Override
	public BreakTimeOfDailyPerformance reflectBreakTime(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance,
			WorkInfoOfDailyPerformance WorkInfo) {
		BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut = new BreakTimeZoneSettingOutPut();
		// 休憩時間帯設定を確認する
		List<TimeLeavingWork> timeLeavingWorks = null;
		if (timeLeavingOfDailyPerformance != null && !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
			timeLeavingWorks = timeLeavingOfDailyPerformance.getTimeLeavingWorks();
		}

		if (timeLeavingWorks == null) {
			return null;
		}
		boolean checkBreakTimeSetting = this.checkBreakTimeSetting(companyId, employeeID, processingDate,
				empCalAndSumExecLogID, WorkInfo, breakTimeZoneSettingOutPut);
		if (!checkBreakTimeSetting) {
			return null;
		}
		List<DeductionTime> lstTimezone = breakTimeZoneSettingOutPut.getLstTimezone();
		Collections.sort(lstTimezone, new Comparator<DeductionTime>() {
			public int compare(DeductionTime o1, DeductionTime o2) {
				int t1 = o1.getStart().v();
				int t2 = o2.getStart().v();
				if (t1 == t2)
					return 0;
				return t1 < t2 ? -1 : 1;
			}
		});

		List<BreakTimeSheet> lstBreakTime = new ArrayList<BreakTimeSheet>();
		int size = lstTimezone.size();
		for (int i = 0; i < size; i++) {
			DeductionTime timeZone = lstTimezone.get(i);
			// 時間帯．休憩枠NO
			int frameNo = i + 1;
			// 出退勤と重複する休憩時間帯のみ追加する
			boolean checkAddBreakTime = checkAddBreakTime(timeLeavingWorks, timeZone, frameNo);
			if (checkAddBreakTime) {
				lstBreakTime.add(new BreakTimeSheet(new BreakFrameNo(frameNo), timeZone.getStart(), timeZone.getEnd(),
						new AttendanceTime(0)));
			}
		}
		// 休憩種類 ← 「就業時間帯から参照」
		return new BreakTimeOfDailyPerformance(employeeID, BreakType.REFER_WORK_TIME, lstBreakTime, processingDate);
	}

	// 出退勤と重複する休憩時間帯のみ追加する
	public boolean checkAddBreakTime(List<TimeLeavingWork> timeLeavingWorks, DeductionTime timeZone, int frameNo) {
		boolean isAddBreaktime = false;
		Collections.sort(timeLeavingWorks, new Comparator<TimeLeavingWork>() {
			public int compare(TimeLeavingWork o1, TimeLeavingWork o2) {
				if (o2 == null || o2.getAttendanceStamp() == null || !o2.getAttendanceStamp().isPresent()
						|| o2.getAttendanceStamp().get().getStamp() == null
						|| !o2.getAttendanceStamp().get().getStamp().isPresent()
						|| o2.getAttendanceStamp().get().getStamp().get().getTimeWithDay() == null) {
					return 1;
				}
				if (o1 == null || o1.getAttendanceStamp() == null || !o1.getAttendanceStamp().isPresent()
						|| o1.getAttendanceStamp().get().getStamp() == null
						|| !o1.getAttendanceStamp().get().getStamp().isPresent()
						|| o1.getAttendanceStamp().get().getStamp().get().getTimeWithDay() == null) {
					return -1;
				}
				// 高須の応急処置
				int t1 = o1.getAttendanceStamp().get().getStamp().get().getTimeWithDay() != null
						? o1.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue() : 0;
				int t2 = o2.getAttendanceStamp().get().getStamp().get().getTimeWithDay() != null
						? o2.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue() : 0;
				// int t1 =
				// o1.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue();
				// int t2 =
				// o2.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue();
				if (t1 == t2)
					return 0;
				return t1 < t2 ? -1 : 1;
			}
		});
		int size = timeLeavingWorks.size();
		for (int i = 0; i < size; i++) {
			TimeLeavingWork timeLeavingWork = timeLeavingWorks.get(i);
			// 重複の判断処理
			TimeWithDayAttr startDate1 = timeZone.getStart();
			TimeWithDayAttr endDate1 = timeZone.getEnd();
			// 高須の応急処置
			TimeWithDayAttr startDate2 = null;
			TimeWithDayAttr endDate2 = null;

			if (timeLeavingWork.getAttendanceStamp() != null && timeLeavingWork.getAttendanceStamp().isPresent()
					&& timeLeavingWork.getAttendanceStamp().get().getStamp() != null
					&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
					&& timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay() != null) {
				startDate2 = timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay();
			}

			if (timeLeavingWork.getLeaveStamp() != null && timeLeavingWork.getLeaveStamp().isPresent()
					&& timeLeavingWork.getLeaveStamp().get().getStamp() != null
					&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()
					&& timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay() != null) {
				endDate2 = timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay();
			}

			// TimeWithDayAttr startDate2 =
			// timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay();
			// TimeWithDayAttr endDate2 =
			// timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay();
			if (startDate2 != null && endDate2 != null) {
				TimeSpanForCalc timeSpanFirstTime = new TimeSpanForCalc(endDate1, startDate1);
				TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(endDate2, startDate2);
				DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
						.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
				DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
						.checkStateAtr(duplicateStateAtr);
				// 非重複
				if (duplicationStatusOfTimeZone != DuplicationStatusOfTimeZone.NON_OVERLAPPING) {
					isAddBreaktime = true;
					break;
				}
			}

		}
		return isAddBreaktime;
	}

	// 休憩時間帯設定を確認する
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut) {
		// fixed thieu reset breaktime
		boolean checkReflect = false;
		Optional<WorkType> workTypeOpt = workTypeRepo.findByPK(companyId,
				WorkInfo.getRecordInfo().getWorkTypeCode().v());
		// 1日半日出勤・1日休日系の判定
		WorkStyle checkWorkDay = this.basicScheduleService.checkWorkDay(workTypeOpt);
		// 1日休日系
		if (checkWorkDay.value == 0) {
			return false;
		} else {
			// 休出かどうかの判断
			int weekdayHolidayClassification = this.checkHolidayOrNot(workTypeOpt);
			if (WorkInfo.getRecordInfo().getWorkTimeCode() != null) {	
				String workTimeCode = WorkInfo.getRecordInfo().getWorkTimeCode().v();
				Optional<WorkTimeSetting> WorkTimeSettingOptional = this.workTimeSettingRepo.findByCode(companyId,
						workTimeCode);
				WorkTimeSetting workTimeSetting = WorkTimeSettingOptional.get();
				// WorkTimeDailyAtr = 通常勤務・変形労働用
				if (workTimeSetting.getWorkTimeDivision().getWorkTimeDailyAtr().value == 0) {

					switch (workTimeSetting.getWorkTimeDivision().getWorkTimeMethodSet().value) {
					case 2:// 流動勤務
						checkReflect = this.confirmIntermissionTimeZone(companyId, weekdayHolidayClassification,
								workTimeCode, breakTimeZoneSettingOutPut);

						break;
					case 1:// 時差勤務
						checkReflect = ConfirmInterTimezoneStaggeredWorkSetting(companyId, employeeID, processingDate,
								empCalAndSumExecLogID, weekdayHolidayClassification, WorkInfo,
								breakTimeZoneSettingOutPut, checkWorkDay);
						break;

					default:// 固定勤務
						checkReflect = this.CheckBreakTimeFromFixedWorkSetting(companyId, weekdayHolidayClassification,
								workTimeCode, breakTimeZoneSettingOutPut, checkWorkDay);
						break;
					}

				} else {
					checkReflect = this.confirmInterFlexWorkSetting(companyId, weekdayHolidayClassification,
							workTimeCode, breakTimeZoneSettingOutPut, checkWorkDay);
				}
			}
		}

		return checkReflect;
	}

	// 休憩時間帯設定を確認する
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, List<ErrorAlarmWorkRecord> errorMaster) {
		return checkBreakTimeSetting(companyId, employeeID, processingDate, empCalAndSumExecLogID, WorkInfo,
				breakTimeZoneSettingOutPut, errorMaster, Optional.empty());
	}

	// 休憩時間帯設定を確認する
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, List<ErrorAlarmWorkRecord> errorMaster,
			Optional<WorkTimeSetting> workTime) {
		// fixed thieu reset breaktime
		Optional<WorkType> workTypeOpt = workTypeRepo.findByPK(companyId,
				WorkInfo.getRecordInfo().getWorkTypeCode().v());
		// 1日半日出勤・1日休日系の判定
		WorkStyle checkWorkDay = this.basicScheduleService.checkWorkDay(workTypeOpt);
		// 1日休日系
		if (checkWorkDay == WorkStyle.ONE_DAY_REST) {
			return false;
		}

		// 休出かどうかの判断
		int weekdayHolidayClassification = this.checkHolidayOrNot(workTypeOpt);

		if (WorkInfo.getRecordInfo().getWorkTimeCode() != null) {
			String workTimeCode = WorkInfo.getRecordInfo().getWorkTimeCode().v();
			WorkTimeSetting wts = workTime
					.orElseGet(() -> this.workTimeSettingRepo.findByCode(companyId, workTimeCode).get());
			// WorkTimeDailyAtr = 通常勤務・変形労働用
			if (wts.getWorkTimeDivision().getWorkTimeDailyAtr().value == 0) {

				switch (wts.getWorkTimeDivision().getWorkTimeMethodSet().value) {
				case 2:// 流動勤務
					return this.confirmIntermissionTimeZone(companyId, weekdayHolidayClassification, workTimeCode,
							breakTimeZoneSettingOutPut);

				case 1:// 時差勤務
					return ConfirmInterTimezoneStaggeredWorkSetting(companyId, employeeID, processingDate,
							empCalAndSumExecLogID, weekdayHolidayClassification, WorkInfo, breakTimeZoneSettingOutPut,
							checkWorkDay, workTypeOpt, errorMaster);

				default:// 固定勤務
					return this.CheckBreakTimeFromFixedWorkSetting(companyId, weekdayHolidayClassification,
							workTimeCode, breakTimeZoneSettingOutPut, checkWorkDay);
				}

			} else {
				return this.confirmInterFlexWorkSetting(companyId, weekdayHolidayClassification, workTimeCode,
						breakTimeZoneSettingOutPut, checkWorkDay);
			}
		}

		return false;
	}

	@Override
	// フレックス勤務設定から休憩時間帯を確認する
	public boolean confirmInterFlexWorkSetting(String companyId, int weekdayHolidayClassification, String workTimeCode,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, WorkStyle checkWorkDay) {

		Optional<FlexWorkSetting> fws = this.flexWorkSettingRepo.find(companyId, workTimeCode);
		if (!fws.isPresent()) {
			return false;
		}
		FlexWorkSetting flexWorkSetting = fws.get();
		List<DeductionTime> lstTimezone = null;
		boolean fixRestTime = true;
		if (weekdayHolidayClassification == 0) {
			List<FlexHalfDayWorkTime> lstHalfDayWorkTimezone = flexWorkSetting.getLstHalfDayWorkTimezone();

			switch (checkWorkDay.value) {
			case 3:// 1日出勤系
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// １日
					if (dayAtr.value == 0) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			case 1: // 午前出勤系
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// 午前
					if (dayAtr.value == 1) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			case 2: // 午後出勤系
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// 午後
					if (dayAtr.value == 2) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			default:
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// １日
					if (dayAtr.value == 0) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();

					}
				}
				break;
			}

		} else {
			fixRestTime = flexWorkSetting.getOffdayWorkTime().getRestTimezone().isFixRestTime();
			lstTimezone = flexWorkSetting.getOffdayWorkTime().getRestTimezone().getFixedRestTimezone().getTimezones();
		}
		if (fixRestTime) {
			breakTimeZoneSettingOutPut.setLstTimezone(lstTimezone);
			return true;
		}
		return false;
	}

	@Override
	// 時差勤務設定から休憩時間帯を確認する
	public boolean ConfirmInterTimezoneStaggeredWorkSetting(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID, int weekdayHolidayClassification,
			WorkInfoOfDailyPerformance WorkInfo, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut,
			WorkStyle checkWorkDay) {

		if (WorkInfo != null && !WorkInfo.getScheduleTimeSheets().isEmpty()) {
			TimeWithDayAttr attendance = null;
			boolean workNoIsOne = false;
			List<ScheduleTimeSheet> scheduleTimeSheets = WorkInfo.getScheduleTimeSheets();
			int size = scheduleTimeSheets.size();
			for (int i = 0; i < size; i++) {
				ScheduleTimeSheet scheduleTimeSheet = scheduleTimeSheets.get(i);
				if (scheduleTimeSheet.getWorkNo().v() == 1) {
					workNoIsOne = true;
					// 日別実績の勤務情報．勤務予定時間帯．出勤
					attendance = scheduleTimeSheet.getAttendance();
					break;
				}
			}
			WorkTypeCode workTypeCode = WorkInfo.getRecordInfo().getWorkTypeCode();
			DailyWork dailyWork = null;
			Optional<WorkType> findByPK = this.workTypeRepo.findByPK(companyId, workTypeCode.v());
			if (findByPK.isPresent()) {
				// 勤務種類．1日の勤務
				dailyWork = findByPK.get().getDailyWork();
			}
			WorkTimeCode workTimeCode = WorkInfo.getRecordInfo().getWorkTimeCode();
			Optional<PredetemineTimeSetting> findByWorkTimeCode = this.predetemineTimeSettingRepo
					.findByWorkTimeCode(companyId, workTimeCode.v());
			PrescribedTimezoneSetting prescribedTimezoneSetting = null;
			if (findByWorkTimeCode.isPresent()) {
				// 所定時間設定．所定時間帯
				prescribedTimezoneSetting = findByWorkTimeCode.get().getPrescribedTimezoneSetting();

			}
			Optional<DiffTimeWorkSetting> DiffTimeWorkSetting = this.diffTimeWorkSettingRepo.find(companyId,
					workTimeCode.v());
			boolean isHasDiffTimeWorkSetting = false;
			if (DiffTimeWorkSetting.isPresent()) {
				isHasDiffTimeWorkSetting = true;
			}

			if (workNoIsOne && dailyWork != null && prescribedTimezoneSetting != null && isHasDiffTimeWorkSetting) {

				// 時差勤務時間の計算 xử ly chung newwaves
				calculateTimeDiffService.caculateJoggingWorkTime(attendance, dailyWork, prescribedTimezoneSetting);

				// 時差勤務の時刻補正 xu ly chung newwaves
				// fixed errorAlarm = false
				boolean errorAlarm = false;
				if (errorAlarm) {
					// エラーアラームをログ出力する
					List<EmployeeDailyPerError> employeeDailyPerErrors = this.employeeDailyPerErrorRepo
							.findList(companyId, employeeID);
					for (EmployeeDailyPerError employeeDailyPerError : employeeDailyPerErrors) {
						Optional<ErrorAlarmWorkRecord> errorAlarmWorkRecordOptional = this.errorAlarmWorkRecordRepo
								.findByCode(employeeDailyPerError.getErrorAlarmWorkRecordCode().v());
						if (errorAlarmWorkRecordOptional.isPresent() && empCalAndSumExecLogID != null) {
							ErrorAlarmWorkRecord errorAlarmWorkRecord = errorAlarmWorkRecordOptional.get();

							ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
									new ErrMessageResource("005"), EnumAdaptor.valueOf(0, ExecutionContent.class),
									processingDate, new ErrMessageContent(TextResource
											.localize(errorAlarmWorkRecord.getMessage().getMessageColor().v())));
							errRepo.add(employmentErrMes);
						}
					}
					return false;
				} else {
					DiffTimeWorkSetting diffTimeWorkSetting = this.diffTimeWorkSettingRepo
							.find(companyId, WorkInfo.getRecordInfo().getWorkTimeCode().v()).get();
					List<DiffTimeHalfDayWorkTimezone> lstHalfDayWorkTimezones = diffTimeWorkSetting
							.getHalfDayWorkTimezones();
					List<DeductionTime> timezones = null;
					if (weekdayHolidayClassification == 0) {
						switch (checkWorkDay.value) {
						case 3:// 1日出勤系
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// １日
								if (dayAtr.value == 0) {
									timezones = new ArrayList<DeductionTime>();
									List<DiffTimeDeductTimezone> restTimezones = fixHalfDayWorkTimezone
											.getRestTimezone().getRestTimezones();
									for (DiffTimeDeductTimezone diffTimeDeductTimezone : restTimezones) {
										DeductionTime d = diffTimeDeductTimezone;
										timezones.add(d);
									}
								}
							}
							break;
						case 1: // 午前出勤系
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// 午前
								if (dayAtr.value == 1) {
									timezones = new ArrayList<DeductionTime>();
									List<DiffTimeDeductTimezone> restTimezones = fixHalfDayWorkTimezone
											.getRestTimezone().getRestTimezones();
									for (DiffTimeDeductTimezone diffTimeDeductTimezone : restTimezones) {
										DeductionTime d = diffTimeDeductTimezone;
										timezones.add(d);
									}
								}
							}
							break;
						case 2: // 午後出勤系
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// 午後
								if (dayAtr.value == 2) {
									timezones = new ArrayList<DeductionTime>();
									List<DiffTimeDeductTimezone> restTimezones = fixHalfDayWorkTimezone
											.getRestTimezone().getRestTimezones();
									for (DiffTimeDeductTimezone diffTimeDeductTimezone : restTimezones) {
										DeductionTime d = diffTimeDeductTimezone;
										timezones.add(d);
									}
								}
							}
							break;
						default:
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// １日
								if (dayAtr.value == 0) {
									timezones = new ArrayList<DeductionTime>();
									List<DiffTimeDeductTimezone> restTimezones = fixHalfDayWorkTimezone
											.getRestTimezone().getRestTimezones();
									for (DiffTimeDeductTimezone diffTimeDeductTimezone : restTimezones) {
										DeductionTime d = diffTimeDeductTimezone;
										timezones.add(d);
									}
								}
							}
							break;
						}
					} else {
						List<DiffTimeDeductTimezone> restTimezones = diffTimeWorkSetting.getDayoffWorkTimezone()
								.getRestTimezone().getRestTimezones();
						timezones = new ArrayList<DeductionTime>();
						for (DiffTimeDeductTimezone diffTimeDeductTimezone : restTimezones) {
							DeductionTime d = diffTimeDeductTimezone;
							timezones.add(d);
						}
					}
					breakTimeZoneSettingOutPut.setLstTimezone(timezones);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	// 時差勤務設定から休憩時間帯を確認する
	public boolean ConfirmInterTimezoneStaggeredWorkSetting(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID, int weekdayHolidayClassification,
			WorkInfoOfDailyPerformance workInfo, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut,
			WorkStyle checkWorkDay, Optional<WorkType> findByPK, List<ErrorAlarmWorkRecord> errorMaster) {

		if (workInfo == null || workInfo.getScheduleTimeSheets().isEmpty()) {
			return false;
		}

		TimeWithDayAttr attendance = workInfo.getScheduleTimeSheets().stream()
				.filter(c -> c.getWorkNo().compareTo(1) == 0).map(c -> c.getAttendance()).findFirst().orElse(null);
		if (attendance == null) {
			return false;
		}
		if (!findByPK.isPresent() || findByPK.get().getDailyWork() == null) {
			// 勤務種類．1日の勤務
			return false;
		}

		String workTimeCode = workInfo.getRecordInfo().getWorkTimeCode().v();
		Optional<PredetemineTimeSetting> ptts = this.predetemineTimeSettingRepo.findByWorkTimeCode(companyId,
				workTimeCode);
		if (!ptts.isPresent() || ptts.get().getPrescribedTimezoneSetting() == null) {
			// 所定時間設定．所定時間帯
			return false;
		}
		Optional<DiffTimeWorkSetting> dtwso = this.diffTimeWorkSettingRepo.find(companyId, workTimeCode);
		if (!dtwso.isPresent()) {
			return false;
		}

		// 時差勤務時間の計算 xử ly chung newwaves
		calculateTimeDiffService.caculateJoggingWorkTime(attendance, findByPK.get().getDailyWork(),
				ptts.get().getPrescribedTimezoneSetting());

		// 時差勤務の時刻補正 xu ly chung newwaves
		// fixed errorAlarm = false
		boolean errorAlarm = false;
		if (errorAlarm) {
			// エラーアラームをログ出力する
			/** TODO: get from other */
			List<ErrMessageInfo> errors = this.employeeDailyPerErrorRepo.findList(companyId, employeeID).stream()
					.map(c -> {
						Optional<ErrorAlarmWorkRecord> errorAlarmWorkRecordOptional = errorMaster.stream()
								.filter(e -> e.getCode().equals(c.getErrorAlarmWorkRecordCode())).findFirst();

						if (errorAlarmWorkRecordOptional.isPresent()) {
							ErrorAlarmWorkRecord errorAlarmWorkRecord = errorAlarmWorkRecordOptional.get();

							return new ErrMessageInfo(employeeID, empCalAndSumExecLogID, new ErrMessageResource("005"),
									ExecutionContent.DAILY_CREATION, processingDate, new ErrMessageContent(TextResource
											.localize(errorAlarmWorkRecord.getMessage().getMessageColor().v())));
						}
						return null;
					}).filter(c -> c != null).collect(Collectors.toList());

			errRepo.addList(errors);

			return false;
		} else {
			if (weekdayHolidayClassification == 0) {
				dtwso.get().getHalfDayWorkTimezones().stream().forEach(lhdwt -> {
					if (lhdwt.getAmPmAtr() == AmPmAtr.ONE_DAY
							|| (lhdwt.getAmPmAtr() == AmPmAtr.AM && checkWorkDay == WorkStyle.MORNING_WORK)
							|| (lhdwt.getAmPmAtr() == AmPmAtr.PM && checkWorkDay == WorkStyle.AFTERNOON_WORK)) {
						breakTimeZoneSettingOutPut.getLstTimezone().addAll(lhdwt.getRestTimezone().getRestTimezones());
					}
				});
				return true;
			}

			breakTimeZoneSettingOutPut.getLstTimezone()
					.addAll(dtwso.get().getDayoffWorkTimezone().getRestTimezone().getRestTimezones());

			return true;
		}
	}

	@Override
	// 流動勤務設定から休憩時間帯を確認する
	public boolean confirmIntermissionTimeZone(String companyId, int weekdayHolidayClassification, String workTimeCode,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut) {
		FlowWorkSetting flowWorkSetting = this.flowWorkSettingRep.find(companyId, workTimeCode).get();

		if (weekdayHolidayClassification == 0) {
			if (flowWorkSetting.getHalfDayWorkTimezone().getRestTimezone().isFixRestTime()) {
				breakTimeZoneSettingOutPut.setLstTimezone(flowWorkSetting.getHalfDayWorkTimezone().getRestTimezone()
						.getFixedRestTimezone().getTimezones());
				return true;
			}

			return false;
		}

		if (flowWorkSetting.getOffdayWorkTimezone().getRestTimeZone().isFixRestTime()) {
			breakTimeZoneSettingOutPut.setLstTimezone(
					flowWorkSetting.getOffdayWorkTimezone().getRestTimeZone().getFixedRestTimezone().getTimezones());
		}
		return false;
	}

	@Override
	// 固定勤務設定から休憩時間帯を確認する
	public boolean CheckBreakTimeFromFixedWorkSetting(String companyId, int weekdayHolidayClassification,
			String workTimeCode, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, WorkStyle checkWorkDay) {

		Optional<FixedWorkSetting> FixedWorkSettingOptional = this.fixedWorkSettingRepo.findByKey(companyId,
				workTimeCode);
		// check null?
		FixedWorkSetting fixedWorkSetting = FixedWorkSettingOptional.get();
		if (weekdayHolidayClassification == 0) {
			
			
			Optional<FixHalfDayWorkTimezone> optional = fixedWorkSetting.getLstHalfDayWorkTimezone().stream().filter(item -> item.getDayAtr() == AmPmAtr.ONE_DAY
					|| (item.getDayAtr() == AmPmAtr.AM && checkWorkDay == WorkStyle.MORNING_WORK)
					|| (item.getDayAtr() == AmPmAtr.PM && checkWorkDay == WorkStyle.AFTERNOON_WORK)).findFirst();
			
			if (optional.isPresent()) {
				List<DeductionTime> timezones = optional.get().getRestTimezone().getLstTimezone();
				breakTimeZoneSettingOutPut.getLstTimezone().addAll(timezones);
			}

//			fixedWorkSetting.getLstHalfDayWorkTimezone().stream().forEach(fhdwt -> {
//				if ((fhdwt.getDayAtr() == AmPmAtr.ONE_DAY)
//						|| (fhdwt.getDayAtr() == AmPmAtr.AM && checkWorkDay == WorkStyle.MORNING_WORK)
//						|| (fhdwt.getDayAtr() == AmPmAtr.PM && checkWorkDay == WorkStyle.AFTERNOON_WORK)) {
//					List<DeductionTime> timezones = fhdwt.getRestTimezone().getLstTimezone();
//					breakTimeZoneSettingOutPut.getLstTimezone().addAll(timezones);
//				}
//			});
			return true;
		}

		breakTimeZoneSettingOutPut.getLstTimezone()
				.addAll(fixedWorkSetting.getOffdayWorkTimezone().getRestTimezone().getLstTimezone());
		return true;
	}

	// 休出かどうかの判断
	public boolean checkHolidayOrNot(String companyId, String workTypeCd) {
		Optional<WorkType> WorkTypeOptional = this.workTypeRepo.findByPK(companyId, workTypeCd);
		if (!WorkTypeOptional.isPresent()) {
			return false;
		}
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// 休日出勤
		if (oneDay.value == 11) {
			return true;
		}
		return false;
	}

	// 休出かどうかの判断
	public int checkHolidayOrNot(Optional<WorkType> WorkTypeOptional) {
		if (!WorkTypeOptional.isPresent()) {
			return 0;
		}
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// 休日出勤
		if (oneDay.value == 11) {
			return 1;
		}
		return 0;
	}

}
