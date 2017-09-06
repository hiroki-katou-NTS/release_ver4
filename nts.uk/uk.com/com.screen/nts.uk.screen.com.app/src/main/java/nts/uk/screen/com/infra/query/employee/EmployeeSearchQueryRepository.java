/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.screen.com.infra.query.employee;

import java.util.Optional;

/**
 * The Interface EmployeeSearchQueryRepository.
 */
public interface EmployeeSearchQueryRepository {
	
	/**
	 * Find in all employee.
	 *
	 * @return the list
	 */
	Optional<Kcp009EmployeeSearchData> findInAllEmployee(String code, System system);
}
