package nts.uk.ctx.sys.gateway.dom.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Getter
@NoArgsConstructor
public class EmployeeImportNew {
	private String pid;

	private String businessName;

	private GeneralDate entryDate;

	private int gender;

	private GeneralDate birthDay;

	/** The employee id. */
	private String employeeId;

	/** The employee code. */
	private String employeeCode;

	private GeneralDate retiredDate;

	public EmployeeImportNew(String pid, String businessName, GeneralDate entryDate, int gender, GeneralDate birthDay,
			String employeeId, String employeeCode, GeneralDate retiredDate) {
		super();
		this.pid = pid;
		this.businessName = businessName;
		this.entryDate = entryDate;
		this.gender = gender;
		this.birthDay = birthDay;
		this.employeeId = employeeId;
		this.employeeCode = employeeCode;
		this.retiredDate = retiredDate;
	}
}
