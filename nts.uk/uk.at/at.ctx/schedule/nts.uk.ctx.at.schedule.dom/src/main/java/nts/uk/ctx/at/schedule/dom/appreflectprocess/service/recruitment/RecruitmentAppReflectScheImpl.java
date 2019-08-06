package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.recruitment;

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
public class RecruitmentAppReflectScheImpl implements RecruitmentAppReflectSche{
	@Inject
	private UpdateScheCommonAppRelect scheCommon;
	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void recruitmentReflect(CommonReflectParamSche param) {
		BasicSchedule scheData = basicScheRepo.find(param.getEmployeeId(), param.getAppDate()).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(param.getEmployeeId(), param.getAppDate());
		// 勤務種類・就業時間帯の変更
		scheCommon.updateScheWorkTimeType(scheData, 
				lstState, 
				param.getWorktypeCode(),
				param.getWorkTimeCode());
		// 開始・終了時刻
		scheCommon.updateStartTimeRflect(new TimeReflectScheDto(param.getEmployeeId(),
				param.getAppDate(),
				param.getStartTime(),
				param.getEndTime(),
				1,
				true,
				true), scheData, lstState);
		basicScheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

}
