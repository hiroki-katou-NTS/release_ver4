package nts.uk.ctx.at.schedule.dom.appreflectprocess.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.BounceAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.ScheduleEditState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.shr.com.time.TimeWithDayAttr;
@Stateless
public class UpdateScheCommonAppRelectImpl implements UpdateScheCommonAppRelect{
	@Override
	public void updateScheWorkTimeType(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode,
			String workTimeCode) {	
		scheData.setWorkTimeCode(workTimeCode);
		scheData.setWorkTypeCode(workTypeCode);		
		//ドメインモデル「勤務予定項目状態」を編集する id = 1
		setStateData(scheData, lstState, 1);	
		//就業時間帯の編集状態を更新する
		setStateData(scheData, lstState, 2);
		
	}
	@Override
	public void updateScheWorkType(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode) {
		scheData.setWorkTypeCode(workTypeCode);
		//ドメインモデル「勤務予定項目状態」を編集する id = 1
		setStateData(scheData, lstState, 1);
	}
	@Override
	public void setStateData(BasicSchedule scheData, List<WorkScheduleState> lstState, int itemId) {
		List<WorkScheduleState> lstStateTmp = lstState.stream().filter(x -> x.getScheduleItemId() == itemId)
				.collect(Collectors.toList());
		if(lstStateTmp.isEmpty()) {
			WorkScheduleState state = new WorkScheduleState(ScheduleEditState.REFLECT_APPLICATION,
					itemId,
					scheData.getDate(),
					scheData.getEmployeeId());
			lstState.add(state);	
		} else {
			lstState.stream().map(x->{
				if(x.getScheduleItemId() == itemId) {
					x.setScheduleEditState(ScheduleEditState.REFLECT_APPLICATION);					
				}
				return x;
			}).collect(Collectors.toList());
		}
	}
	@Override
	public void updateScheWorkTime(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTimeCode) {	
		scheData.setWorkTimeCode(workTimeCode);
		//就業時間帯の編集状態を更新する
		setStateData(scheData, lstState, 2);
	}
	@Override
	public void updateStartTimeRflect(TimeReflectScheDto timeDto, BasicSchedule scheData, List<WorkScheduleState> lstState) {
		List<WorkScheduleTimeZone> workScheduleTimeZones = scheData.getWorkScheduleTimeZones();
		WorkScheduleTimeZone timeZoneData = null;
		if(workScheduleTimeZones.isEmpty()) {
			if(timeDto.isUpdateStart() && timeDto.isUpdateEnd()) {
				timeZoneData = new WorkScheduleTimeZone(1, new TimeWithDayAttr(timeDto.getStartTime() == null ? 0 : timeDto.getStartTime()),
						new TimeWithDayAttr(timeDto.getEndTime()== null ? 0 : timeDto.getEndTime()), BounceAtr.DIRECT_BOUNCE);
				List<WorkScheduleTimeZone> lstTimeZoneData = new ArrayList<>();
				lstTimeZoneData.add(timeZoneData);
				scheData.setWorkScheduleTimeZones(lstTimeZoneData);
			} else {
				return;
			}
			
		} else {
			List<WorkScheduleTimeZone> lstTimeZoneData = workScheduleTimeZones.stream()
					.filter(x -> x.getScheduleCnt() == timeDto.getFrameNumber())
					.collect(Collectors.toList());
			lstTimeZoneData.stream().forEach(x -> {
				x.updateTime(timeDto.isUpdateStart() ? new TimeWithDayAttr(timeDto.getStartTime() == null ? 0 : timeDto.getStartTime()) : x.getScheduleStartClock(), 
					timeDto.isUpdateEnd() ? new TimeWithDayAttr(timeDto.getEndTime() == null ? 0 : timeDto.getEndTime()) : x.getScheduleEndClock());
 			});			
		}
		//開始時刻の編集状態を更新する  勤務予定項目状態
		//予定項目ID=予定開始時刻(予定勤務回数=INPUT．枠番号)の項目ID 3, 5
		if(timeDto.getFrameNumber() == 1) {
			if(timeDto.isUpdateStart()) {
				setStateData(scheData, lstState, 3);
			}
			if(timeDto.isUpdateEnd()) {
				setStateData(scheData, lstState, 4);
			}
				
		} else {
			if(timeDto.isUpdateStart()) {
				setStateData(scheData, lstState, 5);
			}
			if(timeDto.isUpdateEnd()) {
				setStateData(scheData, lstState, 6);
			}	
		}
	}
}
