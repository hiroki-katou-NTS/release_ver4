package nts.uk.ctx.at.request.dom.applicationreflect.service;

/*import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.PriorStampRequestAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ScheTimeReflectRequesAtr;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
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
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectRecordPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorktimeAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeAppParameter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ReflectRecordInfor;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplyTimeRequestAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ExecutionType;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ReflectScheDto;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.WorkScheduleReflectService;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.WorkRecordReflectService;

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
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ReflectResult reflectEmployeeOfApp(Application_New appInfor, InformationSettingOfEachApp reflectSetting) {
		ReflectResult outData = new ReflectResult(true, true);
		GobackReflectPara appGobackTmp = null;
		OvertimeReflectPara overTimeTmp = null;
		WorkChangeCommonReflectPara workchangeData = null;
		HolidayWorkReflectPara holidayworkInfor = null;
		WorkChangeCommonReflectPara absenceData = null;
		CommonReflectPara absenceLeaveAppInfor = null;
		CommonReflectPara recruitmentInfor = null;
		ReflectScheDto reflectScheParam = new ReflectScheDto(appInfor.getEmployeeID(), 
				appInfor.getAppDate(),
				ExecutionType.NORMALECECUTION, 
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
		if(appInfor.getAppType() == ApplicationType.OVER_TIME_APPLICATION
				&& appInfor.getPrePostAtr() == PrePostAtr.PREDICT) {
			Optional<AppOverTime> getFullAppOvertime = overTimeRepo.getFullAppOvertime(appInfor.getCompanyID(), appInfor.getAppID());
			if(!getFullAppOvertime.isPresent()) {
				return outData;
			}
			AppOverTime appOvertimeInfor = getFullAppOvertime.get();			
			overTimeTmp = this.getOverTimeReflect(appInfor, appOvertimeInfor, reflectSetting);
			if(overTimeTmp == null) {
				return outData;
			}
		} else if (appInfor.getAppType() == ApplicationType.GO_RETURN_DIRECTLY_APPLICATION) {
			Optional<GoBackDirectly> optGobackInfo = gobackRepo.findByApplicationID(appInfor.getCompanyID(), appInfor.getAppID());
			if(!optGobackInfo.isPresent()) {
				return outData;
			}
			GoBackDirectly gobackInfo = optGobackInfo.get();
			reflectScheParam.setGoBackDirectly(gobackInfo);
			appGobackTmp = this.getGobackReflectPara(appInfor, gobackInfo, reflectSetting);
			if(appGobackTmp == null) {
				return outData;
			}
		} else if (appInfor.getAppType() == ApplicationType.ABSENCE_APPLICATION) {
			Optional<AppAbsence> optAbsence = absenceRepo.getAbsenceByAppId(appInfor.getCompanyID(), appInfor.getAppID());
			if(!optAbsence.isPresent()) {
				return outData;
			}
			AppAbsence absenceAppData = optAbsence.get();
			reflectScheParam.setForLeave(absenceAppData);
			absenceData = this.getAbsence(appInfor, absenceAppData, reflectSetting);
			if(absenceData == null) {
				return outData;
			}
		} else if (appInfor.getAppType() == ApplicationType.BREAK_TIME_APPLICATION
				&& appInfor.getPrePostAtr() == PrePostAtr.PREDICT) {			
			Optional<AppHolidayWork> getFullAppHolidayWork = holidayWorkRepo.getFullAppHolidayWork(appInfor.getCompanyID(), appInfor.getAppID());
			if(!getFullAppHolidayWork.isPresent()) {
				return outData;
			}
			AppHolidayWork holidayWorkData = getFullAppHolidayWork.get();
			reflectScheParam.setHolidayWork(holidayWorkData);
			holidayworkInfor = this.getHolidayWork(appInfor, holidayWorkData, reflectSetting);
			if(holidayworkInfor == null) {
				return outData;
			}
		} else if (appInfor.getAppType() == ApplicationType.WORK_CHANGE_APPLICATION) {
			Optional<AppWorkChange> getAppworkChangeById = workChangeRepo.getAppworkChangeById(appInfor.getCompanyID(), appInfor.getAppID());
			if(!getAppworkChangeById.isPresent()) {
				return outData;
			}
			AppWorkChange workChange = getAppworkChangeById.get();
			reflectScheParam.setWorkChange(workChange);
			workchangeData = this.getWorkChange(appInfor, workChange, reflectSetting);
			if(workchangeData == null) {
				return outData;
			}
		} else if (appInfor.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION) {
			Optional<AbsenceLeaveApp> optAbsenceLeaveData = absenceLeaveRepo.findByAppId(appInfor.getAppID());
			if(optAbsenceLeaveData.isPresent()) {
				AbsenceLeaveApp absenceLeave = optAbsenceLeaveData.get();
				reflectScheParam.setAbsenceLeave(absenceLeave);
				absenceLeaveAppInfor = this.getAbsenceLeaveAppInfor(appInfor, absenceLeave, reflectSetting);
			} 
			
			Optional<RecruitmentApp> optRecruitmentData = recruitmentRepo.findByAppId(appInfor.getAppID());
			if(optRecruitmentData.isPresent()) {
				RecruitmentApp recruitmentData = optRecruitmentData.get();
				reflectScheParam.setRecruitment(recruitmentData);
				recruitmentInfor = this.getRecruitmentInfor(appInfor, recruitmentData, reflectSetting);
			}
		} 
		else {
			return outData;
		}
		//TODO 反映するかどうか判断 (Xác định để phản ánh)
		//勤務予定へ反映処理	(Xử lý phản ánh đến kế hoạch công việc)
		ReflectInformationResult scheResult = scheReflect.workscheReflect(reflectScheParam);
		if(scheResult == ReflectInformationResult.DONE) {
			appInfor.getReflectionInformation().setStateReflection(ReflectedState_New.REFLECTED);
			appInfor.getReflectionInformation().setNotReason(Optional.of(ReasonNotReflect_New.WORK_CONFIRMED));
		}
		//勤務実績へ反映処理(xử lý phản ảnh thành tích thực chuyên cần)
		ReflectRecordInfor reflectRecordInfor = new ReflectRecordInfor(AppDegreeReflectionAtr.RECORD, AppExecutionType.EXCECUTION, appInfor);		
		AppReflectRecordPara appPara = new AppReflectRecordPara(reflectRecordInfor, 
				appGobackTmp, overTimeTmp, 
				workchangeData, 
				holidayworkInfor, 
				absenceData,
				absenceLeaveAppInfor,
				recruitmentInfor);
		ReflectInformationResult recordResult = workRecordReflect.workRecordreflect(appPara);
		if(recordResult == ReflectInformationResult.DONE) {
			appInfor.getReflectionInformation().setStateReflectionReal(ReflectedState_New.REFLECTED);
			appInfor.getReflectionInformation().setNotReasonReal(Optional.of(ReasonNotReflectDaily_New.ACTUAL_CONFIRMED));
		}
		outData.setRecordResult(recordResult == ReflectInformationResult.DONE  || recordResult == ReflectInformationResult.CHECKFALSE ? true : false);
		outData.setScheResult(scheResult == ReflectInformationResult.DONE || scheResult == ReflectInformationResult.CHECKFALSE ? true
				: false);
		if(outData.isRecordResult() || outData.isScheResult()) {
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
			if(outData.isRecordResult()) {
				interimRegister.registerDateChange(appInfor.getCompanyID(), appInfor.getEmployeeID(), lstDate);	
			}	
		}
		
		return outData;
	}
	
	private WorkChangeCommonReflectPara getWorkChange(Application_New appInfor, AppWorkChange workChange,
			InformationSettingOfEachApp reflectSetting) {
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
				workChange.getWorkTimeEnd1());
		
		 
		return new WorkChangeCommonReflectPara(workchangeInfor, workChange.getExcludeHolidayAtr());		
	}
	
	private CommonReflectPara getAbsenceLeaveAppInfor(Application_New appInfor, AbsenceLeaveApp absenceLeaveApp,
			InformationSettingOfEachApp reflectSetting) {
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
				absenceLeaveApp.getWorkTime1().getEndTime().v());
		return absenceLeave;
	}
	
	private CommonReflectPara getRecruitmentInfor(Application_New appInfor, RecruitmentApp recuitmentApp,
			InformationSettingOfEachApp reflectSetting) {
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
				recuitmentApp.getWorkTime1().getEndTime().v());
		return recruitment;
	}
	
	private HolidayWorkReflectPara getHolidayWork(Application_New appInfor, AppHolidayWork holidayWorkData,
			InformationSettingOfEachApp reflectSetting) {
		HolidayWorkReflectPara holidayPara = null;
		Map<Integer, Integer> mapOvertimeFrame =  new HashMap<>();
		if(!holidayWorkData.getHolidayWorkInputs().isEmpty()) {
			holidayWorkData.getHolidayWorkInputs().stream().forEach(x -> {
				if(x.getAttendanceType() == AttendanceType.BREAKTIME) {
					mapOvertimeFrame.put(x.getFrameNo(), x.getApplicationTime().v());
				}
			});
		}
		HolidayWorktimeAppRequestPara appPara = new HolidayWorktimeAppRequestPara(holidayWorkData.getWorkTypeCode() != null ? holidayWorkData.getWorkTypeCode().v() : null, 
				holidayWorkData.getWorkTimeCode() != null ? holidayWorkData.getWorkTimeCode().v() : null,
				mapOvertimeFrame,
				holidayWorkData.getHolidayShiftNight(),
				appInfor.getReflectionInformation().getStateReflectionReal(), 
				!appInfor.getReflectionInformation().getNotReasonReal().isPresent() ? null : appInfor.getReflectionInformation().getNotReasonReal().get(),
						holidayWorkData.getWorkClock1().getStartTime() == null ? null : holidayWorkData.getWorkClock1().getStartTime().v(),
						holidayWorkData.getWorkClock1().getEndTime() == null ? null : holidayWorkData.getWorkClock1().getEndTime().v());
		holidayPara = new HolidayWorkReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(),
				reflectSetting.isKyushutsu(),
				reflectSetting.getScheAndWorkChange(),
				reflectSetting.isJizenScheYusen(),
				appPara);
		return holidayPara;
		
	}
	
	private WorkChangeCommonReflectPara getAbsence(Application_New appInfor, AppAbsence absenceAppData,
			InformationSettingOfEachApp reflectSetting) {
		CommonReflectPara absenceInfor = null;
		
		absenceInfor = new CommonReflectPara(appInfor.getEmployeeID(),
				appInfor.getAppDate(), 
				reflectSetting.getScheAndWorkChange(), 
				reflectSetting.isJizenScheYusen(), 
				absenceAppData.getWorkTypeCode().v(), 
				absenceAppData.getWorkTimeCode() == null ? null : absenceAppData.getWorkTimeCode().v(),
				appInfor.getStartDate().isPresent() ? appInfor.getStartDate().get() : null,
				appInfor.getEndDate().isPresent() ? appInfor.getEndDate().get() : null,
						null, null);
		return new WorkChangeCommonReflectPara(absenceInfor, absenceAppData.isChangeWorkHour() == true ? 1 : 0);
	}
	
	
	private GobackReflectPara getGobackReflectPara(Application_New appInfor, GoBackDirectly gobackInfo, InformationSettingOfEachApp reflectSetting) {
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
				gobackReques);
		
		return appGobackTmp;
	}
	
	/**
	 * 残業申請
	 * @param appInfor
	 * @return
	 */
	private OvertimeReflectPara getOverTimeReflect(Application_New appInfor, AppOverTime appOvertimeInfor, InformationSettingOfEachApp reflectSetting) {
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
				overtimePara); 
		
		
		return overTimeTmp;
		
	}

}
