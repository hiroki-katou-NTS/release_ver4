package nts.uk.ctx.at.schedule.dom.schedule.workschedulestate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

/**
 * 勤務予定項目状態
 * 
 * @author sonnh1
 *
 */
@Getter
@AllArgsConstructor
public class WorkScheduleState extends AggregateRoot {
	@Setter
	private ScheduleEditState scheduleEditState;
	private int scheduleItemId;
	private GeneralDate ymd;
	private String sId;

	public static WorkScheduleState createFromJavaType(int scheduleEditState, int scheduleItemId, GeneralDate ymd,
			String sId) {
		return new WorkScheduleState(EnumAdaptor.valueOf(scheduleEditState, ScheduleEditState.class),
				scheduleItemId, ymd, sId);
	}
}
