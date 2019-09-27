/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.log.dom.loginrecord;

import java.util.List;
import java.util.Optional;

/**
 * The Interface LoginRecordRepository.
 */
public interface LoginRecordRepository {
	
	/**
	 * Adds the.
	 *
	 * @param loginRecord the login record
	 */
	void add(LoginRecord loginRecord);
	
	/**
	 * Login record infor.
	 *
	 * @param operationId the operation id
	 * @return the login record
	 */
	Optional<LoginRecord> loginRecordInfor(String operationId);
	
	/**
	 * Log record infor.
	 *
	 * @param operationId the operation id
	 * @return the list
	 */
	List<LoginRecord> logRecordInfor(List<String> operationIds);
	
	/**
	 * CLI003F - EA修正履歴No3675
	 * @param operationIds
	 * @return
	 */
	List<LoginRecord> logRecordInforScreenF(List<String> operationIds);
	
	/**
	 * Log record infor.
	 *
	 * @param operationId the operation id
	 * @return the list
	 */
	List<LoginRecord> logRecordInforRefactors(List<String> operationIds, int offset, int limit);
}
