package nts.uk.ctx.at.schedule.app.command.shift.businesscalendar.specificdate;

import lombok.Value;
/**
 * 
 * @author hoatt
 *
 */
@Value
public class SpecificDateItemCommand {
	private String companyId;
	
	private int useAtr;
	
	private int specificDateItemNo;
	
	private String specificName;
}
