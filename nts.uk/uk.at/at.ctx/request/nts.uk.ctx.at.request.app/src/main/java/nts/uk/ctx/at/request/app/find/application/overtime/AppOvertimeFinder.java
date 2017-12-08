package nts.uk.ctx.at.request.app.find.application.overtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.DivergenceReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OverTimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeInputDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.PreAppOvertimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.StartApprovalRootService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.StartCheckErrorService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceID;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.overtime.TimeItemTypeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.service.AppOvertimeReference;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.DisplayPrePost;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeInstructInfomation;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeAndSiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;
import nts.uk.ctx.at.request.dom.application.overtime.service.output.RecordWorkOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.company.divergencereason.DivergenceReason;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.request.dom.setting.requestofeach.RequestAppDetailSetting;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.employmentrule.hourlate.breaktime.breaktimeframe.BreaktimeFrame;
import nts.uk.ctx.at.shared.dom.employmentrule.hourlate.overtime.overtimeframe.OvertimeFrame;
import nts.uk.ctx.at.shared.dom.employmentrule.hourlate.overtime.overtimeframe.OvertimeFrameRepository;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AppOvertimeFinder {
	final String DATE_FORMAT = "yyyy/MM/dd";
	
	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	
	@Inject
	private StartApprovalRootService startApprovalRootService;
	
	@Inject
	private  StartCheckErrorService  startCheckErrorService;
	
	@Inject
	private OvertimeService overtimeService;
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	
	@Inject
	private WorkTimeRepository workTimeRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Inject
	private OvertimeFrameRepository overtimeFrameRepository;
	
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private OvertimeRepository overtimeRepository;
	
	@Inject
	private OvertimeSixProcess overtimeSixProcess;
	
	@Inject
	private BPTimeItemRepository bPTimeItemRepository;
	
	/**
	 * @param url
	 * @param appDate
	 * @param uiType
	 * @return
	 */
	public OverTimeDto getOvertimeByUIType(String url,String appDate,int uiType){
		
		OverTimeDto result = new OverTimeDto();
		ApplicationDto applicationDto = new ApplicationDto();
		List<OvertimeInputDto> overTimeInputs = new ArrayList<>();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		int rootAtr = 1;
		PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
		
		
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				rootAtr, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT));
		result.setManualSendMailAtr(appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value  ==1 ?true : false);
		//アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
		//startApprovalRootService.getApprovalRootPattern(companyID, employeeID, 1, ApplicationType.OVER_TIME_APPLICATION.value, null);
		//アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する 
		startCheckErrorService.checkError(ApplicationType.OVER_TIME_APPLICATION.value);
		// 02_残業区分チェック : check loai lam them
		int overtimeAtr = overtimeService.checkOvertime(url);
		result.setOvertimeAtr(overtimeAtr);
		// 01_初期データ取得
		getData(result,uiType,appDate,companyID,employeeID,appCommonSettingOutput,applicationDto,overtimeAtr,overTimeInputs,preAppOvertimeDto);
		
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
	public List<CaculationTime> getCaculationValue(List<CaculationTime> overtimeHours,List<CaculationTime> bonusTimes,int prePostAtr,String appDate){
		 
		List<CaculationTime> caculationTimes = new ArrayList<>();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		GeneralDateTime inputDate = GeneralDateTime.now();
		int rootAtr = 1;
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
						employeeID,
						rootAtr, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT));
		
		/*
		 * 06_計算処理
		 * TODO
		 */
		// 06-01_色表示チェック
		List<OvertimeInputCaculation> overtimeInputCaculations = new ArrayList<>();
		if(appCommonSettingOutput.requestOfEachCommon != null){
			List<RequestAppDetailSetting> requestAppDetailSettings = appCommonSettingOutput.requestOfEachCommon.getRequestAppDetailSettings();
//			if(requestAppDetailSettings != null){
//				this.overtimeSixProcess.checkDisplayColor(convert(overtimeHours),
//						overtimeInputCaculations,
//						prePostAtr,
//						inputDate,
//						GeneralDate.fromString(appDate, DATE_FORMAT),
//						ApplicationType.OVER_TIME_APPLICATION.value,
//						employeeID, 
//						companyID, 
//						requestAppDetailSettings.get(0));
//			}
			
		}
		// 06-02_残業時間を取得
		List<CaculationTime> caculationTimeHours = this.overtimeSixProcess.getCaculationOvertimeHours(companyID, employeeID, appDate, ApplicationType.OVER_TIME_APPLICATION.value,overtimeHours);
		
		
		// 06-03_加給時間を取得
		List<CaculationTime> caculationTimeBonus= this.overtimeSixProcess.getCaculationBonustime(companyID, employeeID, appDate,  ApplicationType.OVER_TIME_APPLICATION.value,bonusTimes);
		
		for(CaculationTime overtimeHour : caculationTimeHours){
			caculationTimes.add(overtimeHour);
		}
		for(CaculationTime bonusTime : caculationTimeBonus){
			caculationTimes.add(bonusTime);
		}
		// 計算フラグ=0
		
		return caculationTimes;
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
			throw new RuntimeException("khong tim dc doi tuong");
		}
		AppOverTime appOverTime = opAppOverTime.get();
		OverTimeDto overTimeDto = OverTimeDto.fromDomain(appOverTime);
		String employeeName = employeeAdapter.getEmployeeName(appOverTime.getApplication().getApplicantSID());
		overTimeDto.setEmployeeName(employeeName);
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				appOverTime.getApplication().getApplicantSID(),
				1, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), appOverTime.getApplication().getApplicationDate());
		List<RequestAppDetailSetting> requestAppDetailSettings = appCommonSettingOutput.requestOfEachCommon.getRequestAppDetailSettings();
		if(requestAppDetailSettings != null){
			List<RequestAppDetailSetting>  requestAppDetailSetting = requestAppDetailSettings.stream().filter( c -> c.appType == ApplicationType.OVER_TIME_APPLICATION).collect(Collectors.toList());
			if(requestAppDetailSetting != null){
				// 時刻計算利用チェック
				if (requestAppDetailSetting.get(0).getTimeCalUseAtr().value == UseAtr.USE.value) {
					overTimeDto.setDisplayCaculationTime(true);
					// 07_勤務種類取得: lay loai di lam 
					WorkType workType = workTypeRepository.findByPK(companyID, appOverTime.getWorkTypeCode().v()).get();
					overTimeDto.setWorkType(new WorkTypeOvertime(workType.getWorkTypeCode().v(),workType.getName().v()));
					
					List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
					List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, appOverTime.getApplication().getApplicantSID(),requestAppDetailSetting.get(0),appEmploymentWorkType);
					
					List<String> workTypeCodes = new ArrayList<>();
					for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
						workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
					}
					overTimeDto.setWorkTypes(workTypeCodes);
					
					// 08_就業時間帯取得(lay loai gio lam viec) 
					List<SiftType> siftTypes = overtimeService.getSiftType(companyID, appOverTime.getApplication().getApplicantSID(), requestAppDetailSetting.get(0));
					List<String> siftCodes = new ArrayList<>();
					for(SiftType siftType : siftTypes){
						siftCodes.add(siftType.getSiftCode());
					}
					overTimeDto.setSiftTypes(siftCodes);
					
					WorkTime workTime = workTimeRepository.findByCode(companyID, appOverTime.getSiftCode().v()).get();
					overTimeDto.setSiftType(new SiftType(workTime.getSiftCD().v(), workTime.getWorkTimeDisplayName().getWorkTimeName().v()));
					
					;
					
					// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
					boolean displayRestTime = iOvertimePreProcess.getRestTime(requestAppDetailSetting.get(0));
					overTimeDto.setDisplayRestTime(displayRestTime);
					
				}else{
					overTimeDto.setDisplayCaculationTime(false);
				}
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
							applicationReason.getReasonTemp(), applicationReason.getDefaultFlg().value);
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
			//01-07_乖離理由を取得
			overTimeDto.setDisplayDivergenceReasonInput(overTimeDto.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && iOvertimePreProcess.displayDivergenceReasonInput(overtimeRestAppCommonSet));
		}
		List<OvertimeInputDto> overTimeInputs = new ArrayList<>();
		// 01-03_残業枠を取得: chua xong
		overTimeDto.setAppOvertimeNightFlg(appCommonSettingOutput.applicationSetting.getAppOvertimeNightFlg().value);
		List<OvertimeFrame> overtimeFrames = iOvertimePreProcess.getOvertimeHours(appOverTime.getOverTimeAtr().value,companyID);
		
		for(OvertimeFrame overtimeFrame :overtimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceID.NORMALOVERTIME.value);
			overtimeInputDto.setFrameNo(overtimeFrame.getOtFrameNo());
			overtimeInputDto.setFrameName(overtimeFrame.getOvertimeFrameName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		OvertimeInputDto overtimeInputShiftNight = new OvertimeInputDto();
		overtimeInputShiftNight.setAttendanceID(AttendanceID.NORMALOVERTIME.value);
		overtimeInputShiftNight.setFrameNo(11);
		overTimeInputs.add(overtimeInputShiftNight);
		
		OvertimeInputDto overtimeInputFlexExessTime = new OvertimeInputDto();
		overtimeInputFlexExessTime.setAttendanceID(AttendanceID.NORMALOVERTIME.value);
		overtimeInputFlexExessTime.setFrameNo(12);
		overTimeInputs.add(overtimeInputFlexExessTime);
		
		// lay breakTime
		List<BreaktimeFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(BreaktimeFrame breaktimeFrame :breaktimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceID.BREAKTIME.value);
			overtimeInputDto.setFrameNo(breaktimeFrame.getBreakTimeFrameNo());
			overtimeInputDto.setFrameName(breaktimeFrame.getBreakTimeFrameName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		
		
		// 01-04_加給時間を取得: chua xong
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				overTimeDto.setDisplayBonusTime(true);
				List<BonusPayTimeItem> bonusPayTimeItems= this.iOvertimePreProcess.getBonusTime(
						appOverTime.getApplication().getApplicantSID(),
						overtimeRestAppCommonSet,
						appOverTime.getApplication().getApplicationDate().toString(DATE_FORMAT),
						companyID, 
						overTimeDto.getSiftType());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItems){
					OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
					overtimeInputDto.setAttendanceID(AttendanceID.BONUSPAYTIME.value);
					overtimeInputDto.setFrameNo(bonusPayTimeItem.getId());
					overtimeInputDto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
					overtimeInputDto.setTimeItemTypeAtr(bonusPayTimeItem.getTimeItemTypeAtr().value);
					overTimeInputs.add(overtimeInputDto);
				}
			}else{
				overTimeDto.setDisplayBonusTime(false);
			}
		}
		for(int i = 1; i < 11; i++){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceID.RESTTIME.value);
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
		
		overTimeDto.setOverTimeInputs(overTimeInputs);
		//01-09_事前申請を取得
				if(overTimeDto.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value ){
					AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(appOverTime.getApplication().getApplicantSID(),overtimeRestAppCommonSet, appOverTime.getApplication().getApplicationDate().toString(DATE_FORMAT),appOverTime.getApplication().getPrePostAtr().value);
					if(appOvertime != null){
						PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
						convertOverTimeDto(companyID,preAppOvertimeDto,overTimeDto,appOvertime);
					}else{
						overTimeDto.setPreAppPanelFlg(false);
					}
					
		}
				
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.NOTUSE.value){
				overTimeDto.setAllPreAppPanelFlg(false);
			}else{
				overTimeDto.setAllPreAppPanelFlg(true);
			}
		}
		
		return overTimeDto;
	} 
	
	/**
	 * @param appDate
	 * @param prePostAtr
	 * @return
	 */
	public OverTimeDto findByChangeAppDate(String appDate,int prePostAtr,String siftCD, List<CaculationTime> overtimeHours){
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		OverTimeDto result = new OverTimeDto();
		ApplicationDto applicationDto = new ApplicationDto();
		PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
		// 申請日を変更する : chưa xử lí(Hung)
		
		// 01-01_残業通知情報を取得
		int rootAtr = 1;
		AppCommonSettingOutput appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID,
				rootAtr, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT));
		OvertimeInstructInfomation overtimeInstructInfomation = iOvertimePreProcess.getOvertimeInstruct(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayOvertimeInstructInforFlg(overtimeInstructInfomation.isDisplayOvertimeInstructInforFlg());
		result.setOvertimeInstructInformation(overtimeInstructInfomation.getOvertimeInstructInfomation());
		applicationDto.setPrePostAtr(prePostAtr);
		result.setApplication(applicationDto);
		// 01-09_事前申請を取得
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		if(prePostAtr  == PrePostAtr.POSTERIOR.value ){
			
			AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(employeeID,overtimeRestAppCommonSet, appDate,prePostAtr);
			if(appOvertime != null){
				result.setPreAppPanelFlg(true);
				convertOverTimeDto(companyID,preAppOvertimeDto,result,appOvertime);
			}else{
				result.setPreAppPanelFlg(false);
			}
			
		}
		
		// ドメインモデル「申請表示設定」．事前事後区分表示をチェックする
//		if(appCommonSettingOutput.applicationSetting.getDisplayPrePostFlg().value == AppDisplayAtr.NOTDISPLAY.value){
//			// 3.事前事後の判断処理(事前事後非表示する場合)
//			PrePostAtr prePostAtrJudgment = otherCommonAlgorithm.preliminaryJudgmentProcessing(EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT));
//		}
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting". Check base date of approval route )
		List<RequestAppDetailSetting> requestAppDetailSettings = appCommonSettingOutput.requestOfEachCommon.getRequestAppDetailSettings();
		if(requestAppDetailSettings != null){
			List<RequestAppDetailSetting>  requestAppDetailSetting = requestAppDetailSettings.stream().filter( c -> c.appType == ApplicationType.OVER_TIME_APPLICATION).collect(Collectors.toList());
			// 01-18_実績の内容を表示し直す : chưa xử lí
			if(requestAppDetailSetting != null){
				AppOvertimeReference appOvertimeReference = iOvertimePreProcess.getResultContentActual(prePostAtr, siftCD, companyID,employeeID, appDate,requestAppDetailSetting.get(0),overtimeHours);
				result.setAppOvertimeReference(appOvertimeReference);
			}
			if(appCommonSettingOutput.applicationSetting.getBaseDateFlg().value == BaseDateFlg.APP_DATE.value){
				if(requestAppDetailSetting != null){
					// 時刻計算利用チェック
					if (requestAppDetailSetting.get(0).getTimeCalUseAtr().value == UseAtr.USE.value) {
						result.setDisplayCaculationTime(true);
						List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
						// 07_勤務種類取得: lay loai di lam 
						List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, employeeID,requestAppDetailSetting.get(0),appEmploymentWorkType);
						if(!CollectionUtil.isEmpty(workTypeOvertimes)){
							result.setWorkType(workTypeOvertimes.get(0));
						}
						List<String> workTypeCodes = new ArrayList<>();
						for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
							workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
						}
						result.setWorkTypes(workTypeCodes);
						
						// 08_就業時間帯取得(lay loai gio lam viec) 
						List<SiftType> siftTypes = overtimeService.getSiftType(companyID, employeeID, requestAppDetailSetting.get(0));
						List<String> siftCodes = new ArrayList<>();
						for(SiftType siftType : siftTypes){
							siftCodes.add(siftType.getSiftCode());
						}
						result.setSiftTypes(siftCodes);
						if(!CollectionUtil.isEmpty(siftTypes)){
							result.setSiftType(siftTypes.get(0));
						}
					}else{
						result.setDisplayCaculationTime(false);
					}
				}
			}
		if(requestAppDetailSetting != null){
			if (requestAppDetailSetting.get(0).getTimeCalUseAtr().value == UseAtr.USE.value) {
				result.setDisplayCaculationTime(true);
				// 01-14_勤務時間取得(lay thoi gian): chua xong  Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」): to do
				RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,requestAppDetailSetting.get(0),result.getSiftType() == null ? "" : result.getSiftType().getSiftCode());
				result.setDisplayCaculationTime(BooleanUtils.toBoolean(recordWorkOutput.getRecordWorkDisplay().value));
				result.setWorkClockFrom1(recordWorkOutput.getStartTime1());
				result.setWorkClockFrom2(recordWorkOutput.getStartTime2());
				result.setWorkClockTo1(recordWorkOutput.getEndTime1());
				result.setWorkClockTo2(recordWorkOutput.getEndTime2());
			}else{
				result.setDisplayCaculationTime(false);
			}
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
			AppCommonSettingOutput appCommonSettingOutput,ApplicationDto applicationDto,int overtimeAtr,
			List<OvertimeInputDto> overTimeInputs,PreAppOvertimeDto preAppOvertimeDto){
		//申請日付を取得 : lay thong tin lam them
		applicationDto.setApplicationDate(appDate);
		// 01-01_残業通知情報を取得
		OvertimeInstructInfomation overtimeInstructInfomation = iOvertimePreProcess.getOvertimeInstruct(appCommonSettingOutput, appDate, employeeID);
		result.setDisplayOvertimeInstructInforFlg(overtimeInstructInfomation.isDisplayOvertimeInstructInforFlg());
		result.setOvertimeInstructInformation(overtimeInstructInfomation.getOvertimeInstructInfomation());
		//01-02_時間外労働を取得: lay lao dong ngoai thoi gian
		/*
		 * chưa phải làm
		 */
		// 01-13_事前事後区分を取得
		DisplayPrePost displayPrePost =	iOvertimePreProcess.getDisplayPrePost(companyID, uiType,appDate);
		result.setDisplayPrePostFlg(displayPrePost.getDisplayPrePostFlg());
		applicationDto.setPrePostAtr(displayPrePost.getPrePostAtr());
		if(displayPrePost.getPrePostAtr() == InitValueAtr.POST.value){
			result.setReferencePanelFlg(true);
		}
		result.setApplication(applicationDto);
				
		//String workplaceID = employeeAdapter.getWorkplaceId(companyID, employeeID, GeneralDate.today());
		List<RequestAppDetailSetting> requestAppDetailSettings = appCommonSettingOutput.requestOfEachCommon.getRequestAppDetailSettings();
		if(requestAppDetailSettings != null){
			List<RequestAppDetailSetting>  requestAppDetailSetting = requestAppDetailSettings.stream().filter( c -> c.appType == ApplicationType.OVER_TIME_APPLICATION).collect(Collectors.toList());
			if(requestAppDetailSetting != null){
				// 時刻計算利用チェック
				if (requestAppDetailSetting.get(0).getTimeCalUseAtr().value == UseAtr.USE.value) {
					result.setDisplayCaculationTime(true);
					
					List<AppEmploymentSetting> appEmploymentWorkType = appCommonSettingOutput.appEmploymentWorkType;
					// 07_勤務種類取得: lay loai di lam 
					List<WorkTypeOvertime> workTypeOvertimes = overtimeService.getWorkType(companyID, employeeID,requestAppDetailSetting.get(0),appEmploymentWorkType);
					
					List<String> workTypeCodes = new ArrayList<>();
					for(WorkTypeOvertime workTypeOvertime : workTypeOvertimes){
						workTypeCodes.add(workTypeOvertime.getWorkTypeCode());
					}
					result.setWorkTypes(workTypeCodes);
					
					// 08_就業時間帯取得(lay loai gio lam viec) 
					List<SiftType> siftTypes = overtimeService.getSiftType(companyID, employeeID, requestAppDetailSetting.get(0));
					List<String> siftCodes = new ArrayList<>();
					for(SiftType siftType : siftTypes){
						siftCodes.add(siftType.getSiftCode());
					}
					result.setSiftTypes(siftCodes);
					
					// 09_勤務種類就業時間帯の初期選択をセットする
					WorkTypeAndSiftType workTypeAndSiftType = overtimeService.getWorkTypeAndSiftTypeByPersonCon(companyID, employeeID, GeneralDate.fromString(appDate, DATE_FORMAT), workTypeOvertimes, siftTypes);
					result.setWorkType(workTypeAndSiftType.getWorkType());
					result.setSiftType(workTypeAndSiftType.getSiftType());
					// 01-14_勤務時間取得(lay thoi gian): chua xong  Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」): to do
					RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,requestAppDetailSetting.get(0),result.getSiftType().getSiftCode());
					result.setDisplayCaculationTime(BooleanUtils.toBoolean(recordWorkOutput.getRecordWorkDisplay().value));
					result.setWorkClockFrom1(recordWorkOutput.getStartTime1());
					result.setWorkClockFrom2(recordWorkOutput.getStartTime2());
					result.setWorkClockTo1(recordWorkOutput.getEndTime1());
					result.setWorkClockTo2(recordWorkOutput.getEndTime2());
					
					// 01-17_休憩時間取得(lay thoi gian nghi ngoi)
					boolean displayRestTime = iOvertimePreProcess.getRestTime(requestAppDetailSetting.get(0));
					result.setDisplayRestTime(displayRestTime);
					
				}else{
					result.setDisplayCaculationTime(false);
				}
			}
		}
		
		// 01-03_残業枠を取得: chua xong
		result.setAppOvertimeNightFlg(appCommonSettingOutput.applicationSetting.getAppOvertimeNightFlg().value);
		List<OvertimeFrame> overtimeFrames = iOvertimePreProcess.getOvertimeHours(overtimeAtr,companyID);
		
		for(OvertimeFrame overtimeFrame :overtimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceID.NORMALOVERTIME.value);
			overtimeInputDto.setFrameNo(overtimeFrame.getOtFrameNo());
			overtimeInputDto.setFrameName(overtimeFrame.getOvertimeFrameName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		
		// lay breakTime
		List<BreaktimeFrame> breaktimeFrames = iOvertimePreProcess.getBreaktimeFrame(companyID);
		for(BreaktimeFrame breaktimeFrame :breaktimeFrames){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
			overtimeInputDto.setAttendanceID(AttendanceID.BREAKTIME.value);
			overtimeInputDto.setFrameNo(breaktimeFrame.getBreakTimeFrameNo());
			overtimeInputDto.setFrameName(breaktimeFrame.getBreakTimeFrameName().toString());
			overTimeInputs.add(overtimeInputDto);
		}
		
		
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		// xu li hien thi du lieu xin truoc
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getPreDisplayAtr().value == UseAtr.NOTUSE.value){
				result.setAllPreAppPanelFlg(false);
			}else{
				result.setAllPreAppPanelFlg(true);
			}
		}
		// 01-04_加給時間を取得
		if(overtimeRestAppCommonSet.isPresent()){
			if(overtimeRestAppCommonSet.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				result.setDisplayBonusTime(true);
				List<BonusPayTimeItem> bonusPayTimeItems= this.iOvertimePreProcess.getBonusTime(employeeID,overtimeRestAppCommonSet,appDate,companyID, result.getSiftType());
				for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItems){
					OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
					overtimeInputDto.setAttendanceID(AttendanceID.BONUSPAYTIME.value);
					overtimeInputDto.setFrameNo(bonusPayTimeItem.getId());
					overtimeInputDto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
					overtimeInputDto.setTimeItemTypeAtr(bonusPayTimeItem.getTimeItemTypeAtr().value);
					overTimeInputs.add(overtimeInputDto);
				}
			}else{
				result.setDisplayBonusTime(false);
			}
		}
		result.setOverTimeInputs(overTimeInputs);
		// 01-05_申請定型理由を取得, 01-06_申請理由を取得
		Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(companyID,  ApplicationType.OVER_TIME_APPLICATION.value);
		if(appTypeDiscreteSetting.isPresent()){
			// 01-05_申請定型理由を取得
			if(appTypeDiscreteSetting.get().getTypicalReasonDisplayFlg().value == AppDisplayAtr.DISPLAY.value){
				result.setTypicalReasonDisplayFlg(true);
				List<ApplicationReason> applicationReasons = iOvertimePreProcess.getApplicationReasonType(companyID,ApplicationType.OVER_TIME_APPLICATION.value,appTypeDiscreteSetting);
				List<ApplicationReasonDto> applicationReasonDtos = new ArrayList<>();
				for (ApplicationReason applicationReason : applicationReasons) {
					ApplicationReasonDto applicationReasonDto = new ApplicationReasonDto(applicationReason.getReasonID(),
							applicationReason.getReasonTemp(), applicationReason.getDefaultFlg().value);
					applicationReasonDtos.add(applicationReasonDto);
				}
				result.setApplicationReasonDtos(applicationReasonDtos);
			}else{
				result.setTypicalReasonDisplayFlg(false);
			}
			//01-06_申請理由を取得
			result.setDisplayAppReasonContentFlg(iOvertimePreProcess.displayAppReasonContentFlg(appTypeDiscreteSetting));
		}
		if(overtimeRestAppCommonSet.isPresent()){
			//01-08_乖離定型理由を取得
			if(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && overtimeRestAppCommonSet.get().getDivergenceReasonFormAtr().value == UseAtr.USE.value){
				result.setDisplayDivergenceReasonForm(true);
				List<DivergenceReason> divergenceReasons = iOvertimePreProcess.getDivergenceReasonForm(companyID,ApplicationType.OVER_TIME_APPLICATION.value,overtimeRestAppCommonSet);
				convertToDivergenceReasonDto(divergenceReasons,result);
			}else{
				result.setDisplayDivergenceReasonForm(false);
			}
			//01-07_乖離理由を取得
			result.setDisplayDivergenceReasonInput(result.getApplication().getPrePostAtr() != PrePostAtr.PREDICT.value && iOvertimePreProcess.displayDivergenceReasonInput(overtimeRestAppCommonSet));
		}
		//01-09_事前申請を取得
		if(result.getApplication().getPrePostAtr()  == PrePostAtr.POSTERIOR.value ){
			AppOverTime appOvertime = iOvertimePreProcess.getPreApplication(employeeID,overtimeRestAppCommonSet, appDate,result.getApplication().getPrePostAtr());
			if(appOvertime != null){
				result.setPreAppPanelFlg(true);
				convertOverTimeDto(companyID,preAppOvertimeDto,result,appOvertime);
			}else{
				result.setPreAppPanelFlg(false);
			}
			
		}
		
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
			if(appOvertime.getApplication().getApplicationDate() != null){
				preAppOvertimeDto.setAppDatePre(appOvertime.getApplication().getApplicationDate().toString(DATE_FORMAT));
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

			siftType.setSiftCode(appOvertime.getSiftCode().toString());
			Optional<WorkTime> workTime = workTimeRepository.findByCode(companyID,
					appOvertime.getSiftCode().toString());
			if (workTime.isPresent()) {
				siftType.setSiftName(workTime.get().getWorkTimeDisplayName().getWorkTimeName().toString());
			}
			preAppOvertimeDto.setSiftTypePre(siftType);
		}
		preAppOvertimeDto.setWorkClockFrom1Pre(appOvertime.getWorkClockFrom1());
		preAppOvertimeDto.setWorkClockTo1Pre(appOvertime.getWorkClockTo1());
		preAppOvertimeDto.setWorkClockFrom2Pre(appOvertime.getWorkClockFrom2());
		preAppOvertimeDto.setWorkClockTo2Pre(appOvertime.getWorkClockTo2());
		
		List<OvertimeInputDto> overtimeInputDtos = new ArrayList<>();
		List<OverTimeInput> overtimeInputs = appOvertime.getOverTimeInput();
		if (overtimeInputs != null && !overtimeInputs.isEmpty()) {
			List<Integer> frameNo = new ArrayList<>();
			for (OverTimeInput overTimeInput : overtimeInputs) {
				OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
				overtimeInputDto.setAttendanceID(overTimeInput.getAttendanceID().value);
				overtimeInputDto.setFrameNo(overTimeInput.getFrameNo());
				overtimeInputDto.setStartTime(overTimeInput.getStartTime().v());
				overtimeInputDto.setEndTime(overTimeInput.getEndTime().v());
				overtimeInputDto.setApplicationTime(overTimeInput.getApplicationTime().v());
				overtimeInputDtos.add(overtimeInputDto);
				frameNo.add(overTimeInput.getFrameNo());
			}
			List<OvertimeFrame> overtimeFrames = this.overtimeFrameRepository.getOvertimeFrameByFrameNo(frameNo);
			for (OvertimeInputDto overtimeInputDto : overtimeInputDtos) {
				for (OvertimeFrame overtimeFrame : overtimeFrames) {
					if (overtimeInputDto.getFrameNo() == overtimeFrame.getOtFrameNo()) {
						overtimeInputDto.setFrameName(overtimeFrame.getOvertimeFrameName().toString());
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

	private List<OvertimeInputDto> convertOverTimeInputDto(List<OverTimeInput> overtimeInputs,String companyID){
		List<OvertimeInputDto> overTimeInputDtos = new ArrayList<>();
		List<Integer> frameOverTimeNo = new ArrayList<>();
		List<Integer> frameBonusTimeNo = new ArrayList<>();
		List<Integer> frameBonusSpecTimeNo = new ArrayList<>();
		for(OverTimeInput overTimeInput : overtimeInputs){
			OvertimeInputDto overtimeInputDto = new OvertimeInputDto(overTimeInput.getCompanyID(),
					overTimeInput.getAppID(),
					overTimeInput.getAttendanceID().value,
					overTimeInput.getFrameNo(),
					overTimeInput.getTimeItemTypeAtr().value, 
					"", 
					overTimeInput.getStartTime().v(),
					overTimeInput.getEndTime().v(),
					overTimeInput.getApplicationTime().v());
			
			overTimeInputDtos.add(overtimeInputDto);
			
			if(overTimeInput.getAttendanceID().value == AttendanceID.NORMALOVERTIME.value){
				frameOverTimeNo.add(overTimeInput.getFrameNo());
			}
			if(overTimeInput.getAttendanceID().value == AttendanceID.BONUSPAYTIME.value){
				if(overTimeInput.getTimeItemTypeAtr().value == TimeItemTypeAtr.NORMAL_TYPE.value){
					frameBonusTimeNo.add(overTimeInput.getFrameNo());
				}else{
					frameBonusSpecTimeNo.add(overTimeInput.getFrameNo());
				}
			}
			
		}
		List<OvertimeFrame> overtimeFrames = new ArrayList<>();
		if(frameOverTimeNo != null && frameOverTimeNo.size() > 0){
			overtimeFrames = this.overtimeFrameRepository.getOvertimeFrameByFrameNo(frameOverTimeNo);
		}
		List<BonusPayTimeItem> bonusPayTimeItems = new ArrayList<>();
		if(frameBonusTimeNo != null && frameBonusTimeNo.size() > 0){
			bonusPayTimeItems = bPTimeItemRepository.getListBonusPayTimeItemName(companyID, frameBonusTimeNo);
		}
		List<BonusPayTimeItem> specBonusPayTimeItems = new ArrayList<>();
		if(frameBonusSpecTimeNo != null &&  frameBonusSpecTimeNo.size() > 0){
			specBonusPayTimeItems  = bPTimeItemRepository.getListSpecialBonusPayTimeItemName(companyID, frameBonusSpecTimeNo);
		}
		for(OvertimeInputDto dto : overTimeInputDtos){
			// get frameName of Overtime
			if(dto.getAttendanceID() == AttendanceID.NORMALOVERTIME.value){
				for(OvertimeFrame overtimFrame :overtimeFrames){
					if(dto.getFrameNo() == overtimFrame.getOtFrameNo()){
						dto.setFrameName(overtimFrame.getOvertimeFrameName().toString());
						break;
					}
				}
			}
			//get BonusTimeFrame
			if(dto.getAttendanceID() == AttendanceID.BONUSPAYTIME.value){
				if(dto.getTimeItemTypeAtr() == TimeItemTypeAtr.NORMAL_TYPE.value){
					for(BonusPayTimeItem bonusPayTimeItem : bonusPayTimeItems){
						if(dto.getFrameNo() == bonusPayTimeItem.getId()){
							dto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
							break;
						}
					}
				}else{
					for(BonusPayTimeItem bonusPayTimeItem : specBonusPayTimeItems){
						if(dto.getFrameNo() == bonusPayTimeItem.getId()){
							dto.setFrameName(bonusPayTimeItem.getTimeItemName().toString());
							break;
						}
					}
				}
			}
		}
		
		return overTimeInputDtos;
		
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
				1, EnumAdaptor.valueOf(ApplicationType.OVER_TIME_APPLICATION.value, ApplicationType.class), GeneralDate.fromString(appDate, DATE_FORMAT));
		List<RequestAppDetailSetting> requestAppDetailSettings = appCommonSettingOutput.requestOfEachCommon.getRequestAppDetailSettings();
		if(requestAppDetailSettings != null){
			List<RequestAppDetailSetting>  requestAppDetailSetting = requestAppDetailSettings.stream().filter( c -> c.appType == ApplicationType.OVER_TIME_APPLICATION).collect(Collectors.toList());
			// 01-14_勤務時間取得(lay thoi gian): Imported(申請承認)「勤務実績」を取得する(lay domain 「勤務実績」)
			RecordWorkOutput recordWorkOutput = iOvertimePreProcess.getWorkingHours(companyID, employeeID,appDate,requestAppDetailSetting.get(0),siftCD);
			startTime1 = recordWorkOutput.getStartTime1();
			endTime1 = recordWorkOutput.getEndTime1();
			startTime2 = recordWorkOutput.getStartTime2();
			endTime2 = recordWorkOutput.getEndTime2();
			// 01-18_実績の内容を表示し直す
			appOvertimeReference = iOvertimePreProcess.getResultContentActual(prePortAtr, siftCD, companyID,employeeID, appDate,requestAppDetailSetting.get(0),overtimeHours);
		}
		
		return new RecordWorkDto(startTime1, endTime1, startTime2, endTime2, appOvertimeReference);
	} 
}
