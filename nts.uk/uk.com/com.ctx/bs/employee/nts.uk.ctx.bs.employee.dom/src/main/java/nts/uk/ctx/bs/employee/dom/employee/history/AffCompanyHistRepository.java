package nts.uk.ctx.bs.employee.dom.employee.history;

import java.util.List;
import java.util.Set;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AffCompanyHistRepository {
	/** add new affiliation history */
	void add(AffCompanyHist domain);

	/** update one affiliation history */
	void update(AffCompanyHist domain);

	/** remove all affiliation history of one person */
	void remove(AffCompanyHist domain);

	/** remove one affiliation by personId & employeeId */
	void remove(String pId, String employeeId, String hisId);

	/** remove affiliation history of one employee */
	void remove(String pId, String employeeId);

	/** remove all affiliation history of one person */
	void remove(String pId);

	AffCompanyHist getAffCompanyHistoryOfPerson(String personId);

	AffCompanyHist getAffCompanyHistoryOfEmployee(String employeeId);
	
	AffCompanyHist getAffCompanyHistoryOfEmployeeDesc(String cid, String employeeId);
	
	List<AffCompanyHist> getAffCompanyHistoryOfEmployees(List<String> employeeIds);
	
	/**
	 * return AffCompanyHistByEmployee
	 * @param employeeIds
	 * @return
	 */
	List<AffCompanyHistByEmployee> getAffEmployeeHistory(List<String> employeeIds);
	
	List<AffCompanyHistByEmployee> getAffEmployeeHistory(List<String> employeeIds , DatePeriod datePeriod);

	AffCompanyHist getAffCompanyHistoryOfEmployeeAndBaseDate(String employeeId, GeneralDate baseDate);

	List<AffCompanyHist> getAffCompanyHistoryOfEmployeeListAndBaseDate(List<String> employeeIds, GeneralDate baseDate);
	
	AffCompanyHist getAffCompanyHistoryOfHistInfo(String histId);

	/** Hop.NT */

	/** Add new affiliation history */
	void add(String sid, String pId, AffCompanyHistItem item);

	/** Update one affiliation history */
	void update(AffCompanyHistItem item);

	/** End */
	
	List<AffCompanyHist> getAffComHisEmpByLstSidAndPeriod(List<String> employeeIds, DatePeriod datePeriod);
	
	List<String> getLstSidByLstSidAndPeriod(List<String> employeeIds, DatePeriod datePeriod);
}
