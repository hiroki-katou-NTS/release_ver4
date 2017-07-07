package nts.uk.ctx.basic.app.find.company.organization.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.company.organization.employee.Employee;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {
	private String personId;
	private String employeeId;
	private String employeeCode;
	private String employeeMail;
	private GeneralDate retirementDate;
	private GeneralDate joinDate;

	public  static EmployeeDto fromDomain(Employee domain) {
		return new EmployeeDto(domain.getPId(),
				domain.getSId(),
				domain.getSCd().v(),
				domain.getSMail().v(),
				domain.getRetirementDate(),
				domain.getJoinDate());
	}
}
