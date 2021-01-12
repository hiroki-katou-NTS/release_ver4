package nts.uk.ctx.sys.assist.ac.reference.record;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.pub.employee.export.PersonEmpBasicInfoPub;
import nts.uk.ctx.sys.assist.dom.reference.record.EmpBasicInfoAdapter;
import nts.uk.ctx.sys.assist.dom.reference.record.EmpBasicInfoImport;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EmpBasicInfoAdapterImpl implements EmpBasicInfoAdapter {

	@Inject
	private PersonEmpBasicInfoPub personEmpBasicInfoPub;

	@Override
	public List<EmpBasicInfoImport> getEmployeeCodeByEmpId(String empId) {
		List<String> employeeIds = Arrays.asList(empId);
		return personEmpBasicInfoPub.getPerEmpBasicInfo(employeeIds).stream()
				.map(item -> {
					return new EmpBasicInfoImport(item.getPersonId(), item.getEmployeeId(), item.getBusinessName(),
							item.getGender(), item.getBirthday(), item.getEmployeeCode(), item.getJobEntryDate(),
							item.getRetirementDate());
				}).collect(Collectors.toList());
	}

	@Override
	public List<EmpBasicInfoImport> getEmpBasicInfo(String companyId, List<String> employeeIds) {
		return this.personEmpBasicInfoPub.getEmpBasicInfo(companyId, employeeIds).stream().map(item -> {
			return new EmpBasicInfoImport(item.getPersonId(), item.getEmployeeId(), item.getBusinessName(),
					item.getGender(), item.getBirthday(), item.getEmployeeCode(), item.getJobEntryDate(),
					item.getRetirementDate());
		}).collect(Collectors.toList());
	}
}
