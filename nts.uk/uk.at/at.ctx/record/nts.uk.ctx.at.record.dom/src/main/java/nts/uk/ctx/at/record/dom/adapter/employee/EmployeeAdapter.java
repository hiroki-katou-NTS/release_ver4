package nts.uk.ctx.at.record.dom.adapter.employee;

import java.util.List;

/**
 * 
 * @author sonnh1
 *
 */
public interface EmployeeAdapter {
		List<EmployeeDto> getByListSID(List<String> sIds);
}
