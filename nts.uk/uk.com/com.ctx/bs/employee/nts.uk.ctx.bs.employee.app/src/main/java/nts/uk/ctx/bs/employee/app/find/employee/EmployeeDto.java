package nts.uk.ctx.bs.employee.app.find.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.Employee;

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
				domain.getCompanyMail().v(),
				domain.getRetirementDate(),
				domain.getJoinDate());
	}
}
