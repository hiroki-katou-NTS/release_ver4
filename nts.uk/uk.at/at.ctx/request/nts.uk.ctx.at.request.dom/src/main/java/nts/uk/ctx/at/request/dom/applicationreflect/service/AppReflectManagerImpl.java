package nts.uk.ctx.at.request.dom.applicationreflect.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import nts.arc.time.GeneralDate;
import nts.gul.error.ThrowableAnalyzer;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.CommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppDegreeReflectionAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppExecutionType;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectRecordPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.BreakTime;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorktimeAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeAppParameter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ReflectRecordInfor;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplyTimeRequestAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ReflectScheDto;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.WorkScheduleReflectService;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.WorkRecordReflectService;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;

@Stateless
public class AppReflectManagerImpl implements AppReflectManager {
	@Inject
	private OvertimeRepository overTimeRepo;
	@Inject
	private WorkRecordReflectService workRecordReflect;
	@Inject
	private ApplicationRepository_New appRepo;
	@Inject
	private GoBackDirectlyRepository gobackRepo;
	@Inject
	private AppAbsenceRepository absenceRepo;
	@Inject
	private AppHolidayWorkRepository holidayWorkRepo;
	@Inject
	private IAppWorkChangeRepository workChangeRepo;
	@Inject
	private WorkScheduleReflectService scheReflect;
	@Inject
	private AbsenceLeaveAppRepository absenceLeaveRepo;
	@Inject
	private RecruitmentAppRepository recruitmentRepo;
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRegister;
	@Inject
	private AppReflectProcessRecord proRecord;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reflectEmployeeOfApp(Application_New appInfor, InformationSettingOfEachApp reflectSetting,
			ExecutionTypeExImport execuTionType, String excLogId, int currentRecord) {
		try {
			GobackReflectPara appGobackTmp = null;
			OvertimeReflectPara overTimeTmp = null;
			WorkChangeCommonReflectPara workchangeData = null;
			HolidayWorkReflectPara holidayworkInfor = null;
			WorkChangeCommonReflectPara absenceData = null;
			CommonReflectPara absenceLeaveAppInfor = null;
			CommonReflectPara recruitmentInfor = null;
			ReflectScheDto reflectScheParam = new ReflectScheDto(appInfor.getEmployeeID(), 
					appInfor.getAppDate(),
					execuTionType, 
					true,
					ApplyTimeRequestAtr.START,
					appInfor,
					null,
					null,
					null,
					null,
					null,
					null,
					reflectSetting);
			// TODO 再実行かどうか判断する (xác nhận xem có thực hiện lại hay k)
			//申請を取得 (lấy đơn)
			switch(appInfor.getAppType()) {
			case OVER_TIME_APPLICATION:
				if(appInfor.getPrePostAtr() != PrePostAtr.PREDICT) {
					return;
				}
				Optional<AppOverTime> getFullAppOvertime = overTimeRepo.getFullAppOvertime(appInfor.getCompanyID(), appInfor.getAppID());
				if(!getFullAppOvertime.isPresent()) {
					return;
				}
				AppOverTime appOvertimeInfor = getFullAppOvertime.get();			
				overTimeTmp = this.getOverTimeReflect(appInfor, appOvertimeInfor, reflectSetting, excLogId);
				if(overTimeTmp == null) {
					return;
				}
				break;
			case GO_RETURN_DIRECTLY_APPLICATION:
				Optional<GoBackDirectly> optGobackInfo = gobackRepo.findByApplicationID(appInfor.getCompanyID(), appInfor.getAppID());
				if(!optGobackInfo.isPresent()) {
					return;
				}
				GoBackDirectly gobackInfo = optGobackInfo.get();
				reflectScheParam.setGoBackDirectly(gobackInfo);
				appGobackTmp = this.getGobackReflectPara(appInfor, gobackInfo, reflectSetting, excLogId);
				if(appGobackTmp == null) {
					return;
				}
				break;
			case ABSENCE_APPLICATION:
				Optional<AppAbsence> optAbsence = absenceRepo.getAbsenceByAppId(appInfor.getCompanyID(), appInfor.getAppID());
				if(!optAbsence.isPresent()) {
					return;
				}
				AppAbsence absenceAppData = optAbsence.get();
				reflectScheParam.setForLeave(absenceAppData);
				absenceData = this.getAbsence(appInfor, absenceAppData, reflectSetting,excLogId);
				if(absenceData == null) {
					return;
				}
				break;
			case BREAK_TIME_APPLICATION:
				Optional<AppHolidayWork> getFullAppHolidayWork = holidayWorkRepo.getFullAppHolidayWork(appInfor.getCompanyID(), appInfor.getAppID());
				if(!getFullAppHolidayWork.isPresent()) {
					return;
				}
				AppHolidayWork holidayWorkData = getFullAppHolidayWork.get();
				reflectScheParam.setHolidayWork(holidayWorkData);
				holidayworkInfor = this.getHolidayWork(appInfor, holidayWorkData, reflectSetting, excLogId);
				if(holidayworkInfor == null) {
					return;
				}
				break;
			case WORK_CHANGE_APPLICATION:
				Optional<AppWorkChange> getAppworkChangeById = workChangeRepo.getAppworkChangeById(appInfor.getCompanyID(), appInfor.getAppID());
				if(!getAppworkChangeById.isPresent()) {
					return;
				}
				AppWorkChange workChange = getAppworkChangeById.get();
				reflectScheParam.setWorkChange(workChange);
				workchangeData = this.getWorkChange(appInfor, workChange, reflectSetting, excLogId);
				if(workchangeData == null) {
					return;
				}
				break;
			case COMPLEMENT_LEAVE_APPLICATION:
				Optional<AbsenceLeaveApp> optAbsenceLeaveData = absenceLeaveRepo.findByAppId(appInfor.getAppID());
				if(optAbsenceLeaveData.isPresent()) {
					AbsenceLeaveApp absenceLeave = optAbsenceLeaveData.get();
					reflectScheParam.setAbsenceLeave(absenceLeave);
					absenceLeaveAppInfor = this.getAbsenceLeaveAppInfor(appInfor, absenceLeave, reflectSetting, excLogId);
				} 
				
				Optional<RecruitmentApp> optRecruitmentData = recruitmentRepo.findByAppId(appInfor.getAppID());
				if(optRecruitmentData.isPresent()) {
					RecruitmentApp recruitmentData = optRecruitmentData.get();
					reflectScheParam.setRecruitment(recruitmentData);
					recruitmentInfor = this.getRecruitmentInfor(appInfor, recruitmentData, reflectSetting, excLogId);
				}
				break;
			default:
				return;
			}
			
			//TODO 反映するかどうか判断 (Xác định để phản ánh)
			//勤務予定へ反映処理	(Xử lý phản ánh đến kế hoạch công việc)
			boolean isScheReflect = false;
			if(appInfor.getPrePostAtr() == PrePostAtr.PREDICT) {
				isScheReflect = scheReflect.workscheReflect(reflectScheParam);
				if(isScheReflect) {
					appInfor.getReflectionInformation().setStateReflection(ReflectedState_New.REFLECTED);
					appInfor.getReflectionInformation().setNotReason(Optional.of(ReasonNotReflect_New.WORK_CONFIRMED));	
				}				
			}
			
			//勤務実績へ反映処理(xử lý phản ảnh thành tích thực chuyên cần)
			ReflectRecordInfor reflectRecordInfor = new ReflectRecordInfor(AppDegreeReflectionAtr.RECORD, AppExecutionType.EXCECUTION, appInfor);		
			AppReflectRecordPara appPara = new AppReflectRecordPara(reflectRecordInfor, 
					appGobackTmp, overTimeTmp, 
					workchangeData, 
					holidayworkInfor, 
					absenceData,
					absenceLeaveAppInfor,
					recruitmentInfor,
					execuTionType);
			boolean isRecordReflect = workRecordReflect.workRecordreflect(appPara);
			if(isRecordReflect) {
				appInfor.getReflectionInformation().setStateReflectionReal(ReflectedState_New.REFLECTED);
				appInfor.getReflectionInformation().setNotReasonReal(Optional.of(ReasonNotReflectDaily_New.ACTUAL_CONFIRMED));
			}
			if(isRecordReflect || isScheReflect) {
				//暫定データの登録
				List<GeneralDate> lstDate = new ArrayList<>();
				if(appInfor.getStartDate().isPresent() && appInfor.getEndDate().isPresent()) {
					for(int i = 0; appInfor.getStartDate().get().daysTo(appInfor.getEndDate().get()) - i >= 0; i++){
						GeneralDate loopDate = appInfor.getStartDate().get().addDays(i);
						lstDate.add(loopDate);
					}
				} else {
					lstDate.add(appInfor.getAppDate());	
				}	
				appRepo.updateWithVersion(appInfor);
				interimRegister.registerDateChange(appInfor.getCompanyID(), appInfor.getEmployeeID(), lstDate);	
			}
			
		} catch(Exception ex) {
			boolean isError = new ThrowableAnalyzer(ex).findByClass(OptimisticLockException.class).isPresent();
			if(isError && excLogId != "") {
				proRecord.createLogError(appInfor.getEmployeeID(), appInfor.getAppDate(), excLogId);
			}
			int newCountRerun = currentRecord + 1;
			if (newCountRerun == 10) {
				throw ex;
			}
			try {
				Thread.sleep(newCountRerun * 50);
			} catch (InterruptedException e){}
			reflectEmployeeOfApp(appInfor, reflectSetting, execuTionType, excLogId, newCountRerun);
		}
	}
	
	private WorkChangeCommonReflectPara getWorkChange(Application_New appInfor, AppWorkChange workChange,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		CommonReflectPara workchangeInfor = null;
		workchangeInfor = new CommonReflectPara(appInfor.getEmployeeID(), 
				appInfor.getAppDate(),
				reflectSetting.getScheAndWorkChange(), 
				reflectSetting.isJizenScheYusen(), 
				workChange.getWorkTypeCd(), 
				workChange.getWorkTimeCd(), 
				appInfor.getStartDate().get(),
				appInfor.getEndDate().get(),
				workChange.getWorkTimeStart1(),
				workChange.getWorkTimeEnd1(),
				excLogId);
		return new WorkChangeCommonReflectPara(workchangeInfor, workChange.getExcludeHolidayAtr());		
	}
	
	private CommonReflectPara getAbsenceLeaveAppInfor(Application_New appInfor, AbsenceLeaveApp absenceLeaveApp,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		CommonReflectPara absenceLeave = null;
		absenceLeave = new CommonReflectPara(appInfor.getEmployeeID(), 
				appInfor.getAppDate(), 
				reflectSetting.getScheAndWorkChange(), 
				reflectSetting.isJizenScheYusen(), 
				absenceLeaveApp.getWorkTypeCD().v(), 
				absenceLeaveApp.getWorkTimeCD(), 
				null, 
				null, 
				absenceLeaveApp.getWorkTime1().getStartTime().v(),
				absenceLeaveApp.getWorkTime1().getEndTime().v(),
				excLogId);
		return absenceLeave;
	}
	
	private CommonReflectPara getRecruitmentInfor(Application_New appInfor, RecruitmentApp recuitmentApp,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		CommonReflectPara recruitment = null;
		recruitment = new CommonReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(),
				reflectSetting.getScheAndWorkChange(), 
				reflectSetting.isJizenScheYusen(),
				recuitmentApp.getWorkTypeCD().v(), 
				recuitmentApp.getWorkTimeCD().v(), 
				null, 
				null,
				recuitmentApp.getWorkTime1().getStartTime().v(), 
				recuitmentApp.getWorkTime1().getEndTime().v(),
				excLogId);
		return recruitment;
	}
	
	private HolidayWorkReflectPara getHolidayWork(Application_New appInfor, AppHolidayWork holidayWorkData,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		HolidayWorkReflectPara holidayPara = null;
		Map<Integer, Integer> mapWorkHolidayFrame =  new HashMap<>();
		Map<Integer, BreakTime> mapBreakTime = new HashMap<>(); 
		if(!holidayWorkData.getHolidayWorkInputs().isEmpty()) {
			holidayWorkData.getHolidayWorkInputs().stream().forEach(x -> {
				if(x.getAttendanceType() == AttendanceType.BREAKTIME) {
					mapWorkHolidayFrame.put(x.getFrameNo(), x.getApplicationTime().v());
				}
				if(x.getAttendanceType() == AttendanceType.RESTTIME) {
					mapBreakTime.put(x.getFrameNo(), new BreakTime(x.getStartTime().v(), x.getEndTime().v()));
				}
			});
		}
		HolidayWorktimeAppRequestPara appPara = new HolidayWorktimeAppRequestPara(holidayWorkData.getWorkTypeCode() != null ? holidayWorkData.getWorkTypeCode().v() : null, 
				holidayWorkData.getWorkTimeCode() != null ? holidayWorkData.getWorkTimeCode().v() : null,
				mapWorkHolidayFrame,
				holidayWorkData.getHolidayShiftNight(),
				appInfor.getReflectionInformation().getStateReflectionReal(), 
				!appInfor.getReflectionInformation().getNotReasonReal().isPresent() ? null : appInfor.getReflectionInformation().getNotReasonReal().get(),
						holidayWorkData.getWorkClock1().getStartTime() == null ? null : holidayWorkData.getWorkClock1().getStartTime().v(),
						holidayWorkData.getWorkClock1().getEndTime() == null ? null : holidayWorkData.getWorkClock1().getEndTime().v(),
				mapBreakTime);
		holidayPara = new HolidayWorkReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(),
				reflectSetting.isHwScheReflectHwTime(),
				reflectSetting.getScheAndWorkChange(),
				reflectSetting.isJizenScheYusen(),
				reflectSetting.isHwRecordReflectTime(),
				reflectSetting.isHwRecordReflectBreak(),
				appPara,
				excLogId);
		return holidayPara;
		
	}
	
	private WorkChangeCommonReflectPara getAbsence(Application_New appInfor, AppAbsence absenceAppData,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		CommonReflectPara absenceInfor = null;
		
		absenceInfor = new CommonReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(), 
				reflectSetting.getScheAndWorkChange(), 
				reflectSetting.isJizenScheYusen(), 
				absenceAppData.getWorkTypeCode().v(), 
				absenceAppData.getWorkTimeCode() == null ? null : absenceAppData.getWorkTimeCode().v(),
				appInfor.getStartDate().isPresent() ? appInfor.getStartDate().get() : null,
				appInfor.getEndDate().isPresent() ? appInfor.getEndDate().get() : null,
						null, null,
						excLogId);
		return new WorkChangeCommonReflectPara(absenceInfor, absenceAppData.isChangeWorkHour() == true ? 1 : 0);
	}
	
	
	private GobackReflectPara getGobackReflectPara(Application_New appInfor, GoBackDirectly gobackInfo,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		GobackReflectPara appGobackTmp = null;		
		GobackAppRequestPara gobackReques = new GobackAppRequestPara(
				gobackInfo.getWorkChangeAtr().isPresent() ? gobackInfo.getWorkChangeAtr().get() : null, 
				gobackInfo.getSiftCD().isPresent() ? gobackInfo.getSiftCD().get().v() : null, 
				gobackInfo.getWorkTypeCD().isPresent() ? gobackInfo.getWorkTypeCD().get().v() : null, 
				gobackInfo.getWorkTimeStart1().isPresent() ? gobackInfo.getWorkTimeStart1().get().v() : null, 
				gobackInfo.getWorkTimeEnd1().isPresent() ? gobackInfo.getWorkTimeEnd1().get().v() : null, 
				gobackInfo.getWorkTimeStart2().isPresent() ? gobackInfo.getWorkTimeStart2().get().v() : null, 
				gobackInfo.getWorkTimeEnd2().isPresent() ? gobackInfo.getWorkTimeEnd2().get().v() : null, 
				appInfor.getReflectionInformation().getStateReflectionReal(),
				appInfor.getReflectionInformation().getNotReasonReal().isPresent() ? appInfor.getReflectionInformation().getNotReasonReal().get() : null);
		appGobackTmp = new GobackReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(),
				reflectSetting.isChokochoki(),
				reflectSetting.getWorkJikokuYusen(),
				reflectSetting.getScheAndWorkChange(),
				reflectSetting.getScheJikokuYusen(),
				reflectSetting.isJizenScheYusen(),
				gobackReques,
				excLogId);
		
		return appGobackTmp;
	}
	
	/**
	 * 残業申請
	 * @param appInfor
	 * @return
	 */
	private OvertimeReflectPara getOverTimeReflect(Application_New appInfor, AppOverTime appOvertimeInfor,
			InformationSettingOfEachApp reflectSetting, String excLogId) {
		OvertimeReflectPara overTimeTmp = null;
		
		Map<Integer, Integer> mapOvertimeFrame =  new HashMap<>();
		if(!appOvertimeInfor.getOverTimeInput().isEmpty()) {
			appOvertimeInfor.getOverTimeInput().stream().forEach(x -> {
				if(x.getAttendanceType() == AttendanceType.NORMALOVERTIME && x.getFrameNo() <= 10) {
					mapOvertimeFrame.put(x.getFrameNo(), x.getApplicationTime().v());
				}
			});
		}
		
		OvertimeAppParameter overtimePara = new OvertimeAppParameter(appInfor.getReflectionInformation().getStateReflectionReal(),
				appInfor.getReflectionInformation().getNotReasonReal().isPresent() ? appInfor.getReflectionInformation().getNotReasonReal().get() : null,
						appOvertimeInfor.getWorkTypeCode() == null ? null : appOvertimeInfor.getWorkTypeCode().v(),
						appOvertimeInfor.getSiftCode() == null ? null : appOvertimeInfor.getSiftCode().v(),
						appOvertimeInfor.getWorkClockFrom1(),
						appOvertimeInfor.getWorkClockTo1(),
						appOvertimeInfor.getWorkClockFrom2(),
						appOvertimeInfor.getWorkClockTo2(),
						mapOvertimeFrame, 
						appOvertimeInfor.getOverTimeShiftNight(),
						appOvertimeInfor.getFlexExessTime(),
						appOvertimeInfor.getOverTimeAtr(),
						appInfor.getAppReason() == null ? "" : appInfor.getAppReason().v()); 
		overTimeTmp = new OvertimeReflectPara(appInfor.getEmployeeID(), 
				appInfor.getAppDate(), 
				reflectSetting.isZangyouRecordReflect(),
				reflectSetting.isZangyouScheReflect(),
				reflectSetting.isZangyouWorktime(),
				true,
				reflectSetting.getScheAndWorkChange(),
				true, 
				overtimePara,
				excLogId); 
		return overTimeTmp;
	}
}
