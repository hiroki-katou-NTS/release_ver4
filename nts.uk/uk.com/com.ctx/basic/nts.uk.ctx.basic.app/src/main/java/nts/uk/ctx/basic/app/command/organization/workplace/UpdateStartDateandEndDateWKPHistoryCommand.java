package nts.uk.ctx.basic.app.command.organization.workplace;


import lombok.Getter;

/**
 *  update StartDate of historyId1
 *	update endDate of historyId2
 */

@Getter
public class UpdateStartDateandEndDateWKPHistoryCommand {

	private String historyId1;

	private String historyId2;

	private String newStartDate;
	
	private String newEndDate;

}
