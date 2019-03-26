/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.affiliate;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface AffiliationWorkplaceHistoryRepository.
 */
public interface AffWorkplaceHistoryRepository {
	/**
	 * get AffWorkplaceHistory by employee id and stand date
	 * @param employeeId
	 * @param generalDate
	 * @return AffWorkplaceHistory
	 */
	Optional<AffWorkplaceHistory> getByEmpIdAndStandDate(String employeeId, GeneralDate generalDate);
	
	/**
	 *  get AffWorkplaceHistory by history id
	 * @param histId
	 * @return AffWorkplaceHistory
	 */
	Optional<AffWorkplaceHistory> getByHistId(String histId);
	
	Optional<AffWorkplaceHistory> getByEmployeeId(String companyId, String employeeId);
	
	Optional<AffWorkplaceHistory> getByEmployeeIdDesc(String companyId, String employeeId);
	
	List<AffWorkplaceHistory> findByEmployees(List<String> employeeIds, GeneralDate date);
	
	List<AffWorkplaceHistory> findByEmployeesWithPeriod(List<String> employeeIds, DatePeriod period);
	
	Optional<AffWorkplaceHistory> getByHistIdAndBaseDate(String histId, GeneralDate date);
	/**
	 * ドメインモデル「所属職場」を新規登録する
	 * @param item
	 * @param sid
	 * @param cid
	 */
	void add(String cid, String sid, DateHistoryItem item);
	/**
	 * ドメインモデル「所属職場」を削除する
	 * @param histId
	 */
	void delete(String histId);
	
	/**
	 * ドメインモデル「所属職場」を取得する
	 * @param item
	 */
	void update(DateHistoryItem item);
	
	List<AffWorkplaceHistory> getWorkplaceHistoryByEmployeeIdAndDate(GeneralDate baseDate, String employeeId);
	
	List<AffWorkplaceHistory> getWorkplaceHistoryByWkpIdsAndDate(GeneralDate baseDate, List<String> workplaceIds);
	
	List<AffWorkplaceHistory> getWorkplaceHistoryByEmpIdsAndDate(GeneralDate baseDate, List<String> employeeIds);
	
	List<AffWorkplaceHistory> getWorkplaceHistoryByWorkplaceIdAndDate(GeneralDate baseDate, String workplaceId);
	
	List<AffWorkplaceHistory> searchWorkplaceHistory(GeneralDate baseDate, List<String> employeeIds, List<String> workplaceIds);
	
	List<String> getByWplIdAndPeriod(String workplaceId,GeneralDate startDate, GeneralDate endDate);
	
	List<String> getByLstWplIdAndPeriod(List<String> lstWkpId,GeneralDate startDate, GeneralDate endDate);

	List<AffWorkplaceHistory> getByListSid(List<String> employeeIds);
	/**
	 * sids, baseDate <= start date
	 * @param baseDate
	 * @param employeeIds
	 * @return
	 */
	List<AffWorkplaceHistory> getWorkplaceHistoryBySidsAndDateV2(GeneralDate baseDate, List<String> employeeIds);
}
