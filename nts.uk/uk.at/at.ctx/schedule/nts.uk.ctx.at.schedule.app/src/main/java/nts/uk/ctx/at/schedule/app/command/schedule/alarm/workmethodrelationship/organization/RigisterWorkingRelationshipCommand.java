package nts.uk.ctx.at.schedule.app.command.schedule.alarm.workmethodrelationship.organization;

import lombok.Getter;

import java.util.List;

@Getter
public class RigisterWorkingRelationshipCommand {

	//E1_3
	private int unit;

	//E1_3
	private String workplaceId;

	//E1_3
	private String workplaceGroupId;

	//E4_7
	private String workTimeCode;

	//E4_2
	private int typeWorkMethod;

	//E5_3
	private int specifiedMethod;

	//E9
	private List<String> workMethods;

	//E7
	private int typeOfWorkMethods;

}
