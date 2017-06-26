package nts.uk.ctx.at.schedule.app.find.budget.premium.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@AllArgsConstructor
@Value
public class PersonCostCalculationSettingDto {
	String companyID;

	String historyID;
	
	GeneralDate startDate;

	GeneralDate endDate;

	int unitPrice;

	String memo;
	
	List<PremiumSetDto> premiumSets;
}



