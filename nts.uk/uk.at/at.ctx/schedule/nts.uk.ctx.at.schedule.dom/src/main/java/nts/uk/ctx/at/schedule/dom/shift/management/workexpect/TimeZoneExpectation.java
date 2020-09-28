package nts.uk.ctx.at.schedule.dom.shift.management.workexpect;

import java.util.Collections;
import java.util.List;

import lombok.Value;
import nts.arc.layer.dom.objecttype.DomainValue;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanDuplication;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;

/**
 * 時間帯の勤務希望
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.シフト管理.シフト勤務.勤務希望
 * @author dan_pv
 */
@Value
public class TimeZoneExpectation implements WorkExpectation, DomainValue {
	
	/**
	 * 勤務可能な時間帯のリスト
	 */
	private List<TimeSpanForCalc> workableTimeZoneList;
	
	/**
	 * 作る
	 * @param workableTimeZoneList　社員の勤務希望時間帯リスト
	 * @return
	 */
	public static TimeZoneExpectation create(List<TimeSpanForCalc> workableTimeZoneList) {
		
		if ( workableTimeZoneList.isEmpty()) {
			throw new RuntimeException("timezone list is empty!");
		}
		
		return new TimeZoneExpectation(workableTimeZoneList);
	}

	@Override
	public AssignmentMethod getAssignmentMethod() {
		return AssignmentMethod.TIME_ZONE;
	}

	@Override
	public boolean isMatchingExpectation(Require require, WorkInformation workInformation,
			List<TimeSpanForCalc> timeZoneList) {
		
		if (timeZoneList.isEmpty()) {
			return false;
		}
		
		return timeZoneList.stream().allMatch( timeZone -> 
			
			 this.workableTimeZoneList.stream()
					.anyMatch( workableTimeZone -> this.isScheduleTimeZoneInsideExpectedTimeZone(workableTimeZone, timeZone))
		);
		
	}

	@Override
	public WorkExpectDisplayInfo getDisplayInformation(Require require) {
		
		AssignmentMethod asignmentMethod = this.getAssignmentMethod();
		return new WorkExpectDisplayInfo(asignmentMethod, Collections.emptyList(), this.workableTimeZoneList);
	}
	
	private boolean isScheduleTimeZoneInsideExpectedTimeZone(TimeSpanForCalc expectedTimeZone , TimeSpanForCalc scheduleTimeZone) {
		return expectedTimeZone.checkDuplication(scheduleTimeZone) == TimeSpanDuplication.CONTAINS;
	}

}
