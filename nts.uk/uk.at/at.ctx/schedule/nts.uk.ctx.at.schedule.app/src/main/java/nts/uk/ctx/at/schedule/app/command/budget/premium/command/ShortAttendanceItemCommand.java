package nts.uk.ctx.at.schedule.app.command.budget.premium.command;

import lombok.AllArgsConstructor;
import lombok.Value;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Value
public class ShortAttendanceItemCommand {
	Integer shortAttendanceID;
	
	String name;
}
