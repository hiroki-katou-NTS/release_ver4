package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
@Stateless
public class WorkChangeReflectServiceScheImpl implements WorkChangeReflectServiceSche{
	@Inject
	private UpdateScheCommonAppRelect updateSche;
	@Inject
	private BasicScheduleRepository scheRepo;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Override
	public void reflectWorkChange(WorkChangecommonReflectParamSche workchangeParam) {		
		CommonReflectParamSche param = workchangeParam.getCommon();
		//勤種・就時の反映
		for(int i = 0; param.getStartDate().daysTo(param.getEndDate()) - i >= 0; i++){
			GeneralDate loopDate = param.getStartDate().addDays(i);
			Optional<BasicSchedule> optionalEntity = scheRepo.find(param.getEmployeeId(), loopDate);
			if(!optionalEntity.isPresent()) {
				continue;
			}
			BasicSchedule scheData = optionalEntity.get();
			//1日休日の判断
			if(scheData.getWorkTypeCode() != null
					&& workchangeParam.getExcludeHolidayAtr() == 1
					&& workTypeRepo.checkHoliday(scheData.getWorkTypeCode())) {
				continue;
			}
			updateSche.updateScheWorkTimeType(param.getEmployeeId(), loopDate, param.getWorktypeCode(), param.getWorkTimeCode());	
		}			
	}

}
