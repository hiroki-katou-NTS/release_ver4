/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.pub.employee;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface SearchEmployeePub.
 */
public interface SearchEmployeePub {

	/**
	 * Search by employee code.
	 *
	 * @param sCd the s cd
	 * @param systemType the system type
	 * @return the list
	 */
	// <<Public>> 社員コードで検索する
	public List<String> searchByEmployeeCode(String sCd, Integer systemType, GeneralDate referenceDate);
	public List<String> searchByEmployeeCode(String sCd, Integer systemType);

	/**
	 * Search by employee name.
	 *
	 * @param sName the s name
	 * @param systemType the system type
	 * @return the list
	 */
	// <<Public>> 社員名で検索する
	public List<String> searchByEmployeeName(String sName, Integer systemType, GeneralDate referenceDate);
	public List<String> searchByEmployeeName(String sName, Integer systemType);

	/**
	 * Search by entry date.
	 *
	 * @param period the period
	 * @param systemType the system type
	 * @return the list
	 */
	// <<Public>> 入社日で検索する
	public List<String> searchByEntryDate(DatePeriod period, Integer systemType, GeneralDate referenceDate);
	public List<String> searchByEntryDate(DatePeriod period, Integer systemType);

	/**
	 * Search by retirement date.
	 *
	 * @param period the period
	 * @param systemType the system type
	 * @return the list
	 */
	// <<Public>> 退職日で検索する
	public List<String> searchByRetirementDate(DatePeriod period, Integer systemType, GeneralDate referenceDate);
	public List<String> searchByRetirementDate(DatePeriod period, Integer systemType);
}
