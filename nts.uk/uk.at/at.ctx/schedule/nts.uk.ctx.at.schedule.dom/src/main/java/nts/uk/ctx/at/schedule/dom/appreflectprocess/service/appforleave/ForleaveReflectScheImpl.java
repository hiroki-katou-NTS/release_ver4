package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangecommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

@Stateless
public class ForleaveReflectScheImpl implements ForleaveReflectSche{
	@Inject
	private UpdateScheCommonAppRelect updateRelect;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void forlearveReflectSche(WorkChangecommonReflectParamSche param) {
		CommonReflectParamSche reflectParam = param.getCommon();
		GeneralDate appDate = reflectParam.getAppDate();
		BasicSchedule scheData = basicScheRepo.find(reflectParam.getEmployeeId(), appDate).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(reflectParam.getEmployeeId(), appDate);			
		//勤種の反映
		//勤務種類を反映する
		updateRelect.updateScheWorkType(scheData, lstState, reflectParam.getWorktypeCode());
		//就業時間帯を変更する
		if(param.getExcludeHolidayAtr() != 0) {
			updateRelect.updateScheWorkTime(scheData, lstState, reflectParam.getWorkTimeCode());
		}
		//時刻の反映
		this.reflectTime(scheData, lstState, reflectParam.getWorktypeCode(), reflectParam.getStartTime(), reflectParam.getEndTime());
		basicScheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}
	@Override
	public void reflectTime(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode, Integer startTime, Integer endTime) {
		//1日半日出勤・1日休日系の判定
		WorkStyle checkworkDay = basicService.checkWorkDay(workTypeCode);
		if(checkworkDay == WorkStyle.ONE_DAY_REST) {
			updateRelect.updateStartTimeRflect(new TimeReflectScheDto(scheData.getEmployeeId(), scheData.getDate(), startTime, endTime, 1, true, true),
					scheData, lstState);
		}
	}	
}


