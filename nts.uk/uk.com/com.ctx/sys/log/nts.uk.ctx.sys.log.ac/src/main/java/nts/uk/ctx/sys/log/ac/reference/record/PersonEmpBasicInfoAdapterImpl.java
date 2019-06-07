package nts.uk.ctx.sys.log.ac.reference.record;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  author: thuongtv
 */

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.export.PersonEmpBasicInfoPub;
import nts.uk.ctx.bs.employee.pub.employee.export.dto.PersonEmpBasicInfoDto;
import nts.uk.ctx.sys.log.dom.reference.PersonEmpBasicInfoAdapter;

@Stateless
public class PersonEmpBasicInfoAdapterImpl implements PersonEmpBasicInfoAdapter {

	@Inject
	private PersonEmpBasicInfoPub personEmpBasicInfoPub;
	
	@Inject
	private SyEmployeePub sysEmpPub;

	@Override
	public String getEmployeeCodeByEmpId(String empId) {
		List<String> employeeIds = Arrays.asList(empId);
		List<PersonEmpBasicInfoDto> lstPerson = personEmpBasicInfoPub.getPerEmpBasicInfo(employeeIds);
		if (!CollectionUtil.isEmpty(lstPerson)) {
			PersonEmpBasicInfoDto object = lstPerson.get(0);
			return object.getEmployeeCode();
		}
		return "";
	}
	
	
	@Override
	public Map<String,String> getEmployeeCodesByEmpIds(List<String> empIds) {
		//request list 228
		List<EmployeeInfoExport> lstEmp = sysEmpPub.getByListSid(empIds.stream().distinct().collect(Collectors.toList()));
		Map<String,String> mapReturn = new HashMap<>();
		if (!CollectionUtil.isEmpty(lstEmp)) {
			for (EmployeeInfoExport emp : lstEmp) {
				mapReturn.put(emp.getSid(), emp.getScd());
			}
		}
		return mapReturn;
	}
}
