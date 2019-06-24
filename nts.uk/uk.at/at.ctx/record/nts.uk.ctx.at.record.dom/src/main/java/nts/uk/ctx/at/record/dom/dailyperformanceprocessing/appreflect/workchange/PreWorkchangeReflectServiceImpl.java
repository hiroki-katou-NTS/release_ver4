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
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
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
	@Override
	public void workchangeReflect(WorkChangeCommonReflectPara param, boolean isPre) {
		CommonReflectParameter workchangePara = param.getCommon();
		for(int i = 0; workchangePara.getStartDate().daysTo(workchangePara.getEndDate()) - i >= 0; i++){
			GeneralDate loopDate = workchangePara.getStartDate().addDays(i);
			IntegrationOfDaily dailyInfor = preOTService.createIntegrationOfDailyStart(workchangePara.getEmployeeId(), loopDate);
			workchangePara.setBaseDate(loopDate);
			WorkInfoOfDailyPerformance workInfor = dailyInfor.getWorkInformation();
			//1日休日の判断
			if(workInfor.getRecordInfo().getWorkTypeCode() != null
					&& param.getExcludeHolidayAtr() == 1
					&& workTypeRepo.checkHoliday(workInfor.getRecordInfo().getWorkTypeCode().v())) {
				continue;
			}
			
			//予定勤種就時の反映
			boolean isScheReflect = commonService.checkReflectScheWorkTimeType(workchangePara, isPre, workchangePara.getWorkTimeCode());
			TimeReflectPara timeReflect =  new TimeReflectPara(param.getCommon().getEmployeeId(),
					loopDate,
					param.getCommon().getStartTime(),
					param.getCommon().getEndTime(),
					1, true, true);
			if(isScheReflect) {
				commonService.reflectScheWorkTimeWorkType(workchangePara, isPre, dailyInfor);
				//TODO 予定開始終了時刻の反映
				if(param.getCommon().getStartTime() != null 
						&& param.getCommon().getEndTime() != null) {
					workTimeUpdate.updateScheStartEndTime(timeReflect, dailyInfor);	
				}
			}
			ReflectParameter reflectPara = new ReflectParameter(workchangePara.getEmployeeId(),
					loopDate,
					workchangePara.getWorkTimeCode(),
					workchangePara.getWorkTypeCode(),
					true);
			//勤種・就時の反映
			workTimeUpdate.updateWorkTimeType(reflectPara, false, dailyInfor);
			//TODO 開始終了時刻の反映
			if(param.getCommon().getStartTime() != null 
					&& param.getCommon().getEndTime() != null) {
				workTimeUpdate.updateRecordStartEndTimeReflect(timeReflect, dailyInfor);
			}
			//日別実績の勤務情報  変更
			//workRepository.updateByKeyFlush(dailyInfor);
			CommonCalculateOfAppReflectParam calcParam = new CommonCalculateOfAppReflectParam(dailyInfor,
					workchangePara.getEmployeeId(), workchangePara.getBaseDate(),
					ApplicationType.WORK_CHANGE_APPLICATION,
					workchangePara.getWorkTypeCode(),
					workchangePara.getWorkTimeCode() == null ? Optional.empty() : Optional.of(workchangePara.getWorkTimeCode()),
					workchangePara.getStartTime() == null ? Optional.empty() : Optional.of(workchangePara.getStartTime()),
					workchangePara.getEndTime() == null ? Optional.empty() : Optional.of(workchangePara.getEndTime()));
			commonService.calculateOfAppReflect(calcParam);
		}			
	}

}
