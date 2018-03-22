package nts.uk.ctx.at.request.app.find.application.holidaywork;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AppHolidayWorkDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.HolidayWorkInputDto;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.DivergenceReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculationImport;
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
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayPreProcess;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayService;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidaySixProcess;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.AppHolidayWorkPreAndReferDto;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HolidayWorkInstruction;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTimeHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTypeHolidayWork;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.TimeItemTypeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.service.AppOvertimeReference;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.DisplayPrePost;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;
import nts.uk.ctx.at.request.dom.application.overtime.service.output.RecordWorkOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.company.divergencereason.DivergenceReason;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborConditionRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AppHolidayWorkFinder {
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
	private HolidayPreProcess holidayPreProcess;
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	@Inject
	private HolidayService holidayService;
	@Inject
	private PersonalLaborConditionRepository personalLaborConditionRepository;
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private DailyAttendanceTimeCaculation dailyAttendanceTimeCaculation;
	@Inject
	private HolidaySixProcess holidaySixProcess; 
	@Inject
	private AppHolidayWorkRepository appHolidayWorkRepository;
	@Inject
	private BeforePreBootMode beforePreBootMode;
	@Inject
	private InitMode initMode;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	
	/**
	 * getAppHolidayWork for start UI KAF010A
	 * @param appDateInput
	 * @param uiType
	 * @return
	 */
	public AppHolidayWorkDto getAppHolidayWork(String appDateInput,int uiType){
		
		AppHolidayWorkDto result = new AppHolidayWorkDto();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		int rootAtr = 1;
		
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				rootAtr, EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class),appDateInput == null ? null : GeneralDate.fromString(appDateInput, DATE_FORMAT));
		result.setManualSendMailAtr(appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value  ==1 ?true : false);
		//アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
		ApprovalRootPattern approvalRootPattern = collectApprovalRootPatternService.getApprovalRootPatternService(companyID, employeeID, EmploymentRootAtr.APPLICATION, EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class), appCommonSettingOutput.generalDate, "", true);
		//アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する 
		 startupErrorCheckService.startupErrorCheck(appCommonSettingOutput.generalDate, ApplicationType.BREAK_TIME_APPLICATION.value, approvalRootPattern.getApprovalRootContentImport());
		 // 申請対象日のパラメータがあるかチェックする
		 

		 if(appDateInput != null){
			 //13.実績の取得
			 AchievementOutput achievementOutput = collectAchievement.getAchievement(companyID, employeeID,  GeneralDate.fromString(appDateInput, DATE_FORMAT));
			
		 }
		 // アルゴリズム「初期データの取得」を実行する
		 getData(companyID,employeeID,appDateInput,appCommonSettingOutput,result,uiType);
		 String employeeName = "";
			if(Strings.isNotBlank(result.getApplication().getApplicantSID())){
				employeeName = employeeAdapter.getEmployeeName(result.getApplication().getApplicantSID());
				result.setEmployeeID(result.getApplication().getApplicantSID());
			} else {
				employeeName = employeeAdapter.getEmployeeName(employeeID);
				result.setEmployeeID(employeeID);
			}
			result.setEmployeeName(employeeName);
		return result;
	}
	/**
	 * findChangeAppDate
	 * @param appDate
	 * @param prePostAtr
	 * @param siftCD
	 * @param breakTime
	 * @return
	 */
	public AppHolidayWorkDto findChangeAppDate(String appDate,int prePostAtr,String siftCD, List<CaculationTime> breakTime){
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		int rootAtr = 1;
		AppHolidayWorkDto result = new AppHolidayWorkDto();
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				rootAtr, EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class),appDate == null? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		// 01-01_休出通知情報を取得
		HolidayWorkInstruction holidayWorkInstruction = holidayPreProcess.getHolidayInstructionInformation(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayHolidayInstructInforFlg(holidayWorkInstruction.isDisplayHolidayWorkInstructInforFlg());
		result.setHolidayInstructInformation(holidayWorkInstruction.getHolidayWorkInstructInfomation());
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.BREAK_TIME_APPLICATION.value);
		
		// 01-09_事前申請を取得
		getPreAppPanel(overtimeRestAppCommonSet,companyID,employeeID,result,appDate,prePostAtr);
		//01-18_実績内容を取得（新規） : TODO
		// ドメインモデル「申請表示設定」．事前事後区分表示をチェックする
		if (appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().value == AppDisplayAtr.NOTDISPLAY.value) {
			result.setDisplayPrePostFlg(AppDisplayAtr.NOTDISPLAY.value);
			// 3.事前事後の判断処理(事前事後非表示する場合)
			PrePostAtr prePostAtrJudgment = otherCommonAlgorithm.preliminaryJudgmentProcessing(
					EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class),
					appCommonSettingOutput.generalDate);
			if(prePostAtrJudgment != null){
				prePostAtr = prePostAtrJudgment.value;
			}
		}else{
			result.setDisplayPrePostFlg(AppDisplayAtr.DISPLAY.value);
		}
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting". Check base date of approval route )
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		if(appCommonSettingOutput.applicationSetting.getBaseDateFlg().value == BaseDateFlg.APP_DATE.value){
			if(approvalFunctionSetting != null){
				// 時刻計算利用チェック
				if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
						getWorkTypeAndWorkTime(companyID,employeeID,appCommonSettingOutput,result);
							siftCD = result.getWorkTime().getSiftCode();
					}
				}
		}
		//01-14_勤務時間取得
		getWorkingHour(companyID,employeeID,appDate,appCommonSettingOutput,result,siftCD);
		return result;
	}
	/**
	 * getCaculationValue
	 * @param breakTime
	 * @param bonusTimes
	 * @param prePostAtr
	 * @param appDate
	 * @param siftCD
	 * @return
	 */
	public List<CaculationTime> getCaculationValue(List<CaculationTime> breakTime ,int prePostAtr,String appDate,String siftCD,String workTydeCode,String employeeID,GeneralDateTime inputDate){
		if(inputDate == null){
			inputDate = GeneralDateTime.now();
		}
		String companyID = AppContexts.user().companyId();
		List<CaculationTime> result = new ArrayList<>();
		// 6.計算処理 : TODO
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeID, GeneralDate.fromString(appDate, DATE_FORMAT), workTydeCode, siftCD, 100, 200, 100, 200);
		// 06-01_色表示チェック
		result = this.holidaySixProcess.checkDisplayColor(breakTime, dailyAttendanceTimeCaculationImport.getHolidayWorkTime(), prePostAtr, inputDate, GeneralDate.fromString(appDate, DATE_FORMAT), ApplicationType.BREAK_TIME_APPLICATION.value, employeeID, companyID, siftCD);
		// 06-02_休出時間を取得
		this.holidaySixProcess.getCaculationHolidayWork(companyID, employeeID, appDate, ApplicationType.BREAK_TIME_APPLICATION.value, result, dailyAttendanceTimeCaculationImport.getHolidayWorkTime());
		return result;
	}
	
	/**
	 * @param appID
	 * @return
	 */
	public AppHolidayWorkDto getAppHolidayWorkByAppID(String appID){
		
		String companyID = AppContexts.user().companyId();
		// 7.休出申請（詳細）起動前処理
		  //14-1.詳細画面起動前申請共通設定を取得する
		Optional<AppHolidayWork> opAppHolidayWork = appHolidayWorkRepository.getFullAppHolidayWork(companyID, appID);
		if(!opAppHolidayWork.isPresent()){
			throw new RuntimeException("khong tim dc doi tuong");
		}
		AppHolidayWork appHolidayWork = opAppHolidayWork.get();
		AppHolidayWorkDto appHolidayWorkDto = AppHolidayWorkDto.fromDomain(appHolidayWork);
		String employeeName = employeeAdapter.getEmployeeName(appHolidayWork.getApplication().getEmployeeID());
		appHolidayWorkDto.setEmployeeName(employeeName);
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				appHolidayWork.getApplication().getEmployeeID(),
				1, EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class), appHolidayWork.getApplication().getAppDate());
		// 14-2.詳細画面起動前モードの判断
		DetailedScreenPreBootModeOutput detailedScreenPreBootModeOutput = this.beforePreBootMode.judgmentDetailScreenMode(companyID, appHolidayWork.getApplication().getEmployeeID(), appID, appCommonSettingOutput.generalDate);
		DetailScreenInitModeOutput detailScreenInitModeOutput = this.initMode.getDetailScreenInitMode(detailedScreenPreBootModeOutput.getUser(), detailedScreenPreBootModeOutput.getReflectPlanState().value);
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		appHolidayWorkDto.setDisplayCaculationTime(false);
		if(approvalFunctionSetting != null){
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				appHolidayWorkDto.setDisplayCaculationTime(true);
				WorkType workType = workTypeRepository.findByPK(companyID, appHolidayWork.getWorkTypeCode().v()).isPresent() ?  workTypeRepository.findByPK(companyID, appHolidayWork.getWorkTypeCode().v()).get() : null ;
				if(workType != null){
					appHolidayWorkDto.setWorkType(new WorkTypeOvertime(workType.getWorkTypeCode().v(),workType.getName().v()));
				}
				Optional<PersonalLaborCondition> personalLablorCodition = personalLaborConditionRepository.findById(appHolidayWork.getApplication().getEmployeeID(),appHolidayWork.getApplication().getAppDate());
				List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
				// 4_b.勤務種類を取得する（詳細）
				WorkTypeHolidayWork WorkTypes =  holidayService.getListWorkType(companyID, appHolidayWork.getApplication().getEmployeeID(), appEmploymentWorkType, appHolidayWork.getApplication().getAppDate(), personalLablorCodition);
				appHolidayWorkDto.setWorkTypes(WorkTypes.getWorkTypeCodes());
				// 5_a.就業時間帯を取得する（詳細）
				List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, appHolidayWork.getApplication().getEmployeeID(),appHolidayWork.getApplication().getAppDate());
				appHolidayWorkDto.setWorkTimes(listWorkTimeCodes);
				WorkTimeSetting workTime =  workTimeRepository.findByCode(companyID,appHolidayWork.getWorkTimeCode().toString()).isPresent() ? workTimeRepository.findByCode(companyID,appHolidayWork.getWorkTimeCode().toString()).get() : null;
				if(workTime != null){
					appHolidayWorkDto.setWorkTime(new SiftType(appHolidayWork.getWorkTimeCode().toString(),workTime.getWorkTimeDisplayName().getWorkTimeName().toString()));
				}
				// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
				boolean displayRestTime = iOvertimePreProcess.getRestTime(approvalFunctionSetting);
				appHolidayWorkDto.setDisplayRestTime(displayRestTime);
			}
		}
		//01-04_加給時間を取得 :TODO
		
		// 01-05_申請定型理由を取得,01-06_申請理由を取得
		getAppReason(appCommonSettingOutput, appHolidayWorkDto,companyID);
		// 01-08_乖離定型理由を取得, 01-07_乖離理由を取得
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.BREAK_TIME_APPLICATION.value);
		getDivigenceReason(overtimeRestAppCommonSet,appHolidayWorkDto,companyID);
		// 01-09_事前申請を取得
		//getPreAppPanel(overtimeRestAppCommonSet,companyID,employeeID,result,appDate);
		// 6.計算処理 : TODO
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(appHolidayWork.getApplication().getEmployeeID(), 
																								appHolidayWork.getApplication().getAppDate(), 
																								appHolidayWork.getWorkTypeCode() == null ? "" : appHolidayWork.getWorkTypeCode().v(),
																								appHolidayWork.getWorkTimeCode() == null ?"" : appHolidayWork.getWorkTimeCode().toString(), 
																								appHolidayWork.getWorkClock1().getStartTime() == null ? null : appHolidayWork.getWorkClock1().getStartTime().v(), 
																								appHolidayWork.getWorkClock1().getEndTime() == null ? null : appHolidayWork.getWorkClock1().getEndTime().v(), 100, 200);
		List<HolidayWorkInputDto> holidayWorkInputDtos = new ArrayList<>();
		getBreaktime(companyID, holidayWorkInputDtos);
		List<HolidayWorkInputDto> breakTimes = appHolidayWorkDto.getHolidayWorkInputDtos().stream().filter(x -> x.getAttendanceType() == AttendanceType.BREAKTIME.value).collect(Collectors.toList());
		List<Integer> breakFrameNos = new ArrayList<>();
		breakTimes.forEach(x -> {
			breakFrameNos.add(x.getFrameNo());
			});
		
		holidayWorkInputDtos.forEach(x -> {
			breakTimes.forEach(breakTime ->{
				if(x.getFrameNo() == breakTime.getFrameNo() ){
					breakTime.setFrameName(x.getFrameName());
				}
			});
		});
		
		// 06-01_色表示チェック
		List<CaculationTime> breakTimeCal = this.holidaySixProcess.checkDisplayColor(convertHolidayWorkInputDtoToCal(breakTimes),
				dailyAttendanceTimeCaculationImport.getHolidayWorkTime(), 
				appHolidayWork.getApplication().getPrePostAtr().value,
				appHolidayWork.getApplication().getInputDate(),
				appHolidayWork.getApplication().getAppDate(),
				ApplicationType.BREAK_TIME_APPLICATION.value,
				appHolidayWork.getApplication().getEmployeeID(), 
				companyID, appHolidayWork.getWorkTimeCode().toString());
		holidayWorkInputDtos.forEach(x -> {
			breakTimeCal.forEach(breakTime -> {
				if(x.getAttendanceType() == breakTime.getAttendanceID()){
					if(x.getFrameNo() == breakTime.getFrameNo()){
						x.setErrorCode(breakTime.getErrorCode());
					}
				}
			});
		});
		for(int i = 1; i < 11; i++){
			HolidayWorkInputDto holidayInputDto = new HolidayWorkInputDto();
			holidayInputDto.setAttendanceType(AttendanceType.RESTTIME.value);
			holidayInputDto.setFrameNo(i);
			holidayInputDto.setFrameName(Integer.toString(i));
			holidayWorkInputDtos.add(holidayInputDto);
		}
		appHolidayWorkDto.getHolidayWorkInputDtos().forEach(item -> {
			holidayWorkInputDtos.stream().filter(x -> 
				(x.getAttendanceType()== item.getAttendanceType())
				&& (x.getFrameNo()==item.getFrameNo())).findAny().ifPresent(value -> {
						value.setStartTime(item.getStartTime());
						value.setEndTime(item.getEndTime());
						value.setApplicationTime(item.getApplicationTime());
					});;
		});
		appHolidayWorkDto.setHolidayWorkInputDtos(holidayWorkInputDtos);
		return appHolidayWorkDto;
	}
	
	/**
	 * 01-14_勤務時間取得
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param siftCD
	 * @return
	 */
	public RecordWorkDto getRecordWork(String employeeID, String appDate, String siftCD,int prePortAtr,List<CaculationTime> overtimeHours){
		String companyID = AppContexts.user().companyId();
		Integer startTime1 = -1; 
		Integer endTime1 = -1;
		Integer startTime2 = -1;
		Integer endTime2 = -1;
		AppOvertimeReference appOvertimeReference = new AppOvertimeReference();
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				1, EnumAdaptor.valueOf(ApplicationType.BREAK_TIME_APPLICATION.value, ApplicationType.class), appDate == null ? null : GeneralDate.fromString(appDate, DATE_FORMAT));
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		// 01-14_勤務時間取得(lay thoi gian): Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」)
		RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,approvalFunctionSetting,siftCD);
		startTime1 = recordWorkOutput.getStartTime1();
		endTime1 = recordWorkOutput.getEndTime1();
		startTime2 = recordWorkOutput.getStartTime2();
		endTime2 = recordWorkOutput.getEndTime2();
		// 01-18_実績の内容を表示し直す
		//appOvertimeReference = iOvertimePreProcess.getResultContentActual(prePortAtr, siftCD, companyID,employeeID, appDate,approvalFunctionSetting,overtimeHours);
		
		return new RecordWorkDto(startTime1, endTime1, startTime2, endTime2, appOvertimeReference);
	}
	
	/**
	 * convertHolidayWorkInputDtoToCal
	 * @param dtos
	 * @return
	 */
	private List<CaculationTime> convertHolidayWorkInputDtoToCal(List<HolidayWorkInputDto> dtos){
		List<CaculationTime> calTimes = new ArrayList<>();
		for(HolidayWorkInputDto dto : dtos){
			CaculationTime cal = new CaculationTime();
			cal.setAttendanceID(dto.getAttendanceType());
			cal.setFrameNo(dto.getFrameNo());
			cal.setApplicationTime(dto.getApplicationTime());
			cal.setFrameName(dto.getFrameName());
			calTimes.add(cal);
		}
		return calTimes;
	}
	/**
	 * 01_初期データ取得
	 * @param companyID
	 * @param employeeID
	 * @param appDateHolidayWork
	 * @param appCommonSettingOutput
	 * @param result
	 */
	private void getData(String companyID,String employeeID,String appDate,AppCommonSettingOutput appCommonSettingOutput,AppHolidayWorkDto result,int uiType){
		ApplicationDto_New applicationDto = new ApplicationDto_New();
		List<HolidayWorkInputDto> holidayWorkInputDtos = new ArrayList<>();
		//01-12_申請日付取得
		applicationDto.setApplicationDate(appDate);
		// 01-01_休出通知情報を取得
		HolidayWorkInstruction holidayWorkInstruction = holidayPreProcess.getHolidayInstructionInformation(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayHolidayInstructInforFlg(holidayWorkInstruction.isDisplayHolidayWorkInstructInforFlg());
		result.setHolidayInstructInformation(holidayWorkInstruction.getHolidayWorkInstructInfomation());
		//01-02_時間外労働を取得 : waitting
		// 01-13_事前事後区分を取得
		DisplayPrePost displayPrePost =	iOvertimePreProcess.getDisplayPrePost(companyID, uiType,appDate,ApplicationType.BREAK_TIME_APPLICATION.value);
		result.setDisplayPrePostFlg(displayPrePost.getDisplayPrePostFlg());
		applicationDto.setPrePostAtr(displayPrePost.getPrePostAtr());
		result.setApplication(applicationDto);
		//4.勤務種類を取得する, 5.就業時間帯を取得する, 01-17_休憩時間取得
		getWorkTypeAndWorkTime(companyID,employeeID,appCommonSettingOutput,result);
		//01-14_勤務時間取得
		getWorkingHour(companyID,employeeID,appDate,appCommonSettingOutput,result,"");
		// 01-03_休出時間を取得
		getBreaktime(companyID, holidayWorkInputDtos);
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.BREAK_TIME_APPLICATION.value);
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			//時間外表示区分
			result.setExtratimeDisplayFlag(overtimeRestAppCommonSet.get().getExtratimeDisplayAtr().value == 1 ? true : false);
		}
		// 01-04_加給時間を取得
		if(overtimeRestAppCommonSet.isPresent()){
			result.setDisplayBonusTime(false);
			if(overtimeRestAppCommonSet.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				result.setDisplayBonusTime(true);
				List<BonusPayTimeItem> bonusPayTimeItems= this.iOvertimePreProcess.getBonusTime(employeeID,overtimeRestAppCommonSet,appDate,companyID, result.getWorkTime());
				List<BonusPayTimeItem> bonusPayTimeItemNotSpecial = bonusPayTimeItems.stream().filter(x -> x.getTimeItemTypeAtr().value == TimeItemTypeAtr.NORMAL_TYPE.value).collect(Collectors.toList());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItemNotSpecial){
					holidayWorkInputDtos.add(convertBonusPayTimeItem(bonusPayTimeItem,AttendanceType.BONUSPAYTIME.value));
				}
				List<BonusPayTimeItem> bonusPayTimeItemSpecial = bonusPayTimeItems.stream().filter(x -> x.getTimeItemTypeAtr().value == TimeItemTypeAtr.SPECIAL_TYPE.value).collect(Collectors.toList());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItemSpecial){
					holidayWorkInputDtos.add(convertBonusPayTimeItem(bonusPayTimeItem,AttendanceType.BONUSSPECIALDAYTIME.value));
				}
			}
		}
		result.setHolidayWorkInputDtos(holidayWorkInputDtos);
		// 01-05_申請定型理由を取得,01-06_申請理由を取得
		getAppReason(appCommonSettingOutput, result,companyID);
		// 01-08_乖離定型理由を取得, 01-07_乖離理由を取得
		getDivigenceReason(overtimeRestAppCommonSet,result,companyID);
		// 01-09_事前申請を取得
		//getPreAppPanel(overtimeRestAppCommonSet,companyID,employeeID,result,appDate,result.getApplication().getPrePostAtr());
	}
	/**
	 * 01-03_休出時間を取得
	 * @param companyID
	 * @param holidayWorkInputDtos
	 */
	private void getBreaktime(String companyID,List<HolidayWorkInputDto> holidayWorkInputDtos){
		List<WorkdayoffFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(WorkdayoffFrame breaktimeFrame :breaktimeFrames){
			HolidayWorkInputDto holidayWorkInputDto = new HolidayWorkInputDto();
			holidayWorkInputDto.setAttendanceType(AttendanceType.BREAKTIME.value);
			holidayWorkInputDto.setFrameNo(breaktimeFrame.getWorkdayoffFrNo().v().intValueExact());
			holidayWorkInputDto.setFrameName(breaktimeFrame.getWorkdayoffFrName().toString());
			holidayWorkInputDtos.add(holidayWorkInputDto);
		}
	}
	/**
	 * get preApp and set display preApp, appRefer
	 * @param overtimeRestAppCommonSet
	 * @param companyID
	 * @param employeeID
	 * @param result
	 * @param appDate
	 */
	private void getPreAppPanel(Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet, String companyID,String employeeID,AppHolidayWorkDto result,String appDate,int prePost){
		// xu li hien thi du lieu xin truoc
				if(overtimeRestAppCommonSet.isPresent()){
					// hien thi du lieu thuc te
					if(prePost == InitValueAtr.POST.value && overtimeRestAppCommonSet.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
						result.setReferencePanelFlg(true);
					}
					// hien thi don xin truoc
					if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.NOTUSE.value && prePost  == PrePostAtr.POSTERIOR.value){
						result.setAllPreAppPanelFlg(false);
					}else{
						result.setAllPreAppPanelFlg(true);
					}
				}
				// 01-09_事前申請を取得
				if(result.isAllPreAppPanelFlg()){
					//01-09_事前申請を取得
					if(prePost  == PrePostAtr.POSTERIOR.value ){
						result.setPreAppPanelFlg(false);
						AppHolidayWorkPreAndReferDto appHolidayWork = holidayPreProcess.getPreApplicationHoliday(companyID, employeeID,overtimeRestAppCommonSet, appDate,prePost);
						if(appHolidayWork != null){
							result.setPreAppPanelFlg(true);
							result.setPreAppHolidayWorkDto(appHolidayWork);
						}			
					}
				}
	}
	/**
	 * 4.勤務種類を取得する, 5.就業時間帯を取得する,01-14_勤務時間取得, 01-17_休憩時間取得
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param appCommonSettingOutput
	 * @param result
	 */
	private void getWorkTypeAndWorkTime(String companyID,String employeeID,AppCommonSettingOutput appCommonSettingOutput,AppHolidayWorkDto result){
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		result.setDisplayCaculationTime(false);
		if(approvalFunctionSetting != null){
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				result.setDisplayCaculationTime(true);
				// 4.勤務種類を取得する : TODO
				GeneralDate baseDate = appCommonSettingOutput.generalDate;
				//ドメインモデル「個人労働条件」を取得する(lay dieu kien lao dong ca nhan(個人労働条件))
				Optional<PersonalLaborCondition> personalLablorCodition = personalLaborConditionRepository.findById(employeeID,baseDate);
				List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
				WorkTypeHolidayWork WorkTypes =  holidayService.getWorkTypes(companyID, employeeID, appEmploymentWorkType, baseDate, personalLablorCodition);
				result.setWorkTypes(WorkTypes.getWorkTypeCodes());
				WorkTypeOvertime workType = new WorkTypeOvertime();
				workType.setWorkTypeCode(WorkTypes.getWorkTypeCode());
				workType.setWorkTypeName(WorkTypes.getWorkTypeName());
				result.setWorkType(workType);
				// 5.就業時間帯を取得する
				WorkTimeHolidayWork workTimes = this.holidayService.getWorkTimeHolidayWork(companyID, employeeID, baseDate, personalLablorCodition);
				result.setWorkTimes(workTimes.getWorkTimeCodes());
				SiftType workTime = new SiftType();
				workTime.setSiftCode(workTimes.getWorkTimeCode());
				workTime.setSiftName(workTimes.getWorkTimeName());
				result.setWorkTime(workTime);
				
				// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
				boolean displayRestTime = iOvertimePreProcess.getRestTime(approvalFunctionSetting);
				result.setDisplayRestTime(displayRestTime);
			}
		}
	}
	/**
	 * getWorkingHour
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param appCommonSettingOutput
	 * @param result
	 * @param workTimeCode
	 */
	private void getWorkingHour(String companyID,String employeeID,String appDate,AppCommonSettingOutput appCommonSettingOutput,AppHolidayWorkDto result,String workTimeCode){
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSettingOutput.approvalFunctionSetting;
		if(approvalFunctionSetting != null){
			result.setDisplayCaculationTime(false);
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				result.setDisplayCaculationTime(true);
				// 01-14_勤務時間取得(lay thoi gian): chua xong  Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」): to do
				RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,approvalFunctionSetting,result.getWorkTime() == null ? workTimeCode : result.getWorkTime().getSiftCode());
				result.setWorkClockStart1(recordWorkOutput.getStartTime1());
				result.setWorkClockStart2(recordWorkOutput.getStartTime2());
				result.setWorkClockEnd1(recordWorkOutput.getEndTime1());
				result.setWorkClockEnd2(recordWorkOutput.getEndTime2());
			}
		}
	}
	/**
	 * convert BonusPayTimeItem -> HolidayWorkInputDto
	 * @param bonusPayTimeItem
	 * @param attendanceType
	 * @return
	 */
	private HolidayWorkInputDto convertBonusPayTimeItem(BonusPayTimeItem bonusPayTimeItem,int attendanceType){
		HolidayWorkInputDto holidayWorkInputDto = new HolidayWorkInputDto();
		holidayWorkInputDto.setAttendanceType(attendanceType);
		holidayWorkInputDto.setFrameNo(bonusPayTimeItem.getId());
		holidayWorkInputDto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
		return holidayWorkInputDto;
	}
	/**
	 * 01-05_申請定型理由を取得,01-06_申請理由を取得
	 * @param appCommonSettingOutput
	 * @param result
	 * @param companyID
	 */
	private void getAppReason(AppCommonSettingOutput appCommonSettingOutput,AppHolidayWorkDto result,String companyID){
		List<AppTypeDiscreteSetting> appTypeDiscreteSettings = appCommonSettingOutput.appTypeDiscreteSettings;
		if(!CollectionUtil.isEmpty(appTypeDiscreteSettings)){
			Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting = Optional.of(appTypeDiscreteSettings.get(0));
			// 01-05_申請定型理由を取得
			result.setTypicalReasonDisplayFlg(false);
			if(appTypeDiscreteSetting.get().getTypicalReasonDisplayFlg().value == AppDisplayAtr.DISPLAY.value){
				result.setTypicalReasonDisplayFlg(true);
				List<ApplicationReason> applicationReasons = iOvertimePreProcess.getApplicationReasonType(companyID,ApplicationType.BREAK_TIME_APPLICATION.value,appTypeDiscreteSetting);
				List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
				for (ApplicationReason applicationReason : applicationReasons) {
					ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
							applicationReason.getReasonTemp(), applicationReason.getDefaultFlg().value);
					applicationReasonDtos.add(applicationReasonDto);
				}
				result.setApplicationReasonDtos(applicationReasonDtos);
			}
			//01-06_申請理由を取得
			result.setDisplayAppReasonContentFlg(iOvertimePreProcess.displayAppReasonContentFlg(appTypeDiscreteSetting));
		}
	}
	/**
	 * 01-08_乖離定型理由を取得, 01-07_乖離理由を取得
	 * @param overtimeRestAppCommonSet
	 * @param result
	 * @param companyID
	 */
	private void getDivigenceReason(Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet,AppHolidayWorkDto result,String companyID){
		
		if(overtimeRestAppCommonSet.isPresent()){
			//01-08_乖離定型理由を取得
			result.setDisplayDivergenceReasonForm(false);
			if(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && overtimeRestAppCommonSet.get().getDivergenceReasonFormAtr().value == UseAtr.USE.value){
				result.setDisplayDivergenceReasonForm(true);
				List<DivergenceReason> divergenceReasons = iOvertimePreProcess.getDivergenceReasonForm(companyID,ApplicationType.BREAK_TIME_APPLICATION.value,overtimeRestAppCommonSet);
				convertToDivergenceReasonDto(divergenceReasons,result);
			}
			//01-07_乖離理由を取得
			result.setDisplayDivergenceReasonInput(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && iOvertimePreProcess.displayDivergenceReasonInput(overtimeRestAppCommonSet));
		}
	}
	
	/**
	 * convertToDivergenceReasonDto
	 * @param divergenceReasons
	 * @param result
	 */
	private void convertToDivergenceReasonDto(List<DivergenceReason> divergenceReasons, AppHolidayWorkDto result){
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
}
