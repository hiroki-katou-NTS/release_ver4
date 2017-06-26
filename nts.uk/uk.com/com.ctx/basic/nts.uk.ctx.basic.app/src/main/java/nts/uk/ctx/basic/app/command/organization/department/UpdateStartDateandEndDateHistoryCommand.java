package nts.uk.ctx.basic.app.command.organization.department;


import lombok.Getter;

/**
 *  update StartDate of historyId1
 *	update endDate of historyId2
 */

@Getter
public class UpdateStartDateandEndDateHistoryCommand {

	private String historyId1;

	private String historyId2;

	private String newStartDate;
	
	private String newEndDate;

}
