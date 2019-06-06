package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.recruitment;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.StartEndTimeReflectScheService;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
@Stateless
public class RecruitmentAppReflectScheImpl implements RecruitmentAppReflectSche{
	@Inject
	private UpdateScheCommonAppRelect scheCommon;
	@Inject
	private StartEndTimeReflectScheService startEndTimeScheService;
	@Override
	public void recruitmentReflect(CommonReflectParamSche param) {
		// 勤務種類・就業時間帯の変更
		scheCommon.updateScheWorkTimeType(param.getEmployeeId(), 
				param.getDatePara(), 
				param.getWorktypeCode(),
				param.getWorkTimeCode());
		// 開始・終了時刻
		startEndTimeScheService.updateStartTimeRflect(new TimeReflectScheDto(param.getEmployeeId(),
				param.getDatePara(),
				param.getStartTime(),
				param.getEndTime(),
				1,
				true,
				true));
	}

}
