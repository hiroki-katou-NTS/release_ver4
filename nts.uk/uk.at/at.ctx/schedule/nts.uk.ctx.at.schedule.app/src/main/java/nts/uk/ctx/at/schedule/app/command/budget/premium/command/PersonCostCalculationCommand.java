package nts.uk.ctx.at.schedule.app.command.budget.premium.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Value
public class PersonCostCalculationCommand {
	String companyID;

	String historyID;
	
	String startDate;

	String endDate;

	int unitPrice;

	String memo;
	
	List<PremiumSetCommand> premiumSets;
}
