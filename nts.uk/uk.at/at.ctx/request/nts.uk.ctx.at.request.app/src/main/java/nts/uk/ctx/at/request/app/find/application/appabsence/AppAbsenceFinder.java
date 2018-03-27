package nts.uk.ctx.at.request.app.find.application.appabsence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.AppAbsenceDto;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenAFinder;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AbsenceWorkType;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.application.appabsence.service.four.AppAbsenceFourProcess;
import nts.uk.ctx.at.request.dom.application.appabsence.service.three.AppAbsenceThreeProcess;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InitMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.BeforePreBootMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailScreenInitModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailedScreenPreBootModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.CollectApprovalRootPatternService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.StartupErrorCheckService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.output.ApprovalRootPattern;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReasonRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AppAbsenceFinder {
	
	final static String DATE_FORMAT = "yyyy/MM/dd";
	final static String ZEZO_TIME = "00:00";
	final static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
	final static String SPACE = " ";
	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	@Inject
	private CollectApprovalRootPatternService collectApprovalRootPatternService;
	@Inject
	private StartupErrorCheckService startupErrorCheckService;
	@Inject
	private CollectAchievement collectAchievement;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private HdAppSetRepository hdAppSetRepository;
	@Inject
	private ApplicationReasonRepository applicationReasonRepository;
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private AppAbsenceThreeProcess appAbsenceThreeProcess;
	@Inject
	private AppAbsenceFourProcess appAbsenceFourProcess;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private BasicScheduleService basicScheduleService;
	@Inject
	private PredetemineTimeSettingRepository predTimeRepository;
	@Inject
	private AppAbsenceRepository appAbsenceRepository;
	@Inject
	private HolidayShipmentScreenAFinder holidayShipmentScreenAFinder;
	@Inject 
	private BeforePreBootMode beforePreBootMode;
	@Inject 
	private InitMode initMode;
	
	public AppAbsenceDto getAppForLeave(String appDate, String employeeID){
		
		AppAbsenceDto result = new AppAbsenceDto();
		boolean checkCaller = true;
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
			checkCaller = false;
		}
		String companyID = AppContexts.user().companyId();
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		result.setManualSendMailFlg(
				appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value == 1 ? true : false);
		// アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
		ApprovalRootPattern approvalRootPattern = collectApprovalRootPatternService.getApprovalRootPatternService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appCommonSettingOutput.generalDate, "", true);
		// アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する
		startupErrorCheckService.startupErrorCheck(appCommonSettingOutput.generalDate,
				ApplicationType.ABSENCE_APPLICATION.value, approvalRootPattern.getApprovalRootContentImport());
		// 1-1.起動時のエラーチェック
		List<Integer> holidayAppTypes = new ArrayList<>();
		
		if(!CollectionUtil.isEmpty(appCommonSettingOutput.appEmploymentWorkType)){
			for(AppEmploymentSetting appEmploymentSetting : appCommonSettingOutput.appEmploymentWorkType){
				if(!appEmploymentSetting.getHolidayTypeUseFlg()){
					holidayAppTypes.add(appEmploymentSetting.getHolidayOrPauseType());
				}
			}
		}
		if(CollectionUtil.isEmpty(holidayAppTypes)){
			throw new BusinessException("Msg_473");
		}
		result.setHolidayAppTypes(holidayAppTypes);
		if(appDate != null){
			 //13.実績の取得
			 AchievementOutput achievementOutput = collectAchievement.getAchievement(companyID, employeeID,  GeneralDate.fromString(appDate, DATE_FORMAT));
			
		}
		// 1-2.初期データの取得
		getData(appDate,employeeID,companyID,result,checkCaller,appCommonSettingOutput);
		// get employeeName, employeeID
		String employeeName = "";
		employeeName = employeeAdapter.getEmployeeName(employeeID);
		result.setEmployeeID(employeeID);
		result.setEmployeeName(employeeName);
		return result;
	}
	
	/**
	 * INIT KAF006B
	 * @param appID
	 * @return
	 */
	public AppAbsenceDto getByAppID(String appID){
		AppAbsenceDto result = new AppAbsenceDto();
		String companyID = AppContexts.user().companyId();
		// 14-1.詳細画面起動前申請共通設定を取得する
		
		Optional<AppAbsence> opAppAbsence = this.appAbsenceRepository.getAbsenceByAppId(companyID, appID);
		if(!opAppAbsence.isPresent()){
			throw new BusinessException("Msg_198");
		}
		AppAbsence appAbsence = opAppAbsence.get();
		// アルゴリズム「14-2.詳細画面起動前申請共通設定を取得する」を実行する
		DetailedScreenPreBootModeOutput preBootOuput = beforePreBootMode.judgmentDetailScreenMode(companyID, appAbsence.getApplication().getEmployeeID(), appID, appAbsence.getApplication().getAppDate());
		DetailScreenInitModeOutput detail = initMode.getDetailScreenInitMode(preBootOuput.getUser(), preBootOuput.getReflectPlanState().value);
		//init Mode
		result.setInitMode(detail.getOutputMode().value);
		 result = AppAbsenceDto.fromDomain(appAbsence);
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, appAbsence.getApplication().getEmployeeID(), EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appAbsence.getApplication().getAppDate());
		// ドメインモデル「休暇申請設定」を取得する(lấy dữ liệu domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// 2.勤務種類を取得する（詳細）
		List<WorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeDetails(appCommonSettingOutput.appEmploymentWorkType,
				companyID,
				appAbsence.getApplication().getEmployeeID(),
				appAbsence.getHolidayAppType().value,
				appAbsence.getAllDayHalfDayLeaveAtr().value,
				appAbsence.isHalfDayFlg());
		if(!CollectionUtil.isEmpty(workTypes)){
			if(appAbsence.getWorkTypeCode() != null){
				List<WorkType> workTypeCodeInWorkTypes = workTypes.stream().filter(x -> x.getWorkTypeCode().toString().equals(appAbsence.getWorkTypeCode())).collect(Collectors.toList());
				if(!CollectionUtil.isEmpty(workTypeCodeInWorkTypes)){
					result.setWorkTypeCode(appAbsence.getWorkTypeCode().toString());
				}else{
					// アルゴリズム「申請済み勤務種類の存在判定と取得」を実行する
					holidayShipmentScreenAFinder.appliedWorkType(companyID, workTypes, appAbsence.getWorkTypeCode().toString());
				}
			}
		}
		List<AbsenceWorkType> absenceWorkTypes = new ArrayList<>();
		for(WorkType workType : workTypes){
			AbsenceWorkType absenceWorkType = new AbsenceWorkType(workType.getWorkTypeCode().toString(), workType.getWorkTypeCode().toString() +"　　" + workType.getName().toString());
			absenceWorkTypes.add(absenceWorkType);
		}
		// display list work Type
		result.setWorkTypes(absenceWorkTypes);
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, appAbsence.getApplication().getEmployeeID(),appAbsence.getApplication().getAppDate());
		result.setWorkTimeCodes(listWorkTimeCodes);
		// 1-1.起動時のエラーチェック
		List<Integer> holidayAppTypes = new ArrayList<>();

		if (!CollectionUtil.isEmpty(appCommonSettingOutput.appEmploymentWorkType)) {
			for (AppEmploymentSetting appEmploymentSetting : appCommonSettingOutput.appEmploymentWorkType) {
				if (!appEmploymentSetting.getHolidayTypeUseFlg()) {
					holidayAppTypes.add(appEmploymentSetting.getHolidayOrPauseType());
				}
			}
		}
		if (CollectionUtil.isEmpty(holidayAppTypes)) {
			throw new BusinessException("Msg_473");
		}
		result.setHolidayAppTypes(holidayAppTypes);
		getAppReason(result,companyID);
		// get employeeName, employeeID
		String employeeName = "";
		employeeName = employeeAdapter.getEmployeeName(appAbsence.getApplication().getEmployeeID());
		result.setEmployeeName(employeeName);
		// 8.休暇系の設定を取得する :TODO
		return result;
	}
	
	/**
	 * 一画面を全て表示する
	 * @param startAppDate
	 * @param endAppDate
	 * @param workType
	 * @param employeeID
	 * @param holidayType
	 * @param alldayHalfDay
	 * @return
	 */
	public AppAbsenceDto getAllDisplay(String startAppDate, boolean displayHalfDayValue,String employeeID,Integer holidayType,int alldayHalfDay){
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		AppAbsenceDto result = new AppAbsenceDto();
		
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,displayHalfDayValue);
		result.setWorkTypes(workTypes);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		if(CollectionUtil.isEmpty(workTypes)){
			result.setChangeWorkHourFlg(false);
		}else{
			result.setChangeWorkHourFlg(this.appAbsenceFourProcess.getDisplayControlWorkingHours(workTypes.get(0).getWorkTypeCode(), hdAppSet, companyID));
		}
		if(holidayType == HolidayAppType.DIGESTION_TIME.value){
			//TODO
			//9.必要な時間を算出する
		}else if(holidayType == HolidayAppType.SPECIAL_HOLIDAY.value){
			//TODO
			// 10.特別休暇の情報を取得する
		}
		return result;
	}
	/**
	 * 申請日を変更する
	 * getChangeAppDate
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeAppDate(String startAppDate,boolean displayHalfDayValue,String employeeID,String workTypeCode,Integer holidayType,int alldayHalfDay,int prePostAtr){
		AppAbsenceDto result = new AppAbsenceDto();
		ApplicationDto_New application = new ApplicationDto_New();
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
				
		// ドメインモデル「申請表示設定」．事前事後区分表示をチェックする
		if (appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().value == AppDisplayAtr.NOTDISPLAY.value) {
			result.setPrePostFlg(AppDisplayAtr.NOTDISPLAY.value == 1 ? true : false);
			// 3.事前事後の判断処理(事前事後非表示する場合)
			PrePostAtr prePostAtrJudgment = otherCommonAlgorithm.preliminaryJudgmentProcessing(
					EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class),
					appCommonSettingOutput.generalDate);
			if (prePostAtrJudgment != null) {
				prePostAtr = prePostAtrJudgment.value;
			}
		} else {
			result.setPrePostFlg(AppDisplayAtr.DISPLAY.value == 1 ? true : false);
		}
		application.setPrePostAtr(prePostAtr);
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting". Check base date of approval route )
		if(appCommonSettingOutput.applicationSetting.getBaseDateFlg().value == BaseDateFlg.APP_DATE.value){
			String workTypeCDForChange = "";
			// 1.勤務種類を取得する（新規） :TODO
			List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,displayHalfDayValue);
			if(!CollectionUtil.isEmpty(workTypes)){
				List<AbsenceWorkType> workTypeForFilter = workTypes.stream().filter(x -> x.getWorkTypeCode().equals(workTypeCode == null ? "" : workTypeCode)).collect(Collectors.toList());
				if(CollectionUtil.isEmpty(workTypeForFilter)){
					workTypeCDForChange = workTypes.get(0).getWorkTypeCode();
				}else{
					workTypeCDForChange = workTypeCode;
				}
			}
			result.setWorkTypes(workTypes);
			result.setWorkTypeCode(workTypeCDForChange);
		}
		result.setApplication(application);
		return result;
	}
	/**
	 * 終日休暇半日休暇を切替する（新規）
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeByAllDayOrHalfDay(String startAppDate,boolean displayHalfDayValue,String employeeID,Integer holidayType,int alldayHalfDay){
		AppAbsenceDto result = new AppAbsenceDto();
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		if(holidayType == null){
			return result;
		}
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,displayHalfDayValue);
		result.setWorkTypes(workTypes);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if (CollectionUtil.isEmpty(workTypes)) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(this.appAbsenceFourProcess
					.getDisplayControlWorkingHours(workTypes.get(0).getWorkTypeCode(), hdAppSet, companyID));
		}
		if(result.isChangeWorkHourFlg()){
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,appCommonSettingOutput.generalDate);
			result.setWorkTimeCodes(listWorkTimeCodes);
		}
		return result;
	}
	/**
	 * 終日休暇半日休暇を切替する（詳細）
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeByAllDayOrHalfDayForUIDetail(String startAppDate,boolean displayHalfDayValue,String employeeID,Integer holidayType,int alldayHalfDay){
		AppAbsenceDto result = new AppAbsenceDto();
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		if(holidayType == null){
			return result;
		}
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		// 2.勤務種類を取得する（詳細）
		List<WorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeDetails(appCommonSettingOutput.appEmploymentWorkType, 
				companyID, employeeID, holidayType, alldayHalfDay,displayHalfDayValue);
		List<AbsenceWorkType> absenceWorkTypes = new ArrayList<>();
		for(WorkType workType : workTypes){
			AbsenceWorkType absenceWorkType = new AbsenceWorkType(workType.getWorkTypeCode().toString(), workType.getWorkTypeCode().toString() +"　　" + workType.getName().toString());
			absenceWorkTypes.add(absenceWorkType);
		}
		result.setWorkTypes(absenceWorkTypes);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if (CollectionUtil.isEmpty(workTypes)) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(this.appAbsenceFourProcess
					.getDisplayControlWorkingHours(absenceWorkTypes.get(0).getWorkTypeCode(), hdAppSet, companyID));
		}
		if(result.isChangeWorkHourFlg()){
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,appCommonSettingOutput.generalDate);
			result.setWorkTimeCodes(listWorkTimeCodes);
		}
		return result;
	}
	/**
	 * 勤務種類組み合わせ全表示を切替する
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param holidayType
	 * @param alldayHalfDay
	 * @return
	 */
	public AppAbsenceDto getChangeDisplayHalfDay(String startAppDate,boolean displayHalfDayValue,String employeeID,String workTypeCode,Integer holidayType,int alldayHalfDay){
		AppAbsenceDto result = new AppAbsenceDto();
		if(employeeID == null){
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		if(holidayType == null){
			return result;
		}
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(
				appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
				displayHalfDayValue);
		String workTypeCDForChange ="";
		if(!CollectionUtil.isEmpty(workTypes)){
			List<AbsenceWorkType> workTypeForFilter = workTypes.stream().filter(x -> x.getWorkTypeCode().equals(workTypeCode == null ? "" : workTypeCode)).collect(Collectors.toList());
			if(CollectionUtil.isEmpty(workTypeForFilter)){
				workTypeCDForChange = workTypes.get(0).getWorkTypeCode();
			}else{
				workTypeCDForChange = workTypeCode;
			}
		}
		result.setWorkTypes(workTypes);
		result.setWorkTypeCode(workTypeCDForChange);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if (CollectionUtil.isEmpty(workTypes) || (workTypeCDForChange != null && workTypeCDForChange.equals("")) ) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(this.appAbsenceFourProcess
					.getDisplayControlWorkingHours(workTypeCDForChange, hdAppSet, companyID));
		}
		if (result.isChangeWorkHourFlg()) {
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
					appCommonSettingOutput.generalDate);

		}
		return result;
	}
	/**
	 * 勤務種類を変更する
	 * @param startAppDate
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param workTimeCode
	 * @return
	 */
	public AppAbsenceDto getChangeWorkType(String startAppDate,String employeeID,String workTypeCode,Integer holidayType,String workTimeCode){
		String companyID = AppContexts.user().companyId();
		AppAbsenceDto result = new AppAbsenceDto();
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if (workTypeCode == null) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(
					this.appAbsenceFourProcess.getDisplayControlWorkingHours(workTypeCode, hdAppSet, companyID));
		}
		if(result.isChangeWorkHourFlg()){
			// 勤務時間初期値の取得
			PrescribedTimezoneSetting prescribedTimezone = initWorktimeCode(companyID,workTypeCode,workTimeCode);
			if(prescribedTimezone != null){
				if(!CollectionUtil.isEmpty(prescribedTimezone.getLstTimezone()) && prescribedTimezone.getLstTimezone().get(0).isUsed()){
					result.setStartTime1(prescribedTimezone.getLstTimezone().get(0).getStart().v());
					result.setEndTime1(prescribedTimezone.getLstTimezone().get(0).getEnd().v());
				}
			}
		}
		if(holidayType != null && holidayType == HolidayAppType.DIGESTION_TIME.value){
			//TODO
			//9.必要な時間を算出する
		}else if(holidayType != null && holidayType == HolidayAppType.SPECIAL_HOLIDAY.value){
			//TODO
			// 10.特別休暇の情報を取得する
		}
		return result;
	}
	/**
	 * getListWorkTimeCodes
	 * @param startDate
	 * @param employeeID
	 * @return
	 */
	public List<String> getListWorkTimeCodes(String startDate, String employeeID){
		String companyID = AppContexts.user().companyId();
		//1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,startDate == null ? GeneralDate.today() : GeneralDate.fromString(startDate, DATE_FORMAT) );
		return listWorkTimeCodes;
	}
	/**
	 * getWorkingHours
	 * @param workTimeCode
	 * @param workTypeCode
	 * @param holidayType
	 * @return
	 */
	public AppAbsenceDto getWorkingHours(String workTimeCode, String workTypeCode,Integer holidayType){
		String companyID = AppContexts.user().companyId();
		AppAbsenceDto result = new AppAbsenceDto();
		if(holidayType != null && holidayType == HolidayAppType.DIGESTION_TIME.value){
			//TODO
			//9.必要な時間を算出する
		}else{
			// 勤務時間初期値の取得
			PrescribedTimezoneSetting prescribedTimezone = initWorktimeCode(companyID, workTypeCode, workTimeCode);
			if (prescribedTimezone != null) {
				if (!CollectionUtil.isEmpty(prescribedTimezone.getLstTimezone())
						&& prescribedTimezone.getLstTimezone().get(0).isUsed()) {
					result.setStartTime1(prescribedTimezone.getLstTimezone().get(0).getStart().v());
					result.setEndTime1(prescribedTimezone.getLstTimezone().get(0).getEnd().v());
				}
			}
		}
		return result;
	}
	/**
	 * 1-2.初期データの取得
	 * @param appDate
	 * @param employeeID
	 * @param companyID
	 * @param result
	 */
	private void getData(String appDate, String employeeID,String companyID,AppAbsenceDto result,boolean checkCaller,AppCommonSettingOutput appCommonSettingOutput){
		ApplicationDto_New applicationDto = new ApplicationDto_New();
		// show and hide A3_3 -> A3_6
		boolean displayPrePostFlg = false;
		if(appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().equals(AppDisplayAtr.DISPLAY)){
			displayPrePostFlg = true;
		}
		result.setPrePostFlg(displayPrePostFlg);
		// 5.事前事後区分の判断
		InitValueAtr initValueAtr = this.otherCommonAlgorithm.judgmentPrePostAtr(ApplicationType.ABSENCE_APPLICATION,
				appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT), checkCaller);
		applicationDto.setPrePostAtr(initValueAtr.value);
		// ドメインモデル「休暇申請設定」を取得する(lấy dữ liệu domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// ドメインモデル「申請定型理由」を取得する(lấy dữ liệu domain 「申請定型理由」) (hien thị A7_2)
		result.setApplication(applicationDto);
		getAppReason(result,companyID);
		//
		
	}
	/**
	 * ドメインモデル「申請定型理由」を取得する(lấy dữ liệu domain 「申請定型理由」) (hien thị A7_2)
	 * @param result
	 * @param companyID
	 */
	private void getAppReason(AppAbsenceDto result,String companyID){
		List<ApplicationReason> applicationReasons = applicationReasonRepository.getReasonByAppType(companyID,
				ApplicationType.ABSENCE_APPLICATION.value);
		List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
		for (ApplicationReason applicationReason : applicationReasons) {
			ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
					applicationReason.getReasonTemp(), applicationReason.getDefaultFlg().value);
			applicationReasonDtos.add(applicationReasonDto);
		}
		result.setApplicationReasonDtos(applicationReasonDtos);
	}
	
	/**
	 * 勤務時間初期値の取得
	 * @param companyID
	 * @param workTypeCode
	 * @param workTimeCode
	 * @return
	 */
	public PrescribedTimezoneSetting initWorktimeCode(String companyID, String workTypeCode,String workTimeCode){
		
		Optional<WorkType> WkTypeOpt = workTypeRepository.findByPK(companyID, workTypeCode);
		if (WkTypeOpt.isPresent()) {
			// アルゴリズム「1日半日出勤・1日休日系の判定」を実行する
			WorkStyle workStyle = basicScheduleService.checkWorkDay(WkTypeOpt.get().getWorkTypeCode().toString());
			if (!workStyle.equals(WorkStyle.ONE_DAY_REST)) {
				// アルゴリズム「所定時間帯を取得する」を実行する
				// 所定時間帯を取得する
				if(workTimeCode != null && workTimeCode.equals("")){
					PrescribedTimezoneSetting prescribedTzs = this.predTimeRepository
							.findByWorkTimeCode(companyID, workTimeCode).get()
							.getPrescribedTimezoneSetting();
					return prescribedTzs;
				}
			}
		}
		return null;
	}

}
