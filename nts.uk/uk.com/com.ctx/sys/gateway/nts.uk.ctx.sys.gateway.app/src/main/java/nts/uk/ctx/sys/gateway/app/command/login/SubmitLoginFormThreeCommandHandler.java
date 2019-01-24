/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.gateway.app.command.login.dto.CheckChangePassDto;
import nts.uk.ctx.sys.gateway.app.command.login.dto.ParamLoginRecord;
import nts.uk.ctx.sys.gateway.app.command.login.dto.SignonEmployeeInfoData;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.login.EmployCodeEditType;
import nts.uk.ctx.sys.gateway.dom.login.LoginStatus;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeCodeSettingAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeCodeSettingImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LoginMethod;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.shr.com.i18n.TextResource;

/**
 * The Class SubmitLoginFormThreeCommandHandler.
 */
@Stateless
public class SubmitLoginFormThreeCommandHandler extends LoginBaseCommandHandler<SubmitLoginFormThreeCommand> {

	/** The user repository. */
	@Inject
	private UserAdapter userAdapter;

	/** The employee code setting adapter. */
	@Inject
	private SysEmployeeCodeSettingAdapter employeeCodeSettingAdapter;

	/** The employee adapter. */
	@Inject
	private SysEmployeeAdapter employeeAdapter;
	
	@Inject
	private LoginRecordRegistService service;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected CheckChangePassDto internalHanler(CommandHandlerContext<SubmitLoginFormThreeCommand> context) {

		SubmitLoginFormThreeCommand command = context.getCommand();
		
		UserImportNew user = new UserImportNew();
		String oldPassword = null;
		EmployeeImport em = new EmployeeImport();
		String companyCode = command.getCompanyCode();
		String contractCode = command.getContractCode();
		String companyId = contractCode + "-" + companyCode;
		String employeeId = null;
		SignonEmployeeInfoData signonEmployeeInfoData =null;
		
		if (command.isSignOn()) {
			// アルゴリズム「アカウント照合」を実行する
			WindowsAccount windowAcc = this.compareAccount(context.getCommand().getRequest());
			
			//get User
			user = this.getUserAndCheckLimitTime(windowAcc);
			oldPassword = user.getPassword();
			signonEmployeeInfoData = this.getEmployeeInfoCaseSignon(windowAcc,true);
		} else {
			String employeeCode = command.getEmployeeCode();
			oldPassword = command.getPassword();
			
			// check validate input
			this.checkInput(command);
			
			//reCheck Contract
			if (!this.reCheckContract(contractCode, command.getContractPassword())) {
				return new CheckChangePassDto(false, null, true);
			}
			
			// Edit employee code
			employeeCode = this.employeeCodeEdit(employeeCode, companyId);
			
			// Get domain 社員
			em = this.getEmployee(companyId, employeeCode);
			
			// Check del state
			this.checkEmployeeDelStatus(em.getEmployeeId(), false);
					
			// Get User by PersonalId
			user = this.getUser(em.getPersonalId(), companyId,employeeCode);
			
			// check password
			String msgErrorId = this.compareHashPassword(user, oldPassword);
			if (msgErrorId != null){
				String remarkText = companyId + " " + employeeCode + " " + TextResource.localize(msgErrorId);
				ParamLoginRecord param = new ParamLoginRecord(companyId, LoginMethod.NORMAL_LOGIN.value,
						LoginStatus.Fail.value, remarkText, em.getEmployeeId());
				
				// アルゴリズム「ログイン記録」を実行する１
				this.service.callLoginRecord(param);
				return new CheckChangePassDto(false, msgErrorId,false);
			} 
			
			// check time limit
			this.checkLimitTime(user, companyId,employeeCode);
			employeeId = em.getEmployeeId();
		}
		
		//ルゴリズム「エラーチェック」を実行する (Execute algorithm "error check")
		this.errorCheck2(companyId, contractCode, user.getUserId(), command.isSignOn(), employeeId);
		
		//set info to session
		command.getRequest().changeSessionId();
		if (command.isSignOn()){
			this.initSession(user, command.isSignOn());
		} else {
			this.setLoggedInfo(user, em, companyCode);
		}
		//set role Id for LoginUserContextManager
		this.setRoleId(user.getUserId());
		
		//アルゴリズム「ログイン記録」を実行する
		if (!this.checkAfterLogin(user, oldPassword)){
			return new CheckChangePassDto(true, null,false);
		}
		
		Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
		
		if (command.isSignOn()) {
			loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
		}
		
		// アルゴリズム「ログイン記録」を実行する１
		ParamLoginRecord param = new ParamLoginRecord(companyId, loginMethod, LoginStatus.Success.value, null, employeeId);
		this.service.callLoginRecord(param);
		
		return new CheckChangePassDto(false, null,false);
	}

	/**
	 * Check input.
	 *
	 * @param command the command
	 */
	private void checkInput(SubmitLoginFormThreeCommand command) {

		// check input company code
		if (StringUtil.isNullOrEmpty(command.getCompanyCode(), true)) {
			throw new BusinessException("Msg_318");
		}
		// check input employee code
		if (StringUtil.isNullOrEmpty(command.getEmployeeCode(), true)) {
			throw new BusinessException("Msg_312");
		}
//		// check input password
//		if (StringUtil.isNullOrEmpty(command.getPassword(), true)) {
//			throw new BusinessException("Msg_310");
//		}
	}

	/**
	 * Employee code edit.
	 *
	 * @param employeeCode the employee code
	 * @param companyId the company id
	 * @return the string
	 */
	private String employeeCodeEdit(String employeeCode, String companyId) {
		Optional<EmployeeCodeSettingImport> findemployeeCodeSetting = employeeCodeSettingAdapter.getbyCompanyId(companyId);
		if (findemployeeCodeSetting.isPresent()) {
			EmployeeCodeSettingImport employeeCodeSetting = findemployeeCodeSetting.get();
			EmployCodeEditType editType = employeeCodeSetting.getEditType();
			Integer addNumberDigit = employeeCodeSetting.getNumberDigit();
			if (employeeCodeSetting.getNumberDigit() == employeeCode.length()) {
				// not edit employeeCode
				return employeeCode;
			}
			switch (editType) {
			case ZeroBefore:
				employeeCode = StringUtils.leftPad(employeeCode, addNumberDigit, "0");
				break;
			case ZeroAfter:
				employeeCode = StringUtils.rightPad(employeeCode, addNumberDigit, "0");
				break;
			case SpaceBefore:
				employeeCode = StringUtils.leftPad(employeeCode, addNumberDigit);
				break;
			case SpaceAfter:
				employeeCode = StringUtils.rightPad(employeeCode, addNumberDigit);
				break;
			default:
				break;
			}
			return employeeCode;
		}
		return employeeCode;
	}

	/**
	 * Gets the employee.
	 *
	 * @param companyId the company id
	 * @param employeeCode the employee code
	 * @return the employee
	 */
	private EmployeeImport getEmployee(String companyId, String employeeCode) {
		Optional<EmployeeImport> em = employeeAdapter.getCurrentInfoByScd(companyId, employeeCode);
		if (em.isPresent()) {
			return em.get();
		} else {
			String remarkText = companyId + " " + employeeCode + " " + TextResource.localize("Msg_301");
			ParamLoginRecord param = new ParamLoginRecord(companyId, LoginMethod.NORMAL_LOGIN.value,
					LoginStatus.Fail.value, remarkText, null);
			
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
			throw new BusinessException("Msg_301");
		}
	}

	/**
	 * Gets the user.
	 *
	 * @param personalId the personal id
	 * @return the user
	 */
	private UserImportNew getUser(String personalId, String companyId, String employeeCode) {
		Optional<UserImportNew> user = userAdapter.findUserByAssociateId(personalId);
		if (user.isPresent()) {
			return user.get();
		} else {
			String remarkText = companyId + " " + employeeCode + " " + TextResource.localize("Msg_301");
			ParamLoginRecord param = new ParamLoginRecord(companyId, LoginMethod.NORMAL_LOGIN.value,
					LoginStatus.Fail.value, remarkText, null);
			
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
			throw new BusinessException("Msg_301");
		}
	}

	/**
	 * Check limit time.
	 *
	 * @param user the user
	 */
	private void checkLimitTime(UserImportNew user, String companyId, String employeeCode) {
		if (user.getExpirationDate().before(GeneralDate.today())) {
			String remarkText = companyId + " " + employeeCode + " " + TextResource.localize("Msg_316");
			ParamLoginRecord param = new ParamLoginRecord(companyId, LoginMethod.NORMAL_LOGIN.value,
					LoginStatus.Fail.value, remarkText, null);
			
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
			throw new BusinessException("Msg_316");
		}
	}
}
