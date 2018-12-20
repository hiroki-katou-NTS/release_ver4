package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.holidaywork;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.StartEndTimeReflectScheService;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;

@Stateless
public class HolidayWorkReflectScheImpl implements HolidayWorkReflectSche{
	@Inject
	private UpdateScheCommonAppRelect scheCommon;
	@Inject
	private StartEndTimeReflectScheService startEndTimeScheService;
	@Override
	public boolean holidayWorkReflect(CommonReflectParamSche holidayWorkPara) {
		try {
			//勤種・就時の反映
			scheCommon.updateScheWorkTimeType(holidayWorkPara.getEmployeeId(), 
					holidayWorkPara.getDatePara(), 
					holidayWorkPara.getWorktypeCode(),
					holidayWorkPara.getWorkTimeCode());
			//開始・終了時刻
			startEndTimeScheService.updateStartTimeRflect(new TimeReflectScheDto(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getDatePara(),
					holidayWorkPara.getStartTime(),
					holidayWorkPara.getEndTime(),
					1,
					true,
					true));
			return true;	
		}catch (Exception e) {
			return false;
		}
		
	}

}
