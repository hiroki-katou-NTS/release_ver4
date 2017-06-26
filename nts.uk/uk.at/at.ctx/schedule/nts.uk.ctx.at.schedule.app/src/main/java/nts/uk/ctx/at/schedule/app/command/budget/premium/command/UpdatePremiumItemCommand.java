package nts.uk.ctx.at.schedule.app.command.budget.premium.command;

import lombok.Value;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Value
public class UpdatePremiumItemCommand {
	private String companyID;
	
	private Integer iD;
	
	private Integer attendanceID;
	
	private String name;

	private Integer displayNumber;

	private int useAtr;
}
