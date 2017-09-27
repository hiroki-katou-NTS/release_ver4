/**
 * 
 */
package nts.uk.ctx.at.record.app.find.workrecord.operationsetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danpv
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayRestrictionDto {
	
	private String cid;

	private Boolean yearDisplayAtr;
	
	private Boolean yearRemainingNumberCheck;
	
	private Boolean savingYearDisplayAtr;
	
	private Boolean savingYearRemainingNumberCheck;
	
	private Boolean compensatoryDisplayAtr;
	
	private Boolean compensatoryRemainingNumberCheck;
	
	private Boolean substitutionDisplayAtr;
	
	private Boolean substitutionRemainingNumberCheck;

}
