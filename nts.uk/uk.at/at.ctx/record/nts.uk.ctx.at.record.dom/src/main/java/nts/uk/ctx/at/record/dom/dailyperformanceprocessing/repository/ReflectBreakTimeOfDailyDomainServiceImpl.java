package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.BreakTimeZoneSettingOutPut;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectTimezoneOutput;
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
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeDeductTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexHalfDayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
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

	@Override
	public BreakTimeOfDailyPerformance reflectBreakTime(String companyId, String employeeID, GeneralDate processingDate,String empCalAndSumExecLogID,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, WorkInfoOfDailyPerformance WorkInfo) {
		BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut = new BreakTimeZoneSettingOutPut();
		// 休憩時間帯設定を確認する
		List<TimeLeavingWork> timeLeavingWorks = null;
		if (timeLeavingOfDailyPerformance != null && timeLeavingOfDailyPerformance.getTimeLeavingWorks() != null
				&& !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
			timeLeavingWorks = timeLeavingOfDailyPerformance.getTimeLeavingWorks();
		} else {
			Optional<TimeLeavingOfDailyPerformance> TimeLeavingOptional = this.timeLeavingRepo.findByKey(employeeID,
					processingDate);
			if (TimeLeavingOptional.isPresent()) {
				TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance2 = TimeLeavingOptional.get();
				List<TimeLeavingWork> timeLeavingWorks2 = timeLeavingOfDailyPerformance2.getTimeLeavingWorks();
				if (timeLeavingWorks2 != null && !timeLeavingWorks2.isEmpty()) {
					timeLeavingWorks = timeLeavingWorks2;
				}
			}
		}
		if (timeLeavingWorks == null) {
			return null;
		}
		boolean checkBreakTimeSetting = this.checkBreakTimeSetting(companyId, employeeID,  processingDate, empCalAndSumExecLogID, WorkInfo, breakTimeZoneSettingOutPut);
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
			if(checkAddBreakTime){
				lstBreakTime.add(new BreakTimeSheet(new BreakFrameNo(frameNo), timeZone.getStart(), timeZone.getEnd(), new AttendanceTime(0)));
			}
		}
		//休憩種類　←　「就業時間帯から参照」
		return new BreakTimeOfDailyPerformance(employeeID,BreakType.REFER_WORK_TIME  , lstBreakTime, processingDate);

	}

	// 出退勤と重複する休憩時間帯のみ追加する
	public boolean checkAddBreakTime(List<TimeLeavingWork> timeLeavingWorks, DeductionTime timeZone, int frameNo ) {
		boolean isAddBreaktime =false;
		Collections.sort(timeLeavingWorks, new Comparator<TimeLeavingWork>() {
			public int compare(TimeLeavingWork o1, TimeLeavingWork o2) {
				if (o2 == null || o2.getAttendanceStamp() == null || !o2.getAttendanceStamp().isPresent()
						|| o2.getAttendanceStamp().get().getStamp() == null
						|| !o2.getAttendanceStamp().get().getStamp().isPresent()) {
					return 1;
				}
				if (o1 == null || o1.getAttendanceStamp() == null || !o1.getAttendanceStamp().isPresent()
						|| o1.getAttendanceStamp().get().getStamp() == null
						|| !o1.getAttendanceStamp().get().getStamp().isPresent()) {
					return -1;
				}
				int t1 = o1.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue();
				int t2 = o2.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v().intValue();
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
			TimeWithDayAttr startDate2 = timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay();
			TimeWithDayAttr endDate2 = timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay();
			TimeSpanForCalc timeSpanFirstTime = new TimeSpanForCalc(startDate1, endDate1);
			TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(startDate2, endDate2);
			DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
					.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
			DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
					.checkStateAtr(duplicateStateAtr);
			// 非重複
			if (duplicationStatusOfTimeZone != DuplicationStatusOfTimeZone.NON_OVERLAPPING) {
				isAddBreaktime =true;
				break;
			}
		}
		return isAddBreaktime;
	}

	// 休憩時間帯設定を確認する
	public boolean checkBreakTimeSetting(String companyId,String employeeID, GeneralDate processingDate,String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut) {
		// fixed thieu reset breaktime
		boolean checkReflect = false;
		//1日半日出勤・1日休日系の判定
		WorkStyle checkWorkDay = this.basicScheduleService
				.checkWorkDay(WorkInfo.getRecordWorkInformation().getWorkTypeCode().v());
		// 1日休日系
		if (checkWorkDay.value == 0) {
			return false;
		} else {
			// Optional<SingleDaySchedule> holidayWorkScheduleOptional =
			// this.workingConditionItemService.getHolidayWorkSchedule(companyId,
			// WorkInfo.getEmployeeId(), WorkInfo.getYmd(),
			// WorkInfo.getRecordWorkInformation().getWorkTypeCode().v());
			//休日出勤時の勤務情報を取得する
			// 1* Lấy 休日出勤時の勤務情報 trả về 勤務情報なし , 公休出勤時 ....
			// (fixed)
			String result = "勤務情報なし";
			// 1*

			String weekdayHolidayClassification = null;
			if ("勤務情報なし".equals(result)) {
				weekdayHolidayClassification = "平日";
			} else {
				weekdayHolidayClassification = "休日";
			}
			Optional<WorkTimeSetting> WorkTimeSettingOptional = this.workTimeSettingRepo.findByCode(companyId,
					WorkInfo.getRecordWorkInformation().getWorkTimeCode().v());
			WorkTimeSetting workTimeSetting = WorkTimeSettingOptional.get();
			// WorkTimeDailyAtr = 通常勤務・変形労働用
			if (workTimeSetting.getWorkTimeDivision().getWorkTimeDailyAtr().value == 0) {

				switch (workTimeSetting.getWorkTimeDivision().getWorkTimeMethodSet().value) {
				case 0:// 固定勤務
					checkReflect = this.CheckBreakTimeFromFixedWorkSetting(companyId, weekdayHolidayClassification,
							WorkInfo.getRecordWorkInformation().getWorkTimeCode().v(), breakTimeZoneSettingOutPut,
							checkWorkDay);
					break;
				case 2:// 流動勤務
					checkReflect = this.confirmIntermissionTimeZone(companyId, weekdayHolidayClassification,
							WorkInfo.getRecordWorkInformation().getWorkTimeCode().v(), breakTimeZoneSettingOutPut);

					break;
				case 1:// 時差勤務
					checkReflect = ConfirmInterTimezoneStaggeredWorkSetting(companyId, employeeID,   processingDate, empCalAndSumExecLogID, weekdayHolidayClassification,
							WorkInfo, breakTimeZoneSettingOutPut, checkWorkDay);
					break;

				default:
					checkReflect = this.CheckBreakTimeFromFixedWorkSetting(companyId, weekdayHolidayClassification,
							WorkInfo.getRecordWorkInformation().getWorkTimeCode().v(), breakTimeZoneSettingOutPut,
							checkWorkDay);
					break;
				}

			} else {
				checkReflect = this.confirmInterFlexWorkSetting(companyId, weekdayHolidayClassification,
						WorkInfo.getRecordWorkInformation().getWorkTimeCode().v(), breakTimeZoneSettingOutPut,
						checkWorkDay);
			}

		}

		return checkReflect;
	}

	// フレックス勤務設定から休憩時間帯を確認する
	public boolean confirmInterFlexWorkSetting(String companyId, String weekdayHolidayClassification,
			String workTimeCode, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, WorkStyle checkWorkDay) {
		Optional<FlexWorkSetting> FlexWorkSettingOptional = this.flexWorkSettingRepo.find(companyId, workTimeCode);
		FlexWorkSetting flexWorkSetting = FlexWorkSettingOptional.get();
		List<DeductionTime> lstTimezone = null;
		boolean fixRestTime = true;
		if ("平日".equals(weekdayHolidayClassification)) {
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

	// 時差勤務設定から休憩時間帯を確認する
	public boolean ConfirmInterTimezoneStaggeredWorkSetting(String companyId,String employeeID, GeneralDate processingDate,String empCalAndSumExecLogID, String weekdayHolidayClassification,
			WorkInfoOfDailyPerformance WorkInfo, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut,
			WorkStyle checkWorkDay) {

		if (WorkInfo != null && WorkInfo.getScheduleTimeSheets() != null
				&& !WorkInfo.getScheduleTimeSheets().isEmpty()) {
			boolean workNoIsOne = false;
			List<ScheduleTimeSheet> scheduleTimeSheets = WorkInfo.getScheduleTimeSheets();
			int size = scheduleTimeSheets.size();
			for (int i = 0; i < size; i++) {
				ScheduleTimeSheet scheduleTimeSheet = scheduleTimeSheets.get(i);
				if (scheduleTimeSheet.getWorkNo().v() == 1) {
					workNoIsOne = true;
					break;
				}
			}
			if (workNoIsOne) {

				// 時差勤務時間の計算 xử ly chung newwaves
				// 時差勤務の時刻補正 xu ly chung newwaves
				// fixed errorAlarm = false
				boolean errorAlarm = false;
				if (errorAlarm) {
					//エラーアラームをログ出力する
				List<EmployeeDailyPerError> employeeDailyPerErrors = this.employeeDailyPerErrorRepo.findList(companyId, employeeID);
					for (EmployeeDailyPerError employeeDailyPerError : employeeDailyPerErrors) {
						Optional<ErrorAlarmWorkRecord> errorAlarmWorkRecordOptional = this.errorAlarmWorkRecordRepo.findByCode(employeeDailyPerError.getErrorAlarmWorkRecordCode().v());
						if (errorAlarmWorkRecordOptional.isPresent()) {
							ErrorAlarmWorkRecord errorAlarmWorkRecord = errorAlarmWorkRecordOptional.get();
							
							ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
									new ErrMessageResource("005"), EnumAdaptor.valueOf(0, ExecutionContent.class), processingDate,
									new ErrMessageContent(TextResource.localize(errorAlarmWorkRecord.getMessage().getMessageColor().v())));
							errRepo.add(employmentErrMes);
						}
					}
					return false;
				} else {
					DiffTimeWorkSetting diffTimeWorkSetting = this.diffTimeWorkSettingRepo
							.find(companyId, WorkInfo.getRecordWorkInformation().getWorkTimeCode().v()).get();
					List<DiffTimeHalfDayWorkTimezone> lstHalfDayWorkTimezones = diffTimeWorkSetting
							.getHalfDayWorkTimezones();
					List<DeductionTime> timezones = null;
					if ("平日".equals(weekdayHolidayClassification)) {
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

	// 流動勤務設定から休憩時間帯を確認する
	public boolean confirmIntermissionTimeZone(String companyId, String weekdayHolidayClassification,
			String workTimeCode, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut) {
		List<DeductionTime> lstTimezone;
		Optional<FlowWorkSetting> FlowWorkSettingoptional = this.flowWorkSettingRep.find(companyId, workTimeCode);
		FlowWorkSetting flowWorkSetting = FlowWorkSettingoptional.get();

		boolean fixRestTime;
		if ("平日".equals(weekdayHolidayClassification)) {
			fixRestTime = flowWorkSetting.getHalfDayWorkTimezone().getRestTimezone().isFixRestTime();
			lstTimezone = flowWorkSetting.getHalfDayWorkTimezone().getRestTimezone().getFixedRestTimezone()
					.getTimezones();
		} else {
			fixRestTime = flowWorkSetting.getOffdayWorkTimezone().getRestTimeZone().isFixRestTime();
			lstTimezone = flowWorkSetting.getOffdayWorkTimezone().getRestTimeZone().getFixedRestTimezone()
					.getTimezones();
		}
		if (fixRestTime) {
			breakTimeZoneSettingOutPut.setLstTimezone(lstTimezone);
			return true;
		}
		return false;
	}

	// 固定勤務設定から休憩時間帯を確認する
	public boolean CheckBreakTimeFromFixedWorkSetting(String companyId, String weekdayHolidayClassification,
			String workTimeCode, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, WorkStyle checkWorkDay) {

		List<DeductionTime> lstTimezone = new ArrayList<DeductionTime>();
		Optional<FixedWorkSetting> FixedWorkSettingOptional = this.fixedWorkSettingRepo.findByKey(companyId,
				workTimeCode);
		// check null?
		FixedWorkSetting fixedWorkSetting = FixedWorkSettingOptional.get();
		List<FixHalfDayWorkTimezone> lstHalfDayWorkTimezone = fixedWorkSetting.getLstHalfDayWorkTimezone();
		if ("平日".equals(weekdayHolidayClassification)) {
			switch (checkWorkDay.value) {
			case 3:// 1日出勤系
				for (FixHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getDayAtr();
					// １日
					if (dayAtr.value == 0) {
						List<DeductionTime> timezones = fixHalfDayWorkTimezone.getRestTimezone().getLstTimezone();
						lstTimezone.addAll(timezones);
					}
				}
				break;
			case 1: // 午前出勤系
				for (FixHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getDayAtr();
					// 午前
					if (dayAtr.value == 1) {
						List<DeductionTime> timezones = fixHalfDayWorkTimezone.getRestTimezone().getLstTimezone();
						lstTimezone.addAll(timezones);
					}
				}
				break;
			case 2: // 午後出勤系
				for (FixHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getDayAtr();
					// 午後
					if (dayAtr.value == 2) {
						List<DeductionTime> timezones = fixHalfDayWorkTimezone.getRestTimezone().getLstTimezone();
						lstTimezone.addAll(timezones);
					}
				}
				break;
			default:
				for (FixHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getDayAtr();
					// １日
					if (dayAtr.value == 0) {
						List<DeductionTime> timezones = fixHalfDayWorkTimezone.getRestTimezone().getLstTimezone();
						lstTimezone.addAll(timezones);
					}
				}
				break;
			}
		} else {
			List<DeductionTime> timezones = fixedWorkSetting.getOffdayWorkTimezone().getRestTimezone().getLstTimezone();
			lstTimezone.addAll(timezones);
		}
		breakTimeZoneSettingOutPut.setLstTimezone(lstTimezone);
		return true;
	}

}
