package nts.uk.ctx.at.request.dom.application.holidayworktime.commonalgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.CommonOvertimeHoliday;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreActualColorCheck;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.ConfirmMsgOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.PreAppContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementDetail;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ActualContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.setting.CommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.AppHdWorkDispInfoOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CheckBeforeOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HdWorkBreakTimeSetOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HdWorkDispInfoWithDateOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.InitWorkTypeWorkTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.ApplicationTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType_Update;
import nts.uk.ctx.at.request.dom.application.overtime.ExcessState;
import nts.uk.ctx.at.request.dom.application.overtime.OutDateApplication;
import nts.uk.ctx.at.request.dom.application.overtime.OverStateOutput;
import nts.uk.ctx.at.request.dom.application.overtime.CommonAlgorithm.ICommonAlgorithmOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.CheckWorkingInfoResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.OverTimeContent;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkContent;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkHours;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.PrePostInitAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.HolidayWorkAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.HolidayWorkAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSet;
import nts.uk.ctx.at.request.dom.workrecord.dailyrecordprocess.dailycreationwork.BreakTimeZoneSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.EarchInterimRemainCheck;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCheckInputParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngCheckRegister;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AbsenceTenProcessCommon;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.SubstitutionHolidayOutput;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.AppReflectOtHdWork;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.AppReflectOtHdWorkRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.ctx.at.shared.dom.worktype.service.JudgmentOneDayHoliday;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * Refactor5
 * @author huylq
 *
 */
@Stateless
public class CommonAlgorithmHolidayWorkImpl implements ICommonAlgorithmHolidayWork{
	
	@Inject
	private HolidayWorkAppSetRepository holidayWorkAppSetRepository;
	
	@Inject
	private AppReflectOtHdWorkRepository appReflectOtHdWorkRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private JudgmentOneDayHoliday judgmentOneDayHoliday;
	
	@Inject
	private ICommonAlgorithmOverTime commonAlgorithmOverTime;
	
	@Inject
	private CommonOvertimeHoliday commonOvertimeHoliday;
	
	@Inject
	private PreActualColorCheck preActualColorCheck;
	
	@Inject
	private AbsenceTenProcessCommon absenceTenProcessCommon;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private OvertimeService overtimeService;
	
	@Inject
	private RemainNumberTempRequireService requireService;
	
	@Inject
	private InterimRemainDataMngCheckRegister checkRegister;
	
	@Inject
	private CommonAlgorithm commonAlgorithm;

	@Override
	public HolidayWorkAppSet getHolidayWorkSetting(String companyId) {
		Optional<HolidayWorkAppSet> holidayWorkSetting = holidayWorkAppSetRepository.findSettingByCompany(companyId);
		return holidayWorkSetting.orElse(null);
	}

	@Override
	public AppReflectOtHdWork getHdWorkOvertimeReflect(String companyId) {
		Optional<AppReflectOtHdWork> hdWorkOvertimeReflect = appReflectOtHdWorkRepository.findByCompanyId(companyId);
		return hdWorkOvertimeReflect.orElse(null);
	}

	@Override
	public HdWorkDispInfoWithDateOutput getHdWorkDispInfoWithDateOutput(String companyId, String employeeId,
			Optional<GeneralDate> date, GeneralDate baseDate, PrePostInitAtr prePostAtr, AppEmploymentSet employmentSet,
			List<WorkTimeSetting> workTimeList, HolidayWorkAppSet holidayWorkSetting,
			AppReflectOtHdWork hdWorkOvertimeReflect, List<ActualContentDisplay> actualContentDisplayList) {
		HdWorkDispInfoWithDateOutput hdWorkDispInfoWithDateOutput = new HdWorkDispInfoWithDateOutput();
		
		//1-2.起動時勤務種類リストを取得する
		List<WorkType> workTypeList = this.getWorkTypeList(companyId, employmentSet);
		hdWorkDispInfoWithDateOutput.setWorkTypeList(Optional.of(workTypeList));
		
		//1-3.起動時勤務種類・就業時間帯の初期選択		
		InitWorkTypeWorkTime initWork = this.initWork(companyId, employeeId, baseDate, workTypeList, workTimeList, actualContentDisplayList);
		
		hdWorkDispInfoWithDateOutput.setInitWorkType(initWork.getInitWorkTypeCd());
		hdWorkDispInfoWithDateOutput.setInitWorkTime(initWork.getInitWorkTimeCd());
		hdWorkDispInfoWithDateOutput.setInitWorkTypeName(Optional.ofNullable(initWork.getInitWorkType().isPresent() ? initWork.getInitWorkType().get().getName() : null));
		hdWorkDispInfoWithDateOutput.setInitWorkTimeName(Optional.ofNullable(initWork.getInitWorkTime().isPresent() ? 
				initWork.getInitWorkTime().get().getWorkTimeDisplayName().getWorkTimeName() : null));
		
		//	初期表示する出退勤時刻を取得する
		OverTimeContent overTimeContent = this.getOverTimeContent(initWork.getInitWorkTypeCd(), initWork.getInitWorkTimeCd(), actualContentDisplayList);
		
		WorkHours workHours = commonAlgorithmOverTime.initAttendanceTime(companyId, date, overTimeContent, holidayWorkSetting.getApplicationDetailSetting());
		hdWorkDispInfoWithDateOutput.setWorkHours(workHours);
		
		//01-01_休憩時間を取得する
		HdWorkBreakTimeSetOutput hdWorkBreakTimeSetOutput = this.getBreakTime(companyId, ApplicationType.HOLIDAY_WORK_APPLICATION, 
				initWork.getInitWorkTypeCd().isPresent() ? initWork.getInitWorkTypeCd().get().v() : null, 
				initWork.getInitWorkTimeCd().isPresent() ? initWork.getInitWorkTimeCd().get().v() : null, 
						workHours.getStartTimeOp1(), workHours.getEndTimeOp1(), 
						UseAtr.toEnum(holidayWorkSetting.getApplicationDetailSetting().getTimeCalUse().value), 
						hdWorkOvertimeReflect.getHolidayWorkAppReflect().getAfter().getBreakLeaveApplication().getBreakReflectAtr().value==0 ? new Boolean(false) :new Boolean(true));
		hdWorkDispInfoWithDateOutput.setBreakTimeZoneSettingList(Optional.of(new BreakTimeZoneSetting(hdWorkBreakTimeSetOutput.getDeductionTimeLst())));
		
		//07-02_実績取得・状態チェック
		ApplicationTime applicationTime = preActualColorCheck.checkStatus(companyId, employeeId, date.orElse(null), ApplicationType.HOLIDAY_WORK_APPLICATION, 
				initWork.getInitWorkTypeCd().orElse(null), initWork.getInitWorkTimeCd().orElse(null), holidayWorkSetting.getOvertimeLeaveAppCommonSet().getOverrideSet(), 
				Optional.of(holidayWorkSetting.getCalcStampMiss()), hdWorkBreakTimeSetOutput.getDeductionTimeLst(), 
				!actualContentDisplayList.isEmpty() ? Optional.of(actualContentDisplayList.get(0)): Optional.empty());
		hdWorkDispInfoWithDateOutput.setActualApplicationTime(Optional.of(applicationTime));
		
		//10-2.代休の設定を取得する
		SubstitutionHolidayOutput subHolidayOutput = absenceTenProcessCommon.getSettingForSubstituteHoliday(companyId, employeeId, baseDate);
		if(subHolidayOutput != null) {
			hdWorkDispInfoWithDateOutput.setSubHdManage(subHolidayOutput.isSubstitutionFlg());
		}
		
		return hdWorkDispInfoWithDateOutput;
	}

	@Override
	public HdWorkBreakTimeSetOutput getBreakTime(String companyID, ApplicationType appType, String workTypeCD,
			String workTimeCD, Optional<TimeWithDayAttr> startTime, Optional<TimeWithDayAttr> endTime,
			UseAtr timeCalUse, Boolean breakTimeDisp) {
		HdWorkBreakTimeSetOutput result = new HdWorkBreakTimeSetOutput(false, Collections.emptyList());
		// 01-17_休憩時間帯を表示するか判断
		boolean displayRestTime = commonOvertimeHoliday.getRestTime(
				companyID,
				timeCalUse,
				breakTimeDisp,
				ApplicationType.HOLIDAY_WORK_APPLICATION);
		result.setDisplayRestTime(displayRestTime);
		//	休憩時間帯表示区分をチェック 
		if(displayRestTime) {
			//	休憩時間帯を取得する
			List<DeductionTime> deductionTimeLst = commonOvertimeHoliday.getBreakTimes(companyID, workTypeCD, workTimeCD, startTime, endTime);
			result.setDeductionTimeLst(deductionTimeLst);
		}
		return result;
	}
	
	@Override
	public CheckBeforeOutput individualErrorCheck(boolean require, String companyId,
			AppHdWorkDispInfoOutput appHdWorkDispInfoOutput, AppHolidayWork appHolidayWork, Integer mode) {
		CheckBeforeOutput checkBeforeOutput = new CheckBeforeOutput();
		
		//	勤務種類、就業時間帯チェックのメッセージを表示
		this.checkWorkMessageDisp(appHolidayWork.getWorkInformation().getWorkTypeCode().v(), appHolidayWork.getWorkInformation().getWorkTimeCode().v());
		
		//	計算ボタン未クリックチェック
		commonOvertimeHoliday.calculateButtonCheck(appHdWorkDispInfoOutput.getCalculationResult().isPresent() ? 
				appHdWorkDispInfoOutput.getCalculationResult().get().getCalculatedFlag().value : null, 
				EnumAdaptor.valueOf(appHdWorkDispInfoOutput.getHolidayWorkAppSet().getApplicationDetailSetting().getTimeCalUse().value, UseAtr.class));
		
		//	休出時間のチェック
		this.checkHdWorkTime(appHolidayWork.getApplicationTime());
		
		//	事前申請が必須か確認する
		List<PreAppContentDisplay> preAppContentDisplayList = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput()
				.getOpPreAppContentDisplayLst().orElse(Collections.emptyList());
		appHdWorkDispInfoOutput.getHolidayWorkAppSet().getApplicationDetailSetting().checkAdvanceApp(ApplicationType.HOLIDAY_WORK_APPLICATION, 
				appHolidayWork.getApplication().getPrePostAtr(), Optional.empty(), 
				!preAppContentDisplayList.isEmpty() ? preAppContentDisplayList.get(0).getAppHolidayWork() : Optional.empty());
		
		//	事前申請・実績超過チェック
		List<ConfirmMsgOutput> confirmMsgOutputs = this.checkExcess(appHdWorkDispInfoOutput, appHolidayWork);
		
		//	申請時の乖離時間をチェックする
		overtimeService.checkDivergenceTime(require, ApplicationType.HOLIDAY_WORK_APPLICATION, 
				Optional.empty(), Optional.of(appHolidayWork), appHdWorkDispInfoOutput.getHolidayWorkAppSet().getOvertimeLeaveAppCommonSet());
		
		//	社員に対応する締め期間を取得する
		val requireM3 = requireService.createRequire();
        val cacheCarrier = new CacheCarrier();
        DatePeriod closingPeriod = ClosureService.findClosurePeriod(requireM3, cacheCarrier, appHolidayWork.getApplication().getEmployeeID(), 
        		appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput().getBaseDate());
        
        //	登録時の残数チェック
        InterimRemainCheckInputParam checkRegisterParam = new InterimRemainCheckInputParam(companyId, appHolidayWork.getApplication().getEmployeeID(), 
        		new DatePeriod(closingPeriod.start(), closingPeriod.end().addYears(1).addDays(-1)), false, appHolidayWork.getApplication().getAppDate().getApplicationDate(), 
        		new DatePeriod(appHolidayWork.getApplication().getAppDate().getApplicationDate(), appHolidayWork.getApplication().getAppDate().getApplicationDate()), 
        		true, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
        		true, false, false, false, false, false, false);
        EarchInterimRemainCheck earchInterimRemainCheck = checkRegister.checkRegister(checkRegisterParam);
        if(earchInterimRemainCheck.isChkSubHoliday()) {
        	confirmMsgOutputs.add(new ConfirmMsgOutput("Msg_1409", Collections.emptyList())); //missing param
        }
        
        AppOvertimeDetail appOvertimeDetail = new AppOvertimeDetail();
        
        if(mode == 0) {
        	//	３６協定時間上限チェック（月間）
//        	commonOvertimeHoliday.registerHdWorkCheck36TimeLimit(companyId, employeeId, appDate, holidayWorkInputs);  pending
        	//	申請の矛盾チェック
        	commonAlgorithm.appConflictCheck(companyId, appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0), 
        			Arrays.asList(appHolidayWork.getApplication().getAppDate().getApplicationDate()), 
        			Arrays.asList(appHolidayWork.getWorkInformation().getWorkTypeCode().v()), 
        			appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().orElse(Collections.emptyList()));
        } else {
        	//	３６上限チェック(詳細)
//        	commonOvertimeHoliday.updateHdWorkCheck36TimeLimit(companyId, appId, enteredPersonId, employeeId, appDate, holidayWorkInputs);   pending
        }
        
        checkBeforeOutput.setConfirmMsgOutputs(confirmMsgOutputs);
        checkBeforeOutput.setAppOvertimeDetail(appOvertimeDetail);
		return checkBeforeOutput;
	}
	
	@Override
	public void checkWorkMessageDisp(String workTypeCode, String workTimeCode) {
		String companyId = AppContexts.user().companyId();
		CheckWorkingInfoResult checkWorkingInfoResult = otherCommonAlgorithm.checkWorkingInfo(companyId, workTypeCode, workTimeCode);
		if(checkWorkingInfoResult.isWkTypeError() && checkWorkingInfoResult.isWkTimeError()) {
			throw new BusinessException("Msg_1530", "勤務種類コード"+workTypeCode+","
					+"就業時間帯コード"+workTimeCode);
		}else {
			if(checkWorkingInfoResult.isWkTypeError()) {
				throw new BusinessException("Msg_1530", "勤務種類コード"+workTypeCode);
			}
			if(checkWorkingInfoResult.isWkTimeError()) {
				throw new BusinessException("Msg_1530", "就業時間帯コード"+workTimeCode);
			}
		}
	}
	
	@Override
	public void checkHdWorkTime(ApplicationTime applicationTime) {
		int totalTime = 0;
		totalTime += applicationTime.getApplicationTime().stream().mapToInt(appTime -> appTime.getApplicationTime().v()).sum();
		if(applicationTime.getOverTimeShiftNight().isPresent()) {
			totalTime += applicationTime.getOverTimeShiftNight().get().getOverTimeMidNight().v();
			totalTime += applicationTime.getOverTimeShiftNight().get().getMidNightHolidayTimes().stream()
								.mapToInt(midNightHolidayTime -> midNightHolidayTime.getAttendanceTime().v()).sum();
		}
		if(totalTime <= 0) {
			throw new BusinessException("Msg_1654");
		}
	}

	@Override
	public List<ConfirmMsgOutput> checkExcess(AppHdWorkDispInfoOutput appHdWorkDispInfoOutput, AppHolidayWork appHolidayWork){
		List<ConfirmMsgOutput> confirmMsgOutputs = new ArrayList<ConfirmMsgOutput>();
		
		List<PreAppContentDisplay> preAppContentDisplayList = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput()
				.getOpPreAppContentDisplayLst().orElse(Collections.emptyList());
		Optional<AppHolidayWork> appHolidayWorkOp = !preAppContentDisplayList.isEmpty() ? preAppContentDisplayList.get(0).getAppHolidayWork() : Optional.empty();
		
		OverStateOutput overStateOutput = appHdWorkDispInfoOutput.getHolidayWorkAppSet().getOvertimeLeaveAppCommonSet()
				.checkPreApplication(appHolidayWork.getApplication().getPrePostAtr(), 
						Optional.ofNullable(appHolidayWorkOp.isPresent() ? appHolidayWorkOp.get().getApplicationTime() : null), 
						Optional.ofNullable(appHolidayWork.getApplicationTime()), 
						appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().getActualApplicationTime());
		if(overStateOutput.getIsExistApp()) {
			confirmMsgOutputs.add(new ConfirmMsgOutput("Msg_1508", Collections.emptyList()));
		} else {
			OutDateApplication outDateApplication = overStateOutput.getAdvanceExcess();
			ConfirmMsgOutput confirmMsgOutput = 
					this.checkExcessWithMessage(outDateApplication, appHdWorkDispInfoOutput, "Msg_424", ExcessState.EXCESS_ALARM, false);
			if(confirmMsgOutput != null) {
				confirmMsgOutputs.add(confirmMsgOutput);
			}
		}
		switch(overStateOutput.getAchivementStatus()) {
			case EXCESS_ERROR:
				throw new BusinessException("Msg_1746", "employeeName", appHolidayWork.getApplication().getAppDate().getApplicationDate().toString());//huytodo employeeName
			case EXCESS_ALARM:
				confirmMsgOutputs.add(new ConfirmMsgOutput("Msg_1745", 
						Arrays.asList("employeeName", appHolidayWork.getApplication().getAppDate().getApplicationDate().toString())));//huytodo employeeName
				break;
			case NO_EXCESS:
				OutDateApplication outDateApplication = overStateOutput.getAchivementExcess();
				
				this.checkExcessWithMessage(outDateApplication, appHdWorkDispInfoOutput, "Msg_1748", ExcessState.EXCESS_ERROR, true);
				
				ConfirmMsgOutput confirmMsgOutput = 
						this.checkExcessWithMessage(outDateApplication, appHdWorkDispInfoOutput, "Msg_1747", ExcessState.EXCESS_ALARM, false);
				if(confirmMsgOutput != null) {
					confirmMsgOutputs.add(confirmMsgOutput);
				}
				break;
			default: break;
		}
		return confirmMsgOutputs;
	}
	
	public ConfirmMsgOutput checkExcessWithMessage(OutDateApplication outDateApplication, AppHdWorkDispInfoOutput appHdWorkDispInfoOutput,
			String messageId, ExcessState excessState, boolean isErrorMessage) {
		List<String> messageContentList = new ArrayList<String>();
		
		List<Integer> overtimeFrameNoList = outDateApplication.getExcessStateDetail().stream()
							.filter(excessStateDetail -> excessStateDetail.getType().equals(AttendanceType_Update.NORMALOVERTIME) 
									&& excessStateDetail.getExcessState().equals(excessState))
							.map(excessStateDetail -> excessStateDetail.getFrame().v()).collect(Collectors.toList());
		List<Integer> workdayoffFrameNoList = outDateApplication.getExcessStateDetail().stream()
							.filter(excessStateDetail -> excessStateDetail.getType().equals(AttendanceType_Update.BREAKTIME) && 
									excessStateDetail.getExcessState().equals(excessState))
							.map(excessStateDetail -> excessStateDetail.getFrame().v()).collect(Collectors.toList());
		
		appHdWorkDispInfoOutput.getOvertimeFrameList().stream()
		.filter(overtimeFrame -> overtimeFrameNoList.contains(overtimeFrame.getOvertimeWorkFrNo().v().intValue()))
		.map(overtimeFrame -> {
			messageContentList.add(overtimeFrame.getOvertimeWorkFrName().v());
			return overtimeFrame;
		});
		appHdWorkDispInfoOutput.getWorkdayoffFrameList().stream()
		.filter(workdayoffFrame -> workdayoffFrameNoList.contains(workdayoffFrame.getWorkdayoffFrNo().v().intValue()))
		.map(workdayoffFrame -> {
			messageContentList.add(workdayoffFrame.getWorkdayoffFrName().v());
			return workdayoffFrame;
		});
		
		outDateApplication.getExcessStateMidnight().stream()
		.filter(excessStateMidnight -> excessStateMidnight.getExcessState().equals(excessState))
		.map(excessStateMidnight -> {
			switch(excessStateMidnight.getLegalCfl()) {
				case WithinPrescribedHolidayWork:
					messageContentList.add(TextResource.localize("KAF005_341"));
					break;
				case ExcessOfStatutoryHolidayWork:
					messageContentList.add(TextResource.localize("KAF005_342"));
					break;
				case PublicHolidayWork:
					messageContentList.add(TextResource.localize("KAF005_343"));
					break;
					default: break;
			}
			return excessStateMidnight;
		});
		
		if(outDateApplication.getFlex().equals(excessState)) {
			messageContentList.add(TextResource.localize("KAF005_63"));
		}
		
		if(outDateApplication.getOverTimeLate().equals(excessState)) {
			messageContentList.add(TextResource.localize("KAF005_65"));
		}
		
		if(!messageContentList.isEmpty()) {
			String messageContent = messageContentList.stream().collect(Collectors.joining(", "));
			if(isErrorMessage) {
				throw new BusinessException(messageId, 
						appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getBussinessName(),
						messageContent);
			} else {
				return new ConfirmMsgOutput(messageId, 
						Arrays.asList(appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getBussinessName(), 
								messageContent));
			}
		} else {
			return null;
		}
	}
	
	@Override
	public List<WorkType> getWorkTypeList(String companyId, AppEmploymentSet employmentSet){
		List<WorkType> workTypeList = new ArrayList<WorkType>();
		if(employmentSet.getTargetWorkTypeByAppLst() != null && !employmentSet.getTargetWorkTypeByAppLst().isEmpty()) {
			List<String> workTypeCodeList = employmentSet.getTargetWorkTypeByAppLst().stream()
				.filter(workTypeByApp -> workTypeByApp.getAppType().equals(ApplicationType.HOLIDAY_WORK_APPLICATION))
				.map(workTypeByApp -> workTypeByApp.getWorkTypeLst())
				.flatMap(List::stream)
				.distinct()
				.collect(Collectors.toList());
			workTypeList = workTypeRepository.findWorkTypeByCodes(companyId, workTypeCodeList, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value);
			workTypeList.sort(Comparator.comparing(WorkType::getWorkTypeCode));
		} else {
			workTypeList = workTypeRepository.findWorkOneDay(companyId, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.HolidayWork.value);
		}
		return workTypeList;
	}
	
	@Override
	public InitWorkTypeWorkTime initWork(String companyId, String employeeId, GeneralDate baseDate, 
			List<WorkType> workTypeList, List<WorkTimeSetting> workTimeList, List<ActualContentDisplay> actualContentDisplayList) {
		Optional<WorkingConditionItem> workingConditionItem = workingConditionItemRepository.getBySidAndStandardDate(employeeId, baseDate);
		Optional<WorkTypeCode> initWorkTypeCd = Optional.empty();
		Optional<WorkTimeCode> initWorkTimeCd = Optional.empty();
		if(!actualContentDisplayList.isEmpty()) {
			//	休出の法定区分を取得
			Optional<HolidayAtr> holidayAtr = judgmentOneDayHoliday.getHolidayAtr(companyId, actualContentDisplayList.get(0).getOpAchievementDetail().isPresent() ? 
					actualContentDisplayList.get(0).getOpAchievementDetail().get().getWorkTypeCD() : null);
			//	休日日の勤務種類・就業時間帯の初期選択
			if(holidayAtr.isPresent() && workingConditionItem.isPresent()) {
				boolean hasSetting = false;
				
				switch(holidayAtr.get()) {
				case STATUTORY_HOLIDAYS:
					if(workingConditionItem.get().getWorkCategory().getInLawBreakTime().isPresent()) {
						initWorkTypeCd = workingConditionItem.get().getWorkCategory().getInLawBreakTime().get().getWorkTypeCode();
						initWorkTimeCd = workingConditionItem.get().getWorkCategory().getInLawBreakTime().get().getWorkTimeCode();
						hasSetting = true;
					}	
					break;
				case NON_STATUTORY_HOLIDAYS:
					if(workingConditionItem.get().getWorkCategory().getOutsideLawBreakTime().isPresent()) {
						initWorkTypeCd = workingConditionItem.get().getWorkCategory().getOutsideLawBreakTime().get().getWorkTypeCode();
						initWorkTimeCd = workingConditionItem.get().getWorkCategory().getOutsideLawBreakTime().get().getWorkTimeCode();
						hasSetting = true;
					}	
					break;
				case PUBLIC_HOLIDAY:
					if(workingConditionItem.get().getWorkCategory().getHolidayAttendanceTime().isPresent()) {
						initWorkTypeCd = workingConditionItem.get().getWorkCategory().getHolidayAttendanceTime().get().getWorkTypeCode();
						initWorkTimeCd = workingConditionItem.get().getWorkCategory().getHolidayAttendanceTime().get().getWorkTimeCode();
						hasSetting = true;
					}	
					break;
				default: break;
				}
				
				if(!hasSetting) {
					initWorkTimeCd = workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTimeCode();
					
					Optional<HolidayAtr> noneSettingHolidayAtr = judgmentOneDayHoliday.getHolidayAtr(companyId, 
							workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTypeCode().isPresent() ? 
									workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTypeCode().get().v() : null);
					if(noneSettingHolidayAtr.isPresent()) {
						if(holidayAtr.equals(noneSettingHolidayAtr)) {
							initWorkTypeCd = workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTypeCode();
						} else {
							//	指定する勤務種類リストから指定する休日区分の勤務種類を取得する
							Optional<WorkType> specifiedHdWorkType = Optional.empty(); 
							specifiedHdWorkType = workTypeList.stream().filter(workType -> !workType.getWorkTypeSetList().isEmpty() ? 
										workType.getWorkTypeSetList().get(0).getHolidayAtr().equals(holidayAtr.orElse(null)) : false).findFirst();
							if(specifiedHdWorkType.isPresent()) {
								initWorkTypeCd = Optional.of(specifiedHdWorkType.get().getWorkTypeCode());
							}
						}
					}
				}
			}
			if(!initWorkTypeCd.isPresent()) {
				initWorkTypeCd = Optional.of(workTypeList.get(0).getWorkTypeCode());
			}
			if(!initWorkTimeCd.isPresent()) {
				initWorkTimeCd = Optional.of(workTimeList.get(0).getWorktimeCode());
			}
		} else {
			initWorkTypeCd = workingConditionItem.isPresent() ? 
					workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTypeCode() : Optional.empty();
			initWorkTimeCd = workingConditionItem.isPresent() ? 
					workingConditionItem.get().getWorkCategory().getHolidayWork().getWorkTimeCode() : Optional.empty();
		}
		
		Optional<WorkType> initWorkType = workTypeRepository.findByPK(companyId, initWorkTypeCd.isPresent() ? initWorkTypeCd.get().v() : null);
		Optional<WorkTimeSetting> initWorkTime = workTimeSettingRepository.findByCode(companyId, initWorkTimeCd.isPresent() ? initWorkTimeCd.get().v() : null);
		InitWorkTypeWorkTime initWork = new InitWorkTypeWorkTime(initWorkTypeCd, initWorkTimeCd, initWorkType, initWorkTime);
		return initWork;
	}
	
	public WorkContent getWorkContent(HdWorkDispInfoWithDateOutput hdWorkDispInfoWithDateOutput) {
		WorkContent workContent = new WorkContent();
		workContent.setWorkTypeCode(hdWorkDispInfoWithDateOutput.getInitWorkType().isPresent()
				? Optional.of(hdWorkDispInfoWithDateOutput.getInitWorkType().get().v())
				: Optional.empty());
		workContent.setWorkTimeCode(hdWorkDispInfoWithDateOutput.getInitWorkTime().isPresent()
				? Optional.of(hdWorkDispInfoWithDateOutput.getInitWorkTime().get().v())
				: Optional.empty());
		TimeZone timeZoneNo1 = new TimeZone(hdWorkDispInfoWithDateOutput.getWorkHours().getStartTimeOp1().orElse(null),
				hdWorkDispInfoWithDateOutput.getWorkHours().getEndTimeOp1().orElse(null));
		TimeZone timeZoneNo2 = new TimeZone(hdWorkDispInfoWithDateOutput.getWorkHours().getStartTimeOp2().orElse(null),
				hdWorkDispInfoWithDateOutput.getWorkHours().getEndTimeOp2().orElse(null));
		List<TimeZone> timeZones = new ArrayList<TimeZone>();
		timeZones.add(timeZoneNo1);
		timeZones.add(timeZoneNo2);
		workContent.setTimeZones(timeZones);

		List<BreakTimeSheet> breakTimes = new ArrayList<BreakTimeSheet>();
		if (hdWorkDispInfoWithDateOutput.getBreakTimeZoneSettingList().isPresent()) {
			List<DeductionTime> timeZoneList = hdWorkDispInfoWithDateOutput.getBreakTimeZoneSettingList().get()
					.getTimeZones();
			for (int i = 1; i < timeZoneList.size() + 1 && i <= 10; i++) {
				TimeZone timeZone = timeZoneList.get(i - 1);
				BreakTimeSheet breakTimeSheet = new BreakTimeSheet(new BreakFrameNo(i), timeZone.getStart(),
						timeZone.getEnd());
				breakTimes.add(breakTimeSheet);
			}
		}
		workContent.setBreakTimes(breakTimes);
		return workContent;
	}

	public OverTimeContent getOverTimeContent(Optional<WorkTypeCode> workTypeCode, Optional<WorkTimeCode> workTimeCode, List<ActualContentDisplay> actualContentDisplayList) {
		OverTimeContent overTimeContent = new OverTimeContent();
		WorkHours workHoursSPR = new WorkHours();
		workHoursSPR.setStartTimeOp1(Optional.empty());
		workHoursSPR.setStartTimeOp2(Optional.empty());
		workHoursSPR.setEndTimeOp1(Optional.empty());
		workHoursSPR.setEndTimeOp2(Optional.empty());
		WorkHours workHoursActual = new WorkHours();
		if(!actualContentDisplayList.isEmpty()) {
			ActualContentDisplay actualContentDisplay = actualContentDisplayList.get(0);
			if (actualContentDisplay.getOpAchievementDetail().isPresent()) {
				AchievementDetail achievementDetail = actualContentDisplay.getOpAchievementDetail().get();
				if (achievementDetail.getOpWorkTime().isPresent()) {
					workHoursActual.setStartTimeOp1(Optional.of(new TimeWithDayAttr(achievementDetail.getOpWorkTime().get())));
				}
				if (achievementDetail.getOpLeaveTime().isPresent()) {
					workHoursActual.setEndTimeOp1(Optional.of(new TimeWithDayAttr(achievementDetail.getOpLeaveTime().get())));
				}
				if (achievementDetail.getOpWorkTime2().isPresent()) {
					workHoursActual.setStartTimeOp2(Optional.of(new TimeWithDayAttr(achievementDetail.getOpWorkTime2().get())));
				}
				if (achievementDetail.getOpDepartureTime2().isPresent()) {
					workHoursActual.setEndTimeOp2(Optional.of(new TimeWithDayAttr(achievementDetail.getOpDepartureTime2().get())));
				}
			}
		}
		
		overTimeContent.setWorkTypeCode(workTypeCode);
		overTimeContent.setWorkTimeCode(workTimeCode);
		overTimeContent.setSPRTime(Optional.of(workHoursSPR));
		overTimeContent.setActualTime(Optional.of(workHoursActual));
		return overTimeContent;
	}
}