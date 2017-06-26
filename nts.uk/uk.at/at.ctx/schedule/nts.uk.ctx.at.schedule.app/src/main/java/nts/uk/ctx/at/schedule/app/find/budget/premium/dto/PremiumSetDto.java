package nts.uk.ctx.at.schedule.app.find.budget.premium.dto;

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
public class PremiumSetDto {
	String companyID;
	
	String historyID;
	
	Integer premiumID;
	
	Integer rate;
    
	Integer attendanceID;
    
    String name;
    
    Integer displayNumber;
    
    int useAtr;
    
    List<ShortAttendanceItemDto> attendanceItems;
}