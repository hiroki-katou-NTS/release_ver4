package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
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
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

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
			ApprovalFunctionSetting approvalFunctionSetting,Optional<AppEmploymentSetting> appEmploymentSettings) {
		List<WorkTypeOvertime> result = new ArrayList<>();
		// 時刻計算利用チェック
		// アルゴリズム「社員所属雇用履歴を取得」を実行する 
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
		
		if (sEmpHistImport != null 
				&& appEmploymentSettings.isPresent()) {
			//ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する(hien thi list(申請別対象勤務種類))
			List<AppEmployWorkType> lstEmploymentWorkType = CollectionUtil.isEmpty(appEmploymentSettings.get().getListWTOAH()) ? null : 
				CollectionUtil.isEmpty(appEmploymentSettings.get().getListWTOAH().get(0).getWorkTypeList()) ? null :
					appEmploymentSettings.get().getListWTOAH().get(0).getWorkTypeList()
					.stream().map(x -> new AppEmployWorkType(companyID, employeeID, appEmploymentSettings.get().getListWTOAH().get(0).getAppType(),
							appEmploymentSettings.get().getListWTOAH().get(0).getAppType().value == 10 ? appEmploymentSettings.get().getListWTOAH().get(0).getSwingOutAtr().get().value : appEmploymentSettings.get().getListWTOAH().get(0).getAppType().value == 1 ? appEmploymentSettings.get().getListWTOAH().get(0).getHolidayAppType().get().value : 9, x))
					.collect(Collectors.toList());;
			if(!CollectionUtil.isEmpty(lstEmploymentWorkType)) {
				Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType :: getWorkTypeCode));
				List<String> workTypeCodes = new ArrayList<>();
				lstEmploymentWorkType.forEach(x -> {workTypeCodes.add(x.getWorkTypeCode());});			
				result = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream()
						.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
				if(CollectionUtil.isEmpty(result)) {
					throw new BusinessException("Msg_1567");
				}
				return result;
			}
		}
		List<Integer> allDayAtrs = allDayAtrs();
		List<Integer> halfAtrs = halfAtrs();
		result = workTypeRepository.findWorkType(companyID, 0, allDayAtrs, halfAtrs).stream()
				.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
		if(CollectionUtil.isEmpty(result)) {
			throw new BusinessException("Msg_1567");
		}
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
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,baseDate)
				.stream().map(x -> x.getWorktimeCode().v()).collect(Collectors.toList());
		
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
		return Collections.emptyList();
	}

	/**
	 * 登録処理を実行
	 */
	@Override
	public void CreateOvertime(AppOverTime domain, Application_New newApp){
		//Register application
		// error EA refactor 4
		/*appRepository.insert(newApp);*/
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
			CheckWorkingInfoResult checkResult = otherCommonAlgorithm.checkWorkingInfo(companyID,
					workTypeAndSiftType.getWorkType().getWorkTypeCode(),
					workTypeAndSiftType.getSiftType().getSiftCode());
			boolean wkTypeError = checkResult.isWkTypeError();
			boolean wkTimeError = checkResult.isWkTimeError();
			if (wkTypeError) {
				// 先頭の勤務種類を選択する
				workTypeAndSiftType.setWorkType(workTypes.get(0));
			}

			if (wkTimeError) {
				// 先頭の就業時間帯を選択する
				workTypeAndSiftType.setSiftType(siftTypes.get(0));
			}
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
				if(!CollectionUtil.isEmpty(workTypes)){
					workTypeAndSiftType.setWorkType(workTypes.get(0));
				}
			}
			
			//ドメインモデル「個人勤務日区分別勤務」．平日時．就業時間帯コードを選択する
			String wkTimeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get()
					.v().toString();
			if(siftTypes.stream().map(x -> x.getSiftCode()).collect(Collectors.toList()).contains(wkTimeCd)){
				siftType = siftTypes.stream().filter(x -> x.getSiftCode().equals(wkTimeCd)).findAny().get();
				workTypeAndSiftType.setSiftType(siftType);
			} else {
				if(!CollectionUtil.isEmpty(siftTypes)){
					workTypeAndSiftType.setSiftType(siftTypes.get(0));
				}
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
			return workTypeAndSiftType;
	}

	@Override
	public AgreementTimeStatusOfMonthly getTime36Detail(AppOvertimeDetail appOvertimeDetail) {
		if(appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitErrorTime().v() <= 0){
			return null;
		}
		return agreementTimeStatusAdapter.checkAgreementTimeStatus(
				new AttendanceTimeMonth(appOvertimeDetail.getTime36Agree().getApplicationTime().v()+appOvertimeDetail.getTime36Agree().getAgreeMonth().getActualTime().v()), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitAlarmTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getLimitErrorTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getExceptionLimitAlarmTime(), 
				appOvertimeDetail.getTime36Agree().getAgreeMonth().getExceptionLimitErrorTime());
	}
}
