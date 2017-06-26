package nts.uk.ctx.at.schedule.app.find.budget.premium.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Value
public class PremiumItemDto {
	private String companyID;
	
	private Integer iD;
	
	private Integer attendanceID;
	
	private String name;

	private Integer displayNumber;

	private int useAtr;
}
