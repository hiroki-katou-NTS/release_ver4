package nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface GwSyJobTitleAdapter {

	List<EmployeeJobHistImport> findBySid(String employeeId, GeneralDate baseDate);
}
