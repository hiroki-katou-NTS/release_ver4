package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absenceleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCalculateOfAppReflectParam;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.HolidayWorkReflectProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OverTimeRecordAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.StartEndTimeOffReflect;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.StartEndTimeRelectCheck;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AbsenceLeaveReflectServiceImpl implements AbsenceLeaveReflectService{
	@Inject
	private HolidayWorkReflectProcess holidayProcess;
	@Inject
	private WorkUpdateService workUpdate;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private WorkInformationRepository workInforRepo;
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;
	@Inject
	private StartEndTimeOffReflect recordStartEndTimeRelect;
	@Inject
	private CommonProcessCheckService commonService;
	@Inject
	private PreOvertimeReflectService preOTService;
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Override
	public void reflectAbsenceLeave(CommonReflectParameter param, boolean isPre) {
		IntegrationOfDaily dailyInfor = preOTService.calculateForAppReflect(param.getEmployeeId(), param.getAppDate());
		//予定勤種就時開始終了の反映
		this.reflectScheStartEndTime(param, isPre, dailyInfor);
		//勤種就時開始終了の反映
		this.reflectRecordStartEndTime(param, dailyInfor);
		CommonCalculateOfAppReflectParam calcParam = new CommonCalculateOfAppReflectParam(dailyInfor,
				param.getEmployeeId(), param.getAppDate(),
				ApplicationType.COMPLEMENT_LEAVE_APPLICATION,
				param.getWorkTypeCode(),
				param.getWorkTimeCode() == null ? Optional.empty() : Optional.of(param.getWorkTimeCode()),
						param.getStartTime() == null ? Optional.empty() : Optional.of(param.getStartTime()),
								param.getEndTime() == null ? Optional.empty() : Optional.of(param.getEndTime()));
		commonService.calculateOfAppReflect(calcParam);
	}

	@Override
	public void reflectScheStartEndTime(CommonReflectParameter param, boolean isPre, IntegrationOfDaily dailyInfor) {
		GeneralDate appDate = param.getAppDate();
		//予定を反映できるかチェックする(事前)
		if(!holidayProcess.checkScheWorkTimeReflect(param.getEmployeeId(), appDate, 
				param.getWorkTimeCode(), param.isScheTimeReflectAtr(), isPre, param.getScheAndRecordSameChangeFlg())) {
			return;
		}
		//予定勤種の反映
		workUpdate.updateRecordWorkType(param.getEmployeeId(), appDate, param.getWorkTypeCode(), true, dailyInfor);
		//1日半日出勤・1日休日系の判定
		WorkStyle checkworkDay = basicService.checkWorkDay(param.getWorkTypeCode());
		
		if(checkworkDay == WorkStyle.ONE_DAY_REST) {
			//就業時間帯の必須チェック
			SetupType checkNeededOfWorkTimeSetting = basicService.checkNeededOfWorkTimeSetting(param.getWorkTypeCode());
			if(checkNeededOfWorkTimeSetting == SetupType.NOT_REQUIRED) {
				//予定就時の反映 就業時間帯コードをクリア
				workUpdate.updateRecordWorkTime(param.getEmployeeId(), appDate, null, true, dailyInfor);
			}
			//予定開始時刻の反映 開始時刻をクリア
			//予定終了時刻の反映 終了時刻をクリア
			workTimeUpdate.cleanScheTime(param.getEmployeeId(), appDate, dailyInfor);
		} else if (checkworkDay == WorkStyle.AFTERNOON_WORK || checkworkDay == WorkStyle.MORNING_WORK) {
			//就業時間帯が反映できるか
			WorkTimeIsRecordReflect isWorkTimeReflect = this.checkReflectWorktime(param.getEmployeeId(), appDate, param.getWorkTimeCode());
			if(isWorkTimeReflect.isChkReflect()) {
				//就時の反映
				workUpdate.updateRecordWorkTime(param.getEmployeeId(), appDate, isWorkTimeReflect.getWorkTimeCode(), true, dailyInfor);
			}
			//予定開始終了時刻が反映できるか
			StartEndTimeIsRecordReflect isReflectStartEndTime = this.checkReflectScheStartEndTime(param.getEmployeeId(), appDate,
					checkworkDay, param.getStartTime(), param.getEndTime(), isWorkTimeReflect.getWorkTimeCode());
			if(isReflectStartEndTime.isChkReflect()) {
				//予定開始時刻の反映
				//予定終了時刻の反映
				TimeReflectPara timePara = new TimeReflectPara(param.getEmployeeId(), appDate, isReflectStartEndTime.getStartTime(), 
						isReflectStartEndTime.getEndTime(), 1, true, true);
				workUpdate.updateScheStartEndTime(timePara, dailyInfor);
			}
		}
	}

	@Override
	public WorkTimeIsRecordReflect checkReflectWorktime(String employeeId, GeneralDate dateData, String workTime) {
		WorkTimeIsRecordReflect outData = new WorkTimeIsRecordReflect(false, null);
		//INPUT．就業時間帯をチェックする
		if(workTime != null) {
			outData.setChkReflect(true);
			outData.setWorkTimeCode(workTime);
			return outData;
		}
		//ドメインモデル「日別実績の勤務情報」を取得する
		Optional<WorkInfoOfDailyPerformance> optWorkInfor = workInforRepo.find(employeeId, dateData);
		if(optWorkInfor.isPresent()) {
			WorkInfoOfDailyPerformance workInfo = optWorkInfor.get();
			if(workInfo.getScheduleInfo().getWorkTimeCode() != null) {
				outData.setChkReflect(false);
				outData.setWorkTimeCode(workInfo.getScheduleInfo().getWorkTimeCode().v());
				return outData;
			}
		}
		//ドメインモデル「個人労働条件」を取得する
		//ドメインモデル「個人労働条件」を取得する
		//ドメインモデル「個人労働条件」を取得する(lay dieu kien lao dong ca nhan(個人労働条件))
		Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeId, dateData);
		if(!personalLablorCodition.isPresent()) {
			return outData;
		}
		WorkingConditionItem workingConditionData = personalLablorCodition.get();
		workingConditionData.getWorkCategory().getWeekdayTime().getWorkTimeCode().ifPresent(x -> {
			//反映就業時間帯=「平日時」．就業時間帯コード
			outData.setWorkTimeCode(x.v());
				
		});
		outData.setChkReflect(true);
		
		return outData;
	}

	@Override
	public StartEndTimeIsRecordReflect checkReflectScheStartEndTime(String employeeId, GeneralDate baseDate,
			WorkStyle workStype, Integer startTime, Integer endTime, String workTimeCode) {
		StartEndTimeIsRecordReflect outData = new StartEndTimeIsRecordReflect(false, null, null);
		String companyId = AppContexts.user().companyId();
		//INPUT．開始時刻1とINPUT．終了時刻1に値がある
		if(startTime != null && endTime != null) {
			outData.setChkReflect(true);
			outData.setStartTime(startTime);
			outData.setEndTime(endTime);
			return outData;
		}
		//ドメインモデル「就業時間帯の設定」を取得する
		Optional<PredetemineTimeSetting> optFindByCode = predetemineTimeRepo.findByWorkTimeCode(companyId, workTimeCode);
		if(!optFindByCode.isPresent()) {
			return outData;
		}
		PredetemineTimeSetting timeSetting = optFindByCode.get();
		PrescribedTimezoneSetting timeZoneSetting = timeSetting.getPrescribedTimezoneSetting();		
		List<TimezoneUse> lstTimezone = timeZoneSetting.getLstTimezone().stream()
				.filter(x -> x.getWorkNo() == 1)
				.collect(Collectors.toList());
		if(lstTimezone.isEmpty()) {
			return outData;
		}
		outData.setChkReflect(true);
		//INPUT．出勤休日区分をチェックする		
		if(workStype == WorkStyle.MORNING_WORK) {//INPUT．出勤休日区分が午前出勤系			
			//反映開始時刻=「所定時間帯設定」．時間帯．開始(勤務NO=1)
			outData.setStartTime(lstTimezone.get(0).getStart().v());
			//反映終了時刻=「所定時間帯設定」．午前終了時刻
			outData.setEndTime(timeZoneSetting.getMorningEndTime().v());
		} else if(workStype == WorkStyle.AFTERNOON_WORK){
			//反映開始時刻=「所定時間帯設定」．午後開始時刻(勤務NO=1)
			outData.setStartTime(timeZoneSetting.getAfternoonStartTime().v());
			//反映終了時刻=「所定時間帯設定」．時間帯．終了(勤務NO=1)
			outData.setEndTime(lstTimezone.get(0).getEnd().v());
		}
		return outData;
	}

	@Override
	public void reflectRecordStartEndTime(CommonReflectParameter param, IntegrationOfDaily dailyInfor) {
		GeneralDate appDate = param.getAppDate();
		//勤種の反映
		workUpdate.updateRecordWorkType(param.getEmployeeId(), appDate, 
				param.getWorkTypeCode(), false, dailyInfor);
		//1日半日出勤・1日休日系の判定
		WorkStyle checkworkDay = basicService.checkWorkDay(param.getWorkTypeCode());
		if(checkworkDay == WorkStyle.ONE_DAY_REST) {
			//就業時間帯の必須チェック
			SetupType checkNeededOfWorkTimeSetting = basicService.checkNeededOfWorkTimeSetting(param.getWorkTypeCode());
			if(checkNeededOfWorkTimeSetting == SetupType.NOT_REQUIRED) {
				//就時の反映: 就業時間帯コードをクリア
				workUpdate.updateRecordWorkTime(param.getEmployeeId(), appDate, null, false, dailyInfor);
			}
			//開始終了時刻が反映できるか(1日休日)
			if(this.checkReflectRecordStartEndTime(param.getEmployeeId(), appDate, 1, true, dailyInfor.getAttendanceLeave())) {
				//開始時刻の反映 開始時刻をクリア
				//終了時刻の反映 終了時刻をクリア
				workTimeUpdate.cleanRecordTimeData(param.getEmployeeId(), appDate, dailyInfor);
			}
			
		} else if (checkworkDay == WorkStyle.AFTERNOON_WORK || checkworkDay == WorkStyle.MORNING_WORK) {
			//就業時間帯が反映できるか
			WorkTimeIsRecordReflect isWorkTimeReflect = this.checkReflectWorktime(param.getEmployeeId(), appDate, param.getWorkTimeCode());
			if(isWorkTimeReflect.isChkReflect()) {
				//就時の反映
				workUpdate.updateRecordWorkTime(param.getEmployeeId(), appDate,
						isWorkTimeReflect.getWorkTimeCode(), false, dailyInfor);
			}
			//開始終了時刻の反映(事前)
			StartEndTimeRelectCheck startEndTimeData = new StartEndTimeRelectCheck(param.getEmployeeId(), appDate, param.getStartTime(), param.getEndTime(), 
					null, null, param.getWorkTimeCode(), param.getWorkTypeCode(), OverTimeRecordAtr.ALL);
			recordStartEndTimeRelect.startEndTimeOutput(startEndTimeData, dailyInfor);	
			
		}		
	}

	@Override
	public boolean checkReflectRecordStartEndTime(String employeeId, GeneralDate baseDate, Integer frameNo, boolean isAttendence,
			Optional<TimeLeavingOfDailyPerformance> optTimeLeaving) {
		//出勤時刻を取得する
		//打刻元情報を取得する
		if(!optTimeLeaving.isPresent()) {
			return false;
		}
		TimeLeavingOfDailyPerformance timeLeaving = optTimeLeaving.get();
		List<TimeLeavingWork> leavingStamp = timeLeaving.getTimeLeavingWorks();
		if(leavingStamp.isEmpty()) {
			return false;
		}
		List<TimeLeavingWork> lstLeavingStamp1 = leavingStamp.stream()
				.filter(x -> x.getWorkNo().v() == frameNo).collect(Collectors.toList());
		if(lstLeavingStamp1.isEmpty()) {
			return false;
		}
		TimeLeavingWork leavingStamp1 = lstLeavingStamp1.get(0);
		Optional<TimeActualStamp> optTimeActual = null;
		if(isAttendence) {
			//出勤
			optTimeActual = leavingStamp1.getAttendanceStamp();
		} else {
			//退勤
			optTimeActual = leavingStamp1.getLeaveStamp();
		}
		if(!optTimeActual.isPresent()) {
			return false;
		}	
		TimeActualStamp attendanceStamp = optTimeActual.get();
		Optional<WorkStamp> optActualStamp = attendanceStamp.getStamp();
		if(!optActualStamp.isPresent()) {
			return false;
		}
		/*WorkStamp actualStamp = optActualStamp.get();
		//取得した出勤時刻に値がない　OR
		//取得した打刻元情報が「打刻自動セット(個人情報)、直行直帰」
		if(actualStamp.getStampSourceInfo() == null ||  actualStamp.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT
				|| actualStamp.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO) {
			return true;
		}
		return false;*/
		return true;
	}

}
