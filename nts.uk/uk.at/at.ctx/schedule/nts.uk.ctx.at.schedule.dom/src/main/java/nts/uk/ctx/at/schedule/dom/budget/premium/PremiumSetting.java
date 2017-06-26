package nts.uk.ctx.at.schedule.dom.budget.premium;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class PremiumSetting {
	
	private String companyID;
	
	private String historyID;
	
	private Integer premiumID;
	
    private PremiumRate rate;
    
    private Integer attendanceID;
    
    private PremiumName name;
    
    private Integer displayNumber;
    
    private UseAttribute useAtr;
    
    private List<Integer> attendanceItems;

	public PremiumSetting(String companyID, String historyID, Integer premiumID, PremiumRate rate,
			Integer attendanceID, PremiumName name, Integer displayNumber, UseAttribute useAtr,
			List<Integer> attendanceItems) {
		super();
		this.companyID = companyID;
		this.historyID = historyID;
		this.premiumID = premiumID;
		this.rate = rate;
		this.attendanceID = attendanceID;
		this.name = name;
		this.displayNumber = displayNumber;
		this.useAtr = useAtr;
		this.attendanceItems = attendanceItems;
	}
    
}
