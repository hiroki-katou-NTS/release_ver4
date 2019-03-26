package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.JudgmentWorkTypeService;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
@Stateless
public class AbsenceReflectServiceImpl implements AbsenceReflectService{
	
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Inject
	private CommonProcessCheckService commonService;
	@Inject
	private BasicScheduleService basicScheService;
	@Inject
	private JudgmentWorkTypeService judgmentService;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyRepos;
	@Inject
	private PreOvertimeReflectService preOvertime;
	@Override
	public boolean absenceReflect(WorkChangeCommonReflectPara param, boolean isPre) {
		try {
			List<IntegrationOfDaily> lstDaily = this.getByAbsenceReflect(param, isPre);
			commonService.updateDailyAfterReflect(lstDaily);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public WorkInfoOfDailyPerformance reflectScheStartEndTime(String employeeId, GeneralDate baseDate, String workTypeCode, boolean isReflect, WorkInfoOfDailyPerformance dailyInfor) {
		//INPUT．予定勤務種類変更フラグをチェックする
		if(!isReflect) {
			return dailyInfor;
		}
		//予定開始終了時刻をクリアするかチェックする
		//1日半日出勤・1日休日系の判定
		if(basicScheService.checkWorkDay(workTypeCode) == WorkStyle.ONE_DAY_REST) {
			//予定開始時刻の反映
			//予定終了時刻の反映
			TimeReflectPara timeData = new TimeReflectPara(employeeId, baseDate, null, null, 1, true, true);
			dailyInfor = workTimeUpdate.updateScheStartEndTime(timeData, dailyInfor);
			
		}
		return dailyInfor;
	}
	@Override
	public void reflectScheStartEndTime(String employeeId, GeneralDate baseDate, String workTypeCode, boolean isReflect,
			IntegrationOfDaily dailyInfor) {
		//INPUT．予定勤務種類変更フラグをチェックする
		if(!isReflect) {
			return;
		}
		//予定開始終了時刻をクリアするかチェックする
		//1日半日出勤・1日休日系の判定
		if(basicScheService.checkWorkDay(workTypeCode) == WorkStyle.ONE_DAY_REST) {
			//予定開始時刻の反映
			//予定終了時刻の反映
			TimeReflectPara timeData = new TimeReflectPara(employeeId, baseDate, null, null, 1, true, true);
			workTimeUpdate.updateScheStartEndTime(timeData, dailyInfor);
			
		}
	}
	@Override
	public void reflectRecordStartEndTime(String employeeId, GeneralDate baseDate, String workTypeCode, IntegrationOfDaily dailyInfor) {
		boolean isCheckClean =  this.checkTimeClean(employeeId, baseDate, workTypeCode);
		//開始終了時刻をクリアするかチェックする 値：０になる。		
		TimeReflectPara timeData = new TimeReflectPara(employeeId, baseDate, null, null, 1, isCheckClean, isCheckClean);
		workTimeUpdate.updateRecordStartEndTimeReflect(timeData, dailyInfor);
	}
	
	@Override
	public boolean checkTimeClean(String employeeId, GeneralDate baseDate, String workTypeCode) {
		//1日半日出勤・1日休日系の判定
		if(basicScheService.checkWorkDay(workTypeCode) == WorkStyle.ONE_DAY_REST) {
			//勤務種類が１日年休特休かの判断
			boolean temp = judgmentService.checkWorkTypeIsHD(workTypeCode);
			//打刻元情報をチェックする
			if(temp) {
				Optional<TimeLeavingOfDailyPerformance> optTimeLeavingOfDaily = timeLeavingOfDailyRepos.findByKey(employeeId, baseDate);
				if(optTimeLeavingOfDaily.isPresent()) {
					TimeLeavingOfDailyPerformance timeLeavingOfDaily = optTimeLeavingOfDaily.get();
					if(checkReflectNenkyuTokkyu(timeLeavingOfDaily, 1)) {
						return checkReflectNenkyuTokkyu(timeLeavingOfDaily, 2);
					} else {
						return false;
					}
				}
			}
			
			return true;
		}
		return false;
	}
	/**
	 * 打刻元情報をチェックする
	 * @param timeLeavingOfDaily
	 * @param workNo
	 * @return
	 */
	private boolean checkReflectNenkyuTokkyu(TimeLeavingOfDailyPerformance timeLeavingOfDaily, int workNo) {
		List<TimeLeavingWork> timeLeavingWorks = timeLeavingOfDaily.getTimeLeavingWorks().stream()
				.filter(x -> x.getWorkNo().v() == workNo).collect(Collectors.toList());
		if(!timeLeavingWorks.isEmpty()) {
			TimeLeavingWork timeLeaving1 = timeLeavingWorks.get(0);
			Optional<TimeActualStamp> optAttendanceStamp = timeLeaving1.getAttendanceStamp();
			if(optAttendanceStamp.isPresent()) {
				TimeActualStamp attendanceStamp = optAttendanceStamp.get();
				Optional<WorkStamp> optWorkStamp = attendanceStamp.getStamp();
				if(optWorkStamp.isPresent()) {
					WorkStamp workStamp = optWorkStamp.get();
					if(workStamp.getStampSourceInfo() == StampSourceInfo.SPR) {
						return false;
					}
				}
			}
			Optional<TimeActualStamp> optLeaveStamp = timeLeaving1.getLeaveStamp();
			if(optLeaveStamp.isPresent()) {
				TimeActualStamp leaveStamp = optLeaveStamp.get();
				Optional<WorkStamp> optStamp = leaveStamp.getStamp();
				if(optStamp.isPresent()) {
					WorkStamp stamp = optStamp.get();
					if(stamp.getStampSourceInfo() == StampSourceInfo.SPR) {
						return false;
					}
				}
			}
			
		}
		return true;
	}

	@Override
	public List<IntegrationOfDaily> getByAbsenceReflect(WorkChangeCommonReflectPara param, boolean isPre) {
		List<IntegrationOfDaily> lstOutput = new ArrayList<>();
		CommonReflectParameter absencePara = param.getCommon();
		for(int i = 0; absencePara.getStartDate().daysTo(absencePara.getEndDate()) - i >= 0; i++){
			GeneralDate loopDate = absencePara.getStartDate().addDays(i);
			IntegrationOfDaily integrationOfDaily = preOvertime.calculateForAppReflect(absencePara.getEmployeeId(), loopDate);
			WorkInfoOfDailyPerformance dailyInfor = integrationOfDaily.getWorkInformation();
			//1日休日の判断
			if(dailyInfor.getRecordInfo().getWorkTypeCode() != null
					&& workTypeRepo.checkHoliday(dailyInfor.getRecordInfo().getWorkTypeCode().v())) {
				continue;
			}
			boolean isRecordWorkType = false;
			//予定勤種の反映
			if(dailyInfor.getScheduleInfo() == null 
					|| dailyInfor.getScheduleInfo().getWorkTimeCode() == null
					|| commonService.checkReflectScheWorkTimeType(absencePara, isPre, dailyInfor.getScheduleInfo().getWorkTimeCode().v())) {
				isRecordWorkType = true;
				workTimeUpdate.updateRecordWorkType(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTypeCode(), true, 
						integrationOfDaily);
			}				
			//予定開始終了時刻の反映
			this.reflectScheStartEndTime(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTypeCode(), isRecordWorkType,
					integrationOfDaily);			
			//勤種の反映
			workTimeUpdate.updateRecordWorkType(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTypeCode(), false,
					integrationOfDaily);
			//就業時間帯
			if(param.getExcludeHolidayAtr() != 0) {
				if(isRecordWorkType) {
					workTimeUpdate.updateRecordWorkTime(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTimeCode(), true, integrationOfDaily);	
				}				
				workTimeUpdate.updateRecordWorkTime(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTimeCode(), false, integrationOfDaily);
			}
			//開始終了時刻の反映
			this.reflectRecordStartEndTime(absencePara.getEmployeeId(), loopDate, absencePara.getWorkTypeCode(), integrationOfDaily);
			List<IntegrationOfDaily> lstDaily = commonService.lstIntegrationOfDaily(integrationOfDaily, absencePara.getEmployeeId(), loopDate, false);
			lstOutput.addAll(lstDaily);
		}
		
		return lstOutput;
	
	}
	
}
