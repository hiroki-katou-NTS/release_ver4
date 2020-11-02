package nts.uk.ctx.at.request.dom.application.overtime.CommonAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.CommonOvertimeHoliday;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementDetail;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ActualContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AgreeOverTimeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.TimePlaceOutput;
import nts.uk.ctx.at.request.dom.application.common.service.setting.CommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.InitWkTypeWkTimeOutput;
import nts.uk.ctx.at.request.dom.application.overtime.ApplicationTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType_Update;
import nts.uk.ctx.at.request.dom.application.overtime.ExcessState;
import nts.uk.ctx.at.request.dom.application.overtime.ExcessStateDetail;
import nts.uk.ctx.at.request.dom.application.overtime.ExcessStateMidnight;
import nts.uk.ctx.at.request.dom.application.overtime.HolidayMidNightTime;
import nts.uk.ctx.at.request.dom.application.overtime.OutDateApplication;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeShiftNight;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeAppAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeLeaveAppCommonSet;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSet;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.workrecord.dailyrecordprocess.dailycreationwork.BreakTimeZoneSetting;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.DivergenceTimeRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.DivergenceTimeRootRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.DivergenceTimeUseSet;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.AppReflectOtHdWork;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.AppReflectOtHdWorkRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.service.WorkingConditionService;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

public class CommonAlgorithmOverTimeImpl implements ICommonAlgorithmOverTime {
	
	@Inject
	private WorkingConditionService workingConditionService;
	
	@Inject
	private WorkingConditionRepository workingConditionRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private DivergenceTimeRootRepository divergenceTimeRoots;
	
	@Inject
	private OvertimeAppSetRepository overtimeAppSetRepository;
	
	@Inject
	private CommonOvertimeHoliday commonOvertimeholiday;
	
	@Inject
	private AppReflectOtHdWorkRepository appReflectOtHdWorkRepository;
	
	@Inject
	private CommonAlgorithm commonAlgorithm;
	
	@Inject
	private CommonOvertimeHoliday commonOverTime;
	
	@Inject
	public RangeOfDayTimeZoneService rangeOfDayTimeZoneService;
	
	@Override
	public QuotaOuput getOvertimeQuotaSetUse(String companyId, String employeeId, GeneralDate date,
			OvertimeAppAtr overTimeAtr) {
		// pending by domain
		// 社員の労働条件を取得する
		Optional<WorkingConditionItem> workingConditionItem = workingConditionService.findWorkConditionByEmployee(createRequireM1(), employeeId, date);
		
		
		return null;
	}
	
	
	private WorkingConditionService.RequireM1 createRequireM1() {
		return new WorkingConditionService.RequireM1() {
			
			@Override
			public Optional<WorkingConditionItem> workingConditionItem(String historyId) {
				return workingConditionItemRepository.getByHistoryId(historyId);
			}
			
			@Override
			public Optional<WorkingCondition> workingCondition(String companyId, String employeeId, GeneralDate baseDate) {
				return workingConditionRepository.getBySidAndStandardDate(companyId, employeeId, baseDate);
			}
		};
	}


	@Override
	public List<WorkType> getWorkType(Optional<AppEmploymentSet> appEmploymentSettingOp) {
		List<WorkType> workTypes = new ArrayList<>();
		Boolean isC1 = false;
		Boolean isC2 = false;
		Boolean isC3 = false;
		if (appEmploymentSettingOp.isPresent()) {
			AppEmploymentSet appEmploymentSetting = appEmploymentSettingOp.get();
			isC1 = !appEmploymentSetting.getTargetWorkTypeByAppLst().isEmpty();
			if (!isC1) {
				isC2 = appEmploymentSetting.getTargetWorkTypeByAppLst().get(0).isDisplayWorkType();
				isC3 = !appEmploymentSetting.getTargetWorkTypeByAppLst().get(0).getWorkTypeLst().isEmpty();
			}
			// 「申請別対象勤務種類」をチェックする
			if (isC1 && isC2 && isC3) {
				// ドメインモデル「勤務種類」を取得して返す
				List<WorkType> listWorkType = workTypeRepository.findByCidAndWorkTypeCodes(AppContexts.user().companyId(), appEmploymentSetting.getTargetWorkTypeByAppLst().get(0).getWorkTypeLst());
				if (!workTypes.isEmpty()) {
					workTypes = listWorkType.stream().filter(x -> x.isDeprecated()).collect(Collectors.toList());
				}
			}
			
		} 
		
		if (!(isC1 && isC2 && isC3)) {
			// ドメインモデル「勤務種類」を取得して返す
			workTypes = workTypeRepository.findWorkType(AppContexts.user().companyId(), 0, allDayAtrs(), halfAtrs()).stream()
										  .collect(Collectors.toList());
		}
		// 取得した「勤務種類」をチェック
		if (workTypes.isEmpty()) throw new BusinessException("Msg_1567");
		return workTypes;
	}
	
	/**
	 * // １日の勤務＝以下に該当するもの
	 * 　出勤、休出、振出、連続勤務
	 * @return
	 */
	private List<Integer> allDayAtrs(){
		
		List<Integer> allDayAtrs = new ArrayList<>();
		//出勤
		allDayAtrs.add(0);
		//休出
		allDayAtrs.add(11);
		//振出
		allDayAtrs.add(7);
		// 連続勤務
		allDayAtrs.add(10);
		return allDayAtrs;
	}
	/**
	 * 午前 また 午後 in (休日, 振出, 年休, 出勤, 特別休暇, 欠勤, 代休, 時間消化休暇)
	 * @return
	 */
	private List<Integer> halfAtrs(){
		List<Integer> halfAtrs = new ArrayList<>();
		// 休日
		halfAtrs.add(1);
		// 振出
		halfAtrs.add(7);
		// 年休
		halfAtrs.add(2);
		// 出勤
		halfAtrs.add(0);
		//特別休暇
		halfAtrs.add(4);
		// 欠勤
		halfAtrs.add(5);
		// 代休
		halfAtrs.add(6);
		//時間消化休暇
		halfAtrs.add(9);
		return halfAtrs;
	}


	@Override
	public InfoBaseDateOutput getInfoBaseDate(
			String companyId,
			String employeeId,
			GeneralDate date,
			OvertimeAppAtr overTimeAtr,
			List<WorkTimeSetting> workTime,
			Optional<AppEmploymentSet> appEmploymentSettingOp) {
		if (workTime.isEmpty()) throw new BusinessException("Msg_1568");
		InfoBaseDateOutput output = new InfoBaseDateOutput();
		// 指定社員の申請残業枠を取得する
		QuotaOuput quotaOuput = this.getOvertimeQuotaSetUse(companyId, employeeId, date, overTimeAtr);
		output.setQuotaOutput(quotaOuput);
		// 07_勤務種類取得
		List<WorkType> worktypes = this.getWorkType(appEmploymentSettingOp);
		output.setWorktypes(worktypes);
		// INPUT．「就業時間帯の設定<List>」をチェックする
		return output;
	}




	@Override
	public ReasonDissociationOutput getInfoNoBaseDate(
			String companyId,
			ApplicationType appType,
			Optional<OvertimeAppAtr> ovetTimeAtr,
			OvertimeLeaveAppCommonSet overtimeLeaveAppCommonSet) {
		ReasonDissociationOutput output = new ReasonDissociationOutput(); // emptyを返す
		// @「登録時の乖離時間チェック」を確認する
		if (overtimeLeaveAppCommonSet.getCheckDeviationRegister() == NotUseAtr.NOT_USE) // しない
			return output; // emptyを返す
		// する
		// INPUT．「申請種類」」をチェックする
		if (appType == ApplicationType.OVER_TIME_APPLICATION || appType == ApplicationType.HOLIDAY_WORK_APPLICATION) {// 残業申請
																														// OR
																														// 休出時間申請

			/*
			 * 
			 * INPUT．「申請種類」= 残業申請 AND INPUT．「残業申請区分」= 早出残業 ⇒ List<乖離時間NO> = 1 INPUT．「申請種類」=
			 * 残業申請 AND INPUT．「残業申請区分」= 通常残業 ⇒ List<乖離時間NO> = 2 INPUT．「申請種類」= 残業申請 AND
			 * INPUT．「残業申請区分」= 早出残業・通常残業 ⇒ List<乖離時間NO> = 1,2
			 */
			List<Integer> frames = new ArrayList<Integer>();
			if (appType == ApplicationType.HOLIDAY_WORK_APPLICATION) { // INPUT．「申請種類」= 休出時間申請 ⇒ List<乖離時間NO> = 3
				frames.add(3);
			} else if (appType == ApplicationType.HOLIDAY_WORK_APPLICATION) {
				if (ovetTimeAtr.isPresent()) {
					OvertimeAppAtr value = ovetTimeAtr.get();
					// 早出残業
					if (value == OvertimeAppAtr.EARLY_OVERTIME) {
						frames.add(1);
					} else if (value == OvertimeAppAtr.NORMAL_OVERTIME) { // 通常残業
						frames.add(2);
					} else { // 早出残業・通常残業
						frames.add(1);
						frames.add(2);
					}
				}
			}
			// [RQ694]乖離時間Listを取得する
			List<DivergenceTimeRoot> divergenceTimeRootList = divergenceTimeRoots.getList(frames);
			if (divergenceTimeRootList.isEmpty()) {
				return output;
			}
			output.setDivergenceTimeRoots(divergenceTimeRootList);
			// [RQ693]乖離理由の入力方法Listを取得する
			List<DivergenceReasonInputMethod> exportList = Collections.emptyList();
			List<DivergenceReasonInputMethod> divergenceReasonInputMethodListFilter = exportList.stream()
					.filter(x -> x.getDivergenceTimeNo() == DivergenceTimeUseSet.USE.value)
					.collect(Collectors.toList());
			if (divergenceReasonInputMethodListFilter.isEmpty()) {
				return output; // emptyを返す
			}
			output.setDivergenceReasonInputMethod(divergenceReasonInputMethodListFilter);

		} else {
			return output; // emptyを返す
		}
		// 取得した「乖離時間」と「乖離理由の入力方法」を返す
		return output;
	}


	@Override
	public InfoNoBaseDate getInfoNoBaseDate(
			String companyId,
			String employeeId,
			OvertimeAppAtr overtimeAppAtr) {
		InfoNoBaseDate output = new InfoNoBaseDate();
		// 残業申請設定を取得する
		Optional<OvertimeAppSet> overOptional = overtimeAppSetRepository.findSettingByCompanyId(companyId);
		
		OvertimeAppSet overtimeAppSet = overOptional.orElse(null);
		OvertimeLeaveAppCommonSet overtimeLeaveAppCommonSet = overtimeAppSet.getOvertimeLeaveAppCommonSet();
		// 乖離理由の表示区分を取得する
		ReasonDissociationOutput reasonDissociationOutput = this.getInfoNoBaseDate(companyId, ApplicationType.OVER_TIME_APPLICATION, Optional.ofNullable(overtimeAppAtr), overtimeLeaveAppCommonSet);
		// 01-02_時間外労働を取得
		Optional<AgreeOverTimeOutput> agreeOverTimeOutputOp = commonOvertimeholiday.getAgreementTime(companyId, employeeId, ApplicationType.OVER_TIME_APPLICATION);
		
		// ドメインモデル「残業休日出勤申請の反映」を取得する
		Optional<AppReflectOtHdWork> overTimeReflectOp = appReflectOtHdWorkRepository.findByCompanyId(companyId);
		// 取得した情報を「基準日に関係しない情報」にセットして返す
		output.setOverTimeAppSet(overtimeAppSet);
		output.setDivergenceReasonInputMethod(reasonDissociationOutput.getDivergenceReasonInputMethod());
		output.setDivergenceTimeRoot(reasonDissociationOutput.getDivergenceTimeRoots());
		if (agreeOverTimeOutputOp.isPresent()) {
			output.setAgreeOverTimeOutput(agreeOverTimeOutputOp.get());
		}
		if (overTimeReflectOp.isPresent()) {
			output.setOverTimeReflect(overTimeReflectOp.get());
		}
		
		return output;
	}


//	@Override
//	public InfoBaseDateOutput getInfoBaseDate(String companyId,
//			String employeeId,
//			GeneralDate date,
//			OvertimeAppAtr overTimeAtr,
//			List<WorkTimeSetting> workTime,
//			Optional<AppEmploymentSet> appEmploymentSetting) {
//		// TODO Auto-generated method stub
//		return null;
//	}


	@Override
	public void getInfoAppDate(String companyId,
			Optional<GeneralDate> dateOp,
			Optional<Integer> startTimeSPR,
			Optional<Integer> endTimeSPR,
			List<WorkType> workTypeLst, 
			AppDispInfoStartupOutput appDispInfoStartupOutput,
			OvertimeAppSet overtimeAppSet) {
		String employeeId = appDispInfoStartupOutput.getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getSid();
		// 09_勤務種類就業時間帯の初期選択をセットする
		Optional<AchievementDetail> archievementDetail = Optional.empty();
		if (appDispInfoStartupOutput.getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().isPresent()){
			if (!CollectionUtil.isEmpty(appDispInfoStartupOutput.getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().get())) {
				ActualContentDisplay actualContentDisplay = appDispInfoStartupOutput.getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().get().get(0);
				archievementDetail = actualContentDisplay.getOpAchievementDetail();
			}
		}
		InitWkTypeWkTimeOutput initWkTypeWkTimeOutput = commonAlgorithm.initWorkTypeWorkTime(employeeId,
				dateOp.orElse(null),
				workTypeLst,
				appDispInfoStartupOutput.getAppDispInfoWithDateOutput().getOpWorkTimeLst().orElse(Collections.emptyList()),
				archievementDetail.orElse(null));
		// 16_勤務種類・就業時間帯を選択する
		
		
	}


	
	
	@Override
	public BreakTimeZoneSetting selectWorkTypeAndTime(
			String companyId,
			WorkTypeCode workTypeCode,
			WorkTimeCode workTimeCode,
			Optional<TimeWithDayAttr> startTimeOp,
			Optional<TimeWithDayAttr> endTimeOp,
			AchievementDetail achievementDetail) {
		BreakTimeZoneSetting output = new BreakTimeZoneSetting();
		// INPUT．「実績詳細．打刻実績．休憩時間帯」をチェックする
		List<TimePlaceOutput> breakTimes = achievementDetail.getStampRecordOutput().getBreakTime();
		
		if (breakTimes.isEmpty()) {
			// 休憩時間帯を取得する
			List<DeductionTime> timeZones = commonOverTime.getBreakTimes(
					companyId,
					workTypeCode.v(),
					workTimeCode.v(),
					startTimeOp, 
					endTimeOp);
			output.setTimeZones(timeZones);
		} else {
			// 「休憩時間帯設定」<List>を作成する
			output = this.createBreakTime(startTimeOp, endTimeOp, output);
		}
		
		return output;
		
	}


	@Override
	public BreakTimeZoneSetting createBreakTime(Optional<TimeWithDayAttr> startTimeOp, Optional<TimeWithDayAttr> endTimeOp,
			BreakTimeZoneSetting breakTimeZoneSetting) {
		// Input．開始時刻とInput．終了時刻をチェック
		if (!startTimeOp.isPresent() || !endTimeOp.isPresent()) { // 開始時刻　OR　終了時刻　が無い場合
			// OUTPUT．「休憩時間帯設定」　＝　INPUT．「休憩時間帯設定」
			return breakTimeZoneSetting;
		} else {
			List<DeductionTime> result = new ArrayList<>();
			for(DeductionTime deductionTime : breakTimeZoneSetting.getTimeZones()){
				// 状態区分　＝　「重複の判断処理」を実行
				TimeWithDayAttr startTime = startTimeOp.get();
				TimeWithDayAttr endTime = endTimeOp.get();
				TimeSpanForCalc timeSpanFirstTime = new TimeSpanForCalc(endTime, startTime);
				TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(deductionTime.getEnd(), deductionTime.getStart());
				// アルゴリズム「時刻入力期間重複チェック」を実行する
				DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
						.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
				// 重複状態区分チェック
				DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
						.checkStateAtr(duplicateStateAtr);
				// 取得した状態区分をチェック
				if(duplicationStatusOfTimeZone != DuplicationStatusOfTimeZone.NON_OVERLAPPING){
					result.add(deductionTime);
				}
			}
			breakTimeZoneSetting.setTimeZones(result);
		}
		return breakTimeZoneSetting;
	}





	

}
