package nts.uk.ctx.at.request.app.command.application.holidaywork;

import static java.util.stream.Collectors.groupingBy;

/*import java.lang.reflect.Array;*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.HdWorkCheckRegisterDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.AppOvertimeDetailDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.ActualStatusCheckResult;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.CommonOvertimeHoliday;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.OvertimeColorCheck;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreActualColorCheck;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreActualColorResult;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreAppCheckResult;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.IErrorCheckBeforeRegister;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayService;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.IFactoryHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.AppHdWorkDispInfoOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.ColorConfirmResult;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HdWorkCheckRegisterOutput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.AppOvertimeSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.FlexExcessUseSetAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.AppDateContradictionAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.AppTypeSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.displaysetting.DisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckBeforeRegisterHolidayWork {
	@Inject
	private NewBeforeRegister_New newBeforeRegister;
	@Inject
	private IErrorCheckBeforeRegister beforeCheck;
	@Inject
	private IFactoryHolidayWork factoryHolidayWork;
	
	@Inject
	private CommonOvertimeHoliday commonOvertimeHoliday;
	
	@Inject
	private OvertimeRestAppCommonSetRepository overTimeSetRepo;
	
	@Inject
	private PreActualColorCheck preActualColorCheck;
	
	@Inject
	private WithdrawalAppSetRepository withdrawalAppSetRepository;
	
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	
	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	
	@Inject
	private AppOvertimeSettingRepository appOvertimeSettingRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private HolidayService holidayService;
	
	/*public ColorConfirmResult checkBeforeRregisterColor(CreateHolidayWorkCommand command) {
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		
		// check input 申請理由
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyId, 
				ApplicationType.BREAK_TIME_APPLICATION.value).get();
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(DisplayAtr.DISPLAY)){
			typicalReason += command.getAppReasonID();
		}
		if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += command.getApplicationReason();
		}
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(companyId);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(DisplayAtr.DISPLAY)
			||appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
					&& Strings.isBlank(typicalReason+displayReason)) {
				throw new BusinessException("Msg_115");
			}
		}

		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());

		return beforeRegisterColorConfirm(command.getCalculateFlag(), appRoot, holidayWorkDomain, command.isCheckOver1Year());
	}*/

	public HdWorkCheckRegisterDto checkBeforeRegister(CreateHolidayWorkCommand command) {
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = command.getAppHdWorkDispInfoCmd().toDomain();
		
		AppTypeSetting appTypeSetting = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput()
				.getRequestSetting().getApplicationSetting().getListAppTypeSetting().stream()
				.filter(x -> x.getAppType() == ApplicationType.BREAK_TIME_APPLICATION).findFirst().get();
		
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeSetting.getDisplayFixedReason() == DisplayAtr.DISPLAY){
			typicalReason += command.getAppReasonID();
		}
		if(appTypeSetting.getDisplayAppReason() == DisplayAtr.DISPLAY){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += command.getApplicationReason();
		}
		ApplicationSetting applicationSetting = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput()
				.getRequestSetting().getApplicationSetting();
		if(appTypeSetting.getDisplayFixedReason() == DisplayAtr.DISPLAY
			||appTypeSetting.getDisplayAppReason() == DisplayAtr.DISPLAY){
			if (applicationSetting.getAppLimitSetting().getRequiredAppReason()
					&& Strings.isBlank(typicalReason+displayReason)) {
				throw new BusinessException("Msg_115");
			}
		}
		
		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());
		
		HdWorkCheckRegisterOutput output = holidayService.checkBeforeRegister(
				companyId, 
				appHdWorkDispInfoOutput, 
				appRoot, 
				false, 
				holidayWorkDomain, 
				command.getCalculateFlag());
		
		HdWorkCheckRegisterDto hdWorkCheckRegisterDto = new HdWorkCheckRegisterDto();
		
		hdWorkCheckRegisterDto.confirmMsgLst = output.getConfirmMsgLst();
		hdWorkCheckRegisterDto.appOvertimeDetailOtp = AppOvertimeDetailDto.fromDomain(output.getAppOvertimeDetailOtp());
		return hdWorkCheckRegisterDto;
		
		// return CheckBeforeRegister(command.getCalculateFlag(), appRoot, holidayWorkDomain, command.isActualExceedConfirm());
	}
	
	public ColorConfirmResult beforeRegisterColorConfirm(int calculateFlg, Application_New app, AppHolidayWork appHolidayWork, boolean checkOver1Year) {
		String companyID = AppContexts.user().companyId();
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		// 2-1.新規画面登録前の処理を実行する
		newBeforeRegister.processBeforeRegister(app, OverTimeAtr.ALL, checkOver1Year, Collections.emptyList());
		// 登録前エラーチェック
		// 計算ボタン未クリックチェック
		// Get setting info
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet
				.prelaunchAppCommonSetService(app.getCompanyID(), employeeId, 1, ApplicationType.BREAK_TIME_APPLICATION, app.getAppDate());
		// 時刻計算利用する場合にチェックしたい
		ApprovalFunctionSetting requestSetting = appCommonSettingOutput.approvalFunctionSetting;
		if (null != requestSetting) {
			commonOvertimeHoliday.calculateButtonCheck(calculateFlg, requestSetting.getApplicationDetailSetting().get().getTimeCalUse());
		}
		// 03-01_事前申請超過チェック
		Map<AttendanceType, List<HolidayWorkInput>> findMap = appHolidayWork.getHolidayWorkInputs().stream()
				.collect(groupingBy(HolidayWorkInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		Optional<OvertimeRestAppCommonSetting>  overTimeSettingOpt = this.overTimeSetRepo.getOvertimeRestAppCommonSetting(companyID, ApplicationType.BREAK_TIME_APPLICATION.value);
		
		List<HolidayWorkInput> holidayWorkInputs = new ArrayList<>(); 
		holidayWorkInputs.addAll(CollectionUtil.isEmpty(findMap.get(AttendanceType.BREAKTIME)) ? Collections.emptyList() : findMap.get(AttendanceType.BREAKTIME));
		List<OvertimeColorCheck> holidayTimeLst = new ArrayList<>();
		List<WorkdayoffFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(WorkdayoffFrame breaktimeFrame :breaktimeFrames){
			holidayTimeLst.add(OvertimeColorCheck.createApp(
					AttendanceType.BREAKTIME.value, 
					breaktimeFrame.getWorkdayoffFrNo().v().intValueExact(), 
					null));
		}
		boolean appOvertimeNightFlg = appCommonSettingOutput.applicationSetting.getAppOvertimeNightFlg().value == 1 ? true : false;
		boolean flexFLag = false;
		if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr() == FlexExcessUseSetAtr.ALWAYSDISPLAY){
			flexFLag = true;
		} else if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr() == FlexExcessUseSetAtr.DISPLAY){
			Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository
					.getBySidAndStandardDate(app.getEmployeeID(), app.getAppDate());
			if(personalLablorCodition.isPresent()){
				if(personalLablorCodition.get().getLaborSystem() == WorkingSystem.FLEX_TIME_WORK){
					flexFLag = true;
				}
			}
		}
		if(appOvertimeNightFlg) {
			holidayTimeLst.add(OvertimeColorCheck.createApp(AttendanceType.BREAKTIME.value, 11, null));
		}
		if(flexFLag) {
			holidayTimeLst.add(OvertimeColorCheck.createApp(AttendanceType.BREAKTIME.value, 12, null));
		}
		PreActualColorResult preActualColorResult = null;
		if(app.getPrePostAtr()==PrePostAtr.POSTERIOR) {
			UseAtr preExcessDisplaySetting = overTimeSettingOpt.get().getPreExcessDisplaySetting();
			AppDateContradictionAtr performanceExcessAtr = overTimeSettingOpt.get().getPerformanceExcessAtr();
			WithdrawalAppSet withdrawalAppSet = withdrawalAppSetRepository.getWithDraw().get();
			holidayTimeLst = holidayTimeLst.stream().map(x -> {
				Integer value = holidayWorkInputs.stream()
				.filter(y -> y.getAttendanceType().value==x.attendanceID && y.getFrameNo()==x.frameNo)
				.findAny().map(z -> z.getApplicationTime().v()).orElse(null);
				return OvertimeColorCheck.createApp(x.attendanceID, x.frameNo, value);
			}).collect(Collectors.toList());
			// 07-01_事前申請状態チェック
			PreAppCheckResult preAppCheckResult = preActualColorCheck.preAppStatusCheck(
					companyID, 
					employeeId, 
					app.getAppDate(), 
					ApplicationType.BREAK_TIME_APPLICATION);
			// 07-02_実績取得・状態チェック
			ActualStatusCheckResult actualStatusCheckResult = preActualColorCheck.actualStatusCheck(
					companyID, 
					employeeId, 
					app.getAppDate(), 
					ApplicationType.BREAK_TIME_APPLICATION, 
					appHolidayWork.getWorkTypeCode() == null ? null : appHolidayWork.getWorkTypeCode().v(), 
					appHolidayWork.getWorkTimeCode() == null ? null : appHolidayWork.getWorkTimeCode().v(), 
					withdrawalAppSet.getOverrideSet(), 
					Optional.empty());
			// 07_事前申請・実績超過チェック(07_đơn xin trước. check vượt quá thực tế )
			preActualColorResult = preActualColorCheck.preActualColorCheck(
					preExcessDisplaySetting, 
					performanceExcessAtr, 
					ApplicationType.BREAK_TIME_APPLICATION, 
					app.getPrePostAtr(), 
					Collections.emptyList(), 
					holidayTimeLst,
					preAppCheckResult.opAppBefore,
					preAppCheckResult.beforeAppStatus,
					actualStatusCheckResult.actualLst,
					actualStatusCheckResult.actualStatus);
		}
		return new ColorConfirmResult(false, 0, 0, "", Collections.emptyList(), null, preActualColorResult);
	}

	public OvertimeCheckResultDto CheckBeforeRegister(int calculateFlg, Application_New app, AppHolidayWork appHolidayWork, boolean actualExceedConfirm) {
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false, null);
		// ３６協定時間上限チェック（月間）
		Optional<AppOvertimeDetail> appOvertimeDetailOtp = commonOvertimeHoliday.registerHdWorkCheck36TimeLimit(
				app.getCompanyID(), app.getEmployeeID(), app.getAppDate(), appHolidayWork.getHolidayWorkInputs());
		result.setAppOvertimeDetail(AppOvertimeDetailDto.fromDomain(appOvertimeDetailOtp));
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();
		return result;
	}
	
	public ColorConfirmResult checkBeforeUpdateColor(CreateHolidayWorkCommand command) {

		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		int calculateFlg = command.getCalculateFlag();
		// 登録前エラーチェック
		//勤務種類、就業時間帯チェックのメッセージを表示
		otherCommonAlgorithm.checkWorkingInfo(companyId, command.getWorkTypeCode(), command.getSiftTypeCode());
		// 計算ボタン未クリックチェック
		// Get setting info
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet
				.prelaunchAppCommonSetService(appRoot.getCompanyID(), employeeId, 1, ApplicationType.BREAK_TIME_APPLICATION, appRoot.getAppDate());
		// 時刻計算利用する場合にチェックしたい
		ApprovalFunctionSetting requestSetting = appCommonSettingOutput.approvalFunctionSetting;
		if (null != requestSetting) {
			commonOvertimeHoliday.calculateButtonCheck(calculateFlg, requestSetting.getApplicationDetailSetting().get().getTimeCalUse());
		}
		// 事前申請超過チェック
		Map<AttendanceType, List<HolidayWorkInput>> findMap = holidayWorkDomain.getHolidayWorkInputs().stream()
				.collect(groupingBy(HolidayWorkInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<HolidayWorkInput> holidayWorkInputs = findMap.get(AttendanceType.BREAKTIME);
		
		if (holidayWorkInputs != null && !holidayWorkInputs.isEmpty()) {
			ColorConfirmResult colorConfirmResult = commonOvertimeHoliday.preApplicationExceededCheck010(appRoot.getCompanyID(), appRoot.getAppDate(),
					appRoot.getInputDate(), appRoot.getPrePostAtr(), AttendanceType.BREAKTIME.value, holidayWorkInputs, command.getApplicantSID());
			if (colorConfirmResult.isConfirm()) {
				return colorConfirmResult;
			}
		}
		
		return new ColorConfirmResult(false, 0, 0, "", Collections.emptyList(), null, null);
	}
	
	public OvertimeCheckResultDto checkBeforeUpdate(CreateHolidayWorkCommand command) {

		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());
		// 社員ID
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false, null);
		// 事前申請超過チェック
		Map<AttendanceType, List<HolidayWorkInput>> findMap = holidayWorkDomain.getHolidayWorkInputs().stream()
				.collect(groupingBy(HolidayWorkInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<HolidayWorkInput> holidayWorkInputs = findMap.get(AttendanceType.BREAKTIME);
		
		// ３６上限チェック(詳細)
		Optional<AppOvertimeDetail> appOvertimeDetailOtp = commonOvertimeHoliday.updateHdWorkCheck36TimeLimit(
				appRoot.getCompanyID(), command.getAppID(), appRoot.getEnteredPersonID(), appRoot.getEmployeeID(),
				command.getApplicationDate(), holidayWorkInputs);
		result.setAppOvertimeDetail(AppOvertimeDetailDto.fromDomain(appOvertimeDetailOtp));
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();

		return result;
	}

	public static List<HolidayWorkInput> getHolidayWorkInput(CreateHolidayWorkCommand command, String Cid, String appId) {
		List<HolidayWorkInput> holidayWorkInputs = new ArrayList<HolidayWorkInput>();
		/**
		 * 休憩時間  ATTENDANCE_ID = 2
		 */
		if (null != command.getBreakTimes()) {
			holidayWorkInputs.addAll(getHolidayWorkInput(command.getBreakTimes(), Cid, appId, AttendanceType.BREAKTIME.value));
		}
		/**
		 * 残業時間 ATTENDANCE_ID = 1
		 */
		if (null != command.getOvertimeHours()) {
			holidayWorkInputs
					.addAll(getHolidayWorkInput(command.getOvertimeHours(), Cid, appId, AttendanceType.NORMALOVERTIME.value));
		}
		/**
		 * 休出時間 ATTENDANCE_ID = 0
		 */
		if (null != command.getRestTime()) {
			holidayWorkInputs
					.addAll(getHolidayWorkInput(command.getRestTime(), Cid, appId, AttendanceType.RESTTIME.value));
		}
		/**
		 * 加給時間 ATTENDANCE_ID = 3
		 */
		if (null != command.getBonusTimes()) {
			holidayWorkInputs.addAll(getHolidayWorkInput(command.getBonusTimes(), Cid, appId, AttendanceType.BONUSPAYTIME.value));
		}
		return holidayWorkInputs;
	}

	private static List<HolidayWorkInput> getHolidayWorkInput(List<HolidayWorkInputCommand> inputCommand, String Cid, String appId,
			int attendanceId) {
		return inputCommand.stream().filter(item -> {
			Integer startTime = item.getStartTime() == null ? null : item.getStartTime().intValue();
			Integer endTime = item.getEndTime() == null ? null : item.getEndTime().intValue();
			Integer appTime = item.getApplicationTime() == null ? null : item.getApplicationTime().intValue();
			return startTime != null || endTime != null || appTime != null;
		}).map(item -> {
			Integer startTime = item.getStartTime() == null ? null : item.getStartTime().intValue();
			Integer endTime = item.getEndTime() == null ? null : item.getEndTime().intValue();
			Integer appTime = item.getApplicationTime() == null ? null : item.getApplicationTime().intValue();
			return HolidayWorkInput.createSimpleFromJavaType(Cid, appId, attendanceId, item.getFrameNo(), startTime,
					endTime, appTime);
		}).collect(Collectors.toList());
	}
}
