/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.affiliate;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface AffiliationWorkplaceHistoryRepository.
 */
public interface AffWorkplaceHistoryRepository_v1 {
	/**
	 * get AffWorkplaceHistory_ver1 by employee id and stand date
	 * @param employeeId
	 * @param generalDate
	 * @return AffWorkplaceHistory_ver1
	 */
	Optional<AffWorkplaceHistory_ver1> getByEmpIdAndStandDate(String employeeId, GeneralDate generalDate);
	
	/**
	 *  get AffWorkplaceHistory_ver1 by history id
	 * @param histId
	 * @return AffWorkplaceHistory_ver1
	 */
	Optional<AffWorkplaceHistory_ver1> getByHistId(String histId);
	
	Optional<AffWorkplaceHistory_ver1> getAffWorkplaceHistByEmployeeId(String employeeId);
	/**
	 * ドメインモデル「所属職場」を新規登録する
	 * @param domain
	 */
	void add(AffWorkplaceHistory_ver1 domain);
	/**
	 * ドメインモデル「所属職場」を削除する
	 * @param domain
	 */
	void delete(AffWorkplaceHistory_ver1 domain, DateHistoryItem item);
	
	/**
	 * ドメインモデル「所属職場」を取得する
	 * @param domain
	 */
	void update(AffWorkplaceHistory_ver1 domain, DateHistoryItem item);
}
