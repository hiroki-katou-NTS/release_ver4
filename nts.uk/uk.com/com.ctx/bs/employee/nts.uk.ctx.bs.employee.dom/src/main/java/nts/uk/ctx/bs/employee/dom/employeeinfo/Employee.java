package nts.uk.ctx.bs.employee.dom.employeeinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
public class Employee extends AggregateRoot {
	private String companyId;

	private String pId;

	private String sId;

	private EmployeeCode sCd;

	private EmployeeMail sMail;

	private GeneralDate retirementDate;

	private GeneralDate joinDate;

	public static Employee createFromJavaStyle(String companyId, String pId, String sId, String sCd, String sMail,
			GeneralDate retirementDate, GeneralDate joinDate) {
		return new Employee(companyId, 
				pId, 
				sId, 
				new EmployeeCode(sCd), 
				new EmployeeMail(sMail), 
				retirementDate,
				joinDate);
	}
}
