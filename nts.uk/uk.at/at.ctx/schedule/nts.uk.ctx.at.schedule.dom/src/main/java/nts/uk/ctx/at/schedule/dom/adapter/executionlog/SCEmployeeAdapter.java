package nts.uk.ctx.at.schedule.dom.adapter.executionlog;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmployeeDto;

/**
 * The Interface SCEmployeeAdapter.
 */
public interface SCEmployeeAdapter {

	/**
	 * Find by employee id.
	 *
	 * @param sId the s id
	 * @return the employee dto
	 */
	EmployeeDto findByEmployeeId(String sId);
	
	/**
	 * Find employee by list sid
	 * @param sids
	 * @return
	 */
	List<EmployeeDto> findByEmployeeIds(List<String> sids);
}
