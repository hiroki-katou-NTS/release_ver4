/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.gateway.app.command.login.dto.LoginRecordInput;
import nts.uk.ctx.sys.gateway.app.command.login.dto.ParamLoginRecord;
import nts.uk.ctx.sys.gateway.dom.login.adapter.loginrecord.LoginRecordAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.loginrecord.LoginRecordInfor;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LoginMethod;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.context.ScreenIdentifier;
import nts.uk.shr.com.context.loginuser.NullLoginUserContext;
import nts.uk.shr.com.context.loginuser.role.LoginUserRoles;
import nts.uk.shr.com.security.audittrail.UserInfoAdaptorForLog;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.correction.processor.LogBasicInformationWriter;
import nts.uk.shr.com.security.audittrail.basic.LoginInformation;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;

/**
 * The Class LoginRecordRegistService.
 */
@Stateless
public class LoginRecordRegistService {

	/** The user info adaptor for log. */
	@Inject
	private UserInfoAdaptorForLog userInfoAdaptorForLog;

	/** The log basic infor. */
	@Inject
	private LogBasicInformationWriter logBasicInfor;

	/** The login record adapter. */
	@Inject
	private LoginRecordAdapter loginRecordAdapter;

	/**
	 * Call login record.
	 *
	 * @param param the param
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void callLoginRecord(ParamLoginRecord param) {
		// set input
		String programId = AppContexts.programId().substring(0, 6);
		String screenId = AppContexts.programId().substring(6);
		String url = AppContexts.requestedWebApi().getFullRequestPath();

		String queryParam = " ";

		if (url.indexOf("?") != -1) {
			queryParam = url.substring(url.indexOf("?"));
		}

		switch (LoginMethod.valueOf(param.loginMethod)) {
		case NORMAL_LOGIN:
			url = null;
			break;
		case SINGLE_SIGN_ON:
			break;
		default:
			break;
		}
		if (param.remark != null) {
			if (param.remark.length() > 100) {
				param.remark = param.remark.substring(0, 99);
			}
		}

		LoginRecordInput infor = new LoginRecordInput(programId, screenId, queryParam, param.loginStatus,
				param.loginMethod, url, param.remark, param.employeeId);

		// アルゴリズム「ログイン記録」を実行する１
		this.loginRecord(infor, param.companyId);
	}

	/**
	 * Login record.
	 *
	 * @param infor
	 *            the infor
	 * @param companyId
	 *            the company id
	 */
	public void loginRecord(LoginRecordInput infor, String companyId) {
		// 基盤(KIBAN)よりログイン者の基本情報を取得する (Acquire the basic information of
		// the login from the from KIBAN)
		LoginInformation loginInformation = new LoginInformation(
				!AppContexts.requestedWebApi().getRequestIpAddress().isEmpty()
						? AppContexts.requestedWebApi().getRequestIpAddress() : null,
				!AppContexts.requestedWebApi().getRequestPcName().isEmpty()
						? AppContexts.requestedWebApi().getRequestPcName() : null,
				AppContexts.windowsAccount() != null ? AppContexts.windowsAccount().getUserName() : null);

		// 実行日時を取得する (Acquire execution date and time)
		GeneralDateTime dateTime = GeneralDateTime.now();

		// 実行時情報.ログインユーザコンテキスト.ユーザIDが存在する場合 (Execution information. Login user
		// context. If the user ID exists)
		LoginUserContext user = AppContexts.user();

		UserInfo userInfor = new UserInfo(" ", " ", " ");

		if (!(user instanceof NullLoginUserContext) && user.userId() != null) {
			if (user.isEmployee()){
				userInfor = this.userInfoAdaptorForLog.findByEmployeeIdAndCompanyId(user.employeeId(), user.companyId());
			} else {
				UserInfo u = this.userInfoAdaptorForLog.findByUserId(user.userId());
				userInfor = new UserInfo(u.getUserId(), user.employeeId() == null? " " : user.employeeId(), u.getUserName());
			}
		} else {
			if (infor.employeeId != null) {
				userInfor = this.userInfoAdaptorForLog.findByEmployeeIdAndCompanyId(infor.employeeId, companyId);
			}
		}
		// set operationId
		String operationId = UUID.randomUUID().toString();

		// set targetProgram
		ScreenIdentifier targetProgram = new ScreenIdentifier(infor.programId, infor.screenId, infor.queryParam);

		// set authorityInformation
		LoginUserRoles authorityInformation = AppContexts.user().roles();

		// set LogBasicInformation
		LogBasicInformation logBasicInfor = new LogBasicInformation(operationId, companyId, userInfor, loginInformation,
				dateTime, authorityInformation, targetProgram, infor.remark != null ? Optional.of(infor.remark) : Optional.empty());

		boolean lockStatus = false;

		// set loginRecord
		LoginRecordInfor loginRecord = new LoginRecordInfor(operationId, infor.loginMethod, infor.loginStatus,
				lockStatus, infor.url, infor.remark);

		// add domain LoginInformation and LoginRecord
		this.registLoginInfor(logBasicInfor, loginRecord);

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.app.command.login.RegistLoginInfoInterface#registLoginInfor(nts.uk.shr.com.security.audittrail.basic.LogBasicInformation, nts.uk.ctx.sys.gateway.dom.login.adapter.loginrecord.LoginRecordInfor)
	 */
	public void registLoginInfor(LogBasicInformation logBasicInfor, LoginRecordInfor loginRecord) {
		// ドメインモデル「ログ基本情報」に追加する(Add to domain model "Log basic information")
		this.logBasicInfor.save(logBasicInfor);

		// ドメインモデル「ログイン記録」に追加する (Add to domain model 'login record')
		this.loginRecordAdapter.addLoginRecord(loginRecord);
	}

}
