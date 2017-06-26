/**
 * 
 */
package nts.uk.file.pr.app.export.retirementpayment.data;

import lombok.Value;

/**
 * @author hungnm
 *
 */
@Value
public class PersonalBasicDto {
	private String personName;
	
	private String joinDate;
	
	private String retireDate;
	
	private String retireReason;
	
	private String workingYears;
	
	private String otherOfficerWorkingYears;
}
