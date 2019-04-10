package nts.uk.ctx.at.record.dom.adapter.employment;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Data
@NoArgsConstructor
public class EmploymentHisOfEmployeeImport {
	
	private String sId;

	private GeneralDate startDate;

	private GeneralDate endDate;

	private String employmentCD;

	public EmploymentHisOfEmployeeImport(String sID, GeneralDate startDate, GeneralDate endDate, String employmentCD){
		this.sId = sID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.employmentCD = employmentCD;
	}
}
