/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.model.employee;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

/**
 * The Interface EmployeeQueryModelRepository.
 */
public interface RegulationInfoEmployeeRepository {
	
	/**
	 * Find.
	 *
	 * @param comId the com id
	 * @param paramQuery the param query
	 * @return the list
	 */
	public List<RegulationInfoEmployee> find(String comId, EmployeeSearchQuery paramQuery);

	/**
	 * Find info by S ids.
	 *
	 * @param sIds the s ids
	 * @param referenceDate the reference date
	 * @return the list
	 */
	public List<RegulationInfoEmployee> findInfoBySIds(List<String> sIds, GeneralDate referenceDate);

	/**
	 * Find by sid.
	 *
	 * @param comId the com id
	 * @param sid the sid
	 * @return the regulation info employee
	 */
//	public RegulationInfoEmployee findBySid(String comId, String sid, GeneralDateTime baseDate, int systemType);
	public RegulationInfoEmployee findBySid(String comId, String sid, GeneralDateTime baseDate);

	/**
	 * Sort employees.
	 *
	 * @param comId the com id
	 * @param sIds the s ids
	 * @param systemType the system type
	 * @param orderNo the order no
	 * @param nameType the name type
	 * @param referenceDate the reference date
	 * @return the list
	 */
	// 社員を並び替える
	public List<String> sortEmployees(String comId, List<String> sIds, Integer systemType, Integer orderNo, Integer nameType, GeneralDateTime referenceDate);

	/**
	 * Sort employees.
	 *
	 * @param comId the com id
	 * @param sIds the s ids
	 * @param orders the orders
	 * @param referenceDate the reference date
	 * @return the list
	 */
	// 社員を並び替える(任意)
	public List<String> sortEmployees(String comId, List<String> sIds, List<SortingConditionOrder> orders, GeneralDateTime referenceDate);

}
