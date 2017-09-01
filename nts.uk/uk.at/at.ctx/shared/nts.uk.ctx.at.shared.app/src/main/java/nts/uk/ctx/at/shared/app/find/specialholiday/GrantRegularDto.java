package nts.uk.ctx.at.shared.app.find.specialholiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrantRegularDto {
	
	private int specialHolidayCode;
	
	private GeneralDate grantStartDate;
	
	private int months;
	
	private int years;
	
	private int grantRegularMethod;
	
}
