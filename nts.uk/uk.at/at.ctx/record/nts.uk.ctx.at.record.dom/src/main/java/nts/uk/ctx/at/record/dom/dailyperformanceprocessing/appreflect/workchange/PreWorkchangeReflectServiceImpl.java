package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.persistence.exceptions.OptimisticLockException;

import nts.arc.time.GeneralDate;
import nts.gul.error.ThrowableAnalyzer;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;

@Stateless
public class PreWorkchangeReflectServiceImpl implements PreWorkchangeReflectService{
	@Inject
	private CommonProcessCheckService commonService;
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Override
	public boolean workchangeReflect(WorkChangeCommonReflectPara param, boolean isPre) {
		CommonReflectParameter workchangePara = param.getCommon();
		try {			
			for(int i = 0; workchangePara.getStartDate().daysTo(workchangePara.getEndDate()) - i >= 0; i++){
				GeneralDate loopDate = workchangePara.getStartDate().addDays(i);
				workchangePara.setBaseDate(loopDate);
				WorkInfoOfDailyPerformance dailyInfor = workRepository.find(workchangePara.getEmployeeId(), loopDate).get();
				//1日休日の判断
				if(dailyInfor.getRecordInfo().getWorkTypeCode() != null
						&& param.getExcludeHolidayAtr() == 1
						&& workTypeRepo.checkHoliday(dailyInfor.getRecordInfo().getWorkTypeCode().v())) {
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
					dailyInfor = commonService.reflectScheWorkTimeWorkType(workchangePara, isPre, dailyInfor);
					//TODO 予定開始終了時刻の反映
					if(param.getCommon().getStartTime() != null 
							&& param.getCommon().getEndTime() != null) {
						
						dailyInfor = workTimeUpdate.updateScheStartEndTime(timeReflect, dailyInfor);	
					}
				}
				ReflectParameter reflectPara = new ReflectParameter(workchangePara.getEmployeeId(),
						loopDate,
						workchangePara.getWorkTimeCode(),
						workchangePara.getWorkTypeCode(),
						true);
				//勤種・就時の反映
				dailyInfor = workTimeUpdate.updateWorkTimeType(reflectPara, false, dailyInfor);
				//TODO 開始終了時刻の反映
				if(param.getCommon().getStartTime() != null 
						&& param.getCommon().getEndTime() != null) {
					workTimeUpdate.updateRecordStartEndTimeReflect(timeReflect);
				}
				//日別実績の勤務情報  変更
				workRepository.updateByKeyFlush(dailyInfor);
				commonService.calculateOfAppReflect(null, workchangePara.getEmployeeId(), loopDate, false);
			}			
			return true;	
		}catch (Exception ex) {
			boolean isError = new ThrowableAnalyzer(ex).findByClass(OptimisticLockException.class).isPresent();
			if(!isError) {
				throw ex;
			}
			commonService.createLogError(workchangePara.getEmployeeId(), workchangePara.getBaseDate(), workchangePara.getExcLogId());
			return false;
		}
	}

}
