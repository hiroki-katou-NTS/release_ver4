package nts.uk.ctx.at.record.ac.employee;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.employee.NarrowEmployeeAdapter;
import nts.uk.ctx.sys.auth.pub.employee.EmployeePublisher;
import nts.uk.ctx.sys.auth.pub.employee.NarrowEmpByReferenceRange;

@Stateless
public class NarrowEmployeeAdapterImpl implements NarrowEmployeeAdapter {

	@Inject
	private EmployeePublisher employeePublisher;
	
	@Override
	public List<String> findByEmpId(List<String> sID, int roleType) {
		if (sID.isEmpty())
			return Collections.emptyList();
		Optional<NarrowEmpByReferenceRange> narrowEmp = employeePublisher.findByEmpId(sID, roleType, GeneralDate.today());
		if (!narrowEmp.isPresent())
			return Collections.emptyList();
		return narrowEmp.get().getEmployeeID();
	}

}
