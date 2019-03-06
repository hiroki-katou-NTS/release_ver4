package nts.uk.ctx.at.record.pub.workinformation;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
public class InfoCheckNotRegisterPubExport {
	private String employeeId;
	
	private GeneralDate workingDate;
	
	private String workTimeCode;
	
	private String workTypeCode;

	public InfoCheckNotRegisterPubExport(String employeeId, String workTimeCode, String workTypeCode, GeneralDate workingDate) {
		super();
		this.employeeId = employeeId;
		this.workTimeCode = workTimeCode;
		this.workTypeCode = workTypeCode;
		this.workingDate = workingDate;
	}
	
}
