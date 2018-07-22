/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.log.dom.loginrecord;

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
	
}
