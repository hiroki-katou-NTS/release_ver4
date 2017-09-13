package nts.uk.ctx.bs.employee.pubimp.employee;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.Employee;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeRepository;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoDtoExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoPub;

@Stateless
public class EmployeeInfoPubImp implements EmployeeInfoPub {

	@Inject
	private EmployeeRepository repo;

	@Override
	public Optional<EmployeeInfoDtoExport> getEmployeeInfo(String companyId, String employeeCode, GeneralDate entryDate) {
		// TODO Auto-generated method stub
		
		Optional<Employee> domain = repo.findByEmployeeCode(companyId, employeeCode , entryDate);

		if (!domain.isPresent()) {
			return Optional.empty();
		} else {
			Employee _domain = domain.get();
			return Optional.of(new EmployeeInfoDtoExport(_domain.getCompanyId(), _domain.getSCd().v(), _domain.getSId(),
					_domain.getPId()));
		}

	}

}
