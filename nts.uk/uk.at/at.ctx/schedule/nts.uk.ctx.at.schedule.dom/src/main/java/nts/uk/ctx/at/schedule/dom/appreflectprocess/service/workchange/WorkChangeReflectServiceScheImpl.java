package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.BounceAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.ScheduleEditState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;
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
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private WorkTimeSettingService workTimeService;
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
		//勤務種類、就業時間帯を反映
		updateSche.updateScheWorkTimeType(scheData, lstState, param.getWorktypeCode(), param.getWorkTimeCode());
		List<WorkScheduleState> lstTimeState = lstState.stream().filter(x -> (x.getScheduleItemId() == 3 || x.getScheduleItemId() == 4)
				&& x.getScheduleEditState() != ScheduleEditState.REFLECT_APPLICATION).collect(Collectors.toList());
		if(param.getStartTime() == null && param.getEndTime() == null) {
			WorkStyle checkworkDay = basicService.checkWorkDay(param.getWorktypeCode());
			if(checkworkDay != WorkStyle.ONE_DAY_REST) {
				List<WorkScheduleTimeZone> workScheduleTimeZones = scheData.getWorkScheduleTimeZones().stream()
						.filter(x -> x.getScheduleCnt() == 1).collect(Collectors.toList());
				if(workScheduleTimeZones.isEmpty()
						|| (!workScheduleTimeZones.isEmpty() && lstTimeState.isEmpty())) {
					//所定時間帯を取得する
					PredetermineTimeSetForCalc getTimezone = workTimeService.getPredeterminedTimezone(AppContexts.user().companyId(), 
							param.getWorkTimeCode(),
							param.getWorktypeCode(),
							1);
					List<TimezoneUse> lstTimeZone = getTimezone.getTimezones().stream()
							.filter(x -> x.getUseAtr() == UseSetting.USE && x.getWorkNo() == 1)
							.collect(Collectors.toList());
					if(!lstTimeZone.isEmpty()) {
						TimezoneUse timeZone = lstTimeZone.get(0);
						WorkScheduleTimeZone timeZoneData = new WorkScheduleTimeZone(1, new TimeWithDayAttr(timeZone.getStart().v()),
								new TimeWithDayAttr(timeZone.getEnd().v()),
								BounceAtr.DIRECT_BOUNCE);
						workScheduleTimeZones.stream().forEach(x -> scheData.getWorkScheduleTimeZones().remove(x));
						scheData.getWorkScheduleTimeZones().add(timeZoneData);
					}
				}
			}
		}
		//出退勤時刻を反映
		if(param.getStartTime() != null && param.getEndTime() != null) {
			// 開始・終了時刻
			updateSche.updateStartTimeRflect(new TimeReflectScheDto(param.getEmployeeId(),
					param.getAppDate(),
					param.getStartTime(),
					param.getEndTime(),
					1,
					true,
					true), scheData, lstState);
		}
		scheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

}
