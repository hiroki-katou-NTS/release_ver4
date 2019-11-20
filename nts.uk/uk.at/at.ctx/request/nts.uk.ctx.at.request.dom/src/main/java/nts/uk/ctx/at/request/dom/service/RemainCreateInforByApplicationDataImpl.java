package nts.uk.ctx.at.request.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

/*import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMng;*/

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
//import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.AppRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.PrePostAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByApplicationData;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class RemainCreateInforByApplicationDataImpl implements RemainCreateInforByApplicationData{
	@Inject
	private ApplicationRepository_New appRepository;
	@Inject
	private IAppWorkChangeRepository workChangeService;
	@Inject
	private GoBackDirectlyRepository goBackRepo;
	@Inject
	private AppAbsenceRepository absenceRepo;
	@Inject
	private RecruitmentAppRepository recAppRepo;
	@Inject
	private AbsenceLeaveAppRepository absAppRepo;
	@Inject
	private OvertimeRepository overtimeRepo;
	@Inject
	private AppHolidayWorkRepository holidayWorkRepo; 
	@Inject
	private IAppWorkChangeRepository workChangeRepos;
	
	@Override
	public List<AppRemainCreateInfor> lstRemainDataFromApp(String cid, String sid, DatePeriod dateData) {
		
		List<Integer> lstReflect = new ArrayList<>();
		lstReflect.add(ReflectedState_New.NOTREFLECTED.value);
		lstReflect.add(ReflectedState_New.WAITREFLECTION.value);
		List<Integer> lstAppType = this.lstAppType();
		List<Application_New> lstAppData = appRepository.getByPeriodReflectType(sid, dateData, lstReflect, lstAppType);
		return this.lstResult(cid, sid, lstAppData);
	}
	@Override
	public List<AppRemainCreateInfor> lstRemainDataFromApp(String cid, String sid, List<GeneralDate> dates) {
		List<Integer> lstReflect = new ArrayList<>();
		lstReflect.add(ReflectedState_New.NOTREFLECTED.value);
		lstReflect.add(ReflectedState_New.WAITREFLECTION.value);
		List<Integer> lstAppType = this.lstAppType();
		List<Application_New> lstAppData = appRepository.getByListDateReflectType(sid, dates, lstReflect, lstAppType);
		return this.lstResult(cid, sid, lstAppData);
	}

	private List<Integer> lstAppType(){
		List<Integer> lstAppType = new ArrayList<>();
		lstAppType.add(ApplicationType.ABSENCE_APPLICATION.value);
		lstAppType.add(ApplicationType.WORK_CHANGE_APPLICATION.value);
		lstAppType.add(ApplicationType.ANNUAL_HOLIDAY_APPLICATION.value);
		lstAppType.add(ApplicationType.COMPLEMENT_LEAVE_APPLICATION.value);
		lstAppType.add(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value);
		lstAppType.add(ApplicationType.LONG_BUSINESS_TRIP_APPLICATION.value);
		lstAppType.add(ApplicationType.OVER_TIME_APPLICATION.value);
		lstAppType.add(ApplicationType.BREAK_TIME_APPLICATION.value);
		return lstAppType;
	}
	private List<AppRemainCreateInfor> lstResult(String cid, String sid, List<Application_New> lstAppData){
		List<AppRemainCreateInfor> lstOutputData = new ArrayList<>();
		lstAppData.stream().forEach(appData -> {
			AppRemainCreateInfor outData = new AppRemainCreateInfor();
			outData.setSid(sid);
			outData.setAppDate(appData.getAppDate());
			outData.setAppId(appData.getAppID());			
			outData.setAppType(EnumAdaptor.valueOf(appData.getAppType().value, ApplicationType.class));
			outData.setPrePosAtr(EnumAdaptor.valueOf(appData.getPrePostAtr().value, PrePostAtr.class));
			outData.setInputDate(appData.getInputDate());
			outData.setWorkTimeCode(Optional.empty());
			outData.setWorkTypeCode(Optional.empty());
			outData.setStartDate(appData.getStartDate());
			outData.setEndDate(appData.getEndDate());
			switch(outData.getAppType()) {
			case WORK_CHANGE_APPLICATION:
				Optional<AppWorkChange> workChange = workChangeService.getAppworkChangeById(cid, appData.getAppID());
				workChange.ifPresent(x -> {
					outData.setWorkTimeCode(x.getWorkTimeCd() == null ? Optional.empty() : Optional.of(x.getWorkTimeCd()));
					outData.setWorkTypeCode(x.getWorkTypeCd() == null ? Optional.empty() : Optional.of(x.getWorkTypeCd()));
				});
				break;
			case GO_RETURN_DIRECTLY_APPLICATION:
				Optional<GoBackDirectly> goBack = goBackRepo.findByApplicationID(cid, appData.getAppID());
				goBack.ifPresent(x -> {
					outData.setWorkTimeCode(x.getSiftCD().isPresent() ? Optional.of(x.getSiftCD().get().v()) : Optional.empty());
					outData.setWorkTypeCode(x.getWorkTypeCD().isPresent() ? Optional.of(x.getWorkTypeCD().get().v()) : Optional.empty());
				});
				break;
			case ABSENCE_APPLICATION:
				Optional<AppAbsence> absence = absenceRepo.getAbsenceByAppId(cid, appData.getAppID());
				absence.ifPresent(x -> {
					outData.setWorkTypeCode(x.getWorkTypeCode() == null ? Optional.empty() : Optional.of(x.getWorkTypeCode().v()));
					if(x.isChangeWorkHour()) {
						outData.setWorkTimeCode(x.getWorkTimeCode() == null ? Optional.empty() : Optional.of(x.getWorkTimeCode().v()));
					}
				});
				break;
			case COMPLEMENT_LEAVE_APPLICATION:
				Optional<AbsenceLeaveApp> optAbsApp = absAppRepo.findByAppId(appData.getAppID());
				optAbsApp.ifPresent(x -> {
					outData.setWorkTypeCode(x.getWorkTypeCD() == null ? Optional.empty() : Optional.of(x.getWorkTypeCD().v()));
					if(x.getChangeWorkHoursType() == NotUseAtr.USE) {
						outData.setWorkTimeCode(x.getWorkTimeCD() == null ? Optional.empty() : Optional.of(x.getWorkTimeCD()));						
					}
				});	
				
				Optional<RecruitmentApp> recApp = recAppRepo.findByID(appData.getAppID());
				recApp.ifPresent(y -> {
					outData.setWorkTimeCode(Optional.of(y.getWorkTimeCD().v()));
					outData.setWorkTypeCode(Optional.of(y.getWorkTypeCD().v()));
				});	
				
				break;
			case OVER_TIME_APPLICATION:
				Optional<AppOverTime> overTimeData = overtimeRepo.getAppOvertimeFrame(cid, appData.getAppID());
				Integer appBreakTimeTotal = 0;
				Integer appOvertimeTimeTotal = 0;
				if(overTimeData.isPresent()){
					AppOverTime x = overTimeData.get();
					outData.setWorkTimeCode(x.getSiftCode() == null ? Optional.empty() : Optional.of(x.getSiftCode().v()));
					outData.setWorkTypeCode(x.getWorkTypeCode() == null ? Optional.empty() : Optional.of(x.getWorkTypeCode().v()));
					//申請休出時間合計を設定する
					List<OverTimeInput> lstInput = x.getOverTimeInput().stream()
							.filter(y -> y.getAttendanceType() == AttendanceType.BREAKTIME)
							.collect(Collectors.toList());
					for (OverTimeInput inputData : lstInput) {
						appBreakTimeTotal += inputData.getApplicationTime().v();
					}
					//申請残業時間合計を設定する
					lstInput = x.getOverTimeInput().stream()
							.filter(y -> y.getAttendanceType() == AttendanceType.NORMALOVERTIME)
							.collect(Collectors.toList());
					for (OverTimeInput inputData : lstInput) {
						appOvertimeTimeTotal += inputData.getApplicationTime().v();
					}
				}
				outData.setAppBreakTimeTotal(Optional.of(appBreakTimeTotal));
				outData.setAppOvertimeTimeTotal(Optional.of(appOvertimeTimeTotal));
				break;
			case BREAK_TIME_APPLICATION:
				Optional<AppHolidayWork> holidayWork = holidayWorkRepo.getAppHolidayWorkFrame(cid, appData.getAppID());
				Integer breakTimeTotal = 0;
				Integer overtimeTimeTotal = 0;
				if(holidayWork.isPresent()) {
					AppHolidayWork holidayWorkData = holidayWork.get();
					outData.setWorkTimeCode(holidayWorkData.getWorkTimeCode() == null ? Optional.empty() : Optional.of(holidayWorkData.getWorkTimeCode().v()));
					outData.setWorkTypeCode(holidayWorkData.getWorkTypeCode() == null ? Optional.empty() : Optional.of(holidayWorkData.getWorkTypeCode().v()));
					//申請休出時間合計を設定する
					List<HolidayWorkInput> holidayWorkInputs = holidayWorkData.getHolidayWorkInputs()
							.stream().filter(a -> a.getAttendanceType() == AttendanceType.BREAKTIME)
							.collect(Collectors.toList());
					for (HolidayWorkInput holidayInput : holidayWorkInputs) {
						breakTimeTotal += holidayInput.getApplicationTime().v();
					}
					holidayWorkInputs = holidayWorkData.getHolidayWorkInputs()
							.stream().filter(a -> a.getAttendanceType() == AttendanceType.NORMALOVERTIME)
							.collect(Collectors.toList());
					for (HolidayWorkInput holidayInput : holidayWorkInputs) {
						overtimeTimeTotal += holidayInput.getApplicationTime().v();
					}
				}
				outData.setAppBreakTimeTotal(Optional.of(breakTimeTotal));
				outData.setAppOvertimeTimeTotal(Optional.of(overtimeTimeTotal));
				break;
			default:
				break;
			}
			lstOutputData.add(outData);
		});
		return lstOutputData;
	}
	@Override
	public Integer excludeHolidayAtr(String cid, String appID) {
		Optional<AppWorkChange> data = workChangeRepos.getAppworkChangeById(cid, appID);
		if(data.isPresent()) {
			return data.get().getExcludeHolidayAtr();
		}
		return null;
	}
}
