package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
@Stateless
public class WorkChangeReflectServiceScheImpl implements WorkChangeReflectServiceSche{
	@Inject
	private UpdateScheCommonAppRelect updateSche;
	@Inject
	private BasicScheduleRepository scheRepo;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void reflectWorkChange(WorkChangecommonReflectParamSche workchangeParam) {		
		CommonReflectParamSche param = workchangeParam.getCommon();
		//勤種・就時の反映
		GeneralDate loopDate = param.getAppDate();
		BasicSchedule scheData = scheRepo.find(param.getEmployeeId(), loopDate).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(param.getEmployeeId(), loopDate);			
		//1日休日の判断
		if(scheData.getWorkTypeCode() != null
				&& workchangeParam.getExcludeHolidayAtr() == 1
				&& workTypeRepo.checkHoliday(scheData.getWorkTypeCode())) {
			return;
		}
		updateSche.updateScheWorkTimeType(scheData, lstState, param.getWorktypeCode(), param.getWorkTimeCode());	
		scheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

}
