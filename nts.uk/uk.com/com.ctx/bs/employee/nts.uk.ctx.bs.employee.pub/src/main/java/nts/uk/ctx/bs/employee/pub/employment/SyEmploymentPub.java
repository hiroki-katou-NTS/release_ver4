/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.employment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface WorkplacePub.
 */
public interface SyEmploymentPub {

	/**
	 * Find S job hist by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	// RequestList31
	Optional<SEmpHistExport> findSEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
	
	/**
	 * Find S job hist by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	//giống RQ 31 nhưng là truyền vào SID
	List<SEmpHistExport> findSEmpHistBySid(String companyId, String employeeId);

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	// RequestList89
	List<EmpCdNameExport> findAll(String companyId);
	
	/**
	 * Find by emp codes.
	 *
	 * @param companyId the company id
	 * @param empCodes the emp codes
	 * @return the list
	 */
	List<ShEmploymentExport> findByEmpCodes(String companyId, List<String> empCodes);
	
	/** 
	 * Find by List Sids and Period
	 * @param sids
	 * @param period
	 * @return
	 */
	//RequestList264
	List<EmploymentHisExport> findByListSidAndPeriod(List<String> sids , DatePeriod datePeriod);
	
	/**
	 * Gets the emp hist by sid and period.
	 *
	 * @param sids the sids
	 * @param datePeriod the date period
	 * @return the emp hist by sid and period
	 */
	// RequestList31-3
	// 社員ID（List）と指定期間から社員の雇用履歴を取得
	List<AffPeriodEmpCdHistExport> getEmpHistBySidAndPeriod(List<String> sids , DatePeriod datePeriod);
	
	/**
	 * Gets the employment map.
	 *
	 * @param companyId the company id
	 * @param empCodes the emp codes
	 * @return the employment map
	 */
	Map<String, String> getEmploymentMapCodeName(String companyId, List<String> empCodes);
	// RequestList31 - ver2
	Map<String, SEmpHistExport> findSEmpHistBySidVer2(String companyId, List<String> lstSID, GeneralDate baseDate);
}
