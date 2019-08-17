package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementTimeStatusAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneService;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneSharedOutPut;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class OvertimeServiceImpl implements OvertimeService {
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	@Inject
	private OvertimeRepository overTimeRepository;
	@Inject
	ApplicationApprovalService_New appRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private CollectAchievement collectAchievement;
	
	@Inject
	private AgreementTimeStatusAdapter agreementTimeStatusAdapter;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private BreakTimeZoneService timeService;
	
	@Inject
	public RangeOfDayTimeZoneService rangeOfDayTimeZoneService;
	
	@Override
	public int checkOvertimeAtr(String url) {
		if(url == null){
			return OverTimeAtr.ALL.value;
		}
		switch(url){
		case "0":
			return OverTimeAtr.PREOVERTIME.value;
		case "1":
			return OverTimeAtr.REGULAROVERTIME.value;
		case "2":
			return OverTimeAtr.ALL.value;
		default:
			return OverTimeAtr.ALL.value;
			
		}
//			if(url.equals("0")){
//				return OverTimeAtr.PREOVERTIME.value;
//			}else if(url.equals("1")){
//				return OverTimeAtr.REGULAROVERTIME.value;
//			}else if(url.equals("2")){
//				return OverTimeAtr.ALL.value;
//			}
//		return 2;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService#getWorkType(java.lang.String, java.lang.String, java.util.Optional, java.util.Optional)
	 */
	@Override
	public List<WorkTypeOvertime> getWorkType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting,List<AppEmploymentSetting> appEmploymentSettings) {
		List<WorkTypeOvertime> result = new ArrayList<>();
		if(approvalFunctionSetting == null || !approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
			return result;
		}
		// 時刻計算利用チェック
		// アルゴリズム「社員所属雇用履歴を取得」を実行する 
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
		
		if (sEmpHistImport != null 
				&& !CollectionUtil.isEmpty(appEmploymentSettings)) {
			//ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する(hien thi list(申請別対象勤務種類))
			List<AppEmployWorkType> lstEmploymentWorkType = appEmploymentSettings.get(0).getLstWorkType();
			if(!CollectionUtil.isEmpty(lstEmploymentWorkType)) {
				Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType :: getWorkTypeCode));
				List<String> workTypeCodes = new ArrayList<>();
				lstEmploymentWorkType.forEach(x -> {workTypeCodes.add(x.getWorkTypeCode());});			
				result = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream()
						.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
				return result;
			}
		}
		List<Integer> allDayAtrs = allDayAtrs();
		List<Integer> halfAtrs = halfAtrs();
		result = workTypeRepository.findWorkType(companyID, 0, allDayAtrs, halfAtrs).stream()
				.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
		return result;
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
	public List<SiftType> getSiftType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting,GeneralDate baseDate) {
		List<SiftType> result = new ArrayList<>();
		if (approvalFunctionSetting != null) {
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				// 1.職場別就業時間帯を取得
				List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,baseDate);
				
				if(!CollectionUtil.isEmpty(listWorkTimeCodes)){
					List<WorkTimeSetting> workTimes =  workTimeRepository.findByCodes(companyID,listWorkTimeCodes);
					for(WorkTimeSetting workTime : workTimes){
						SiftType siftType = new SiftType();
						siftType.setSiftCode(workTime.getWorktimeCode().toString());
						siftType.setSiftName(workTime.getWorkTimeDisplayName().getWorkTimeName().toString());
						result.add(siftType);
					}
					return result;
				}
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 登録処理を実行
	 */
	@Override
	public void CreateOvertime(AppOverTime domain, Application_New newApp){
		//Register application
		appRepository.insert(newApp);
		//Register overtime
		overTimeRepository.Add(domain);
	}

	@Override
	/** 09_勤務種類就業時間帯の初期選択をセットする */
	public WorkTypeAndSiftType getWorkTypeAndSiftTypeByPersonCon(String companyID,String employeeID, GeneralDate baseDate,
			List<WorkTypeOvertime> workTypes, List<SiftType> siftTypes) {
		WorkTypeAndSiftType workTypeAndSiftType = new WorkTypeAndSiftType();
		if (baseDate != null) {
			//申請日の入力があり
			//勤務種類と就業時間帯を取得できた
			workTypeAndSiftType = getDataDateExists(companyID, employeeID, baseDate);
            if (StringUtil.isNullOrEmpty(workTypeAndSiftType.getWorkType().getWorkTypeCode(), true)
                    || StringUtil.isNullOrEmpty(workTypeAndSiftType.getSiftType().getSiftCode(), true)) {
				//取得できなかった
				workTypeAndSiftType = getDataNoDateExists(companyID, employeeID, workTypes, siftTypes);
			}
		} else {
			//申請日の入力がない
			workTypeAndSiftType = getDataNoDateExists(companyID, employeeID, workTypes, siftTypes);
		}
		
		if (workTypeAndSiftType.getWorkType() != null && workTypeAndSiftType.getSiftType() != null) {
			// 12.マスタ勤務種類、就業時間帯データをチェック
			CheckWorkingInfoResult checkResult = checkWorkingInfo(companyID,
					workTypeAndSiftType.getWorkType().getWorkTypeCode(),
					workTypeAndSiftType.getSiftType().getSiftCode());
			boolean wkTypeError = checkResult.isWkTypeError();
			boolean wkTimeError = checkResult.isWkTimeError();
			String workTypeCode = null;
			String siftCD = null;
			if (wkTypeError) {
				// 先頭の勤務種類を選択する
				workTypeAndSiftType.setWorkType(workTypes.get(0));
			}

			if (wkTimeError) {
				// 先頭の就業時間帯を選択する
				workTypeAndSiftType.setSiftType(siftTypes.get(0));
			}
			workTypeCode = workTypeAndSiftType.getWorkType().getWorkTypeCode();
			siftCD = workTypeAndSiftType.getSiftType().getSiftCode();

			// 休憩時間帯を取得する

			BreakTimeZoneSharedOutPut breakTime = getBreakTimes(companyID, workTypeCode, siftCD);
			workTypeAndSiftType.setBreakTimes(breakTime.getLstTimezone());
		}
		return workTypeAndSiftType;
	}
	
	private WorkTypeAndSiftType getDataNoDateExists(String companyID, String employeeID,
			List<WorkTypeOvertime> workTypes, List<SiftType> siftTypes) {
		WorkTypeAndSiftType workTypeAndSiftType = new WorkTypeAndSiftType();
		WorkTypeOvertime workTypeOvertime = new  WorkTypeOvertime();
		SiftType siftType = new SiftType();
		GeneralDate baseDate = GeneralDate.today();
		//ドメインモデル「個人労働条件」を取得する(lay dieu kien lao dong ca nhan(個人労働条件))
		Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeID,baseDate);
		
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getWeekdayTime() == null){
			//先頭の勤務種類を選択する
			if(!CollectionUtil.isEmpty(workTypes)){
				workTypeAndSiftType.setWorkType(workTypes.get(0));
			}
			//先頭の就業時間帯を選択する
			if(!CollectionUtil.isEmpty(siftTypes)){
				workTypeAndSiftType.setSiftType(siftTypes.get(0));
			}
		}else{
			
			String wktypeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTypeCode().get()
					.v().toString();
			if(workTypes.stream().map(x -> x.getWorkTypeCode()).collect(Collectors.toList()).contains(wktypeCd)){
				workTypeOvertime = workTypes.stream().filter(x -> x.getWorkTypeCode().equals(wktypeCd)).findAny().get();
				//ドメインモデル「個人勤務日区分別勤務」．平日時．勤務種類コードを選択する
				workTypeAndSiftType.setWorkType(workTypeOvertime);
			} else {
				//先頭の勤務種類を選択する
				workTypeAndSiftType.setWorkType(workTypes.get(0));
			}
			
			//ドメインモデル「個人勤務日区分別勤務」．平日時．就業時間帯コードを選択する
			String wkTimeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get()
					.v().toString();
			if(siftTypes.stream().map(x -> x.getSiftCode()).collect(Collectors.toList()).contains(wkTimeCd)){
				siftType = siftTypes.stream().filter(x -> x.getSiftCode().equals(wkTimeCd)).findAny().get();
				workTypeAndSiftType.setSiftType(siftType);
			} else {
				workTypeAndSiftType.setSiftType(siftTypes.get(0));
			}
		}
		return workTypeAndSiftType;
	}

	private WorkTypeAndSiftType getDataDateExists(String companyID, String employeeID, GeneralDate baseDate) {
		WorkTypeAndSiftType workTypeAndSiftType = new WorkTypeAndSiftType();
		//実績の取得
		AchievementOutput achievementOutput = collectAchievement.getAchievement(companyID, employeeID, baseDate);
			workTypeAndSiftType.setWorkType(new WorkTypeOvertime(achievementOutput.getWorkType().getWorkTypeCode(), achievementOutput.getWorkType().getName()));
			workTypeAndSiftType.setSiftType(new SiftType(achievementOutput.getWorkTime().getWorkTimeCD(), achievementOutput.getWorkTime().getWorkTimeName()));
			String workTypeCode = workTypeAndSiftType.getWorkType().getWorkTypeCode();
			String siftCD = workTypeAndSiftType.getSiftType().getSiftCode();
			BreakTimeZoneSharedOutPut breakTime = getBreakTimes(companyID, workTypeCode, siftCD);
			workTypeAndSiftType.setBreakTimes(breakTime.getLstTimezone());
			return workTypeAndSiftType;
	}

	/**
	 * 12.マスタ勤務種類、就業時間帯データをチェック
	 * @param companyID
	 * @param wkTypeCode
	 * @param wkTimeCode
	 * @return
	 */
	@Override
	public CheckWorkingInfoResult checkWorkingInfo(String companyID, String wkTypeCode, String wkTimeCode) {
		CheckWorkingInfoResult result = new CheckWorkingInfoResult();
		
		
		// 「勤務種類CD ＝＝ Null」 をチェック
		boolean isWkTypeCDNotEmpty = !StringUtil.isNullOrEmpty(wkTypeCode, true);
		if (isWkTypeCDNotEmpty) {
			String WkTypeName = null;
			Optional<WorkType> wkTypeOpt = this.workTypeRepository.findByPK(companyID, wkTypeCode);
			if (wkTypeOpt.isPresent()) {
				WkTypeName = wkTypeOpt.get().getName().v();
			}
			// 「勤務種類名称を取得する」 ＝＝NULL をチェック
			boolean isWkTypeNameEmpty = StringUtil.isNullOrEmpty(WkTypeName, true);
			if (isWkTypeNameEmpty ) {
				// 勤務種類エラーFlg ＝ True
				result.setWkTypeError(true);
			}
		}
		// 「就業時間帯CD ＝＝ NULL」をチェック
		boolean isWkTimeCDNotEmpty = !StringUtil.isNullOrEmpty(wkTimeCode, true);
		if (isWkTimeCDNotEmpty) {
			// 「就業時間帯名称を取得する」＝＝ NULL をチェック
			String WkTimeName = null;
			Optional<WorkTimeSetting> wwktimeOpt = this.workTimeRepository.findByCode(companyID, wkTimeCode);
			if (wwktimeOpt.isPresent()) {
				WkTimeName = wwktimeOpt.get().getWorkTimeDisplayName().getWorkTimeName().v();
			}
			boolean isWkTimeNameEmpty = StringUtil.isNullOrEmpty(WkTimeName, true);
			if (isWkTimeNameEmpty) {
				// 就業時間帯エラーFlg ＝ True
				result.setWkTimeError(true);
			}
		}
			
		
		return result;
	}


	@Override
	//休憩時間帯を取得する
	public BreakTimeZoneSharedOutPut getBreakTimes(String companyID, String workTypeCode, String workTimeCode) {
		// 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = this.basicService.checkWorkDay(workTypeCode);
		// 平日か休日か判断する
		WeekdayHolidayClassification weekDay = checkHolidayOrNot(workTypeCode);
		// 休憩時間帯の取得
		BreakTimeZoneSharedOutPut breakTimeZoneSharedOutPut = this.timeService.getBreakTimeZone(companyID, workTimeCode, weekDay.value, workStyle);
		Collections.sort(breakTimeZoneSharedOutPut.getLstTimezone(), new Comparator<DeductionTime>(){
			@Override
            public int compare(DeductionTime o1, DeductionTime o2) {
                return o1.getStart().v().compareTo(o2.getStart().v());
            }
		});
		return breakTimeZoneSharedOutPut;
	}
	
	// 休憩時間帯を取得する
	public List<DeductionTime> getBreakTimes(String companyID, String workTypeCode, String workTimeCode, 
			Optional<TimeWithDayAttr> opStartTime, Optional<TimeWithDayAttr> opEndTime) {
		List<DeductionTime> result = new ArrayList<>();
		// 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = this.basicService.checkWorkDay(workTypeCode);
		// 平日か休日か判断する
		WeekdayHolidayClassification weekDay = checkHolidayOrNot(workTypeCode);
		// 休憩時間帯の取得
		BreakTimeZoneSharedOutPut breakTimeZoneSharedOutPut = this.timeService.getBreakTimeZone(companyID, workTimeCode,
				weekDay.value, workStyle);
		Collections.sort(breakTimeZoneSharedOutPut.getLstTimezone(), new Comparator<DeductionTime>() {
			@Override
			public int compare(DeductionTime o1, DeductionTime o2) {
				return o1.getStart().v().compareTo(o2.getStart().v());
			}
		});
		// Input．開始時刻とInput．終了時刻をチェック
		if(!opStartTime.isPresent() || !opEndTime.isPresent()){
			return breakTimeZoneSharedOutPut.getLstTimezone();
		}
		for(DeductionTime deductionTime : breakTimeZoneSharedOutPut.getLstTimezone()){
			// 状態区分　＝　「重複の判断処理」を実行
			TimeWithDayAttr startTime = opStartTime.get();
			TimeWithDayAttr endTime = opEndTime.get();
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
		return result;
	}

	private WeekdayHolidayClassification checkHolidayOrNot(String workTypeCd) {
		String companyId =  AppContexts.user().companyId();
		Optional<WorkType> WorkTypeOptional = this.workTypeRepository.findByPK(companyId, workTypeCd);
		if (!WorkTypeOptional.isPresent()) {
			return WeekdayHolidayClassification.WEEKDAY;
		}
		// check null?
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// 休日出勤
		if (oneDay.value == 11) {
			return WeekdayHolidayClassification.HOLIDAY;
		}
		return WeekdayHolidayClassification.WEEKDAY;
	}

	@Override
	public Integer getTime36Detail(AppOvertimeDetail appOvertimeDetail) {
		if(appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitErrorTime().v() <= 0){
			return null;
		}
		return agreementTimeStatusAdapter.checkAgreementTimeStatus(
				new AttendanceTimeMonth(appOvertimeDetail.getTime36Agree().getApplicationTime().v()+appOvertimeDetail.getTime36Agree().getAgreeMonth().getActualTime().v()), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitAlarmTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitErrorTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getExceptionLimitAlarmTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getExceptionLimitErrorTime()).value;
	}
}
