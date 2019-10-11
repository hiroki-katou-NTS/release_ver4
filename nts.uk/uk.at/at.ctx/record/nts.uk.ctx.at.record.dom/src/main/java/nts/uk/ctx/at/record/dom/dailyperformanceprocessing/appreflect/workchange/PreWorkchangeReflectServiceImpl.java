package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCalculateOfAppReflectParam;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.PreHolidayWorktimeReflectService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;

@Stateless
public class PreWorkchangeReflectServiceImpl implements PreWorkchangeReflectService{
	@Inject
	private CommonProcessCheckService commonService;
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Inject
	private PreHolidayWorktimeReflectService preOTService;
	@Inject
	private BasicScheduleService basicScheService;
	@Override
	public void workchangeReflect(WorkChangeCommonReflectPara param, boolean isPre) {
		CommonReflectParameter workchangePara = param.getCommon();
		GeneralDate appDate = workchangePara.getAppDate();
		IntegrationOfDaily dailyInfor = preOTService.createIntegrationOfDailyStart(workchangePara.getEmployeeId(), appDate);
		WorkInfoOfDailyPerformance workInfor = dailyInfor.getWorkInformation();
		//1日休日の判断
		if(workInfor.getRecordInfo().getWorkTypeCode() != null
				&& param.getExcludeHolidayAtr() == 1
				&& workTypeRepo.checkHoliday(workInfor.getRecordInfo().getWorkTypeCode().v())) {
			return;
		}
		//予定勤種を反映できるかチェックする
		boolean isScheReflect = commonService.checkReflectScheWorkTimeType(workchangePara, isPre, workchangePara.getWorkTimeCode());
		TimeReflectPara timeReflect =  new TimeReflectPara(param.getCommon().getEmployeeId(),
				appDate,
				param.getCommon().getStartTime(),
				param.getCommon().getEndTime(),
				1, true, true);
		if(isScheReflect) {
			//予定勤種就時の反映
			commonService.reflectScheWorkTimeWorkType(workchangePara, isPre, dailyInfor);
			//予定開始終了時刻の反映
			if(param.getCommon().getStartTime() != null 
					&& param.getCommon().getEndTime() != null) {
				workTimeUpdate.updateScheStartEndTime(timeReflect, dailyInfor);	
			} else {
				//1日半日出勤・1日休日系の判定
				if(basicScheService.checkWorkDay(workchangePara.getWorkTypeCode()) == WorkStyle.ONE_DAY_REST) {
					workTimeUpdate.cleanScheTime(workchangePara.getEmployeeId(),
							workchangePara.getAppDate(), dailyInfor);
				}
			}
		}
		ReflectParameter reflectPara = new ReflectParameter(workchangePara.getEmployeeId(),
				appDate,
				workchangePara.getWorkTimeCode(),
				workchangePara.getWorkTypeCode(),
				true);
		//勤種・就時の反映
		workTimeUpdate.updateWorkTimeType(reflectPara, false, dailyInfor);
		//開始終了時刻の反映
		if(param.getCommon().getStartTime() != null 
				&& param.getCommon().getEndTime() != null) {
			workTimeUpdate.updateRecordStartEndTimeReflect(timeReflect, dailyInfor);
		} else {
			if(basicScheService.checkWorkDay(workchangePara.getWorkTypeCode()) == WorkStyle.ONE_DAY_REST) {
				workTimeUpdate.cleanRecordTimeData(workchangePara.getEmployeeId(),
						workchangePara.getAppDate(), dailyInfor);
			} else {
				//振出申請を申請して反映した。取り消し申請は半日振出半日休日を出す、反映した後出勤・退勤時刻が勤務種類と就業時間帯の時刻の通り反映する
				dailyInfor.getAttendanceLeave().ifPresent(x -> {
					x.getAttendanceLeavingWork(1).ifPresent(a -> {
						a.getAttendanceStamp().ifPresent(y -> {
							y.getStamp().ifPresent(z -> {
								if(z.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION) {
									z.setStampSourceInfo(StampSourceInfo.GO_STRAIGHT);
								}
							});
						});
						a.getLeaveStamp().ifPresent(y -> {
							y.getStamp().ifPresent(z -> {
								if(z.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION) {
									z.setStampSourceInfo(StampSourceInfo.GO_STRAIGHT);
								}
							});
						});
					});
				});
			}
		}
		//日別実績の勤務情報  変更
		CommonCalculateOfAppReflectParam calcParam = new CommonCalculateOfAppReflectParam(dailyInfor,
				workchangePara.getEmployeeId(),
				appDate,
				ApplicationType.WORK_CHANGE_APPLICATION,
				workchangePara.getWorkTypeCode(),
				Optional.ofNullable(workchangePara.getWorkTimeCode()),
				Optional.ofNullable(workchangePara.getStartTime()),
				Optional.ofNullable(workchangePara.getEndTime()),
				isPre,
				workchangePara.getIPUSOpt(),
				workchangePara.getApprovalSet());
		commonService.calculateOfAppReflect(calcParam);
	}

}
