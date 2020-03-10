package nts.uk.ctx.at.request.app.find.application.appabsence;

import java.util.ArrayList;
import java.util.Collections;
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
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.AppForSpecLeaveDto;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.ChangeRelationShipDto;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.HolidayAppTypeName;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.ParamGetAllAppAbsence;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.SettingNo65;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenAFinder;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.TimeZoneUseDto;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.EmployeeOvertimeDto;
import nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting.DisplayReasonDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AbsenceWorkType;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeave;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeaveRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.service.AbsenceServiceProcess;
import nts.uk.ctx.at.request.dom.application.appabsence.service.CheckDispHolidayType;
import nts.uk.ctx.at.request.dom.application.appabsence.service.NumberOfRemainOutput;
import nts.uk.ctx.at.request.dom.application.appabsence.service.four.AppAbsenceFourProcess;
import nts.uk.ctx.at.request.dom.application.appabsence.service.three.AppAbsenceThreeProcess;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeInfoImport;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InitMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.BeforePreBootMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailScreenInitModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailedScreenPreBootModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.CollectApprovalRootPatternService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.StartupErrorCheckService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.output.ApprovalRootPattern;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
//import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
//import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReasonRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.UseAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.DisplayReasonRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.MaxNumberDayType;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.SpecialHolidayEvent;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.CheckWkTypeSpecHdEventOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.DateSpecHdRelationOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.MaxDaySpecHdOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.SpecialHolidayEventAlgorithm;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author loivt
 *
 */
@Stateless
public class AppAbsenceFinder {

	final static String DATE_FORMAT = "yyyy/MM/dd";
	final static String ZEZO_TIME = "00:00";
	final static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
	final static String SPACE = " ";
	@Inject
	private BeforePrelaunchAppCommonSet bfPrelaunchAppCmSet;
	@Inject
	private CollectApprovalRootPatternService colApprRootPatternSv;
	@Inject
	private StartupErrorCheckService startupErrorCheckService;
//	@Inject
//	private CollectAchievement collectAchievement;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private HdAppSetRepository hdAppSetRepository;
	@Inject
	private ApplicationReasonRepository repoAppReason;
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
	private AppAbsenceRepository repoAppAbsence;
	@Inject
	private HolidayShipmentScreenAFinder hdShipmentScreenAFinder;
	@Inject
	private BeforePreBootMode beforePreBootMode;
	@Inject
	private InitMode initMode;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	@Inject
	private AtEmployeeAdapter atEmployeeAdapter;
	@Inject
	private AbsenceServiceProcess absenseProcess;
	@Inject
	private SpecialHolidayEventAlgorithm specHdEventAlg;
	@Inject
	private AppForSpecLeaveRepository repoAppLeaveSpec;
	@Inject
	private DisplayReasonRepository displayRep;
	
	@Inject
	private WorkTypeRepository wkTypeRepo;
	/**
	 * 1.休暇申請（新規）起動前処理
	 * @param appDate
	 * @param employeeID
	 * @param employeeIDs
	 * @return
	 */
	public AppAbsenceDto getAppForLeave(String appDate, String employeeID,List<String> employeeIDs) {

		AppAbsenceDto result = new AppAbsenceDto();
		boolean checkCaller = false;
		if (employeeID == null && CollectionUtil.isEmpty(employeeIDs)) {
			employeeID = AppContexts.user().employeeId();
			checkCaller = true;
		}else if(!CollectionUtil.isEmpty(employeeIDs)){
			employeeID = employeeIDs.get(0);
			checkCaller = true;
			List<EmployeeInfoImport> employees = this.atEmployeeAdapter.getByListSID(employeeIDs);
			if(!CollectionUtil.isEmpty(employees)){
				List<EmployeeOvertimeDto> employeeOTs = new ArrayList<>();
				for(EmployeeInfoImport emp : employees){
					EmployeeOvertimeDto employeeOT = new EmployeeOvertimeDto(emp.getSid(), emp.getBussinessName());
					employeeOTs.add(employeeOT);
				}
				result.setEmployees(employeeOTs);
			}
		}
		String companyID = AppContexts.user().companyId();
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSet = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		result.setManualSendMailFlg(
				appCommonSet.applicationSetting.getManualSendMailAtr().value == 1 ? true : false);
		result.setSendMailWhenApprovalFlg(appCommonSet.appTypeDiscreteSettings.get(0).getSendMailWhenApprovalFlg().value == 1 ? true : false);
		result.setSendMailWhenRegisterFlg(appCommonSet.appTypeDiscreteSettings.get(0).getSendMailWhenRegisterFlg().value == 1 ? true : false);
		// アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
		ApprovalRootPattern approvalRootPattern = colApprRootPatternSv.getApprovalRootPatternService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appCommonSet.generalDate, "", true);
		// アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する
		startupErrorCheckService.startupErrorCheck(appCommonSet.generalDate,
				ApplicationType.ABSENCE_APPLICATION.value, approvalRootPattern.getApprovalRootContentImport());
		
		//hoatt - 2018.07.16 bug #97414
		//アルゴリズム「14.休暇種類表示チェック」を実行する
		CheckDispHolidayType checkDis = absenseProcess.checkDisplayAppHdType(companyID, employeeID, approvalRootPattern.getBaseDate());
		result.setCheckDis(checkDis);
		//----------
		if (appCommonSet.appTypeDiscreteSettings != null) {
			result.setMailFlg(!appCommonSet.appTypeDiscreteSettings.get(0).getSendMailWhenRegisterFlg()
					.equals(AppCanAtr.CAN));
		}
		// ドメインモデル「休暇申請設定」を取得する(lấy dữ liệu domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if(hdAppSet.isPresent() && hdAppSet.get().getDayDispSet()==UseAtr.USE) {
			result.setDayDispSet(UseAtr.USE.value);
		}
		
		// 1-1.起動時のエラーチェック
		List<HolidayAppTypeName> holidayAppTypes = new ArrayList<>();
		
		holidayAppTypes = this.getHolidayAppTypeName(hdAppSet,holidayAppTypes,appCommonSet);
		holidayAppTypes.sort((a, b) -> a.getHolidayAppTypeCode().compareTo(b.getHolidayAppTypeCode()));
		result.setHolidayAppTypeName(holidayAppTypes);
		//----------------
		
		
		//申請対象日のパラメータがあるかチェックする(kiểm tra có parameter 申請対象日 hay không)
		if (appDate != null) {//ある(có)
			//アルゴリズム「実績の取得」を実行する(thực hiện xử lý 「実績の取得」)
			// 13.実績の取得
//			AchievementOutput achievementOutput = collectAchievement.getAchievement(companyID, employeeID,
//					GeneralDate.fromString(appDate, DATE_FORMAT));

		}
		//アルゴリズム「初期データの取得」を実行する(thực hiện xử lý 「初期データの取得」)
		// 1-2.初期データの取得
		this.getData(appDate, employeeID, companyID, result, checkCaller, appCommonSet, approvalRootPattern.getBaseDate());
		// get employeeName, employeeID
		String employeeName = "";
		employeeName = employeeAdapter.getEmployeeName(employeeID);
		result.setEmployeeID(employeeID);
		result.setEmployeeName(employeeName);
		if(appCommonSet.applicationSetting != null){
			result.setAppReasonRequire(appCommonSet.applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED));
		}
		appCommonSet.getAppTypeDiscreteSettings();
		//hoatt - 2018.08.18 bug #98638 ver16
		//ドメインモデル「申請種類別設定」.事前事後区分を変更できる
		List<AppTypeDiscreteSetting> appTypeSetAb = appCommonSet.getAppTypeDiscreteSettings().stream()
				.filter(c -> c.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)).collect(Collectors.toList());
		if(!appTypeSetAb.isEmpty()){
			result.setPrPostChange(appTypeSetAb.get(0).getPrePostCanChangeFlg().value == 1 ? true : false);
		}
		result.setDisplayReasonDtoLst(displayRep.findDisplayReason(companyID).stream()
				.map(x -> DisplayReasonDto.fromDomain(x)).collect(Collectors.toList()));
		List<AppEmploymentSetting> appEmpSetAs = appCommonSet.getAppEmploymentWorkType().stream()
				.filter(c -> c.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)).collect(Collectors.toList());
		boolean subVacaTypeUseFlg = false;
		boolean subHdTypeUseFlg = false;
		for (AppEmploymentSetting appEmpSet : appEmpSetAs) {
			if(appEmpSet.getHolidayOrPauseType() == 7){//振休
				subVacaTypeUseFlg = appEmpSet.getHolidayTypeUseFlg();
			}
			if(appEmpSet.getHolidayOrPauseType() == 1){//代休
				subHdTypeUseFlg = appEmpSet.getHolidayTypeUseFlg();
			}
		}
		result.setSetingNo65(new SettingNo65(
				hdAppSet.isPresent() ? hdAppSet.get().getPridigCheck().value : 0,//休暇申請設定．年休より優先消化チェック区分
				checkDis.isSubVacaManage(),//振休管理設定．管理区分
				subVacaTypeUseFlg,//休暇申請対象勤務種類．休暇種類を利用しない（振休）
				checkDis.isSubHdManage(),//代休管理設定．管理区分
				subHdTypeUseFlg//休暇申請対象勤務種類．休暇種類を利用しない（代休）
				));
		return result;
	}

	/**
	 * INIT KAF006B
	 * 5.休暇申請（詳細）起動前処理
	 * @param appID
	 * @return
	 */
	public AppAbsenceDto getByAppID(String appID) {
		AppAbsenceDto result = new AppAbsenceDto();
		String companyID = AppContexts.user().companyId();
		String employeeIDLogin = AppContexts.user().employeeId();
		//アルゴリズム「詳細画面起動前申請共通設定を取得する」を実行する - 「Lấy app common setting trước khi khởi động màn hình detail」
		//14-1.詳細画面起動前申請共通設定を取得する
		Optional<AppAbsence> opAppAbsence = repoAppAbsence.getAbsenceByAppId(companyID, appID);
		if (!opAppAbsence.isPresent()) {
			throw new BusinessException("Msg_198");
		}
		AppAbsence appAbsence = opAppAbsence.get();
		result = AppAbsenceDto.fromDomain(appAbsence);
		result.setDisplayReasonDtoLst(displayRep.findDisplayReason(companyID).stream().map(x -> DisplayReasonDto.fromDomain(x)).collect(Collectors.toList()));
		//アルゴリズム「詳細画面起動前モードの判断」を実行する-「Kiểm tra mode trước khi khởi động màn hình detail」
		//アルゴリズム「14-2.詳細画面起動前申請共通設定を取得する」を実行する
		DetailedScreenPreBootModeOutput preBootOuput = beforePreBootMode.judgmentDetailScreenMode(companyID,
				employeeIDLogin, appID, appAbsence.getApplication().getAppDate());
		//アルゴリズム「詳細画面の初期モード」を実行する-[Mode Khởi tạo ban đầu của màn hình Detail]
		//14-3.詳細画面の初期モード
		DetailScreenInitModeOutput detail = initMode.getDetailScreenInitMode(preBootOuput.getUser(),
				preBootOuput.getReflectPlanState().value);
		result.setInitMode(detail.getOutputMode().value);
		//--
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSet = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, appAbsence.getApplication().getEmployeeID(), EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				appAbsence.getApplication().getAppDate());
		//---
		//HOATT - thieu
		//アルゴリズム「実績の取得」を実行する-[Lấy kết quả thực tế]
		//13.実績を取得する(getAchievement)
		//--
		// ドメインモデル「休暇申請設定」を取得する(lấy dữ liệu domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		if(hdAppSet.isPresent() && hdAppSet.get().getDayDispSet()==UseAtr.USE) {
			result.setDayDispSet(UseAtr.USE.value);
		}
		//アルゴリズム「勤務種類を取得する（詳細）」を実行する-[Lấy WorkType(detail)]
		//2.勤務種類を取得する（詳細）
		List<WorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeDetails(
				appCommonSet.appEmploymentWorkType, companyID, appAbsence.getApplication().getEmployeeID(),
				appAbsence.getHolidayAppType().value, appAbsence.getAllDayHalfDayLeaveAtr().value,
				appAbsence.isHalfDayFlg());
		//取得した勤務種類リストに「ドメインモデル「休暇申請」．勤務種類コード」が存在するかチェックする-(Check WorkTypeCode có tồn tại k?)
		boolean masterUnreg = false;
		if (!CollectionUtil.isEmpty(workTypes)) {
			if (appAbsence.getWorkTypeCode() != null) {
				List<WorkType> workTypeCodeInWorkTypes = workTypes.stream()
						.filter(x -> x.getWorkTypeCode().toString().equals(appAbsence.getWorkTypeCode() == null ? null : appAbsence.getWorkTypeCode().toString()))
						.collect(Collectors.toList());
				if (!CollectionUtil.isEmpty(workTypeCodeInWorkTypes)) {//存在する
					result.setWorkTypeCode(appAbsence.getWorkTypeCode().toString());
				} else {
					// アルゴリズム「申請済み勤務種類の存在判定と取得」を実行する - [Kiểm tra sự tồn tại  và lấy WorkType đã xin ]
					Optional<WorkType> wkTypeOpt = wkTypeRepo.findByPK(companyID, appAbsence.getWorkTypeCode().v());
					if(wkTypeOpt.isPresent()) {
						result.setWorkTypeCode(appAbsence.getWorkTypeCode().v());
					} else {
						masterUnreg = true;
						result.setWorkTypeCode(workTypes.stream().findFirst().map(x -> x.getWorkTypeCode().v()).orElse(null));
					}
					result.setMasterUnreg(masterUnreg);
					
				}
			}
		}
		List<AbsenceWorkType> absenceWorkTypes = new ArrayList<>();
		for (WorkType workType : workTypes) {
			AbsenceWorkType absenceWorkType = new AbsenceWorkType(workType.getWorkTypeCode().toString(),
					workType.getWorkTypeCode().toString() + "　　" + workType.getName().toString());
			absenceWorkTypes.add(absenceWorkType);
		}
		// display list work Type
		result.setWorkTypes(absenceWorkTypes.stream().distinct().collect(Collectors.toList()));
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		if(appAbsence.getWorkTypeCode() == null){
			result.setDisplayWorkChangeFlg(false);
		}else{
			result.setDisplayWorkChangeFlg(this.appAbsenceFourProcess
					.getDisplayControlWorkingHours(appAbsence.getWorkTypeCode().toString(), hdAppSet, companyID));
		}
		//アルゴリズム「職場別就業時間帯を取得」を実行する-[Lấy WorkTime cho từng workplace]
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID,
				appAbsence.getApplication().getEmployeeID(), appAbsence.getApplication().getAppDate());
		result.setWorkTimeCodes(listWorkTimeCodes);
		if (result.getWorkTimeCode() != null) {
			WorkTimeSetting workTime = workTimeRepository.findByCode(companyID, result.getWorkTimeCode()).isPresent()
					? workTimeRepository.findByCode(companyID, result.getWorkTimeCode()).get() : null;
			if (workTime != null) {
				result.setWorkTimeName(workTime.getWorkTimeDisplayName().getWorkTimeName().toString());
			}
		}
		// 1-1.起動時のエラーチェック
		List<HolidayAppTypeName> holidayAppTypes = new ArrayList<>();

		holidayAppTypes = getHolidayAppTypeName(hdAppSet, holidayAppTypes, appCommonSet);
		holidayAppTypes.sort((a, b) -> a.getHolidayAppTypeCode().compareTo(b.getHolidayAppTypeCode()));
		result.setHolidayAppTypeName(holidayAppTypes);
		getAppReason(result, companyID);
		// get employeeName, employeeID
		String employeeName = "";
		employeeName = employeeAdapter.getEmployeeName(appAbsence.getApplication().getEmployeeID());
		result.setEmployeeName(employeeName);
		if(appCommonSet.applicationSetting != null){
			result.setAppReasonRequire(appCommonSet.applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED));
		}
		//--
		//HOATT - thieu
		//ドメインモデル「申請定型理由」を取得する-(Lấy domain 「ApplicationFormReason」)
		//--
		// 8.休暇系の設定を取得する :TODO
		//選択する休暇種類をチェックする-(Check HolidayType-loại phép đang chọn)
		if(appAbsence.getHolidayAppType().equals(HolidayAppType.SPECIAL_HOLIDAY)){//選択する休暇種類が特別休暇(holidayType = nghỉ đặc biệt)
			//hoatt - 2018.08.08 - doi ung specHd
			//get 特別休暇申請
			Optional<AppForSpecLeave> appSpec = repoAppLeaveSpec.getAppForSpecLeaveById(companyID, appID);
			if(!appSpec.isPresent()){
				return result;
			}
			AppForSpecLeave specLeave = appSpec.get();
			result.setSpecHdDto(new AppForSpecLeaveDto(specLeave.getAppID(), specLeave.isMournerFlag(), 
					specLeave.getRelationshipCD().v(), specLeave.getRelationshipReason().v()));
			//指定する勤務種類が事象に応じた特別休暇かチェックする
			CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyID, appAbsence.getWorkTypeCode().v());
			if(!checkSpecHd.isSpecHdForEventFlag()){//その以外
				return result;
			}
			////事象に応じた特休フラグがtrue(SpecialHolidayEventFlag = true)
			SpecialHolidayEvent specHd = checkSpecHd.getSpecHdEvent().get();
			MaxDaySpecHdOutput maxDay = null;
			List<DateSpecHdRelationOutput> lstRela = new ArrayList<>();
			//取得したメインモデル「事象に対する特別休暇」．上限日数．種類をチェックする-(Check domain [SpecialHolidayEvent]. GrantDay.Type)
			if(specHd.getMaxNumberDay().equals(MaxNumberDayType.LIMIT_FIXED_DAY)){//種類が固定日数を上限とする(type = FixedDayGrant)
				//指定する特休枠の上限日数を取得する
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd, Optional.of(specLeave.getRelationshipCD().v()));
			}else{//その以外
				//指定する特休枠の続柄に対する上限日数を取得する-(get MaxDay SpecHd ByRela FrameNo)
				lstRela = specHdEventAlg.getMaxDaySpecHdByRelaFrameNo(companyID, checkSpecHd.getFrameNo().get());
				//指定する特休枠の上限日数を取得する - (get MaxDay SpecHd)
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd, lstRela.size() == 0 ? Optional.empty() : Optional.of(lstRela.get(0).getRelationCD()));
			}
			result.setSpecHdForEventFlag(checkSpecHd.isSpecHdForEventFlag());
			result.setMaxNumberDayType(specHd.getMaxNumberDay().value);
			result.setMaxDayObj(maxDay);
			result.setLstRela(lstRela);
			result.setMakeInvitation(specHd.getMakeInvitation().value);
		}
		//No.376
		//残数取得する
		//Bug#101904
		//・基準日＝システム日付
		NumberOfRemainOutput numberRemain = absenseProcess.getNumberOfRemaining(companyID, appAbsence.getApplication().getEmployeeID(),
				GeneralDate.today());
		result.setNumberRemain(numberRemain);
		return result;
	}

	/**
	 * 一画面を全て表示する
	 * 
	 * @param startAppDate
	 * @param endAppDate
	 * @param workType
	 * @param employeeID
	 * @param holidayType
	 * @param alldayHalfDay
	 * @return
	 */
	public AppAbsenceDto getAllDisplay(ParamGetAllAppAbsence param) {
		String employeeID = param.getEmployeeID();
		String startAppDate = param.getStartAppDate();
		Integer holidayType = param.getHolidayType();
		Integer alldayHalfDay = param.getAlldayHalfDay();
		boolean displayHalfDayValue = param.isDisplayHalfDayValue();
		if (employeeID == null) {
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		AppAbsenceDto result = new AppAbsenceDto();

		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSet = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(
				appCommonSet.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
				displayHalfDayValue, hdAppSet);
		result.setWorkTypes(workTypes);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		if (CollectionUtil.isEmpty(workTypes)) {
			result.setChangeWorkHourFlg(false);
			return result;
		} 
		result.setChangeWorkHourFlg(this.appAbsenceFourProcess
				.getDisplayControlWorkingHours(workTypes.get(0).getWorkTypeCode(), hdAppSet, companyID));
		
		if (holidayType == HolidayAppType.DIGESTION_TIME.value) {
			// TODO
			// 9.必要な時間を算出する
		} else if (holidayType == HolidayAppType.SPECIAL_HOLIDAY.value) {
			//hoatt - 2018.08.08 - doi ung specHd
			//指定する勤務種類が事象に応じた特別休暇かチェックする
			CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyID, workTypes.get(0).getWorkTypeCode());
			if(!checkSpecHd.isSpecHdForEventFlag()){//その以外
				return result;
			}
			//事象に応じた特休フラグがtrue(specialHolidayEventFlag = true)
			//取得したメインモデル「事象に対する特別休暇」．上限日数．種類をチェックする-(Check domain [SpecialHolidayEvent]. GrantDay.Type)
			SpecialHolidayEvent specHd = checkSpecHd.getSpecHdEvent().get();
			result.setMaxNumberDayType(specHd.getMaxNumberDay().value);
			MaxDaySpecHdOutput maxDay = null;
			List<DateSpecHdRelationOutput> lstRela = new ArrayList<>();
			if(specHd.getMaxNumberDay().equals(MaxNumberDayType.LIMIT_FIXED_DAY)){//種類が固定日数を上限とする(type = FixedDayGrant)
				//指定する特休枠の上限日数を取得する
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd,
						param.getRelationCD() == null ? Optional.empty() : Optional.of(param.getRelationCD()));
			}else{//その以外
				//指定する特休枠の続柄に対する上限日数を取得する-(get MaxDay SpecHd ByRela FrameNo)
				lstRela = specHdEventAlg.getMaxDaySpecHdByRelaFrameNo(companyID, checkSpecHd.getFrameNo().get());
				//指定する特休枠の上限日数を取得する - (get MaxDay SpecHd)
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd, lstRela.size() == 0 ? Optional.empty() : Optional.of(lstRela.get(0).getRelationCD()));
			}
			result.setMaxDayObj(maxDay);
			result.setLstRela(lstRela);
			result.setMakeInvitation(specHd.getMakeInvitation().value);
		}
		return result;
	}

	/**
	 * 申請日を変更する getChangeAppDate
	 * 
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeAppDate(String startAppDate, boolean displayHalfDayValue, String employeeID,
			String workTypeCode, Integer holidayType, int alldayHalfDay, int prePostAtr) {
		AppAbsenceDto result = new AppAbsenceDto();
		ApplicationDto_New application = new ApplicationDto_New();
		if (employeeID == null) {
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();

		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));

		// ドメインモデル「申請表示設定」．事前事後区分表示をチェックする
		if (appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().value == AppDisplayAtr.NOTDISPLAY.value) {
			result.setPrePostFlg(AppDisplayAtr.NOTDISPLAY.value == 1 ? true : false);
			// 3.事前事後の判断処理(事前事後非表示する場合)
			PrePostAtr prePostAtrJudgment = otherCommonAlgorithm.preliminaryJudgmentProcessing(
					EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class),
					appCommonSettingOutput.generalDate,0);
			if (prePostAtrJudgment != null) {
				prePostAtr = prePostAtrJudgment.value;
			}
		} else {
			result.setPrePostFlg(AppDisplayAtr.DISPLAY.value == 1 ? true : false);
		}
		application.setPrePostAtr(prePostAtr);
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting".
		// Check base date of approval route )
		if (appCommonSettingOutput.applicationSetting.getBaseDateFlg().value == BaseDateFlg.APP_DATE.value) {
			if(holidayType != null){
				String workTypeCDForChange = "";
				Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
				// 1.勤務種類を取得する（新規） :TODO
				List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(
						appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
						displayHalfDayValue, hdAppSet);
				if (!CollectionUtil.isEmpty(workTypes)) {
					List<AbsenceWorkType> workTypeForFilter = workTypes.stream()
							.filter(x -> x.getWorkTypeCode().equals(workTypeCode == null ? "" : workTypeCode))
							.collect(Collectors.toList());
					if (CollectionUtil.isEmpty(workTypeForFilter)) {
						workTypeCDForChange = workTypes.get(0).getWorkTypeCode();
					} else {
						workTypeCDForChange = workTypeCode;
					}
				}
				result.setWorkTypes(workTypes);
				result.setWorkTypeCode(workTypeCDForChange);
			}
		}
		result.setApplication(application);
		//2018/07/31　EA修正履歴No.2402　にて追加
		//アルゴリズム「14.休暇種類表示チェック」を実行する
		CheckDispHolidayType checkDis = absenseProcess.checkDisplayAppHdType(companyID, employeeID, GeneralDate.fromString(startAppDate, DATE_FORMAT));
		List<HolidayAppTypeName> holidayAppTypes = new ArrayList<>();
		// ドメインモデル「休暇申請設定」を取得する(lấy dữ liệu domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		holidayAppTypes = this.getHolidayAppTypeName(hdAppSet,holidayAppTypes,appCommonSettingOutput);
		holidayAppTypes.sort((a, b) -> a.getHolidayAppTypeCode().compareTo(b.getHolidayAppTypeCode()));
		result.setHolidayAppTypeName(holidayAppTypes);
		result.setCheckDis(checkDis);
		return result;
	}

	/**
	 * 終日休暇半日休暇を切替する（新規）
	 * 
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeByAllDayOrHalfDay(String startAppDate, boolean displayHalfDayValue, String employeeID,
			Integer holidayType, int alldayHalfDay) {
		AppAbsenceDto result = new AppAbsenceDto();
		if (employeeID == null) {
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		if (holidayType == null) {
			return result;
		}
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));

		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(
				appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
				displayHalfDayValue, hdAppSet);
		result.setWorkTypes(workTypes);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)
		if (CollectionUtil.isEmpty(workTypes)) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(this.appAbsenceFourProcess
					.getDisplayControlWorkingHours(workTypes.get(0).getWorkTypeCode(), hdAppSet, companyID));
		}
		if (result.isChangeWorkHourFlg()) {
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
					appCommonSettingOutput.generalDate);
			result.setWorkTimeCodes(listWorkTimeCodes);
		}
		return result;
	}

	/**
	 * 終日休暇半日休暇を切替する（詳細）
	 * 
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param alldayHalfDay
	 * @param prePostAtr
	 * @return
	 */
	public AppAbsenceDto getChangeByAllDayOrHalfDayForUIDetail(String startAppDate, boolean displayHalfDayValue,
			String employeeID, Integer holidayType, int alldayHalfDay) {
		AppAbsenceDto result = new AppAbsenceDto();
		if (employeeID == null) {
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		if (holidayType == null) {
			return result;
		}
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		// 2.勤務種類を取得する（詳細）
		List<WorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeDetails(
				appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
				displayHalfDayValue);
		List<AbsenceWorkType> absenceWorkTypes = new ArrayList<>();
		for (WorkType workType : workTypes) {
			AbsenceWorkType absenceWorkType = new AbsenceWorkType(workType.getWorkTypeCode().toString(),
					workType.getWorkTypeCode().toString() + "　　" + workType.getName().toString());
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
		if (result.isChangeWorkHourFlg()) {
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
					appCommonSettingOutput.generalDate);
			result.setWorkTimeCodes(listWorkTimeCodes);
		}
		return result;
	}

	/**
	 * 勤務種類組み合わせ全表示を切替する
	 * 
	 * @param startAppDate
	 * @param displayHalfDayValue
	 * @param employeeID
	 * @param holidayType
	 * @param alldayHalfDay
	 * @return
	 */
	public AppAbsenceDto getChangeDisplayHalfDay(String startAppDate, boolean displayHalfDayValue, String employeeID,
			String workTypeCode, Integer holidayType, int alldayHalfDay) {
		AppAbsenceDto result = new AppAbsenceDto();
		if (employeeID == null) {
			employeeID = AppContexts.user().employeeId();
		}
		String companyID = AppContexts.user().companyId();
		// 1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = bfPrelaunchAppCmSet.prelaunchAppCommonSetService(
				companyID, employeeID, EmploymentRootAtr.APPLICATION.value,
				EnumAdaptor.valueOf(ApplicationType.ABSENCE_APPLICATION.value, ApplicationType.class),
				startAppDate == null ? null : GeneralDate.fromString(startAppDate, DATE_FORMAT));
		if (holidayType == null) {
			return result;
		}
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
		// 1.勤務種類を取得する（新規）
		List<AbsenceWorkType> workTypes = this.appAbsenceThreeProcess.getWorkTypeCodes(
				appCommonSettingOutput.appEmploymentWorkType, companyID, employeeID, holidayType, alldayHalfDay,
				displayHalfDayValue, hdAppSet);
		String workTypeCDForChange = "";
		if (!CollectionUtil.isEmpty(workTypes)) {
			List<AbsenceWorkType> workTypeForFilter = workTypes.stream()
					.filter(x -> x.getWorkTypeCode().equals(workTypeCode == null ? "" : workTypeCode))
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(workTypeForFilter)) {
				workTypeCDForChange = workTypes.get(0).getWorkTypeCode();
			} else {
				workTypeCDForChange = workTypeCode;
			}
		}
		result.setWorkTypes(workTypes);
		result.setWorkTypeCode(workTypeCDForChange);
		// 1.就業時間帯の表示制御(xu li hien thị A6_1)

		if (CollectionUtil.isEmpty(workTypes) || (workTypeCDForChange != null && workTypeCDForChange.equals(""))) {
			result.setChangeWorkHourFlg(false);
		} else {
			result.setChangeWorkHourFlg(
					this.appAbsenceFourProcess.getDisplayControlWorkingHours(workTypeCDForChange, hdAppSet, companyID));
		}
		if (result.isChangeWorkHourFlg()) {
			// 2.就業時間帯を取得する
			// 1.職場別就業時間帯を取得
//			List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
//					appCommonSettingOutput.generalDate);

		}
		return result;
	}

	/**
	 * 勤務種類を変更する
	 * 
	 * @param startAppDate
	 * @param employeeID
	 * @param workTypeCode
	 * @param holidayType
	 * @param workTimeCode
	 * @return
	 */
	public AppAbsenceDto getChangeWorkType(ParamGetAllAppAbsence param) {
		String workTypeCode = param.getWorkTypeCode();
		Integer holidayType = param.getHolidayType();
		String workTimeCode = param.getWorkTimeCode();
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
		if (result.isChangeWorkHourFlg()) {
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
		if (holidayType != null && holidayType == HolidayAppType.DIGESTION_TIME.value) {
			// TODO
			// 9.必要な時間を算出する
		} else if (holidayType != null && holidayType == HolidayAppType.SPECIAL_HOLIDAY.value) {
			//hoatt - 2018.08.08 - doi ung specHd
			//指定する勤務種類が事象に応じた特別休暇かチェックする
			CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyID, workTypeCode);
			if(!checkSpecHd.isSpecHdForEventFlag()){//その以外
				return result;
			}
			////事象に応じた特休フラグがtrue(SpecialHolidayEventFlag = true)
			SpecialHolidayEvent specHd = checkSpecHd.getSpecHdEvent().get();
			//指定する特休枠の上限日数を取得する
			MaxDaySpecHdOutput maxDay = null;
			//取得したメインモデル「事象に対する特別休暇」．上限日数．種類をチェックする-(Check domain [SpecialHolidayEvent]. GrantDay.Type)
			//指定する特休枠の続柄に対する上限日数を取得する-(get MaxDay SpecHd ByRela FrameNo)
			List<DateSpecHdRelationOutput> lstRela = specHdEventAlg.getMaxDaySpecHdByRelaFrameNo(companyID, checkSpecHd.getFrameNo().get());
			//取得したメインモデル「事象に対する特別休暇」．上限日数．種類をチェックする-(Check domain [SpecialHolidayEvent]. GrantDay.Type)
			if(specHd.getMaxNumberDay().equals(MaxNumberDayType.LIMIT_FIXED_DAY)){//種類が固定日数を上限とする(type = FixedDayGrant)
				//指定する特休枠の上限日数を取得する
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd, lstRela.size() == 0 ? Optional.empty() : Optional.of(lstRela.get(0).getRelationCD()));
			}else{//その以外
				//指定する特休枠の続柄に対する上限日数を取得する-(get MaxDay SpecHd ByRela FrameNo)
				lstRela = specHdEventAlg.getMaxDaySpecHdByRelaFrameNo(companyID, checkSpecHd.getFrameNo().get());
				//指定する特休枠の上限日数を取得する - (get MaxDay SpecHd)
				maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), specHd, lstRela.size() == 0 ? Optional.empty() : Optional.of(lstRela.get(0).getRelationCD()));
			}
			result.setMaxDayObj(maxDay);
			result.setSpecHdForEventFlag(checkSpecHd.isSpecHdForEventFlag());
			result.setLstRela(lstRela);
			result.setMaxNumberDayType(specHd.getMaxNumberDay().value);
			result.setMakeInvitation(specHd.getMakeInvitation().value);
		}
		result.setHolidayAppType(holidayType);
		return result;
	}

	/**
	 * getListWorkTimeCodes
	 * 
	 * @param startDate
	 * @param employeeID
	 * @return
	 */
	public List<String> getListWorkTimeCodes(String startDate, String employeeID) {
		String companyID = AppContexts.user().companyId();
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
				startDate == null ? GeneralDate.today() : GeneralDate.fromString(startDate, DATE_FORMAT));
		return listWorkTimeCodes;
	}

	/**
	 * getWorkingHours
	 * 
	 * @param workTimeCode
	 * @param workTypeCode
	 * @param holidayType
	 * @return
	 */
	public List<TimeZoneUseDto> getWorkingHours(String workTimeCode, String workTypeCode, Integer holidayType) {
		String companyID = AppContexts.user().companyId();
		List<TimeZoneUseDto> result = new ArrayList<>();
		if (holidayType != null && holidayType == HolidayAppType.DIGESTION_TIME.value) {
			// TODO
			// 9.必要な時間を算出する
		} else {
			// 勤務時間初期値の取得
			List<TimezoneUse> listTimezone = initWorkingHours(companyID, workTypeCode, workTimeCode);
			if (!CollectionUtil.isEmpty(listTimezone)) {
				return listTimezone.stream().map(x -> TimeZoneUseDto.fromDomain(x)).collect(Collectors.toList());
			}
		}
		return result;
	}

	/**
	 * 1-2.初期データの取得
	 * 
	 * @param appDate
	 * @param employeeID
	 * @param companyID
	 * @param baseDate - 基準日 ※アルゴリズム「新規画面起動時の承認ルート取得パターン」のoutputを使う
	 * @param result
	 */
	private void getData(String appDate, String employeeID, String companyID, AppAbsenceDto result, boolean checkCaller,
			AppCommonSettingOutput appCommonSet, GeneralDate baseDate) {
		ApplicationDto_New applicationDto = new ApplicationDto_New();
		// show and hide A3_3 -> A3_6
		boolean displayPrePostFlg = false;
		if (appCommonSet.applicationSetting.getDisplayPrePostFlg().equals(AppDisplayAtr.DISPLAY)) {
			displayPrePostFlg = true;
		}
		result.setPrePostFlg(displayPrePostFlg);
		// 5.事前事後区分の判断
		InitValueAtr initValueAtr = this.otherCommonAlgorithm.judgmentPrePostAtr(ApplicationType.ABSENCE_APPLICATION,
				appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT), checkCaller);
		applicationDto.setPrePostAtr(initValueAtr.value);
		// ドメインモデル「申請定型理由」を取得する(lấy dữ liệu domain 「申請定型理由」) (hien thị A7_2)
		result.setApplication(applicationDto);
		getAppReason(result, companyID);
		//No.376
		//残数取得する
		NumberOfRemainOutput numberRemain = absenseProcess.getNumberOfRemaining(companyID, employeeID, baseDate);
		result.setNumberRemain(numberRemain);
	}

	/**
	 * ドメインモデル「申請定型理由」を取得する(lấy dữ liệu domain 「申請定型理由」) (hien thị A7_2)
	 * 
	 * @param result
	 * @param companyID
	 */
	private void getAppReason(AppAbsenceDto result, String companyID) {
		List<ApplicationReason> applicationReasons = repoAppReason.getReasonByAppType(companyID,
				ApplicationType.ABSENCE_APPLICATION.value);
		List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
		for (ApplicationReason applicationReason : applicationReasons) {
			ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
					applicationReason.getReasonTemp().v(), applicationReason.getDefaultFlg().value);
			applicationReasonDtos.add(applicationReasonDto);
		}
		result.setApplicationReasonDtos(applicationReasonDtos);
	}

	/**
	 * 勤務時間初期値の取得
	 * 
	 * @param companyID
	 * @param workTypeCode
	 * @param workTimeCode
	 * @return
	 */
	public PrescribedTimezoneSetting initWorktimeCode(String companyID, String workTypeCode, String workTimeCode) {

		Optional<WorkType> WkTypeOpt = workTypeRepository.findByPK(companyID, workTypeCode);
		if (WkTypeOpt.isPresent()) {
			// アルゴリズム「1日半日出勤・1日休日系の判定」を実行する
			WorkStyle workStyle = basicScheduleService.checkWorkDay(WkTypeOpt.get().getWorkTypeCode().toString());
			if (workStyle == null) {
				return null;
			}
			if (!workStyle.equals(WorkStyle.ONE_DAY_REST)) {
				// アルゴリズム「所定時間帯を取得する」を実行する
				// 所定時間帯を取得する
				if (workTimeCode != null && !workTimeCode.equals("")) {
					if (this.predTimeRepository.findByWorkTimeCode(companyID, workTimeCode).isPresent()) {
						PrescribedTimezoneSetting prescribedTzs = this.predTimeRepository
								.findByWorkTimeCode(companyID, workTimeCode).get().getPrescribedTimezoneSetting();
						return prescribedTzs;
					}
				}
			}
		}
		return null;
	}
	private boolean checkHdType(List<AppEmploymentSetting> appEmploymentWorkType, int hdType){
		for (AppEmploymentSetting appEmploymentSetting : appEmploymentWorkType) {
			if(appEmploymentSetting.getHolidayOrPauseType() == hdType){
				//ドメインモデル「休暇申請対象勤務種類」．休暇種類を利用しないがtrue -> ×
				//ドメインモデル「休暇申請対象勤務種類」．休暇種類を利用しないがfalse -> 〇
				return appEmploymentSetting.getHolidayTypeUseFlg() ? false : true;
			}
		}
		//ドメインモデル「休暇申請対象勤務種類」が取得できない場合 -> 〇
		return true;
	}
	private List<HolidayAppTypeName> getHolidayAppTypeName(Optional<HdAppSet> hdAppSet,
			List<HolidayAppTypeName> holidayAppTypes,AppCommonSettingOutput appCommonSettingOutput){
		List<Integer> holidayAppTypeCodes = new ArrayList<>();
		for(int hdType = 0; hdType <=7; hdType ++){
			if(hdType == 5 || hdType == 6){
				continue;
			}
			if(this.checkHdType(appCommonSettingOutput.appEmploymentWorkType, hdType)){
				holidayAppTypeCodes.add(hdType);
			}
		}
			//comment hoatt 2018.07.16 bug #97414
//			if (CollectionUtil.isEmpty(holidayAppTypeCodes)) {
//				throw new BusinessException("Msg_473");
//			}
		for (Integer holidayCode : holidayAppTypeCodes) {
				switch (holidayCode) {
				case 0://年休
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getYearHdName() == null ? "" : hdAppSet.get().getYearHdName().toString()));
					break;
				case 1://代休
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getObstacleName() == null ? "" : hdAppSet.get().getObstacleName().toString()));
					break;
				case 2:
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getAbsenteeism()== null ? "" : hdAppSet.get().getAbsenteeism().toString()));
					break;
				case 3:
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getSpecialVaca() == null ? "" : hdAppSet.get().getSpecialVaca().toString()));
					break;
				case 4://積立
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getYearResig() == null ? "" : hdAppSet.get().getYearResig().toString()));
					break;
				case 7://振休
					holidayAppTypes.add(new HolidayAppTypeName(holidayCode,
							hdAppSet.get().getFurikyuName() == null ? "" :  hdAppSet.get().getFurikyuName().toString()));
					break;
				default:
					break;
				}
		}
		return holidayAppTypes;

	}
	
	public List<TimezoneUse> initWorkingHours(String companyID, String workTypeCode, String workTimeCode) {

		Optional<WorkType> WkTypeOpt = workTypeRepository.findByPK(companyID, workTypeCode);
		if (WkTypeOpt.isPresent()) {
			// アルゴリズム「1日半日出勤・1日休日系の判定」を実行する
			WorkStyle workStyle = basicScheduleService.checkWorkDay(WkTypeOpt.get().getWorkTypeCode().toString());
			if (workStyle == null) {
				return Collections.emptyList();
			}
			if (!workStyle.equals(WorkStyle.ONE_DAY_REST)) {
				// アルゴリズム「所定時間帯を取得する」を実行する
				// 所定時間帯を取得する
				return hdShipmentScreenAFinder.getTimeZones(companyID, workTimeCode, EnumAdaptor.valueOf(workStyle.value, AttendanceHolidayAttr.class));
			}
		}
		return Collections.emptyList();
	}
	/**
	 * 続柄・喪主を選択する
	 * @author hoatt
	 * @return
	 */
	public ChangeRelationShipDto changeRelationShip(String workTypeCD, String relationCD){
		//hoatt - 2018.08.08 - doi ung specHd
		String companyId = AppContexts.user().companyId();
		//続柄の選択値を変更する
		//指定する勤務種類が事象に応じた特別休暇かチェックする
		CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyId, workTypeCD);
		MaxDaySpecHdOutput maxDay = null;
		if(checkSpecHd.isSpecHdForEventFlag()){
			//指定する特休枠の上限日数を取得する
			maxDay = specHdEventAlg.getMaxDaySpecHd(companyId, checkSpecHd.getFrameNo().get(),
					checkSpecHd.getSpecHdEvent().get(), Optional.of(relationCD));
		}
		return new ChangeRelationShipDto(checkSpecHd, maxDay);
	};

}
