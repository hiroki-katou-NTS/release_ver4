/**
 * 
 */
package nts.uk.shr.pereg.app.find.dto;

import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregPersonId;
import nts.uk.shr.pereg.app.PeregRecordId;

/**
 * @author danpv
 *
 */
public class PeregDomainDto {

	@PeregRecordId
	private String recordId;

	@PeregEmployeeId
	private String employeeId;

	@PeregPersonId
	private String personId;

	public PeregDomainDto() {
	}

	public PeregDomainDto(String recordId, String employeeId, String personId) {
		this.recordId = recordId;
		this.employeeId = employeeId;
		this.personId = personId;
	}

}
