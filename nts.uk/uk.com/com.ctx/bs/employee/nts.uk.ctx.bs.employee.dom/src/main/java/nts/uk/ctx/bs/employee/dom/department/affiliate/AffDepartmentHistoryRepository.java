package nts.uk.ctx.bs.employee.dom.department.affiliate;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;

public interface AffDepartmentHistoryRepository {
	/**
	 *  get AffDepartmentHistory by employeeId and standard date
	 *  
	 * @param employeeId
	 * @param historyId
	 * @param standardDate
	 * @return AffDepartmentHistory domain
	 */
	Optional<AffDepartmentHistory> getAffDeptHistByEmpHistStandDate(String employeeId, GeneralDate standardDate);
	
	/**
	 * get AffDepartmentHistory by history id
	 * @param historyId
	 * @return AffDepartmentHistory
	 */
	Optional<AffDepartmentHistory> getByHistId(String historyId);
	
	/**
	 * Get affiliation department history by employeeid
	 * @param employeeId
	 * @return
	 */
	Optional<AffDepartmentHistory> getAffDepartmentHistorytByEmployeeId(String employeeId);
	/**
	 * ドメインモデル「所属部門」を新規登録する
	 * @param domain
	 */
	void add(AffDepartmentHistory domain);
	/**
	 * 取得した「所属部門」を更新する
	 * @param domain
	 */
	void update(AffDepartmentHistory domain, DateHistoryItem item);
	/**
	 * ドメインモデル「所属部門（兼務）」を削除する
	 * @param domain
	 */
	void delete(AffDepartmentHistory domain, DateHistoryItem item);
}
