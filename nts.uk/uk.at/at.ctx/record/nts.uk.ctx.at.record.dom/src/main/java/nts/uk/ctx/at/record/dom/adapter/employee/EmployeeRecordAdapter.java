package nts.uk.ctx.at.record.dom.adapter.employee;

import java.util.List;
import java.util.Map;

public interface EmployeeRecordAdapter {
	
	EmployeeRecordImport getPersonInfor(String employeeId);
	
	List<EmployeeRecordImport> getPersonInfor(List<String> listEmployeeId);
	
	Map<String, String> mapEmpCodeToId(List<String> listEmpCode);

}
