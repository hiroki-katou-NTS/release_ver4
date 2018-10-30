package nts.uk.ctx.at.request.app.find.application.overtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AgreeOverTimeDto;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.AppOvertimeDetailDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.DivergenceReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.EmployeeOvertimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OverTimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeInputDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.PreAppOvertimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Changeable;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.TimeWithCalculationImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.CollectApprovalRootPatternService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.StartupErrorCheckService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.output.ApprovalRootPattern;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.AgreementTimeService;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.overtime.service.AppOvertimeReference;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.DisplayPrePost;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeFourProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeInstructInfomation;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeAndSiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;
import nts.uk.ctx.at.request.dom.application.overtime.service.output.RecordWorkOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.AppOvertimeSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.FlexExcessUseSetAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.company.divergencereason.DivergenceReason;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.DeductionTimeDto;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneSharedOutPut;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class AppOvertimeFinder {
	final static String DATE_FORMAT = "yyyy/MM/dd";
	final static String ZEZO_TIME = "0:00";
	final static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
	final static String SPACE = " ";
	
	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	
	@Inject
	private OvertimeService overtimeService;
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Inject
	private OvertimeWorkFrameRepository overtimeFrameRepository;
	
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	
	@Inject
	private OvertimeRepository overtimeRepository;
	@Inject
	private OvertimeSixProcess overtimeSixProcess;
	@Inject
	private OvertimeFourProcess overtimeFourProcess;
	@Inject
	private CollectApprovalRootPatternService collectApprovalRootPatternService;
	@Inject
	private StartupErrorCheckService startupErrorCheckService;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private DailyAttendanceTimeCaculation dailyAttendanceTimeCaculation;
	@Inject
	private AppOvertimeSettingRepository appOvertimeSettingRepository;
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private AtEmployeeAdapter atEmployeeAdapter;
	
	@Inject
	private AgreementTimeService agreementTimeService;
	
	/**
	 * @param url
	 * @param appDate
	 * @param uiType
	 * @return
	 */
	public OverTimeDto getOvertimeByUIType(String url,String appDate,int uiType,Integer timeStart1,Integer timeEnd1,String reasonContent,List<String> employeeIDs,String employeeID){
		
		OverTimeDto result = new OverTimeDto();
		ApplicationDto_New applicationDto = new ApplicationDto_New();
		List<OvertimeInputDto> overTimeInputs = new ArrayList<>();
		String companyID = AppContexts.user().companyId();
		if(CollectionUtil.isEmpty(employeeIDs) && employeeID == null){
			 employeeID = AppContexts.user().employeeId();
		}else if(!CollectionUtil.isEmpty(employeeIDs)){
			employeeID = employeeIDs.get(0);
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
		
		int rootAtr = 1;
		PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
		
		
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID, employeeID, rootAtr, 
				EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class),appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		result.setManualSendMailAtr(appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value  == 1 ? false : true);
		result.setSendMailWhenApprovalFlg(appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenApprovalFlg().value == 1 ? true : false);
		result.setSendMailWhenRegisterFlg(appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenRegisterFlg().value == 1 ? true : false);
		if(CollectionUtil.isEmpty(employeeIDs) || employeeIDs.size() == 1){
			//アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
			ApprovalRootPattern approvalRootPattern = collectApprovalRootPatternService.getApprovalRootPatternService(companyID, employeeID, EmploymentRootAtr.APPLICATION, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), appCommonSettingOutput.generalDate, "", true);
			//アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する 
			 startupErrorCheckService.startupErrorCheck(appCommonSettingOutput.generalDate, ApplicationType.OVER_TIME_APPLICATION.value, approvalRootPattern.getApprovalRootContentImport());
		}
		// 02_残業区分チェック : check loai lam them
		int overtimeAtr = overtimeService.checkOvertimeAtr(url);
		result.setOvertimeAtr(overtimeAtr);
		// 01_初期データ取得
		getData(result,uiType,appDate,companyID,employeeID,appCommonSettingOutput,applicationDto,overtimeAtr,overTimeInputs,preAppOvertimeDto,timeStart1,timeEnd1,reasonContent);
		
		result.setApplication(applicationDto);
		String employeeName = "";
		if(Strings.isNotBlank(applicationDto.getApplicantSID())){
			employeeName = employeeAdapter.getEmployeeName(applicationDto.getApplicantSID());
			result.setEmployeeID(applicationDto.getApplicantSID());
		} else {
			employeeName = employeeAdapter.getEmployeeName(employeeID);
			result.setEmployeeID(employeeID);
		}
		result.setEmployeeName(employeeName);
		return result;
	}
	
	/**
	 * @return
	 */
	public List<CaculationTime> getCaculationValue(List<CaculationTime> overtimeHours,
			List<CaculationTime> bonusTimes,
			int prePostAtr,
			String appDate,
			String siftCD,
			String workTypeCode,Integer startTime,Integer endTime,List<Integer> startTimeRests,List<Integer> endTimeRests){
		 
		List<CaculationTime> caculationTimes = new ArrayList<>();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		GeneralDateTime inputDate = GeneralDateTime.now();
		int rootAtr = 1;
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
						employeeID,
						rootAtr, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		
		// 6.計算処理 : 
		//表示しない
		if(!isSettingDisplay(appCommonSettingOutput)){
			//休憩時間帯を取得する
			BreakTimeZoneSharedOutPut breakTime = this.overtimeService.getBreakTimes(companyID,workTypeCode, siftCD);
			
			if (!CollectionUtil.isEmpty(breakTime.getLstTimezone())) {
				startTimeRests = breakTime.getLstTimezone().stream().map(x->  x.getStart().v()).collect(Collectors.toList());

				endTimeRests = breakTime.getLstTimezone().stream().map(x->  x.getEnd().v()).collect(Collectors.toList());
			}
		}
		
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeID, GeneralDate.fromString(appDate, DATE_FORMAT), workTypeCode, siftCD, startTime, endTime, startTimeRests, endTimeRests);
		Map<Integer,TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
		List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,dailyAttendanceTimeCaculationImport.getFlexTime(),dailyAttendanceTimeCaculationImport.getMidNightTime());
		// 06-01_色表示チェック
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		if(approvalFunctionSetting != null){
			overtimeHours = this.overtimeSixProcess.checkDisplayColor(overtimeHours,
					overtimeInputCaculations,
					prePostAtr,
					inputDate,
					appDate == null ? null :GeneralDate.fromString(appDate, DATE_FORMAT),
					ApplicationType.OVER_TIME_APPLICATION.value,
					employeeID, 
					companyID, 
					approvalFunctionSetting,
					siftCD);
		}
		// 06-02_残業時間を取得
		List<CaculationTime> caculationTimeHours = this.overtimeSixProcess.getCaculationOvertimeHours(companyID, employeeID, appDate, ApplicationType.OVER_TIME_APPLICATION.value,overtimeHours,overtimeInputCaculations);
		caculationTimes.addAll(caculationTimeHours);
		
		// 06-03_加給時間を取得
		List<CaculationTime> caculationTimeBonus= this.overtimeSixProcess.getCaculationBonustime(companyID, employeeID, appDate,  ApplicationType.OVER_TIME_APPLICATION.value,bonusTimes);
		caculationTimes.addAll(caculationTimeBonus);
		
		// 計算フラグ=0(client setting)
		
		return caculationTimes;
	}

	



	private boolean isSettingDisplay(AppCommonSettingOutput appCommonSettingOutput) {
		return appCommonSettingOutput.approvalFunctionSetting.getApplicationDetailSetting().get()
				.getBreakInputFieldDisp().equals(true)
				&& appCommonSettingOutput.getApprovalFunctionSetting().getApplicationDetailSetting().get()
						.getTimeCalUse().equals(UseAtr.USE);

	}

	private List<OvertimeInputCaculation> convertMaptoList(Map<Integer,TimeWithCalculationImport> overTime,TimeWithCalculationImport flexTime,TimeWithCalculationImport midNightTime){
		List<OvertimeInputCaculation> result = new ArrayList<>();
		
		for(Map.Entry<Integer,TimeWithCalculationImport> entry : overTime.entrySet()){
			OvertimeInputCaculation overtimeCal = new OvertimeInputCaculation(AttendanceType.NORMALOVERTIME.value, entry.getKey(), entry.getValue().getCalTime());
			result.add(overtimeCal);
		}
		OvertimeInputCaculation flexTimeCal = new OvertimeInputCaculation(AttendanceType.NORMALOVERTIME.value, 12,(flexTime.getCalTime() == null || flexTime.getCalTime() < 0)? null : flexTime.getCalTime());
		OvertimeInputCaculation midNightTimeCal = new OvertimeInputCaculation(AttendanceType.NORMALOVERTIME.value, 11, midNightTime.getCalTime());
		result.add(flexTimeCal);
		result.add(midNightTimeCal);
		return result;
	}
	
	/**
	 * @param appID
	 * @return
	 */
	public OverTimeDto findDetailByAppID(String appID){
		String companyID = AppContexts.user().companyId();
		// 07_勤務種類取得: lay loai di lam 
		// 08_就業時間帯取得(lay loai gio lam viec)
		// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
		// 01-04_加給時間を取得: chua xong
		Optional<AppOverTime> opAppOverTime = overtimeRepository.getFullAppOvertime(companyID, appID);
		if(!opAppOverTime.isPresent()){
			throw new BusinessException("Msg_198");
		}
		AppOverTime appOverTime = opAppOverTime.get();
		OverTimeDto overTimeDto = OverTimeDto.fromDomain(appOverTime);
		String employeeName = employeeAdapter.getEmployeeName(appOverTime.getApplication().getEmployeeID());
		overTimeDto.setEmployeeName(employeeName);
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				appOverTime.getApplication().getEmployeeID(),
				1, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), appOverTime.getApplication().getAppDate());
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		if(approvalFunctionSetting != null){
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				overTimeDto.setDisplayCaculationTime(true);
				// 07_勤務種類取得: lay loai di lam 
				String workTypeCD = appOverTime.getWorkTypeCode() == null ? "" : appOverTime.getWorkTypeCode().v();
				WorkType workType = workTypeRepository.findByPK(companyID, workTypeCD).orElse(null);
				if(workType != null){
					overTimeDto.setWorkType(new WorkTypeOvertime(workType.getWorkTypeCode().v(),workType.getName().v()));
				}
				List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
				List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, appOverTime.getApplication().getEmployeeID(),approvalFunctionSetting,appEmploymentWorkType);
				
				List<String> workTypeCodes = new ArrayList<>();
				for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
					workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
				}
				overTimeDto.setWorkTypes(workTypeCodes);
				
				// 08_就業時間帯取得(lay loai gio lam viec) 
				List<SiftType> siftTypes = overtimeService.getSiftType(companyID, appOverTime.getApplication().getEmployeeID(), approvalFunctionSetting,appOverTime.getApplication().getAppDate());
				List<String> siftCodes = new ArrayList<>();
				for(SiftType siftType : siftTypes){
					siftCodes.add(siftType.getSiftCode());
				}
				overTimeDto.setSiftTypes(siftCodes);
				
				String workTimeCD = appOverTime.getSiftCode() == null ? "" : appOverTime.getSiftCode().v();
				WorkTimeSetting workTime = workTimeRepository.findByCode(companyID, workTimeCD).orElse(null);
				if(workTime != null){
					overTimeDto.setSiftType(new SiftType(workTime.getWorktimeCode().v(), workTime.getWorkTimeDisplayName().getWorkTimeName().v()));
				}else{
					overTimeDto.setSiftType(new SiftType("", ""));
				}
				
				// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
				boolean displayRestTime = iOvertimePreProcess.getRestTime(approvalFunctionSetting);
				overTimeDto.setDisplayRestTime(displayRestTime);
				
			}else{
				overTimeDto.setDisplayCaculationTime(false);
			}
		}
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(companyID,  ApplicationType.OVER_TIME_APPLICATION.value);
		if(appTypeDiscreteSetting.isPresent()){
			// 01-05_申請定型理由を取得
			if(appTypeDiscreteSetting.get().getTypicalReasonDisplayFlg().value == AppDisplayAtr.DISPLAY.value){
				overTimeDto.setTypicalReasonDisplayFlg(true);
				List<ApplicationReason> applicationReasons = iOvertimePreProcess.getApplicationReasonType(companyID,ApplicationType.OVER_TIME_APPLICATION.value,appTypeDiscreteSetting);
				List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
				for (ApplicationReason applicationReason : applicationReasons) {
					ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
							applicationReason.getReasonTemp().v(), applicationReason.getDefaultFlg().value);
					applicationReasonDtos.add(applicationReasonDto);
				}
				overTimeDto.setApplicationReasonDtos(applicationReasonDtos);
			}else{
				overTimeDto.setTypicalReasonDisplayFlg(false);
			}
			//01-06_申請理由を取得
			overTimeDto.setDisplayAppReasonContentFlg(iOvertimePreProcess.displayAppReasonContentFlg(appTypeDiscreteSetting));
		}
		if(overtimeRestAppCommonSet.isPresent()){
			//01-08_乖離定型理由を取得
			if(overTimeDto.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && overtimeRestAppCommonSet.get().getDivergenceReasonFormAtr().value == UseAtr.USE.value){
				overTimeDto.setDisplayDivergenceReasonForm(true);
				List<DivergenceReason> divergenceReasons = iOvertimePreProcess.getDivergenceReasonForm(companyID,ApplicationType.OVER_TIME_APPLICATION.value,overtimeRestAppCommonSet);
				convertToDivergenceReasonDto(divergenceReasons,overTimeDto);
			}else{
				overTimeDto.setDisplayDivergenceReasonForm(false);
			}
			//時間外表示区分
			overTimeDto.setExtratimeDisplayFlag(overtimeRestAppCommonSet.get().getExtratimeDisplayAtr().value == 1 ? true : false);
			//01-07_乖離理由を取得
			overTimeDto.setDisplayDivergenceReasonInput(overTimeDto.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && iOvertimePreProcess.displayDivergenceReasonInput(overtimeRestAppCommonSet));
		}
		List<OvertimeInputDto> overTimeInputs = new ArrayList<>();
		// 01-03_残業枠を取得: chua xong
		overTimeDto.setAppOvertimeNightFlg(appCommonSettingOutput.applicationSetting.getAppOvertimeNightFlg().value);
		List<OvertimeWorkFrame> overtimeFrames = iOvertimePreProcess.getOvertimeHours(appOverTime.getOverTimeAtr().value,companyID);
		
		for(OvertimeWorkFrame overtimeFrame :overtimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			overtimeInputDto.setFrameNo(overtimeFrame.getOvertimeWorkFrNo().v().intValue());
			overtimeInputDto.setFrameName(overtimeFrame.getOvertimeWorkFrName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		OvertimeInputDto overtimeInputShiftNight = new OvertimeInputDto();
		overtimeInputShiftNight.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
		overtimeInputShiftNight.setFrameNo(11);
		overTimeInputs.add(overtimeInputShiftNight);
		
		OvertimeInputDto overtimeInputFlexExessTime = new OvertimeInputDto();
		overtimeInputFlexExessTime.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
		overtimeInputFlexExessTime.setFrameNo(12);
		overTimeInputs.add(overtimeInputFlexExessTime);
		
		// lay breakTime
		List<WorkdayoffFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(WorkdayoffFrame breaktimeFrame :breaktimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceType.BREAKTIME.value);
			overtimeInputDto.setFrameNo(breaktimeFrame.getWorkdayoffFrNo().v().intValueExact());
			overtimeInputDto.setFrameName(breaktimeFrame.getWorkdayoffFrName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		
		
		// 01-04_加給時間を取得: chua xong
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				overTimeDto.setDisplayBonusTime(true);
				List<BonusPayTimeItem> bonusPayTimeItems= this.iOvertimePreProcess.getBonusTime(
						appOverTime.getApplication().getEmployeeID(),
						overtimeRestAppCommonSet,
						appOverTime.getApplication().getAppDate().toString(DATE_FORMAT),
						companyID, 
						overTimeDto.getSiftType());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItems){
					OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
					overtimeInputDto.setAttendanceID(AttendanceType.BONUSPAYTIME.value);
					overtimeInputDto.setFrameNo(bonusPayTimeItem.getId());
					overtimeInputDto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
					overtimeInputDto.setTimeItemTypeAtr(bonusPayTimeItem.getTimeItemTypeAtr().value);
					overTimeInputs.add(overtimeInputDto);
				}
			}else{
				overTimeDto.setDisplayBonusTime(false);
			}
		}
		List<Integer> restStartTimes =  new ArrayList<Integer>();
		List<Integer> restEndTimes =  new ArrayList<Integer>();
		for(int i = 1; i < 11; i++){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceType.RESTTIME.value);
			overtimeInputDto.setFrameNo(i);
			overtimeInputDto.setFrameName(Integer.toString(i));
			overtimeInputDto.setTimeItemTypeAtr(0);
			overTimeInputs.add(overtimeInputDto);
		}
		overTimeDto.getOverTimeInputs().forEach(item -> {
			overTimeInputs.stream().filter(x -> 
				(x.getAttendanceID()==item.getAttendanceID())
				&& (x.getFrameNo()==item.getFrameNo())
				&& (x.getTimeItemTypeAtr()==item.getTimeItemTypeAtr())
					).findAny().ifPresent(value -> {
						value.setStartTime(item.getStartTime());
						value.setEndTime(item.getEndTime());
						value.setApplicationTime(item.getApplicationTime());
					});;
		});
		List<OvertimeInputDto> overtimeRestTimes = overTimeInputs.stream().filter(x -> x.getAttendanceID() == AttendanceType.RESTTIME.value).collect(Collectors.toList());
		if(!CollectionUtil.isEmpty(overtimeRestTimes)){
			restStartTimes = overtimeRestTimes.stream().filter(x -> x.getStartTime() != null).map(x -> x.getStartTime())
					.collect(Collectors.toList());
			restEndTimes = overtimeRestTimes.stream().filter(x -> x.getEndTime() != null).map(x -> x.getEndTime())
					.collect(Collectors.toList());
		}
		overTimeDto.setOverTimeInputs(overTimeInputs);
		
		// xu li hien thi du lieu xin truoc
				if(overtimeRestAppCommonSet.isPresent()){
					// hien thi du lieu thuc te
					overTimeDto.setReferencePanelFlg(false);
					if(overTimeDto.getApplication().getPrePostAtr() == InitValueAtr.POST.value && overtimeRestAppCommonSet.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
						overTimeDto.setReferencePanelFlg(true);
					}
					// hien thi don xin truoc
					overTimeDto.setAllPreAppPanelFlg(false);
					if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.USE.value && overTimeDto.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value){
						overTimeDto.setAllPreAppPanelFlg(true);
					}
				}
		//01-09_事前申請を取得
				if(overTimeDto.isAllPreAppPanelFlg()){
					if(overTimeDto.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value ){
						AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(companyID, appOverTime.getApplication().getEmployeeID(),overtimeRestAppCommonSet, appOverTime.getApplication().getAppDate().toString(DATE_FORMAT),appOverTime.getApplication().getPrePostAtr().value);
						if(appOvertime != null){
							overTimeDto.setPreAppPanelFlg(true);
							PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
							convertOverTimeDto(companyID,preAppOvertimeDto,overTimeDto,appOvertime);
						}else{
							overTimeDto.setPreAppPanelFlg(false);
						}
						
					}
				}
				
		//xu li tinh toan
		List<OvertimeInputDto> overtimeHours = overTimeInputs.stream().filter(x -> x.getAttendanceID() == AttendanceType.NORMALOVERTIME.value).collect(Collectors.toList());
		checkColorCaculationForUIB(overtimeHours
				,overTimeDto.getApplication().getPrePostAtr()
				,overTimeDto.getApplication().getApplicationDate()
				,overTimeDto.getApplication().getInputDate()
				,overTimeDto.getSiftType() == null ? "" : overTimeDto.getSiftType().getSiftCode()
				,overTimeDto.getWorkType() == null ? "" : overTimeDto.getWorkType().getWorkTypeCode()
				,overTimeDto.getWorkClockFrom1()
				,overTimeDto.getWorkClockTo1()
				,restStartTimes,restEndTimes);
		for(OvertimeInputDto overtime : overtimeHours){
			for(OvertimeInputDto overtimeHour : overTimeDto.getOverTimeInputs()){
				if(overtime.getAttendanceID() == overtimeHour.getAttendanceID()){
					if(overtime.getFrameNo() == overtimeHour.getFrameNo()){
						overtimeHour.setErrorCode(overtime.getErrorCode());
					}
				}
			}
		}
		
		List<OvertimeWorkFrame> otFrames = iOvertimePreProcess.getOvertimeHours(appOverTime.getOverTimeAtr().value,companyID);
		// dùng cho xử lí tính toán
		List<CaculationTime> overTimeHours = new ArrayList<>(); 
		for(OvertimeWorkFrame overtimeFrame :overtimeFrames){
			CaculationTime cal = new CaculationTime();
			cal.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			cal.setFrameNo(overtimeFrame.getOvertimeWorkFrNo().v().intValueExact());
			cal.setFrameName(overtimeFrame.getOvertimeWorkFrName().toString());
			overTimeHours.add(cal);
		}
		for(int i = 11; i<= 12;i++){
			CaculationTime caculationTime = new CaculationTime();
			caculationTime.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			caculationTime.setFrameNo(i);
			overTimeHours.add(caculationTime);
		}
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation
				.getCalculation(appOverTime.getApplication().getEmployeeID(), 
						appOverTime.getApplication().getAppDate(),
						appOverTime.getWorkTypeCode() ==  null ? null : appOverTime.getWorkTypeCode().v(),
						appOverTime.getSiftCode() ==  null ? null : appOverTime.getSiftCode().v(),
						appOverTime.getWorkClockFrom1(), appOverTime.getWorkClockTo1(), null, null);
		Map<Integer, TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
		List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,
				dailyAttendanceTimeCaculationImport.getFlexTime(),
				dailyAttendanceTimeCaculationImport.getMidNightTime());
		// 01-18_実績の内容を表示し直す : chưa xử lí
		if (approvalFunctionSetting != null) {
			AppOvertimeReference appOvertimeReference = iOvertimePreProcess.getResultContentActual(
					appOverTime.getApplication().getPrePostAtr().value, 
					appOverTime.getWorkTypeCode() ==  null ? null : appOverTime.getWorkTypeCode().v(),
					companyID, appOverTime.getApplication().getEmployeeID(), 
					appOverTime.getApplication().getAppDate().toString("yyyy/MM/dd"), 
					approvalFunctionSetting, 
					overTimeHours, 
					overtimeInputCaculations);
			overTimeDto.setAppOvertimeReference(appOvertimeReference);
		}
		// display flex
		if(appOvertimeSettingRepository.getAppOver().isPresent()){
			if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr().equals(FlexExcessUseSetAtr.NOTDISPLAY)){
				overTimeDto.setFlexFLag(false);
			}else if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr().equals(FlexExcessUseSetAtr.DISPLAY)){
				GeneralDate baseDate = appCommonSettingOutput.generalDate;
				Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(appOverTime.getApplication().getEmployeeID(),baseDate);
				if(personalLablorCodition.isPresent()){
					if(personalLablorCodition.get().getLaborSystem().isFlexTimeWork()){
						overTimeDto.setFlexFLag(true);
					}else{
						overTimeDto.setFlexFLag(false);
					}
				}
			}else{
				overTimeDto.setFlexFLag(true);
			}
			//ドメインモデル「残業申請設定」.勤種変更可否フラグがtrueの場合
			overTimeDto.setWorkTypeChangeFlg(false);
			if(appOvertimeSettingRepository.getAppOver().get().getWorkTypeChangeFlag().equals(Changeable.CHANGEABLE)){
				overTimeDto.setWorkTypeChangeFlg(true);
			}
		}
		Optional<OvertimeRestAppCommonSetting> otRestAppCommonSet = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		if(otRestAppCommonSet.isPresent()&&(otRestAppCommonSet.get().getExtratimeDisplayAtr()==UseAtr.USE)){
			Map<String, AppOverTime> appOverTimeDetailMap = overtimeRepository.getListAppOvertimeFrame(companyID, Arrays.asList(appID));
			if(!appOverTimeDetailMap.isEmpty()){
				Optional<AppOvertimeDetail> appOvertimeDetail = appOverTimeDetailMap.entrySet().stream().findFirst().get().getValue().getAppOvertimeDetail();
				overTimeDto.setAppOvertimeDetailDto(AppOvertimeDetailDto.fromDomain(appOvertimeDetail));
				if(!appOvertimeDetail.isPresent()){
					overTimeDto.setAppOvertimeDetailStatus(null);
				} else {
					overTimeDto.setAppOvertimeDetailStatus(overtimeService.getTime36Detail(appOvertimeDetail.get()));
				}
			}
		}
		return overTimeDto;
	} 
	public List<OvertimeInputDto> checkColorCaculationForUIB(List<OvertimeInputDto> overtimeHours,int prePostAtr,String appDate,String inputDate,String siftCD,
			String workTypeCode,Integer startTime,Integer endTime,List<Integer> startTimeRests,List<Integer> endTimeRests){
		
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		// 6.計算処理 : 
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeID,
				GeneralDate.fromString(appDate, DATE_FORMAT),
				workTypeCode, siftCD, startTime, endTime, startTimeRests , endTimeRests);
		Map<Integer,TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
		List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,dailyAttendanceTimeCaculationImport.getFlexTime(),dailyAttendanceTimeCaculationImport.getMidNightTime());
		List<CaculationTime> overTimeInputs = new ArrayList<>();
		for(OvertimeInputDto overtime : overtimeHours){
			CaculationTime calcu = new CaculationTime();
			calcu.setAttendanceID(overtime.getAttendanceID());
			calcu.setFrameNo(overtime.getFrameNo());
			calcu.setApplicationTime(overtime.getApplicationTime());
			overTimeInputs.add(calcu);
		}
		
		overTimeInputs = this.overtimeFourProcess.checkDisplayColor(overTimeInputs, overtimeInputCaculations, prePostAtr, inputDate == null ? null : GeneralDateTime.fromString(inputDate, DATE_TIME_FORMAT), appDate == null ? null :GeneralDate.fromString(appDate, DATE_FORMAT), ApplicationType.OVER_TIME_APPLICATION.value, employeeID, companyID);
		for(OvertimeInputDto overtime : overtimeHours){
			for(CaculationTime calculation : overTimeInputs){
				if(overtime.getFrameNo() == calculation.getFrameNo()){
					overtime.setErrorCode(calculation.getErrorCode());
				}
			}
		}
		return overtimeHours;
	}
	
	/**
	 * @param appDate
	 * @param prePostAtr
	 * @return
	 */
	public OverTimeDto findByChangeAppDate(String appDate,int prePostAtr,String siftCD, List<CaculationTime> overtimeHours,String workTypeCode,Integer startTime,Integer endTime,List<Integer> startTimeRests,List<Integer> endTimeRests,int overtimeAtr){
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		OverTimeDto result = new OverTimeDto();
		ApplicationDto_New applicationDto = new ApplicationDto_New();
		PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
		// 申請日を変更する : chưa xử lí(Hung)
		
		// 01-01_残業通知情報を取得
		int rootAtr = 1;
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				rootAtr, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class),appDate == null? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		OvertimeInstructInfomation overtimeInstructInfomation = iOvertimePreProcess.getOvertimeInstruct(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayOvertimeInstructInforFlg(overtimeInstructInfomation.isDisplayOvertimeInstructInforFlg());
		result.setOvertimeInstructInformation(overtimeInstructInfomation.getOvertimeInstructInfomation());
		result.setManualSendMailAtr(appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value  == 1 ? false : true);
		result.setSendMailWhenApprovalFlg(appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenApprovalFlg().value == 1 ? true : false);
		result.setSendMailWhenRegisterFlg(appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenRegisterFlg().value == 1 ? true : false);
		applicationDto.setPrePostAtr(prePostAtr);
		result.setApplication(applicationDto);
		// 01-09_事前申請を取得
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			// hien thi du lieu thuc te
			result.setReferencePanelFlg(false);
			if(result.getApplication().getPrePostAtr() == InitValueAtr.POST.value && overtimeRestAppCommonSet.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
				result.setReferencePanelFlg(true);
			}
			// hien thi don xin truoc
			result.setAllPreAppPanelFlg(false);
			if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.USE.value && result.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value){
				result.setAllPreAppPanelFlg(true);
			}
		}
		//01-09_事前申請を取得
		if(result.isAllPreAppPanelFlg()){
			if(prePostAtr  == PrePostAtr.POSTERIOR.value ){
				AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(companyID, employeeID,overtimeRestAppCommonSet, appDate,prePostAtr);
				if(appOvertime != null){
					result.setPreAppPanelFlg(true);
					convertOverTimeDto(companyID,preAppOvertimeDto,result,appOvertime);
				}else{
					result.setPreAppPanelFlg(false);
				}
				
			}
		}
		
		
		// ドメインモデル「申請表示設定」．事前事後区分表示をチェックする
		result.setDisplayPrePostFlg(AppDisplayAtr.DISPLAY.value);
		if(appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().value == AppDisplayAtr.NOTDISPLAY.value){
			result.setDisplayPrePostFlg(AppDisplayAtr.NOTDISPLAY.value);
			// 3.事前事後の判断処理(事前事後非表示する場合)
			PrePostAtr prePostAtrJudgment = otherCommonAlgorithm.preliminaryJudgmentProcessing(EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT),overtimeAtr);
			if(prePostAtrJudgment != null){
				prePostAtr = prePostAtrJudgment.value;
			}
		}
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting". Check base date of approval route )
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
			// 6.計算処理 : 
				DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeID, GeneralDate.fromString(appDate, DATE_FORMAT), workTypeCode, siftCD, startTime, endTime,  startTimeRests , endTimeRests);
				Map<Integer,TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
				List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,dailyAttendanceTimeCaculationImport.getFlexTime(),dailyAttendanceTimeCaculationImport.getMidNightTime());
			// 01-18_実績の内容を表示し直す : chưa xử lí
			if(approvalFunctionSetting != null){
				AppOvertimeReference appOvertimeReference = iOvertimePreProcess.getResultContentActual(prePostAtr, siftCD, companyID,employeeID, appDate,approvalFunctionSetting,overtimeHours,overtimeInputCaculations);
				result.setAppOvertimeReference(appOvertimeReference);
			}
			if(approvalFunctionSetting != null){
				// 時刻計算利用チェック
				if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
					result.setDisplayCaculationTime(true);
					List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
					// 07_勤務種類取得: lay loai di lam 
					List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, employeeID,approvalFunctionSetting,appEmploymentWorkType);
					/*if(!CollectionUtil.isEmpty(workTypeOvertimes)){
						result.setWorkType(workTypeOvertimes.get(0));
					}*/
					List<String> workTypeCodes = new ArrayList<>();
					for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
						workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
					}
					result.setWorkTypes(workTypeCodes);
					// 08_就業時間帯取得(lay loai gio lam viec) 
					List<SiftType> siftTypes = overtimeService.getSiftType(companyID, employeeID, approvalFunctionSetting,GeneralDate.fromString(appDate, "yyyy/MM/dd"));
					List<String> siftCodes = new ArrayList<>();
					for(SiftType siftType : siftTypes){
						siftCodes.add(siftType.getSiftCode());
					}
					result.setSiftTypes(siftCodes);
					/*if(!CollectionUtil.isEmpty(siftTypes)){
						result.setSiftType(siftTypes.get(0));
					}*/
					
					
					// 09_勤務種類就業時間帯の初期選択をセットする
					WorkTypeAndSiftType workTypeAndSiftType = overtimeService.getWorkTypeAndSiftTypeByPersonCon(companyID, employeeID, 
							Strings.isBlank(appDate) ? null : GeneralDate.fromString(appDate, "yyyy/MM/dd"), 
							workTypeOvertimes, siftTypes);
					result.setWorkType(workTypeAndSiftType.getWorkType());
					result.setSiftType(workTypeAndSiftType.getSiftType());
					result.setTimezones(workTypeAndSiftType.getBreakTimes().stream().map(domain->{
						DeductionTimeDto dto = new DeductionTimeDto();
						domain.saveToMemento(dto);
						return dto;
					}).collect(Collectors.toList()));
				}else{
					result.setDisplayCaculationTime(false);
				}
			}
			if(approvalFunctionSetting != null){
				if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
					result.setDisplayCaculationTime(true);
					// 01-14_勤務時間取得(lay thoi gian): chua xong  Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」): to do
					RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,approvalFunctionSetting,result.getSiftType() == null ? siftCD : result.getSiftType().getSiftCode());
					result.setDisplayCaculationTime(BooleanUtils.toBoolean(recordWorkOutput.getRecordWorkDisplay().value));
					result.setWorkClockFrom1(recordWorkOutput.getStartTime1());
					result.setWorkClockFrom2(recordWorkOutput.getStartTime2());
					result.setWorkClockTo1(recordWorkOutput.getEndTime1());
					result.setWorkClockTo2(recordWorkOutput.getEndTime2());
				}else{
					result.setDisplayCaculationTime(false);
				}
			}
		String employeeName = "";
		if(Strings.isNotBlank(applicationDto.getApplicantSID())){
			employeeName = employeeAdapter.getEmployeeName(applicationDto.getApplicantSID());
		} else {
			employeeName = employeeAdapter.getEmployeeName(employeeID);
		}
		result.setEmployeeName(employeeName);
		
		return result;
		
	}
	
	/**
	 * @param result
	 * @param uiType
	 * @param appDate
	 * @param companyID
	 * @param employeeID
	 * @param appCommonSettingOutput
	 * @param applicationDto
	 * @param overtimeAtr
	 * @param overTimeInputs
	 */
	private void getData(OverTimeDto result,int uiType,String appDate,String companyID,String employeeID,
			AppCommonSettingOutput appCommonSettingOutput,ApplicationDto_New applicationDto,int overtimeAtr,
			List<OvertimeInputDto> overTimeInputs,PreAppOvertimeDto preAppOvertimeDto,Integer startTime1,Integer endTime1,String reasonContent){
		//申請日付を取得 : lay thong tin lam them
		applicationDto.setApplicationDate(appDate);
		// 01-01_残業通知情報を取得
		OvertimeInstructInfomation overtimeInstructInfomation = iOvertimePreProcess.getOvertimeInstruct(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayOvertimeInstructInforFlg(overtimeInstructInfomation.isDisplayOvertimeInstructInforFlg());
		result.setOvertimeInstructInformation(overtimeInstructInfomation.getOvertimeInstructInfomation());
		//01-02_時間外労働を取得: lay lao dong ngoai thoi gian
		Optional<OvertimeRestAppCommonSetting> otRestAppCommonSet = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		if(otRestAppCommonSet.isPresent()&&(otRestAppCommonSet.get().getExtratimeDisplayAtr()==UseAtr.USE)){
			result.setAgreementTimeDto(AgreeOverTimeDto.fromDomain(agreementTimeService.getAgreementTime(companyID, employeeID)));
		}
		// 01-13_事前事後区分を取得
		DisplayPrePost displayPrePost =	iOvertimePreProcess.getDisplayPrePost(companyID, uiType,appDate,ApplicationType.OVER_TIME_APPLICATION.value,overtimeAtr);
		result.setDisplayPrePostFlg(displayPrePost.getDisplayPrePostFlg());
		applicationDto.setPrePostAtr(displayPrePost.getPrePostAtr());
		result.setApplication(applicationDto);
		result.setPrePostCanChangeFlg(displayPrePost.isPrePostCanChangeFlg());
				
		//String workplaceID = employeeAdapter.getWorkplaceId(companyID, employeeID, GeneralDate.today());
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		if(approvalFunctionSetting != null){
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				result.setDisplayCaculationTime(true);
				
				List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
				// 07_勤務種類取得: lay loai di lam 
				List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, employeeID,approvalFunctionSetting,appEmploymentWorkType);
				
				List<String> workTypeCodes = new ArrayList<>();
				for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
					workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
				}
				result.setWorkTypes(workTypeCodes);
				GeneralDate baseDate = appCommonSettingOutput.generalDate;
				// 08_就業時間帯取得(lay loai gio lam viec) 
				List<SiftType> siftTypes = overtimeService.getSiftType(companyID, employeeID, approvalFunctionSetting,baseDate);
				List<String> siftCodes = new ArrayList<>();
				for(SiftType siftType : siftTypes){
					siftCodes.add(siftType.getSiftCode());
				}
				result.setSiftTypes(siftCodes);
				
				// 09_勤務種類就業時間帯の初期選択をセットする
				WorkTypeAndSiftType workTypeAndSiftType = overtimeService.getWorkTypeAndSiftTypeByPersonCon(companyID, employeeID, 
						Strings.isBlank(appDate) ? null : GeneralDate.fromString(appDate, "yyyy/MM/dd"), workTypeOvertimes, siftTypes);
				result.setWorkType(workTypeAndSiftType.getWorkType());
				result.setSiftType(workTypeAndSiftType.getSiftType());
				result.setTimezones(workTypeAndSiftType.getBreakTimes().stream().map(domain->{
					DeductionTimeDto dto = new DeductionTimeDto();
					domain.saveToMemento(dto);
					return dto;
				}).collect(Collectors.toList()));
				// 01-14_勤務時間取得(lay thoi gian): chua xong  Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」): to do
				RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,approvalFunctionSetting,result.getSiftType() == null? "" :result.getSiftType().getSiftCode());
				result.setDisplayCaculationTime(BooleanUtils.toBoolean(recordWorkOutput.getRecordWorkDisplay().value));
				result.setWorkClockFrom1(recordWorkOutput.getStartTime1());
				result.setWorkClockFrom2(recordWorkOutput.getStartTime2());
				result.setWorkClockTo1(recordWorkOutput.getEndTime1());
				result.setWorkClockTo2(recordWorkOutput.getEndTime2());
				if(startTime1 != null){
					result.setWorkClockFrom1(startTime1);
				}
				if(endTime1 != null){
					result.setWorkClockTo1(endTime1);
				}
				
				// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
				boolean displayRestTime = iOvertimePreProcess.getRestTime(approvalFunctionSetting);
				result.setDisplayRestTime(displayRestTime);
				
			}else{
				result.setDisplayCaculationTime(false);
			}
		}
		
		// 01-03_残業枠を取得: chua xong
		result.setAppOvertimeNightFlg(appCommonSettingOutput.applicationSetting.getAppOvertimeNightFlg().value);
		List<OvertimeWorkFrame> overtimeFrames = iOvertimePreProcess.getOvertimeHours(overtimeAtr,companyID);
		// dùng cho xử lí tính toán
		List<CaculationTime> overTimeHours = new ArrayList<>(); 
		for(OvertimeWorkFrame overtimeFrame :overtimeFrames){
			CaculationTime cal = new CaculationTime();
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			overtimeInputDto.setFrameNo(overtimeFrame.getOvertimeWorkFrNo().v().intValueExact());
			overtimeInputDto.setFrameName(overtimeFrame.getOvertimeWorkFrName().toString());
			cal.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			cal.setFrameNo(overtimeFrame.getOvertimeWorkFrNo().v().intValueExact());
			cal.setFrameName(overtimeFrame.getOvertimeWorkFrName().toString());
			overTimeHours.add(cal);
			overTimeInputs.add(overtimeInputDto);
		}
		for(int i = 11; i<= 12;i++){
			CaculationTime caculationTime = new CaculationTime();
			caculationTime.setAttendanceID(AttendanceType.NORMALOVERTIME.value);
			caculationTime.setFrameNo(i);
			overTimeHours.add(caculationTime);
		}
		
		// lay breakTime
		List<WorkdayoffFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(WorkdayoffFrame breaktimeFrame :breaktimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceType.BREAKTIME.value);
			overtimeInputDto.setFrameNo(breaktimeFrame.getWorkdayoffFrNo().v().intValueExact());
			overtimeInputDto.setFrameName(breaktimeFrame.getWorkdayoffFrName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		
		
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			//時間外表示区分
			result.setExtratimeDisplayFlag(overtimeRestAppCommonSet.get().getExtratimeDisplayAtr().value == 1 ? true : false);
		}
		// 01-04_加給時間を取得
		// dùng cho xử lí tính toán
		List<CaculationTime> bonusTimes = new ArrayList<>();
		if(overtimeRestAppCommonSet.isPresent()){
			result.setDisplayBonusTime(false);
			if(overtimeRestAppCommonSet.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				result.setDisplayBonusTime(true);
				List<BonusPayTimeItem> bonusPayTimeItems= this.iOvertimePreProcess.getBonusTime(employeeID,overtimeRestAppCommonSet,appDate,companyID, result.getSiftType());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItems){
					CaculationTime cal = new CaculationTime();
					OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
					overtimeInputDto.setAttendanceID(AttendanceType.BONUSPAYTIME.value);
					overtimeInputDto.setFrameNo(bonusPayTimeItem.getId());
					overtimeInputDto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
					overtimeInputDto.setTimeItemTypeAtr(bonusPayTimeItem.getTimeItemTypeAtr().value);
					overTimeInputs.add(overtimeInputDto);
					cal.setAttendanceID(AttendanceType.BONUSPAYTIME.value);
					cal.setFrameNo(bonusPayTimeItem.getId());
					cal.setTimeItemTypeAtr(bonusPayTimeItem.getTimeItemTypeAtr().value);
					cal.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
					bonusTimes.add(cal);
				}
			}
		}
		result.setOverTimeInputs(overTimeInputs);
		// 01-05_申請定型理由を取得, 01-06_申請理由を取得
		Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(companyID,  ApplicationType.OVER_TIME_APPLICATION.value);
		if(appTypeDiscreteSetting.isPresent()){
			result.setTypicalReasonDisplayFlg(false);
			// 01-05_申請定型理由を取得
			if(appTypeDiscreteSetting.get().getTypicalReasonDisplayFlg().value == AppDisplayAtr.DISPLAY.value){
				result.setTypicalReasonDisplayFlg(true);
				List<ApplicationReason> applicationReasons = iOvertimePreProcess.getApplicationReasonType(companyID,ApplicationType.OVER_TIME_APPLICATION.value,appTypeDiscreteSetting);
				List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
				for (ApplicationReason applicationReason : applicationReasons) {
					ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
							applicationReason.getReasonTemp().v(), applicationReason.getDefaultFlg().value);
					applicationReasonDtos.add(applicationReasonDto);
				}
				result.setApplicationReasonDtos(applicationReasonDtos);
			}
			//01-06_申請理由を取得
			result.setDisplayAppReasonContentFlg(iOvertimePreProcess.displayAppReasonContentFlg(appTypeDiscreteSetting));
			if(result.isDisplayAppReasonContentFlg()){
				applicationDto.setApplicationReason(reasonContent);
			}
		}
		if(overtimeRestAppCommonSet.isPresent()){
			result.setDisplayDivergenceReasonForm(false);
			//01-08_乖離定型理由を取得
			if(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && overtimeRestAppCommonSet.get().getDivergenceReasonFormAtr().value == UseAtr.USE.value){
				result.setDisplayDivergenceReasonForm(true);
				List<DivergenceReason> divergenceReasons = iOvertimePreProcess.getDivergenceReasonForm(companyID,ApplicationType.OVER_TIME_APPLICATION.value,overtimeRestAppCommonSet);
				convertToDivergenceReasonDto(divergenceReasons,result);
			}
			//01-07_乖離理由を取得
			result.setDisplayDivergenceReasonInput(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && iOvertimePreProcess.displayDivergenceReasonInput(overtimeRestAppCommonSet));
		}
		
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
				//dung cho thay doi xin truoc xin sau
				result.setPerformanceDisplayAtr(true);
			}
			if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.USE.value){
				result.setPreDisplayAtr(true);
			}
			// hien thi du lieu thuc te
			if(result.getApplication().getPrePostAtr() == InitValueAtr.POST.value && overtimeRestAppCommonSet.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
				result.setReferencePanelFlg(true);
			}
			// hien thi don xin truoc
			if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.USE.value && result.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value){
				result.setAllPreAppPanelFlg(true);
			}else{
				result.setAllPreAppPanelFlg(false);
			}
		}
		if(result.isAllPreAppPanelFlg()){
			//01-09_事前申請を取得
			if(result.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value ){
				result.setPreAppPanelFlg(false);
				AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(companyID, employeeID,overtimeRestAppCommonSet, appDate,result.getApplication().getPrePostAtr());
				if(appOvertime != null){
					result.setPreAppPanelFlg(true);
					convertOverTimeDto(companyID,preAppOvertimeDto,result,appOvertime);
				}			
			}
		}
		if(result.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value && appDate != null){
			// 6.計算処理 :
			DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation
					.getCalculation(employeeID, GeneralDate.fromString(appDate, DATE_FORMAT),
							result.getWorkType() == null ? null : result.getWorkType().getWorkTypeCode(),
							result.getSiftType() ==  null ? null : result.getSiftType().getSiftCode(),
							result.getWorkClockFrom1(), result.getWorkClockTo1(), null, null);
			Map<Integer, TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
			List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,
					dailyAttendanceTimeCaculationImport.getFlexTime(),
					dailyAttendanceTimeCaculationImport.getMidNightTime());
			// 01-18_実績の内容を表示し直す : chưa xử lí
			if (approvalFunctionSetting != null) {
				AppOvertimeReference appOvertimeReference = iOvertimePreProcess.getResultContentActual(result.getApplication().getPrePostAtr(), 
						result.getSiftType() ==  null ? null : result.getSiftType().getSiftCode(),
						companyID, employeeID, appDate, approvalFunctionSetting, overTimeHours, overtimeInputCaculations);
				result.setAppOvertimeReference(appOvertimeReference);
			}
		}
		if(appOvertimeSettingRepository.getAppOver().isPresent()){
			// display flex
			if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr().equals(FlexExcessUseSetAtr.NOTDISPLAY)){
				result.setFlexFLag(false);
			}else if(appOvertimeSettingRepository.getAppOver().get().getFlexJExcessUseSetAtr().equals(FlexExcessUseSetAtr.DISPLAY)){
				GeneralDate baseDate = appCommonSettingOutput.generalDate;
				Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeID,baseDate);
				if(personalLablorCodition.isPresent()){
					if(personalLablorCodition.get().getLaborSystem().isFlexTimeWork()){
						result.setFlexFLag(true);
					}else{
						result.setFlexFLag(false);
					}
				}
			}else{
				result.setFlexFLag(true);
			}
			//ドメインモデル「残業申請設定」.勤種変更可否フラグがtrueの場合
			result.setWorkTypeChangeFlg(false);
			if(appOvertimeSettingRepository.getAppOver().get().getWorkTypeChangeFlag().equals(Changeable.CHANGEABLE)){
				result.setWorkTypeChangeFlg(true);
			}
		}
		// xử lí tính toán
		if(result.getApplication().getApplicationDate() != null
				&& result.getWorkClockFrom1() != null
				&& result.getWorkClockTo1() != null
				&& result.getWorkType() != null
				&& !StringUtils.isEmpty(result.getWorkType().getWorkTypeCode()) 
				&& result.getSiftType() != null
				&& !StringUtils.isEmpty(result.getSiftType().getSiftCode())
				&& result.isDisplayCaculationTime()){
			result.setCaculationTimes(this.getCaculationValue(overTimeHours,
					bonusTimes,
					result.getApplication().getPrePostAtr(), 
					appDate,
					result.getSiftType().getSiftCode(),
					result.getWorkType().getWorkTypeCode(),
					result.getWorkClockFrom1(),
					result.getWorkClockTo1(),
					null,
					null));
			result.setResultCaculationTimeFlg(true);
			
		}
		result.setApplication(applicationDto);
	}

	/**
	 * @param divergenceReasons
	 * @param result
	 */
	private void convertToDivergenceReasonDto(List<DivergenceReason> divergenceReasons, OverTimeDto result){
		List<DivergenceReasonDto> divergenceReasonDtos = new ArrayList<>();
		for(DivergenceReason divergenceReason : divergenceReasons){
			DivergenceReasonDto divergenceReasonDto = new DivergenceReasonDto();
			divergenceReasonDto.setDivergenceReasonID(divergenceReason.getReasonTypeItem().getReasonID());
			divergenceReasonDto.setReasonTemp(divergenceReason.getReasonTypeItem().getReasonTemp().toString());
			divergenceReasonDto.setDivergenceReasonIdDefault(divergenceReason.getReasonTypeItem().getDefaultFlg().value);
			
			divergenceReasonDtos.add(divergenceReasonDto);
		}
		result.setDivergenceReasonDtos(divergenceReasonDtos);
	}
	
	/**
	 * @param companyID
	 * @param applicationDto
	 * @param result
	 * @param appOvertime
	 */
	private void convertOverTimeDto(String companyID,PreAppOvertimeDto preAppOvertimeDto, OverTimeDto result,AppOverTime appOvertime){
		if(appOvertime.getApplication() != null){
			if(appOvertime.getApplication().getAppDate() != null){
				preAppOvertimeDto.setAppDatePre(appOvertime.getApplication().getAppDate().toString(DATE_FORMAT));
			}
		}
		
		if (appOvertime.getWorkTypeCode() != null) {
			WorkTypeOvertime workTypeOvertime = new WorkTypeOvertime();
			workTypeOvertime.setWorkTypeCode(appOvertime.getWorkTypeCode().toString());
			Optional<WorkType> workType = workTypeRepository.findByPK(companyID,
					appOvertime.getWorkTypeCode().toString());
			if (workType.isPresent()) {
				workTypeOvertime.setWorkTypeName(workType.get().getName().toString());
			}
			preAppOvertimeDto.setWorkTypePre(workTypeOvertime);
		}
		if (appOvertime.getSiftCode() != null) {
			SiftType siftType = new SiftType();

			siftType.setSiftCode(appOvertime.getSiftCode().toString().equals("000")? "" : appOvertime.getSiftCode().toString());
			Optional<WorkTimeSetting> workTime = workTimeRepository.findByCode(companyID,
					appOvertime.getSiftCode().toString());
			if (workTime.isPresent()) {
				siftType.setSiftName(workTime.get().getWorkTimeDisplayName().getWorkTimeName().toString());
			}
			preAppOvertimeDto.setSiftTypePre(siftType);
		}

		preAppOvertimeDto.setWorkClockFromTo1Pre(convertWorkClockFromTo(appOvertime.getWorkClockFrom1(),appOvertime.getWorkClockTo1()));
		preAppOvertimeDto.setWorkClockFromTo2Pre(convertWorkClockFromTo(appOvertime.getWorkClockFrom2(),appOvertime.getWorkClockTo2()));

		
		List<OvertimeInputDto> overtimeInputDtos = new ArrayList<>();
		List<OverTimeInput> overtimeInputs = appOvertime.getOverTimeInput();
		if (overtimeInputs != null && !overtimeInputs.isEmpty()) {
			List<Integer> frameNo = new ArrayList<>();
			for (OverTimeInput overTimeInput : overtimeInputs) {
				OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
				overtimeInputDto.setAttendanceID(overTimeInput.getAttendanceType().value);
				overtimeInputDto.setFrameNo(overTimeInput.getFrameNo());
				overtimeInputDto.setStartTime(overTimeInput.getStartTime() == null ? null : overTimeInput.getStartTime().v());
				overtimeInputDto.setEndTime(overTimeInput.getEndTime() == null ? null : overTimeInput.getEndTime().v());
				overtimeInputDto.setApplicationTime(overTimeInput.getApplicationTime()== null ? null : overTimeInput.getApplicationTime().v());
				overtimeInputDtos.add(overtimeInputDto);
				frameNo.add(overTimeInput.getFrameNo());
			}
			List<OvertimeWorkFrame> overtimeFrames = this.overtimeFrameRepository.getOvertimeWorkFrameByFrameNos(companyID,frameNo);
			for (OvertimeInputDto overtimeInputDto : overtimeInputDtos) {
				for (OvertimeWorkFrame overtimeFrame : overtimeFrames) {
					if (overtimeInputDto.getFrameNo() == overtimeFrame.getOvertimeWorkFrNo().v().intValueExact()) {
						overtimeInputDto.setFrameName(overtimeFrame.getOvertimeWorkFrName().toString());
						continue;
					}
				}
			}
			preAppOvertimeDto.setOverTimeInputsPre(overtimeInputDtos);
			preAppOvertimeDto.setOverTimeShiftNightPre(appOvertime.getOverTimeShiftNight());
			preAppOvertimeDto.setFlexExessTimePre(appOvertime.getFlexExessTime());
			
		}
		result.setPreAppOvertimeDto(preAppOvertimeDto);
	}
	private String convertWorkClockFromTo(Integer startTime, Integer endTime){
		String WorkClockFromTo = "";
		if(startTime == null && endTime != null){
			TimeWithDayAttr endTimeWithDay = new TimeWithDayAttr(endTime);
			WorkClockFromTo = "　"
					+  "　~　"
					+ endTimeWithDay.getDayDivision().description
					+ convert(endTime);
		}
		if(startTime != null && endTime != null){
			TimeWithDayAttr startTimeWithDay = new TimeWithDayAttr(startTime);
			TimeWithDayAttr endTimeWithDay = new TimeWithDayAttr(endTime);
		 WorkClockFromTo = startTimeWithDay.getDayDivision().description 
							+ convert(startTime) + "　~　"
							+ endTimeWithDay.getDayDivision().description
							+ convert(endTime);
		}
		return WorkClockFromTo;
	}
	private String convert(Integer minute) {
		if(minute == null){
			return null;
		}
		TimeWithDayAttr timeConvert = new TimeWithDayAttr(minute);
		return timeConvert.getInDayTimeWithFormat();
	}


	
	/**
	 * 01-14_勤務時間取得
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param siftCD
	 * @return
	 */
	public RecordWorkDto getRecordWork(String employeeID, String appDate, String siftCD,int prePortAtr,List<CaculationTime> overtimeHours,String workTypeCode,List<Integer> startRestTimes,List<Integer> endRestTimes){
		
		String companyID = AppContexts.user().companyId();
		Integer startTime1 = null; 
		Integer endTime1 = null;
		Integer startTime2 = null;
		Integer endTime2 = null;
		GeneralDate inputDate=  appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT);
		AppOvertimeReference appOvertimeReference = new AppOvertimeReference();
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				1, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), inputDate);
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		// 01-14_勤務時間取得(lay thoi gian): Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」)
		RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,approvalFunctionSetting,siftCD);
		startTime1 = recordWorkOutput.getStartTime1();
		endTime1 = recordWorkOutput.getEndTime1();
		startTime2 = recordWorkOutput.getStartTime2();
		endTime2 = recordWorkOutput.getEndTime2();
		//休憩時間帯を取得する
		BreakTimeZoneSharedOutPut breakTime = this.overtimeService.getBreakTimes(companyID,workTypeCode, siftCD);
		List<DeductionTimeDto> timeZones = breakTime.getLstTimezone().stream().map(domain->{
			DeductionTimeDto dto = new DeductionTimeDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
		// 01-18_実績の内容を表示し直す
		// 6.計算処理 : 
				DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeID,inputDate, workTypeCode, siftCD, startTime1, endTime1, startRestTimes , endRestTimes);
				Map<Integer,TimeWithCalculationImport> overTime = dailyAttendanceTimeCaculationImport.getOverTime();
				List<OvertimeInputCaculation> overtimeInputCaculations = convertMaptoList(overTime,dailyAttendanceTimeCaculationImport.getFlexTime(),dailyAttendanceTimeCaculationImport.getMidNightTime());
		appOvertimeReference = iOvertimePreProcess.getResultContentActual(prePortAtr, siftCD, companyID,employeeID, appDate,approvalFunctionSetting,overtimeHours,overtimeInputCaculations);
		
		return new RecordWorkDto(startTime1, endTime1, startTime2, endTime2, appOvertimeReference, timeZones);
	} 
}
