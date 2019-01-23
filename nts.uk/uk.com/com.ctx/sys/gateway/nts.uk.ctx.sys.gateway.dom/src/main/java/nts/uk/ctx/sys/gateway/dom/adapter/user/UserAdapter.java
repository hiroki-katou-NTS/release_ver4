/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.user;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;


/**
 * The Interface UserAdapter.
 */
public interface UserAdapter {
	
	/**
	 * Find user by contract and login id.
	 *
	 * @param contractCode the contract code
	 * @param loginId the login id
	 * @return the optional
	 */
	Optional<UserImport> findUserByContractAndLoginId(String contractCode, String loginId);
	
	/**
	 * Gets the user by associate id.
	 *
	 * @param associatePersonId the associate person id
	 * @return the user by associate id
	 */
	Optional<UserImportNew> findUserByAssociateId(String associatePersonId);
	
	
	/**
	 * Gets the list users by list person ids.
	 *
	 * @param listPersonIds the list person ids
	 * @return the list users by list person ids
	 */
	List<UserImport> getListUsersByListPersonIds(List<String> listPersonIds);
	
	
	/**
	 * Find by user id.
	 *
	 * @param userId the user id
	 * @return the optional
	 */
	Optional<UserImportNew> findByUserId(String userId);
	
	/**
	 * Password policy check.
	 *
	 * @param userId the user id
	 * @param newPass the new pass
	 * @param contractCode the contract code
	 * @return the check before change pass input
	 */
	//check passPolicy
	CheckBeforeChangePass passwordPolicyCheck(String userId, String newPass, String contractCode);
	
	/**
	 * Password policy check for submit.
	 *
	 * @param userId the user id
	 * @param newPass the new pass
	 * @param contractCode the contract code
	 * @return the check before change pass
	 */
	//check passPolicy
	CheckBeforeChangePass passwordPolicyCheckForSubmit(String userId, String newPass, String contractCode);
	
	/**
	 * Check before change password.
	 *
	 * @param userId the user id
	 * @param currentPass the current pass
	 * @param newPass the new pass
	 * @param reNewPass the re new pass
	 * @return the check before change pass
	 */
	CheckBeforeChangePass checkBeforeChangePassword(String userId, String currentPass, String newPass, String reNewPass);
	
	/**
	 * Check before reset password.
	 *
	 * @param userId the user id
	 * @param newPass the new pass
	 * @param reNewPass the re new pass
	 * @return the check before change pass
	 */
	//check before Reset Pass
	CheckBeforeChangePass checkBeforeResetPassword(String userId, String newPass, String reNewPass);
	
	/**
	 * Update password.
	 *
	 * @param userId the user id
	 * @param newPassword the new password
	 */
	void updatePassword(String userId,String newPassword);
	 
	/** requestlist 313 adapter
	 * @param userId
	 * @return
	 */
	Optional<UserInforExImport> getByEmpID(String empID);

	/**
	 * Find user by employee id.
	 *
	 * @param sid the sid
	 * @return the optional
	 */
	Optional<UserImportNew> findUserByEmployeeId(String sid);
	
	/**
	 * Find user by contract and login id new.
	 *
	 * @param contractCode the contract code
	 * @param loginId the login id
	 * @return the optional
	 */
	Optional<UserImportNew> findUserByContractAndLoginIdNew(String contractCode, String loginId);
	
	/**
	 * Gets the by user I dand date.
	 *
	 * @param userId the user id
	 * @param systemDate the system date
	 * @return the by user I dand date
	 */
	Optional<UserImportNew> getByUserIDandDate(String userId , GeneralDate systemDate);
	
	/**
	 * Gets the user.
	 *
	 * @param userIds the user ids
	 * @return the user
	 */
	List<UserDto> getUser(List<String> userIds);
}
