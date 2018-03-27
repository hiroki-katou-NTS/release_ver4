package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleSidDto;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ReflectStampOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectOnHolidayOutPut;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectRangeOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectTimezoneOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeZoneOutput;
import nts.uk.ctx.at.record.dom.stamp.StampItem;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampIncorrectOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.DoubleStampAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.LackOfStampingAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.MissingOfTemporaryStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PCLogOnOffIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PClogOnOffLackOfStamp;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.StampIncorrectOrderAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryDoubleStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryStampOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemService;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class ReflectStampDomainServiceImpl implements ReflectStampDomainService {

	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private BasicScheduleService basicScheduleService;

	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	@Inject
	private BasicScheduleAdapter basicScheduleAdapter;

	@Inject
	private LackOfStampingAlgorithm lackOfStamping;

	@Inject
	private StampIncorrectOrderAlgorithm stampIncorrectOrderAlgorithm;

	@Inject
	private DoubleStampAlgorithm doubleStampAlgorithm;

	@Inject
	private MissingOfTemporaryStampChecking missingOfTemporaryStampChecking;

	@Inject
	private TemporaryStampOrderChecking temporaryStampOrderChecking;

	@Inject
	private TemporaryDoubleStampChecking temporaryDoubleStampChecking;

	@Inject
	private GoingOutStampLeakageChecking goingOutStampLeakageChecking;

	@Inject
	private GoingOutStampOrderChecking goingOutStampOrderChecking;

	@Inject
	private BreakTimeStampLeakageChecking breakTimeStampLeakageChecking;

	@Inject
	private BreakTimeStampIncorrectOrderChecking breakTimeStampIncorrectOrderChecking;

	@Inject
	private ReflectEmbossingDomainService ReflectEmbossingDomainService;

	@Inject
	private StampDomainService stampDomainService;

	@Inject
	private WorkingConditionItemService workingConditionItemService;

	@Inject
	private ExitStampCheck exitStampCheck;

	@Inject
	private ExitStampIncorrectOrderCheck exitStampIncorrectOrderCheck;

	@Inject
	private PClogOnOffLackOfStamp pClogOnOffLackOfStamp;

	@Inject
	private PCLogOnOffIncorrectOrderCheck pCLogOnOffIncorrectOrderCheck;
	
	@Inject
	private WorkTimeSettingService workTimeSettingService;

	@Override
	public ReflectStampOutput reflectStampInfo(String companyID, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, String empCalAndSumExecLogID,
			ExecutionType reCreateAttr, BreakTimeOfDailyPerformance breakTimeOfDailyPerformance) {

		WorkTypeCode workTypeCode = workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode();

		WorkTimeCode workTimeCode = workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTimeCode();

		// 打刻反映時の出勤休日扱いチェック - 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = basicScheduleService.checkWorkDay(workTypeCode.v());

		// result data
		StampReflectRangeOutput stampReflectRangeOutput = new StampReflectRangeOutput();

		// after check 打刻反映時の出勤休日扱いチェック
		// 終了状態：休日扱い - 休日系の打刻範囲を取得する
		if (workStyle == WorkStyle.ONE_DAY_REST) {
			stampReflectRangeOutput = this.holidayStampRange(companyID, workInfoOfDailyPerformance, processingDate,
					employeeID);
		}
		// 終了状態：出勤扱い - 出勤系の打刻範囲を取得する
		else {
			stampReflectRangeOutput = this.attendanSytemStampRange(workTimeCode, companyID, workInfoOfDailyPerformance);
		}

		// 打刻取得範囲の作成
		createStampReflectRangeInaDay(stampReflectRangeOutput);

		// 外出の打刻反映範囲を取得する
		stampReflectRangeOutput.setGoOut(stampReflectRangeOutput.getStampRange());
		stampReflectRangeOutput.setTemporary(stampReflectRangeOutput.getStampRange());

		// 打刻を反映する - Dung code
		List<StampItem> lstStampItem = this.stampDomainService.handleData(stampReflectRangeOutput, reCreateAttr,
				empCalAndSumExecLogID, processingDate, employeeID, companyID);
		ReflectStampOutput reflectStamp = null;
		if (lstStampItem == null) {
			reflectStamp = null;
		}

		if (lstStampItem != null && !lstStampItem.isEmpty()) {
			reflectStamp = this.ReflectEmbossingDomainService.reflectStamp(workInfoOfDailyPerformance,
					timeLeavingOfDailyPerformance, lstStampItem, stampReflectRangeOutput, processingDate, employeeID,
					companyID);
			// エラーチェック
			this.errorCheck(companyID, employeeID, processingDate, workInfoOfDailyPerformance,
					reflectStamp.getTimeLeavingOfDailyPerformance(), reflectStamp.getOutingTimeOfDailyPerformance(),
					reflectStamp.getTemporaryTimeOfDailyPerformance(), breakTimeOfDailyPerformance);
		}

		if (lstStampItem != null && lstStampItem.isEmpty()) {
			reflectStamp = new ReflectStampOutput();
			reflectStamp.setTimeLeavingOfDailyPerformance(timeLeavingOfDailyPerformance);
			// reflectStamp.setLstStamp(lstStampItem);
			// reflectStamp.setOutingTimeOfDailyPerformance(outingTimeOfDailyPerformance);
			// reflectStamp.setTemporaryTimeOfDailyPerformance(temporaryTimeOfDailyPerformance);
		}

		return reflectStamp;
	}

	/*
	 * 1日分の打刻反映範囲を取得
	 */
	private StampReflectRangeOutput attendanSytemStampRange(WorkTimeCode workTimeCode, String companyID,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {

		StampReflectRangeOutput stampReflectRangeOutput = null;

		// ドメインモデル「就業時間帯の設定」を取得
		// TODO - wait new wave
		// Optional<WorkTimeSetting> workTimeSetting =
		// workTimeSettingRepository.findByCode(companyID, workTimeCode.v());

		// 1日分の打刻反映範囲を取得
		// if (workTimeSetting.isPresent()) {
		if (true) {
//			
//		// ドメインモデル「就業時間帯の設定」を取得
//		Optional<WorkTimeSetting> workTimeSetting = workTimeSettingRepository.findByCode(companyID, workTimeCode.v());
//
//		// 1日分の打刻反映範囲を取得
//		if (workTimeSetting.isPresent()) {

			stampReflectRangeOutput = new StampReflectRangeOutput();

//			// 打刻反映時間帯を取得する
//			// TODO
//			 List<StampReflectTimezone> stampReflectTimezones = this.workTimeSettingService.getStampReflectTimezone(companyID, workTimeCode.v()); 
//
//			if (!stampReflectTimezones.isEmpty()) {
//				List<StampReflectTimezoneOutput> stampReflectRangeOutputs = new ArrayList<>();
//				stampReflectTimezones.stream().forEach(timezone -> {
//					StampReflectTimezoneOutput stampReflectTimezoneOutput = new StampReflectTimezoneOutput(
//							timezone.getWorkNo(), timezone.getClassification(), timezone.getEndTime(),
//							timezone.getStartTime());
//					stampReflectRangeOutputs.add(stampReflectTimezoneOutput);
//				});
//				stampReflectRangeOutput.setLstStampReflectTimezone(stampReflectRangeOutputs);
//			} else {
//				return stampReflectRangeOutput;
//			}
				// fake data
				List<StampReflectTimezoneOutput> lstStampReflectTimezone = new ArrayList<>();
				StampReflectTimezoneOutput stampReflectTimezoneOutput1 = new StampReflectTimezoneOutput(new WorkNo(1),
						GoLeavingWorkAtr.GO_WORK, new TimeWithDayAttr(720), new TimeWithDayAttr(480));
				StampReflectTimezoneOutput stampReflectTimezoneOutput2 = new StampReflectTimezoneOutput(new WorkNo(1),
						GoLeavingWorkAtr.LEAVING_WORK, new TimeWithDayAttr(1320), new TimeWithDayAttr(1020));
				lstStampReflectTimezone.add(stampReflectTimezoneOutput1);
				lstStampReflectTimezone.add(stampReflectTimezoneOutput2);
				stampReflectRangeOutput.setLstStampReflectTimezone(lstStampReflectTimezone);
		}
		return stampReflectRangeOutput;
	}

	/*
	 * 休日系の打刻範囲を取得する
	 */
	private StampReflectRangeOutput holidayStampRange(String companyID,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance, GeneralDate processingDate, String employeeId) {

		StampReflectOnHolidayOutPut stampReflectOnHolidayOutPut = new StampReflectOnHolidayOutPut();

		// get workTimeCode
		WorkTimeCode workTimeCode = workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTimeCode();

		// 当日の打刻反映範囲を取得 - 当日の就業時間帯コードを取得
		// start get data of this day
		if (workTimeCode != null) {
			// use workTypeCode
			WorkTypeCode workTypeCode = workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode();
			// 休日出勤時の勤務情報を取得する - new wave
			Optional<SingleDaySchedule> singleDaySchedule = workingConditionItemService
					.getHolidayWorkSchedule(companyID, employeeId, processingDate, workTypeCode.v());
			if (!singleDaySchedule.isPresent()
					|| (singleDaySchedule.isPresent() && !singleDaySchedule.get().getWorkTimeCode().isPresent())) {
				workTimeCode = null;
			} else {
				workTimeCode = new WorkTimeCode(singleDaySchedule.get().getWorkTimeCode().get().v());
			}
		}

		// 当日の打刻反映範囲を取得 - end get data of this day
		StampReflectRangeOutput stampReflectRangeOutput = this.attendanSytemStampRange(workTimeCode, companyID,
				workInfoOfDailyPerformance);
		// set data of this day
		stampReflectOnHolidayOutPut.setStampReflectThisDay(stampReflectRangeOutput);

		// 前々日の打刻反映範囲を取得 - get data of two day before
		GeneralDate twoDayBeforeDate = processingDate.addDays(-2);
		StampReflectRangeOutput stampTwoDay = this.calculationStamp(workInfoOfDailyPerformance, twoDayBeforeDate,
				employeeId, companyID, 1);
		// set data of two day before
		stampReflectOnHolidayOutPut.setStampReflectTwoDayBefore(stampTwoDay);

		// 前日の打刻反映範囲を取得 - get data of previous day
		GeneralDate previousDay = processingDate.addDays(-1);
		StampReflectRangeOutput stampPreviousDay = this.calculationStamp(workInfoOfDailyPerformance, previousDay,
				employeeId, companyID, 2);
		stampReflectOnHolidayOutPut.setStampReflectPreviousDay(stampPreviousDay);

		// 翌日の打刻反映範囲を取得 - get data of next day
		GeneralDate nextDay = processingDate.addDays(1);
		StampReflectRangeOutput stampNextDay = this.calculationStamp(workInfoOfDailyPerformance, nextDay, employeeId,
				companyID, 3);
		stampReflectOnHolidayOutPut.setStampReflectNextDay(stampNextDay);

		if (stampTwoDay != null) {
			// 前々日との関係から打刻反映範囲を補正
			this.stampReflectCorrection(stampTwoDay);
		}

		if (stampPreviousDay != null) {
			// 前日との関係から打刻範囲範囲を補正
			this.stampReflectCorrection(stampPreviousDay);
		}

		if (stampNextDay != null) {
			// 翌日との関係から打刻反映範囲を補正
			this.nextDayCorrection(stampNextDay);
		}

		return stampReflectRangeOutput;
	}

	/*
	 * function common for calculation for two day before, previous day, next
	 * day
	 */
	private StampReflectRangeOutput calculationStamp(WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
			GeneralDate processingDate, String employeeId, String companyID, int dayAttr) {

		/**
		 * dayAttr 1 : two day before , 2 : previous day 3 : next day
		 */

		// 打刻反映時の出勤休日扱いチェック
		// 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = basicScheduleService
				.checkWorkDay(workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode().v());

		StampReflectRangeOutput stampReflectRangeOutput = null;

		if (workStyle != WorkStyle.ONE_DAY_REST) {
			// get workTimeCode of processingDate
			String worktimeCode = this.stampRangeCheckWorkRecord(processingDate, employeeId);
			if (worktimeCode != null) {
				// 1日分の打刻反映範囲を取得
				stampReflectRangeOutput = this.attendanSytemStampRange(new WorkTimeCode(worktimeCode), companyID,
						workInfoOfDailyPerformance);

				if (stampReflectRangeOutput != null
						&& !stampReflectRangeOutput.getLstStampReflectTimezone().isEmpty()) {
					// loop list 出退勤
					stampReflectRangeOutput.getLstStampReflectTimezone().stream().forEach(timezone -> {
						if (dayAttr == 1) {
							// 開始時刻をパラメータ「ズラす時間」だけズラす
							TimeWithDayAttr startTimeTwoDay = new TimeWithDayAttr(timezone.getStartTime().v() - 2880);
							if (startTimeTwoDay.v() < (-720)) {
								startTimeTwoDay = new TimeWithDayAttr(-720);
							}
							// 終了時刻をパラメータ「ズラす時間」だけズラす
							TimeWithDayAttr endTimeTwoDay = new TimeWithDayAttr(timezone.getEndTime().v() - 2880);
							if (endTimeTwoDay.v() < (-720)) {
								endTimeTwoDay = new TimeWithDayAttr(-720);
							}
							timezone.setStartTime(startTimeTwoDay);
							timezone.setEndTime(endTimeTwoDay);

						} else if (dayAttr == 2) {
							TimeWithDayAttr startTimePreviousDay = new TimeWithDayAttr(
									timezone.getStartTime().v() - 1440);
							if (startTimePreviousDay.v() < (-720)) {
								startTimePreviousDay = new TimeWithDayAttr(-720);
							}
							TimeWithDayAttr endTimePreviousDay = new TimeWithDayAttr(timezone.getEndTime().v() - 1440);
							if (endTimePreviousDay.v() < (-720)) {
								endTimePreviousDay = new TimeWithDayAttr(-720);
							}
							timezone.setStartTime(startTimePreviousDay);
							timezone.setEndTime(endTimePreviousDay);
						} else if (dayAttr == 3) {
							TimeWithDayAttr startTimeNextDay = new TimeWithDayAttr(timezone.getStartTime().v() + 1440);
							if (startTimeNextDay.v() > 4319) {
								startTimeNextDay = new TimeWithDayAttr(4319);
							}
							TimeWithDayAttr endTimeNextDay = new TimeWithDayAttr(timezone.getEndTime().v() + 1440);
							if (endTimeNextDay.v() > 4319) {
								endTimeNextDay = new TimeWithDayAttr(4319);
							}
							timezone.setStartTime(startTimeNextDay);
							timezone.setEndTime(endTimeNextDay);
						}
					});
				}

			}

		}

		return stampReflectRangeOutput;
	}

	/*
	 * 打刻範囲チェック用勤務実績取得
	 */
	public String stampRangeCheckWorkRecord(GeneralDate processingDate, String employeeId) {
		// ドメインモデル「日別実績の勤務情報」を取得
		Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance = this.workInformationRepository
				.find(employeeId, processingDate);

		if (workInfoOfDailyPerformance.isPresent()) {
			// Imported(就業.勤務実績)「勤務予定基本情報」を取得する
			Optional<BasicScheduleSidDto> basicScheduleHasData = this.basicScheduleAdapter
					.findAllBasicSchedule(employeeId, processingDate);
			if (basicScheduleHasData.isPresent()) {
				return basicScheduleHasData.get().getWorkTimeCode();
			} else {
				return null;
			}
		} else {
			if (!(workInfoOfDailyPerformance.get().getRecordWorkInformation().getWorkTimeCode() == null)) {
				return workInfoOfDailyPerformance.get().getRecordWorkInformation().getWorkTimeCode().v();
			} else {
				if (!(workInfoOfDailyPerformance.get().getScheduleWorkInformation().getWorkTimeCode() == null)) {
					return workInfoOfDailyPerformance.get().getScheduleWorkInformation().getWorkTimeCode().v();
				} else {
					return null;
				}
			}
		}
	}

	/*
	 * Stamp Reflect Correction
	 */
	private void stampReflectCorrection(StampReflectRangeOutput stampReflectRangeOutput) {

		if (!stampReflectRangeOutput.getLstStampReflectTimezone().isEmpty()) {
			// 前々日の打刻反映範囲から一番遅い時刻を取得
			TimeWithDayAttr lastestTimeGoWork = getLastestTimeFromListStampReflect(GoLeavingWorkAtr.GO_WORK,
					stampReflectRangeOutput);
			// 出勤の打刻反映範囲を補正
			deleteBeforeDesignatedTimeFromStampReflect(GoLeavingWorkAtr.GO_WORK, lastestTimeGoWork,
					stampReflectRangeOutput);
			// 前日の打刻反映範囲から一番遅い時刻を取得
			TimeWithDayAttr lastestTimeLeavingWork = getLastestTimeFromListStampReflect(GoLeavingWorkAtr.LEAVING_WORK,
					stampReflectRangeOutput);
			// 退勤の打刻反映範囲を補正
			deleteBeforeDesignatedTimeFromStampReflect(GoLeavingWorkAtr.LEAVING_WORK, lastestTimeLeavingWork,
					stampReflectRangeOutput);
		}
	};

	/*
	 * next day correction
	 */
	private void nextDayCorrection(StampReflectRangeOutput stampReflectRangeOutput) {

		if (!stampReflectRangeOutput.getLstStampReflectTimezone().isEmpty()) {
			// 翌日の打刻反映範囲から一番早い時刻を取得
			TimeWithDayAttr earliestTimeGoWork = getEarliestTimeFromStampReflect(GoLeavingWorkAtr.GO_WORK,
					stampReflectRangeOutput);

			// 出勤の打刻反映範囲を補正
			deleteAfterDesignatedTimeFromStampReflect(GoLeavingWorkAtr.GO_WORK, earliestTimeGoWork,
					stampReflectRangeOutput);

			// 翌日の打刻反映範囲から一番早い時刻を取得
			TimeWithDayAttr earliestLeavingWork = getEarliestTimeFromStampReflect(GoLeavingWorkAtr.LEAVING_WORK,
					stampReflectRangeOutput);

			// 退勤の打刻反映範囲を補正
			deleteAfterDesignatedTimeFromStampReflect(GoLeavingWorkAtr.LEAVING_WORK, earliestLeavingWork,
					stampReflectRangeOutput);
		}
	}

	/*
	 * 1日の打刻反映範囲を作成
	 */
	private void createStampReflectRangeInaDay(StampReflectRangeOutput stampReflectRangeOutput) {

		// 一番早い時刻を取得
		TimeWithDayAttr earliestTime = getEarliestTimeFromStampReflect(null, stampReflectRangeOutput);

		// 一番遅い時刻を取得
		TimeWithDayAttr lastestTime = getLastestTimeFromListStampReflect(null, stampReflectRangeOutput);

		TimeZoneOutput temporary = new TimeZoneOutput(earliestTime, lastestTime);

		stampReflectRangeOutput.setStampRange(temporary);
	}

	/*
	 * エラーチェック
	 */
	private void errorCheck(String companyID, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance,
			OutingTimeOfDailyPerformance outingTimeOfDailyPerformance,
			TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance,
			BreakTimeOfDailyPerformance breakTimeOfDailyPerformance) {

		// 出勤系打刻漏れをチェックする
		this.lackOfStamping.lackOfStamping(companyID, employeeID, processingDate, workInfoOfDailyPerformance,
				timeLeavingOfDailyPerformance);

		// 出勤系打刻順序不正をチェックする
		this.stampIncorrectOrderAlgorithm.stampIncorrectOrder(companyID, employeeID, processingDate,
				timeLeavingOfDailyPerformance);

		// 出勤系二重打刻をチェックする
		this.doubleStampAlgorithm.doubleStamp(companyID, employeeID, processingDate, timeLeavingOfDailyPerformance);

		// TODO: ドメインモデル「臨時勤務管理」を取得する
		// get data from domain : 臨時勤務管理, this domain, now haven't created
		// fake data :
		UseAtr useAtr = UseAtr.USE;
		if (useAtr == UseAtr.USE) {

			// 臨時系打刻漏れをチェックする
			// missingOfTemporaryStampChecking.missingOfTemporaryStampChecking(companyID,
			// employeeID, processingDate, temporaryTimeOfDailyPerformance);

			// 臨時系打刻順序不正をチェックする
			// temporaryStampOrderChecking.temporaryStampOrderChecking(employeeID,
			// companyID, processingDate, temporaryTimeOfDailyPerformance);

			// 臨時系二重打刻をチェックする
			// temporaryDoubleStampChecking.temporaryDoubleStampChecking(companyID,
			// employeeID, processingDate, temporaryTimeOfDailyPerformance);
		}
		// 外出系打刻漏れをチェックする
		// goingOutStampLeakageChecking.goingOutStampLeakageChecking(companyID,
		// employeeID, processingDate, outingTimeOfDailyPerformance);

		// 外出系打刻順序不正をチェックする
		// goingOutStampOrderChecking.goingOutStampOrderChecking(companyID,
		// employeeID, processingDate, outingTimeOfDailyPerformance,
		// timeLeavingOfDailyPerformance, temporaryTimeOfDailyPerformance);

		// 休憩系打刻漏れをチェックする
//		 breakTimeStampLeakageChecking.breakTimeStampLeakageChecking(companyID,
//		 employeeID, processingDate, breakTimeOfDailyPerformance);

		// 休憩系打刻順序不正をチェックする
//		 breakTimeStampIncorrectOrderChecking.breakTimeStampIncorrectOrderChecking(companyID,
//		 employeeID, processingDate, breakTimeOfDailyPerformance);
		UseAtr useAtr2 = UseAtr.USE;
		if (useAtr2 == UseAtr.USE) {
			// 入退門の打刻漏れをチェックする
			// TODO - has not attendanceLeavingGateOfDaily
			// exitStampCheck.exitStampCheck(companyID, employeeID,
			// processingDate, attendanceLeavingGateOfDaily,
			// workInfoOfDailyPerformance);
			// 入退門の打刻順序不正をチェックする
			// TODO - has not attendanceLeavingGateOfDaily
			// exitStampIncorrectOrderCheck.exitStampIncorrectOrderCheck(companyID,
			// employeeID, processingDate, attendanceLeavingGateOfDaily,
			// timeLeavingOfDailyPerformance);
		}

		UseAtr useAtr3 = UseAtr.USE;
		if (useAtr3 == UseAtr.USE) {
			// PCログオンログオフの打刻漏れをチェックする
			// this.pClogOnOffLackOfStamp.pClogOnOffLackOfStamp(companyID,
			// employeeID, processingDate, pCLogOnInfoOfDaily,
			// workInfoOfDailyPerformance);

			// PCログオンログオフの打刻順序不正をチェックする
			// this.pCLogOnOffIncorrectOrderCheck.pCLogOnOffIncorrectOrderCheck(companyID,
			// employeeID, processingDate, pCLogOnInfoOfDaily,
			// timeLeavingOfDailyPerformance);
		}
	}

	/*
	 * 打刻反映範囲のListから一番早い時刻を取得
	 */
	public TimeWithDayAttr getEarliestTimeFromStampReflect(GoLeavingWorkAtr goLeavingWorkAtr,
			StampReflectRangeOutput stampReflectRangeOutput) {

		List<StampReflectTimezoneOutput> stampReflectTimezoneList = new ArrayList<>();

		if (goLeavingWorkAtr == null) {
			stampReflectTimezoneList = stampReflectRangeOutput.getLstStampReflectTimezone();
		} else {
			stampReflectTimezoneList = stampReflectRangeOutput.getLstStampReflectTimezone().stream()
					.filter(item -> item.getClassification() == goLeavingWorkAtr).collect(Collectors.toList());
		}

		TimeWithDayAttr earliestTime = null;
		for (StampReflectTimezoneOutput stampReflect : stampReflectTimezoneList) {
			if ((earliestTime == null) || stampReflect.getStartTime().lessThan(earliestTime)) {
				earliestTime = new TimeWithDayAttr(stampReflect.getStartTime().v());
			}
		}
		;

		return earliestTime;
	}

	/*
	 * 打刻反映範囲から指定時刻以降を削除
	 */
	public void deleteAfterDesignatedTimeFromStampReflect(GoLeavingWorkAtr goLeavingWorkAtr,
			TimeWithDayAttr earliestTime, StampReflectRangeOutput stampReflectRangeOutput) {
		List<StampReflectTimezoneOutput> list = stampReflectRangeOutput.getLstStampReflectTimezone().stream()
				.filter(item -> item.getClassification() == goLeavingWorkAtr).collect(Collectors.toList());

		for (StampReflectTimezoneOutput timeZone : list) {
			if (timeZone.getStartTime().lessThan(earliestTime) && timeZone.getEndTime().greaterThan(earliestTime)) {
				timeZone.setEndTime(new TimeWithDayAttr(earliestTime.v() - 1));
			} else if (timeZone.getStartTime().greaterThan(earliestTime)) {
				stampReflectRangeOutput = new StampReflectRangeOutput();
			}
		}
	}

	/*
	 * 打刻反映範囲のListから一番遅い時刻を取得
	 */
	public TimeWithDayAttr getLastestTimeFromListStampReflect(GoLeavingWorkAtr goLeavingWorkAtr,
			StampReflectRangeOutput stampReflectRangeOutput) {
		List<StampReflectTimezoneOutput> stampReflectTimezoneList = new ArrayList<>();
		if (goLeavingWorkAtr == null) {
			stampReflectTimezoneList = stampReflectRangeOutput.getLstStampReflectTimezone();
		} else {
			stampReflectTimezoneList = stampReflectRangeOutput.getLstStampReflectTimezone().stream()
					.filter(item -> item.getClassification() == goLeavingWorkAtr).collect(Collectors.toList());
		}

		TimeWithDayAttr lastestTime = null;
		for (StampReflectTimezoneOutput stampReflect : stampReflectTimezoneList) {
			if ((lastestTime == null) || stampReflect.getEndTime().greaterThan(lastestTime)) {
				lastestTime = new TimeWithDayAttr(stampReflect.getEndTime().v());
			}
		}
		;

		return lastestTime;
	}

	/*
	 * 打刻反映範囲から指定時刻以前を削除
	 */
	public void deleteBeforeDesignatedTimeFromStampReflect(GoLeavingWorkAtr goLeavingWorkAtr,
			TimeWithDayAttr lastestTime, StampReflectRangeOutput stampReflectRangeOutput) {

		List<StampReflectTimezoneOutput> list = stampReflectRangeOutput.getLstStampReflectTimezone().stream()
				.filter(item -> item.getClassification() == goLeavingWorkAtr).collect(Collectors.toList());

		for (StampReflectTimezoneOutput timeZone : list) {
			if (timeZone.getStartTime().lessThan(lastestTime) && timeZone.getEndTime().greaterThan(lastestTime)) {
				timeZone.setStartTime(new TimeWithDayAttr(lastestTime.v() + 1));
			} else if (timeZone.getEndTime().lessThan(lastestTime)) {
				stampReflectRangeOutput = new StampReflectRangeOutput();
			}
		}
	}

}
