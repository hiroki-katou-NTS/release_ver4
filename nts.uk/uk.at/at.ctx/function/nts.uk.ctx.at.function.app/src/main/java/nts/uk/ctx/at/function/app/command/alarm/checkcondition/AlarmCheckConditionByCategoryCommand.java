package nts.uk.ctx.at.function.app.command.alarm.checkcondition;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.adapter.FixedConWorkRecordAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapterDto;

/**
 * 
 * @author HungTT
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class AlarmCheckConditionByCategoryCommand {

	private String code;

	private String name;

	private int category;

	private AlarmCheckTargetConditionCommand targetCondition;

	private List<String> availableRoles;
	
	private DailyAlarmCheckConditionCommand dailyAlarmCheckCondition;
	
	private Schedule4WeekAlarmCheckConditionCommand schedule4WeekAlarmCheckCondition;
	
	private int action;

	public AlarmCheckConditionByCategoryCommand(String code, String name, int category,
			AlarmCheckTargetConditionCommand targetCondition, List<String> availableRoles,
			DailyAlarmCheckConditionCommand dailyAlarmCheckCondition,
			Schedule4WeekAlarmCheckConditionCommand schedule4WeekAlarmCheckCondition, int action) {
		super();
		this.code = code;
		this.name = name;
		this.category = category;
		this.targetCondition = targetCondition;
		this.availableRoles = availableRoles;
		this.dailyAlarmCheckCondition = dailyAlarmCheckCondition;
		this.schedule4WeekAlarmCheckCondition = schedule4WeekAlarmCheckCondition;
		this.action = action;
	}

}
