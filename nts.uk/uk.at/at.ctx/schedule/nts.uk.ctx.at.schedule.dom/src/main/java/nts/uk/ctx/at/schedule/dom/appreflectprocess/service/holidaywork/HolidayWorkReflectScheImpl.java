package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.holidaywork;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;

@Stateless
public class HolidayWorkReflectScheImpl implements HolidayWorkReflectSche{
	@Inject
	private UpdateScheCommonAppRelect scheCommon;
	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void holidayWorkReflect(CommonReflectParamSche holidayWorkPara) {
		BasicSchedule scheData = basicScheRepo.find(holidayWorkPara.getEmployeeId(), holidayWorkPara.getAppDate()).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(holidayWorkPara.getEmployeeId(), holidayWorkPara.getAppDate());
		//勤種・就時の反映
		scheCommon.updateScheWorkTimeType(scheData, 
				lstState, 
				holidayWorkPara.getWorktypeCode(),
				holidayWorkPara.getWorkTimeCode());
		//開始・終了時刻
		if(holidayWorkPara.getStartTime() != null
				&& holidayWorkPara.getEndTime() != null) {
			scheCommon.updateStartTimeRflect(new TimeReflectScheDto(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getAppDate(),
					holidayWorkPara.getStartTime(),
					holidayWorkPara.getEndTime(),
					1,
					true,
					true), scheData, lstState);	
		}
		basicScheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

}
