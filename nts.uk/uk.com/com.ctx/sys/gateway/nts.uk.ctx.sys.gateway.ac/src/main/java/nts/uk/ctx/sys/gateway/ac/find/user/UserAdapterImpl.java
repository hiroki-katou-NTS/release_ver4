/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.pub.user.ChangeUserPasswordPublisher;
import nts.uk.ctx.sys.auth.pub.user.CheckBeforeChangePassOutput;
import nts.uk.ctx.sys.auth.pub.user.CheckBeforePasswordPublisher;
import nts.uk.ctx.sys.auth.pub.user.PasswordMessageObject;
import nts.uk.ctx.sys.auth.pub.user.UserExport;
import nts.uk.ctx.sys.auth.pub.user.UserPublisher;
import nts.uk.ctx.sys.auth.pub.user.getuser.GetUserPublish;
import nts.uk.ctx.sys.gateway.dom.adapter.user.CheckBeforeChangePass;
import nts.uk.ctx.sys.gateway.dom.adapter.user.PasswordMessageImport;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserDto;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImport;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserInforExImport;

/**
 * The Class UserAdapterImpl.
 */
@Stateless
public class UserAdapterImpl implements UserAdapter {

	/** The user publisher. */
	@Inject
	private UserPublisher userPublisher;

	@Inject
	private CheckBeforePasswordPublisher checkPasswordPublisher;

	@Inject
	private ChangeUserPasswordPublisher changeUserPasswordPublisher;

	@Inject
	private GetUserPublish getUserPublish ;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter
	 * #findUserByContractAndLoginId(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<UserImportNew> findUserByContractAndLoginIdNew(String contractCode, String loginId) {
		Optional<UserExport> user = this.userPublisher.getUserByContractAndLoginId(contractCode, loginId);

		// Check found or not!
		if (user.isPresent()) {
			return this.covertToImportDomainNew(user);
		}
		return Optional.empty();
	}

	@Override
	public Optional<UserImport> findUserByContractAndLoginId(String contractCode, String loginId) {
		Optional<UserExport> user = this.userPublisher.getUserByContractAndLoginId(contractCode, loginId);

		// Check found or not!
		if (user.isPresent()) {
			return this.covertToImportDomain(user);
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter
	 * #findUserByAssociateId(java.lang.String)
	 */
	@Override
	public Optional<UserImportNew> findUserByAssociateId(String associatePersonId) {
		Optional<UserExport> user = this.userPublisher.getUserByAssociateId(associatePersonId);

		// Check found or not!
		if (user.isPresent()) {
			return this.covertToImportDomainNew(user);
		}
		return Optional.empty();
	}

	/**
	 * Covert to import domain.
	 *
	 * @param user
	 *            the user
	 * @return the optional
	 */
	private Optional<UserImport> covertToImportDomain(Optional<UserExport> user) {
		UserExport userInfo = user.get();
		return Optional.of(UserImport.builder().
				userId(userInfo.getUserID()).
				userName(userInfo.getUserName()).
				mailAddress(userInfo.getMailAddress()).
				loginId(userInfo.getLoginID())
				.associatePersonId(userInfo.getAssociatedPersonID()).password(userInfo.getPassword())
				.expirationDate(userInfo.getExpirationDate()).contractCode(userInfo.getContractCode()).build());
	}

	/**
	 * Covert to import domain new.
	 *
	 * @param user the user
	 * @return the optional
	 */
	private Optional<UserImportNew> covertToImportDomainNew(Optional<UserExport> user) {
		UserExport userInfo = user.get();
		
		return Optional.of(new UserImportNew(
				userInfo.getUserID(),
				userInfo.getUserName(),
				userInfo.getMailAddress(),
				userInfo.getLoginID(),
				userInfo.getAssociatedPersonID(),
				userInfo.getPassword(),
				userInfo.getContractCode(),
				userInfo.getExpirationDate(),
				Integer.valueOf( userInfo.getPassStatus())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#
	 * getListUsersByListPersonIds(java.util.List)
	 */
	@Override
	public List<UserImport> getListUsersByListPersonIds(List<String> listPersonIds) {
		return this.userPublisher.getListUserByListAsId(listPersonIds).stream()
				.map(userInfo -> UserImport.builder().userId(userInfo.getUserID()).userName(userInfo.getUserName())
						.mailAddress(userInfo.getMailAddress()).loginId(userInfo.getLoginID())
						.associatePersonId(userInfo.getAssociatedPersonID()).password(userInfo.getPassword())
						.expirationDate(userInfo.getExpirationDate()).contractCode(userInfo.getContractCode()).build())
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#findByUserId(java.
	 * lang.String)
	 */
	@Override
	public Optional<UserImportNew> findByUserId(String userId) {
		Optional<UserExport> optUserExport = this.userPublisher.getByUserId(userId);
		if (optUserExport.isPresent()) {
			return this.covertToImportDomainNew(optUserExport);
		}
		return Optional.empty();
	}

	@Override
	public Optional<UserInforExImport> getByEmpID(String empID) {
		return userPublisher.getByEmpID(empID)
				.map(x -> new UserInforExImport(x.getUserID(), x.getLoginID(), x.getEmpID(), x.getEmpCD()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#passwordPolicyCheck(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CheckBeforeChangePass passwordPolicyCheck(String userId, String newPass, String contractCode) {
		CheckBeforeChangePassOutput result = this.checkPasswordPublisher.passwordPolicyCheck(userId, newPass,
				contractCode);

		return new CheckBeforeChangePass(result.isError(), this.convert(result.getMessage()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#
	 * passwordPolicyCheckForSubmit(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CheckBeforeChangePass passwordPolicyCheckForSubmit(String userId, String newPass, String contractCode) {
		CheckBeforeChangePassOutput result = this.checkPasswordPublisher.passwordPolicyCheckForSubmit(userId, newPass,
				contractCode);

		return new CheckBeforeChangePass(result.isError(), this.convert(result.getMessage()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#
	 * checkBeforeChangePassword(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public CheckBeforeChangePass checkBeforeChangePassword(String userId, String currentPass, String newPass,
			String reNewPass) {
		CheckBeforeChangePassOutput result = this.checkPasswordPublisher.checkBeforeChangePassword(userId, currentPass,
				newPass, reNewPass);
		return new CheckBeforeChangePass(result.isError(), this.convert(result.getMessage()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#
	 * checkBeforeResetPassword(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CheckBeforeChangePass checkBeforeResetPassword(String userId, String newPass, String reNewPass) {
		CheckBeforeChangePassOutput result = this.checkPasswordPublisher.checkBeforeResetPassword(userId, newPass,
				reNewPass);
		return new CheckBeforeChangePass(result.isError(), this.convert(result.getMessage()));
	}

	/**
	 * Convert.
	 *
	 * @param lstMsg
	 *            the lst msg
	 * @return the list
	 */
	// covert from List<PasswordMessageObject> to List<PasswordMessageImport>
	private List<PasswordMessageImport> convert(List<PasswordMessageObject> lstMsg) {
		return lstMsg.stream().map(item -> {
			return new PasswordMessageImport(item.getMessage(), item.getParam());
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter#updatePassword(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void updatePassword(String userId, String newPassword) {
		changeUserPasswordPublisher.changePass(userId, newPassword);
	}
	
	@Override
	public Optional<UserImportNew> getByUserIDandDate(String userId , GeneralDate systemDate) {
		Optional<UserExport> user = this.userPublisher.getByUserIDandDate(userId, systemDate);

		// Check found or not!
		if (user.isPresent()) {
			return this.covertToImportDomainNew(user);
		}
		return Optional.empty();
	}

	@Override
	public List<UserDto> getUser(List<String> userIds) {
		return this.getUserPublish.getUser(userIds).stream().map(item -> {
		     UserDto dto = new UserDto();
		     dto.setUserId(item.getUserId());
		     dto.setLoginId(item.getLoginId());
		     dto.setUserName(item.getUserName().get());
		     dto.setAssociatedPersonID(item.getAssociatedPersonID().get());
		     dto.setMailAddress(item.getMailAddress().get());
		     dto.setPassword(item.getPassword());
		     return dto;
		    }).collect(Collectors.toList());
	}

	@Override
	public Optional<UserImportNew> findUserByEmployeeId(String sid) {
		Optional<UserExport> user = this.userPublisher.getBySid(sid);

		// Check found or not!
		if (user.isPresent()) {
			return this.covertToImportDomainNew(user);
		}
		return Optional.empty();
	}
}
