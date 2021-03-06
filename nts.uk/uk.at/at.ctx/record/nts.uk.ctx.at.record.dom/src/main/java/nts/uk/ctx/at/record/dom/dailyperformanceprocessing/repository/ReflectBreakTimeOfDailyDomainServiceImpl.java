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
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.BreakTimeZoneSettingOutPut;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.ErrMessageResource;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.ScheduleTimeSheet;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.caltimediff.CalculateTimeDiffService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
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

	@Override
	public BreakTimeOfDailyPerformance reflectBreakTimeZone(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, WorkInfoOfDailyPerformance workInfo) {
		Optional<BreakTimeOfDailyPerformance> breakOpt = this.breakTimeOfDailyPerformanceRepo.find(employeeID,
				processingDate, 0);
		if (breakOpt.isPresent()) {
			return null;
		}
		// ????????????????????????????????????
		List<TimeLeavingWork> timeLeavingWorks = null;
		if (timeLeavingOfDailyPerformance != null && timeLeavingOfDailyPerformance.getAttendance().getTimeLeavingWorks() != null
				&& !timeLeavingOfDailyPerformance.getAttendance().getTimeLeavingWorks().isEmpty()) {
			timeLeavingWorks = timeLeavingOfDailyPerformance.getAttendance().getTimeLeavingWorks();
		} else {
			Optional<TimeLeavingOfDailyPerformance> TimeLeavingOptional = this.timeLeavingRepo.findByKey(employeeID,
					processingDate);
			if (TimeLeavingOptional.isPresent()) {
				TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance2 = TimeLeavingOptional.get();
				List<TimeLeavingWork> timeLeavingWorks2 = timeLeavingOfDailyPerformance2.getAttendance().getTimeLeavingWorks();
				if (timeLeavingWorks2 != null && !timeLeavingWorks2.isEmpty()) {
					timeLeavingWorks = timeLeavingWorks2;
				}
			}
		}
		if (timeLeavingWorks == null) {
			return null;
		}
		TimeLeavingOfDailyPerformance ofDailyPerformance = new TimeLeavingOfDailyPerformance(employeeID, processingDate, timeLeavingOfDailyPerformance.getAttendance());
		WorkInfoOfDailyPerformance dailyPerformance = new WorkInfoOfDailyPerformance(employeeID, processingDate, workInfo.getWorkInformation());
		return this.reflectBreakTime(companyId, employeeID, processingDate, empCalAndSumExecLogID,
				ofDailyPerformance, dailyPerformance);
	}

	@Override
	public BreakTimeOfDailyPerformance reflectBreakTime(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance,
			WorkInfoOfDailyPerformance WorkInfo) {
		BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut = new BreakTimeZoneSettingOutPut();
		// ????????????????????????????????????
		List<TimeLeavingWork> timeLeavingWorks = null;
		if (timeLeavingOfDailyPerformance != null && !timeLeavingOfDailyPerformance.getAttendance().getTimeLeavingWorks().isEmpty()) {
			timeLeavingWorks = timeLeavingOfDailyPerformance.getAttendance().getTimeLeavingWorks();
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
			// ?????????????????????NO
			int frameNo = i + 1;
			// ?????????????????????????????????????????????????????????
			boolean checkAddBreakTime = checkAddBreakTime(timeLeavingWorks, timeZone, frameNo);
			if (checkAddBreakTime) {
				lstBreakTime.add(new BreakTimeSheet(new BreakFrameNo(frameNo), timeZone.getStart(), timeZone.getEnd(),
						new AttendanceTime(0)));
			}
		}
		// ???????????? ??? ?????????????????????????????????
		return new BreakTimeOfDailyPerformance(employeeID, processingDate, lstBreakTime);
	}

	// ?????????????????????????????????????????????????????????
	public boolean checkAddBreakTime(List<TimeLeavingWork> timeLeavingWorks, DeductionTime timeZone, int frameNo) {
		boolean isAddBreaktime = false;
		Collections.sort(timeLeavingWorks, new Comparator<TimeLeavingWork>() {
			public int compare(TimeLeavingWork o1, TimeLeavingWork o2) {
				if (o2 == null || o2.getAttendanceStamp() == null || !o2.getAttendanceStamp().isPresent()
						|| o2.getAttendanceStamp().get().getStamp() == null
						|| !o2.getAttendanceStamp().get().getStamp().isPresent()
						|| o2.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay() == null) {
					return 1;
				}
				if (o1 == null || o1.getAttendanceStamp() == null || !o1.getAttendanceStamp().isPresent()
						|| o1.getAttendanceStamp().get().getStamp() == null
						|| !o1.getAttendanceStamp().get().getStamp().isPresent()
						|| o1.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay() == null) {
					return -1;
				}
				// ?????????????????????
				int t1 = o1.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay() != null
						? o1.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get().v().intValue() : 0;
				int t2 = o2.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay() != null
						? o2.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get().v().intValue() : 0;
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
			// ?????????????????????
			TimeWithDayAttr startDate1 = timeZone.getStart();
			TimeWithDayAttr endDate1 = timeZone.getEnd();
			// ?????????????????????
			TimeWithDayAttr startDate2 = null;
			TimeWithDayAttr endDate2 = null;

			if (timeLeavingWork.getAttendanceStamp() != null && timeLeavingWork.getAttendanceStamp().isPresent()
					&& timeLeavingWork.getAttendanceStamp().get().getStamp() != null
					&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
					&& timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay() != null) {
				startDate2 = timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get();
			}

			if (timeLeavingWork.getLeaveStamp() != null && timeLeavingWork.getLeaveStamp().isPresent()
					&& timeLeavingWork.getLeaveStamp().get().getStamp() != null
					&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()
					&& timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay() != null) {
				endDate2 = timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get();
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
				// ?????????
				if (duplicationStatusOfTimeZone != DuplicationStatusOfTimeZone.NON_OVERLAPPING) {
					isAddBreaktime = true;
					break;
				}
			}

		}
		return isAddBreaktime;
	}

	// ????????????????????????????????????
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut) {
		// fixed thieu reset breaktime
		boolean checkReflect = false;
		Optional<WorkType> workTypeOpt = workTypeRepo.findByPK(companyId,
				WorkInfo.getWorkInformation().getRecordInfo().getWorkTypeCode().v());
		// 1??????????????????1?????????????????????
		WorkStyle checkWorkDay = this.basicScheduleService.checkWorkDay(workTypeOpt);
		// 1????????????
		if (checkWorkDay.value == 0) {
			return false;
		} else {
			// ???????????????????????????
			int weekdayHolidayClassification = this.checkHolidayOrNot(workTypeOpt);
			if (WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode() != null) {	
				String workTimeCode = WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode().v();
				Optional<WorkTimeSetting> WorkTimeSettingOptional = this.workTimeSettingRepo.findByCode(companyId,
						workTimeCode);
				WorkTimeSetting workTimeSetting = WorkTimeSettingOptional.get();
				// WorkTimeDailyAtr = ??????????????????????????????
				if (workTimeSetting.getWorkTimeDivision().getWorkTimeDailyAtr().value == 0) {

					switch (workTimeSetting.getWorkTimeDivision().getWorkTimeMethodSet().value) {
					case 2:// ????????????
						checkReflect = this.confirmIntermissionTimeZone(companyId, weekdayHolidayClassification,
								workTimeCode, breakTimeZoneSettingOutPut);

						break;
					case 1:// ????????????
						checkReflect = ConfirmInterTimezoneStaggeredWorkSetting(companyId, employeeID, processingDate,
								empCalAndSumExecLogID, weekdayHolidayClassification, WorkInfo,
								breakTimeZoneSettingOutPut, checkWorkDay);
						break;

					default:// ????????????
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

	// ????????????????????????????????????
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, List<ErrorAlarmWorkRecord> errorMaster) {
		return checkBreakTimeSetting(companyId, employeeID, processingDate, empCalAndSumExecLogID, WorkInfo,
				breakTimeZoneSettingOutPut, errorMaster, Optional.empty());
	}

	// ????????????????????????????????????
	public boolean checkBreakTimeSetting(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance WorkInfo,
			BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut, List<ErrorAlarmWorkRecord> errorMaster,
			Optional<WorkTimeSetting> workTime) {
		// fixed thieu reset breaktime
		Optional<WorkType> workTypeOpt = workTypeRepo.findByPK(companyId,
				WorkInfo.getWorkInformation().getRecordInfo().getWorkTypeCode().v());
		// 1??????????????????1?????????????????????
		WorkStyle checkWorkDay = this.basicScheduleService.checkWorkDay(workTypeOpt);
		// 1????????????
		if (checkWorkDay == WorkStyle.ONE_DAY_REST) {
			return false;
		}

		// ???????????????????????????
		int weekdayHolidayClassification = this.checkHolidayOrNot(workTypeOpt);

		if (WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode() != null) {
			String workTimeCode = WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode().v();
			WorkTimeSetting wts = workTime
					.orElseGet(() -> this.workTimeSettingRepo.findByCode(companyId, workTimeCode).get());
			// WorkTimeDailyAtr = ??????????????????????????????
			if (wts.getWorkTimeDivision().getWorkTimeDailyAtr().value == 0) {

				switch (wts.getWorkTimeDivision().getWorkTimeMethodSet().value) {
				case 2:// ????????????
					return this.confirmIntermissionTimeZone(companyId, weekdayHolidayClassification, workTimeCode,
							breakTimeZoneSettingOutPut);

				case 1:// ????????????
					return ConfirmInterTimezoneStaggeredWorkSetting(companyId, employeeID, processingDate,
							empCalAndSumExecLogID, weekdayHolidayClassification, WorkInfo, breakTimeZoneSettingOutPut,
							checkWorkDay, workTypeOpt, errorMaster);

				default:// ????????????
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
	// ???????????????????????????????????????????????????????????????
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
			case 3:// 1????????????
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// ??????
					if (dayAtr.value == 0) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			case 1: // ???????????????
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// ??????
					if (dayAtr.value == 1) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			case 2: // ???????????????
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// ??????
					if (dayAtr.value == 2) {
						lstTimezone = fixHalfDayWorkTimezone.getRestTimezone().getFixedRestTimezone().getTimezones();
						fixRestTime = fixHalfDayWorkTimezone.getRestTimezone().isFixRestTime();
					}
				}
				break;
			default:
				for (FlexHalfDayWorkTime fixHalfDayWorkTimezone : lstHalfDayWorkTimezone) {
					AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmpmAtr();
					// ??????
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
	// ??????????????????????????????????????????????????????
	public boolean ConfirmInterTimezoneStaggeredWorkSetting(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID, int weekdayHolidayClassification,
			WorkInfoOfDailyPerformance WorkInfo, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut,
			WorkStyle checkWorkDay) {

		if (WorkInfo != null && !WorkInfo.getWorkInformation().getScheduleTimeSheets().isEmpty()) {
			TimeWithDayAttr attendance = null;
			boolean workNoIsOne = false;
			List<ScheduleTimeSheet> scheduleTimeSheets = WorkInfo.getWorkInformation().getScheduleTimeSheets();
			int size = scheduleTimeSheets.size();
			for (int i = 0; i < size; i++) {
				ScheduleTimeSheet scheduleTimeSheet = scheduleTimeSheets.get(i);
				if (scheduleTimeSheet.getWorkNo().v() == 1) {
					workNoIsOne = true;
					// ????????????????????????????????????????????????????????????
					attendance = scheduleTimeSheet.getAttendance();
					break;
				}
			}
			WorkTypeCode workTypeCode = WorkInfo.getWorkInformation().getRecordInfo().getWorkTypeCode();
			DailyWork dailyWork = null;
			Optional<WorkType> findByPK = this.workTypeRepo.findByPK(companyId, workTypeCode.v());
			if (findByPK.isPresent()) {
				// ???????????????1????????????
				dailyWork = findByPK.get().getDailyWork();
			}
			WorkTimeCode workTimeCode = WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode();
			Optional<PredetemineTimeSetting> findByWorkTimeCode = this.predetemineTimeSettingRepo
					.findByWorkTimeCode(companyId, workTimeCode.v());
			PrescribedTimezoneSetting prescribedTimezoneSetting = null;
			if (findByWorkTimeCode.isPresent()) {
				// ????????????????????????????????????
				prescribedTimezoneSetting = findByWorkTimeCode.get().getPrescribedTimezoneSetting();

			}
			Optional<DiffTimeWorkSetting> DiffTimeWorkSetting = this.diffTimeWorkSettingRepo.find(companyId,
					workTimeCode.v());
			boolean isHasDiffTimeWorkSetting = false;
			if (DiffTimeWorkSetting.isPresent()) {
				isHasDiffTimeWorkSetting = true;
			}

			if (workNoIsOne && dailyWork != null && prescribedTimezoneSetting != null && isHasDiffTimeWorkSetting) {

				// ??????????????????????????? x??? ly chung newwaves
				calculateTimeDiffService.caculateJoggingWorkTime(attendance, dailyWork, prescribedTimezoneSetting);

				// ??????????????????????????? xu ly chung newwaves
				// fixed errorAlarm = false
				boolean errorAlarm = false;
				if (errorAlarm) {
					// ??????????????????????????????????????????
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
							.find(companyId, WorkInfo.getWorkInformation().getRecordInfo().getWorkTimeCode().v()).get();
					List<DiffTimeHalfDayWorkTimezone> lstHalfDayWorkTimezones = diffTimeWorkSetting
							.getHalfDayWorkTimezones();
					List<DeductionTime> timezones = null;
					if (weekdayHolidayClassification == 0) {
						switch (checkWorkDay.value) {
						case 3:// 1????????????
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// ??????
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
						case 1: // ???????????????
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// ??????
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
						case 2: // ???????????????
							for (DiffTimeHalfDayWorkTimezone fixHalfDayWorkTimezone : lstHalfDayWorkTimezones) {
								AmPmAtr dayAtr = fixHalfDayWorkTimezone.getAmPmAtr();
								// ??????
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
								// ??????
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

	// ??????????????????????????????????????????????????????
	public boolean ConfirmInterTimezoneStaggeredWorkSetting(String companyId, String employeeID,
			GeneralDate processingDate, String empCalAndSumExecLogID, int weekdayHolidayClassification,
			WorkInfoOfDailyPerformance workInfo, BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut,
			WorkStyle checkWorkDay, Optional<WorkType> findByPK, List<ErrorAlarmWorkRecord> errorMaster) {

		if (workInfo == null || workInfo.getWorkInformation().getScheduleTimeSheets().isEmpty()) {
			return false;
		}

		TimeWithDayAttr attendance = workInfo.getWorkInformation().getScheduleTimeSheets().stream()
				.filter(c -> c.getWorkNo().compareTo(1) == 0).map(c -> c.getAttendance()).findFirst().orElse(null);
		if (attendance == null) {
			return false;
		}
		if (!findByPK.isPresent() || findByPK.get().getDailyWork() == null) {
			// ???????????????1????????????
			return false;
		}

		String workTimeCode = workInfo.getWorkInformation().getRecordInfo().getWorkTimeCode().v();
		Optional<PredetemineTimeSetting> ptts = this.predetemineTimeSettingRepo.findByWorkTimeCode(companyId,
				workTimeCode);
		if (!ptts.isPresent() || ptts.get().getPrescribedTimezoneSetting() == null) {
			// ????????????????????????????????????
			return false;
		}
		Optional<DiffTimeWorkSetting> dtwso = this.diffTimeWorkSettingRepo.find(companyId, workTimeCode);
		if (!dtwso.isPresent()) {
			return false;
		}

		// ??????????????????????????? x??? ly chung newwaves
		calculateTimeDiffService.caculateJoggingWorkTime(attendance, findByPK.get().getDailyWork(),
				ptts.get().getPrescribedTimezoneSetting());

		// ??????????????????????????? xu ly chung newwaves
		// fixed errorAlarm = false
		boolean errorAlarm = false;
		if (errorAlarm) {
			// ??????????????????????????????????????????
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
	// ??????????????????????????????????????????????????????
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
	// ??????????????????????????????????????????????????????
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
				List<DeductionTime> timezones = optional.get().getRestTimezone().getTimezones();
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
				.addAll(fixedWorkSetting.getOffdayWorkTimezone().getRestTimezone().getTimezones());
		return true;
	}

	// ???????????????????????????
	public boolean checkHolidayOrNot(String companyId, String workTypeCd) {
		Optional<WorkType> WorkTypeOptional = this.workTypeRepo.findByPK(companyId, workTypeCd);
		if (!WorkTypeOptional.isPresent()) {
			return false;
		}
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// ????????????
		if (oneDay.value == 11) {
			return true;
		}
		return false;
	}

	// ???????????????????????????
	public int checkHolidayOrNot(Optional<WorkType> WorkTypeOptional) {
		if (!WorkTypeOptional.isPresent()) {
			return 0;
		}
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// ????????????
		if (oneDay.value == 11) {
			return 1;
		}
		return 0;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param companyId
	 * @param employeeID
	 * @param processingDate
	 * @param WorkInfo
	 * @return
	 */
	@Override
	public Optional<BreakTimeOfDailyPerformance> getBreakTime(String companyId, String employeeID,
			GeneralDate processingDate, WorkInfoOfDailyPerformance WorkInfo) {
		// Optional<BreakTimeOfDailyPerformance> breakOpt =
		// this.breakTimeOfDailyPerformanceRepo.find(employeeID,
		// processingDate, 0);
		// if (breakOpt.isPresent()) {
		// return Optional.empty();
		// }
		BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut = new BreakTimeZoneSettingOutPut();

		// ????????????????????????????????????
		boolean checkBreakTimeSetting = this.checkBreakTimeSetting(companyId, employeeID, processingDate, null,
				WorkInfo, breakTimeZoneSettingOutPut);
		if (!checkBreakTimeSetting) {
			return Optional.empty();
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
		// ?????????????????????????????????if?????????????????????????????????????????????????????????
		if (true) {
			for (int i = 0; i < size; i++) {
				DeductionTime timeZone = lstTimezone.get(i);
				// ?????????????????????NO
				int frameNo = i + 1;
				lstBreakTime.add(new BreakTimeSheet(new BreakFrameNo(frameNo), timeZone.getStart(), timeZone.getEnd(),
						new AttendanceTime(0)));
			}
		}
		// ???????????? ??? ?????????????????????????????????
		return Optional.of(new BreakTimeOfDailyPerformance(employeeID, processingDate, lstBreakTime));
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param companyId
	 * @param employeeID
	 * @param processingDate
	 * @param WorkInfo
	 * @return
	 */
	@Override
	public Optional<BreakTimeOfDailyPerformance> getBreakTime(String companyId, String employeeID,
			GeneralDate processingDate, WorkInfoOfDailyPerformance WorkInfo, List<ErrorAlarmWorkRecord> errorMaster) {
		return getBreakTime(companyId, employeeID, processingDate, WorkInfo, errorMaster, Optional.empty());
	}

	public Optional<BreakTimeOfDailyPerformance> getBreakTime(String companyId, String employeeID,
			GeneralDate processingDate, WorkInfoOfDailyPerformance WorkInfo, List<ErrorAlarmWorkRecord> errorMaster,
			Optional<WorkTimeSetting> workTime) {
		// Optional<BreakTimeOfDailyPerformance> breakOpt =
		// this.breakTimeOfDailyPerformanceRepo.find(employeeID,
		// processingDate, 0);
		// if (breakOpt.isPresent()) {
		// return Optional.empty();
		// }
		BreakTimeZoneSettingOutPut breakTimeZoneSettingOutPut = new BreakTimeZoneSettingOutPut();

		// ????????????????????????????????????
		boolean checkBreakTimeSetting = this.checkBreakTimeSetting(companyId, employeeID, processingDate, null,
				WorkInfo, breakTimeZoneSettingOutPut, errorMaster, workTime);
		if (!checkBreakTimeSetting) {
			return Optional.empty();
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
		// ?????????????????????????????????if?????????????????????????????????????????????????????????
		if (true) {
			for (int i = 0; i < size; i++) {
				DeductionTime timeZone = lstTimezone.get(i);
				// ?????????????????????NO
				int frameNo = i + 1;
				lstBreakTime.add(new BreakTimeSheet(new BreakFrameNo(frameNo), timeZone.getStart(), timeZone.getEnd(),
						new AttendanceTime(0)));
			}
		}
		// ???????????? ??? ?????????????????????????????????
		return Optional.of(new BreakTimeOfDailyPerformance(employeeID, processingDate, lstBreakTime));
	}
}
