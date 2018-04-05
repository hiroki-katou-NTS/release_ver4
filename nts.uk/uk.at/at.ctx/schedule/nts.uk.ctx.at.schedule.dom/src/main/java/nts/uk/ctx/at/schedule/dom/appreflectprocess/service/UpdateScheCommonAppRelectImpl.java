package nts.uk.ctx.at.schedule.dom.appreflectprocess.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.ScheduleEditState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
@Stateless
public class UpdateScheCommonAppRelectImpl implements UpdateScheCommonAppRelect{
	@Inject
	private BasicScheduleRepository basicSche;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void updateScheWorkTimeType(String employeeId, GeneralDate baseDate, String workTypeCode,
			String workTimeCode) {		
		basicSche.changeWorkTypeTime(employeeId, baseDate, workTypeCode, workTimeCode);
		//ドメインモデル「勤務予定項目状態」を編集する id = 1
		WorkScheduleState scheData = new WorkScheduleState(ScheduleEditState.REFLECT_APPLICATION,
				1,
				baseDate,
				employeeId);
		workScheReposi.updateScheduleEditState(scheData);
		//就業時間帯の編集状態を更新する
		WorkScheduleState scheDataTime = new WorkScheduleState(ScheduleEditState.REFLECT_APPLICATION,
				2,
				baseDate,
				employeeId);
		workScheReposi.updateScheduleEditState(scheDataTime);
	}

}
