/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.adapter.employee;

import java.util.List;

import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * The Interface EmpEmployeeAdapter.
 */
public interface EmpEmployeeAdapter {

	/**
	 * Find by emp id.
	 *
	 * @param empId the emp id
	 * @return the employee imported
	 */
	// for RequestList #1-2
	EmployeeImport findByEmpId(String empId);
	EmployeeImport findByEmpIdRequire(CacheCarrier cacheCarrier, String empId);
	
	List<EmployeeImport> findByEmpId(List<String> empIds);
	
	// RequestList335
	List<String> getListEmpByWkpAndEmpt(List<String> wkps , List<String> lstempts , DatePeriod dateperiod);
	
	// RequestList61
	List<PersonEmpBasicInfoImport> getPerEmpBasicInfo(List<String> employeeIds);
	
	/**
	 * 社員ID（List）と指定期間から所属会社履歴項目を取得
	 * @param sids
	 * @param datePeriod
	 * @return
	 */
	List<AffCompanyHistSharedImport> getAffCompanyHistByEmployee(CacheCarrier cacheCarrier, List<String> sids, DatePeriod datePeriod);
	
	EmployeeRecordImport findByAllInforEmpId(CacheCarrier cacheCarrier, String empId);
	/**
	 * 社員ID(List)と期間から分類の全ての情報を取得する
	 * @param companyId
	 * @param employeeIds
	 * @param datePeriod
	 * @return
	 */
	List<SClsHistImport> lstClassByEmployeeId(CacheCarrier cacheCarrier,String companyId, List<String> employeeIds,
			DatePeriod datePeriod);
	
	AffCompanyHistSharedImport GetAffComHisBySidAndBaseDate(String sid, GeneralDate baseDate);
	
	AffCompanyHistSharedImport GetAffComHisBySid(String cid, String sid);
	
	/**
	 * đối ứng cho cps003
	 * @param cid
	 * @param sid
	 * @return
	 */
	List<AffCompanyHistSharedImport> getAffComHisBySids(String cid, List<String> sid);
}
