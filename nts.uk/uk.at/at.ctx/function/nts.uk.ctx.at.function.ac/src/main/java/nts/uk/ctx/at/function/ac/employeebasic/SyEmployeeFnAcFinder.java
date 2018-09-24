package nts.uk.ctx.at.function.ac.employeebasic;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.employeebasic.EmployeeBasicInfoFnImport;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.SyEmployeeFnAdapter;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeBasicInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class SyEmployeeFnAcFinder implements SyEmployeeFnAdapter {

	@Inject
	private SyEmployeePub syEmployeePub; 
	
	@Override
	public List<EmployeeBasicInfoFnImport> findBySIds(List<String> sIds) {
		List<EmployeeBasicInfoExport> data = syEmployeePub.findBySIds(sIds);
		if(data.isEmpty())
			return Collections.emptyList();
		return data.stream().map(c->convertToEmployeeBasicInfoExport(c)).collect(Collectors.toList());
	}
	private EmployeeBasicInfoFnImport convertToEmployeeBasicInfoExport(EmployeeBasicInfoExport export) {
		return new EmployeeBasicInfoFnImport(
				export.getPId(),
				export.getEmployeeId(),
				export.getPName(),
				export.getGender(),
				export.getBirthDay(),
				export.getPMailAddr().v(),
				export.getEmployeeCode(),
				export.getEntryDate(),
				export.getRetiredDate(),
				export.getCompanyMailAddr().v()
				);
	}
	@Override
	public List<String> getListEmployeeId(List<String> wkpIds, DatePeriod dateperiod) {
		List<String> listSyEmployee = syEmployeePub.getListEmployeeId(wkpIds, dateperiod);
		if(listSyEmployee.isEmpty())
			return Collections.emptyList();
		return listSyEmployee;
	}

}
