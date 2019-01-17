package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangecommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.StartEndTimeReflectScheService;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.ScheduleEditState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;

@Stateless
public class ForleaveReflectScheImpl implements ForleaveReflectSche{
	@Inject
	private UpdateScheCommonAppRelect updateRelect;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private StartEndTimeReflectScheService startEndTimeScheService;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Override
	public boolean forlearveReflectSche(WorkChangecommonReflectParamSche param) {
		try {
			CommonReflectParamSche reflectParam = param.getCommon();
			for(int i = 0; reflectParam.getStartDate().daysTo(reflectParam.getEndDate()) - i >= 0; i++){
				GeneralDate loopDate = reflectParam.getStartDate().addDays(i);
				Optional<BasicSchedule> optionalEntity = basicScheRepo.find(reflectParam.getEmployeeId(), loopDate);
				if (optionalEntity.isPresent()) {
					BasicSchedule schedule = optionalEntity.get();
					if(workTypeRepo.checkHoliday(schedule.getWorkTypeCode())) {
						continue;
					}
				}
				//勤種の反映
				//勤務種類を反映する
				updateRelect.updateScheWorkType(reflectParam.getEmployeeId(), loopDate, reflectParam.getWorktypeCode());
				//就業時間帯を変更する
				if(param.getExcludeHolidayAtr() != 0) {
					updateRelect.updateScheWorkTime(reflectParam.getEmployeeId(), loopDate, reflectParam.getWorkTimeCode());
				}
				//時刻の反映
				this.reflectTime(reflectParam.getEmployeeId(), loopDate, reflectParam.getWorktypeCode(), reflectParam.getStartTime(), reflectParam.getEndTime());
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
		
		
	}
	@Override
	public void reflectTime(String employeeId, GeneralDate dateData, String workTypeCode, Integer startTime, Integer endTime) {
		//1日半日出勤・1日休日系の判定
		WorkStyle checkworkDay = basicService.checkWorkDay(workTypeCode);
		if(checkworkDay == WorkStyle.ONE_DAY_REST) {
			startEndTimeScheService.updateStartTimeRflect(new TimeReflectScheDto(employeeId, dateData, startTime, endTime, 1, true, true));
		}
	}	
}


