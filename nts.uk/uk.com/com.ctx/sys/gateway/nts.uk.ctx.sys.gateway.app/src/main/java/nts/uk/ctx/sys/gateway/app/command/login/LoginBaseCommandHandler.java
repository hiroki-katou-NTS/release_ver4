/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.security.hash.password.PasswordHash;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.gateway.app.command.login.dto.CheckChangePassDto;
import nts.uk.ctx.sys.gateway.app.command.login.dto.ParamLoginRecord;
import nts.uk.ctx.sys.gateway.app.command.login.dto.SignonEmployeeInfoData;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsImport;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoDtoImport;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.StatusOfEmployment;
import nts.uk.ctx.sys.gateway.dom.adapter.employment.GwSyEmploymentAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.employment.SEmpHistImport;
import nts.uk.ctx.sys.gateway.dom.adapter.status.employment.StatusEmploymentAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.status.employment.StatusOfEmploymentImport;
import nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle.EmployeeJobHistImport;
import nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle.GwSyJobTitleAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.syworkplace.GwSyWorkplaceAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.syworkplace.SWkpHistImport;
import nts.uk.ctx.sys.gateway.dom.adapter.user.CheckBeforeChangePass;
import nts.uk.ctx.sys.gateway.dom.adapter.user.PassStatus;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.login.Contract;
import nts.uk.ctx.sys.gateway.dom.login.ContractCode;
import nts.uk.ctx.sys.gateway.dom.login.ContractRepository;
import nts.uk.ctx.sys.gateway.dom.login.LoginStatus;
import nts.uk.ctx.sys.gateway.dom.login.adapter.CompanyInformationAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleFromUserIdAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleIndividualGrantAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleType;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.CompanyInforImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.CompanyInformationImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeDataMngInfoImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeGeneralInfoImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImportNew;
import nts.uk.ctx.sys.gateway.dom.login.dto.RoleImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.RoleIndividualGrantImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.SDelAtr;
import nts.uk.ctx.sys.gateway.dom.login.service.CollectCompanyList;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.AccountLockPolicy;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.AccountLockPolicyRepository;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.LockInterval;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.LockOutMessage;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.PasswordPolicy;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.PasswordPolicyRepository;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LockOutData;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LockOutDataDto;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LockOutDataRepository;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LockType;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.lockoutdata.LoginMethod;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog.LoginLog;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog.LoginLogDto;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog.LoginLogRepository;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog.OperationSection;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog.SuccessFailureClassification;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.service.NotifyResult;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.service.PassWordPolicyAlgorithm;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountInfo;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;
import nts.uk.shr.com.enumcommon.Abolition;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.system.config.InstallationType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class LoginBaseCommandHandler.
 *
 * @param <T>
 *            the generic type
 */
@Stateless
public abstract class LoginBaseCommandHandler<T> extends CommandHandlerWithResult<T, CheckChangePassDto> {

	/** The employee adapter. */
	@Inject
	private SysEmployeeAdapter employeeAdapter;

	/** The company information adapter. */
	@Inject
	private CompanyInformationAdapter companyInformationAdapter;

	/** The manager. */
	@Inject
	private LoginUserContextManager manager;

	/** The contract repository. */
	@Inject
	private ContractRepository contractRepository;

	/** The role from user id adapter. */
	@Inject
	private RoleFromUserIdAdapter roleFromUserIdAdapter;

	/** The window account repository. */
	@Inject
	private WindowsAccountRepository windowAccountRepository;

	/** The user adapter. */
	@Inject
	private UserAdapter userAdapter;

	/** The account lock policy repository. */
	@Inject
	private AccountLockPolicyRepository accountLockPolicyRepository;

	/** The login log repository. */
	@Inject
	private LoginLogRepository loginLogRepository;

	/** The lock out data repository. */
	@Inject
	private LockOutDataRepository lockOutDataRepository;

	/** The Constant FIST_COMPANY. */
	private static final Integer FIST_COMPANY = 0;

	/** The role individual grant adapter. */
	@Inject
	private RoleIndividualGrantAdapter roleIndividualGrantAdapter;

	/** The Password policy repo. */
	@Inject
	private PasswordPolicyRepository pwPolicyRepo;

	/** The role adapter. */
	@Inject
	private RoleAdapter roleAdapter;

	/** The employee info adapter. */
	@Inject
	private EmployeeInfoAdapter employeeInfoAdapter;

	/** The company bs adapter. */
	@Inject
	private CompanyBsAdapter companyBsAdapter;
	
	/** The service. */
	@Inject
	private LoginRecordRegistService service;
	
	@Inject
	private EmployeeGeneralInfoAdapter employeeGeneralInfoAdapter;
	
	@Inject
	private StatusEmploymentAdapter statusEmploymentAdapter;
	@Inject
	private CollectCompanyList collectComList;
	
	/** The sy job title adapter. */
	@Inject
	private GwSyJobTitleAdapter syJobTitleAdapter;
	
	/** The sy workplace adapter. */
	@Inject
	private GwSyWorkplaceAdapter syWorkplaceAdapter;
	
	/** The sy employment adapter. */
	@Inject
	private GwSyEmploymentAdapter syEmploymentAdapter;
	
	@Inject
	private PassWordPolicyAlgorithm pWPolicyAlg;
	
	private static final boolean IS_EMPLOYMENT = true;
	private static final boolean IS_CLASSIFICATION = false;
	private static final boolean IS_JOBTITLE = true;
	private static final boolean IS_WORKPLACE = true;
	private static final boolean IS_DEPARTMENT = false;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected CheckChangePassDto handle(CommandHandlerContext<T> context) {
		return this.internalHanler(context);
	}

	/**
	 * Internal hanler.
	 *
	 * @param context the context
	 * @return the check change pass dto
	 */
	protected abstract CheckChangePassDto internalHanler(CommandHandlerContext<T> context);

	/**
	 * Re check contract.
	 *
	 * @param contractCode
	 *            the contract code
	 * @param contractPassword
	 *            the contract password
	 */
	protected boolean reCheckContract(String contractCode, String contractPassword) {
		InstallationType systemConfig = AppContexts.system().getInstallationType();
		// case Cloud
		if (systemConfig.value == InstallationType.CLOUD.value) {
			// reCheck contract
			// pre check contract
			if (!this.checkContractInput(contractCode, contractPassword)) {
				return false;
			}
			// contract auth
			if (!this.contractAccAuth(contractCode, contractPassword)) {
				return false;
			}
			return true;
		}
		return true;
	}

	/**
	 * Check contract input.
	 *
	 * @param contractCode
	 *            the contract code
	 * @param contractPassword
	 *            the contract password
	 */
	private boolean checkContractInput(String contractCode, String contractPassword) {
		if (StringUtil.isNullOrEmpty(contractCode, true)) {
			return false;
		}
		if (StringUtil.isNullOrEmpty(contractPassword, true)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the employee info case signon.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param isSignOn the is sign on
	 * @return the employee info case signon
	 */
	//EA修正履歴 No.3067、3068、3069
	protected SignonEmployeeInfoData getEmployeeInfoCaseSignon(WindowsAccount windowAcc, Boolean isSignOn) {
		// imported（GateWay）「会社情報」を取得する
		CompanyInformationImport companyInformationImport = this.companyInformationAdapter.findById(windowAcc.getCompanyId());

		// Imported（GateWay）「社員」を取得する
		Optional<EmployeeImportNew> opEm = this.employeeAdapter.getEmployeeBySid(windowAcc.getEmployeeId());

		// 社員が削除されたかを取得
		this.checkEmployeeDelStatus(windowAcc.getEmployeeId(), isSignOn);
		
		return new SignonEmployeeInfoData(companyInformationImport, opEm.get());
	}
	
	/**
	 * Check employee del status.
	 *
	 * @param sid
	 *            the sid
	 * @return the check change pass dto
	 */
	protected CheckChangePassDto checkEmployeeDelStatus(String sid, boolean isSignon) {
		// get Employee status
		Optional<EmployeeDataMngInfoImport> optMngInfo = this.employeeAdapter.getSdataMngInfo(sid);
		
		Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
		if (isSignon){
			loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
		}

		if (!optMngInfo.isPresent() || !SDelAtr.NOTDELETED.equals(optMngInfo.get().getDeletedStatus())) {
			ParamLoginRecord param = new ParamLoginRecord(" ", loginMethod, LoginStatus.Fail.value,
					TextResource.localize("Msg_301"), null);
			
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
			
			throw new BusinessException("Msg_301");
		}

		return new CheckChangePassDto(false, null, false);
	}

	/**
	 * Contract acc auth.
	 *
	 * @param contractCode
	 *            the contract code
	 * @param contractPassword
	 *            the contract password
	 */
	private boolean contractAccAuth(String contractCode, String contractPassword) {
		Optional<Contract> contract = contractRepository.getContract(contractCode);
		if (contract.isPresent()) {
			// check contract pass
			if (!PasswordHash.verifyThat(contractPassword, contract.get().getContractCode().v())
					.isEqualTo(contract.get().getPassword().v())) {
				return false;
			}
			// check contract time
			if (contract.get().getContractPeriod().start().after(GeneralDate.today())
					|| contract.get().getContractPeriod().end().before(GeneralDate.today())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Sets the logged info.
	 *
	 * @param user
	 *            the user
	 * @param em
	 *            the em
	 * @param companyCode
	 *            the company code
	 */
	public void setLoggedInfo(UserImportNew user, EmployeeImport em, String companyCode) {
		// set info to session
		manager.loggedInAsEmployee(user.getUserId(), em.getPersonalId(), user.getContractCode(), em.getCompanyId(),
				companyCode, em.getEmployeeId(), em.getEmployeeCode());
	}
    /**
    * CCG007-C.セッション生成
    * @param user ドメインモデル「ユーザ」
    * @param em 社員
    * @param companyCode 会社コード
    */
   public void initSessionC(UserImportNew user, EmployeeImport em, String companyCode) {
       //「ログインユーザコンテキスト」を新規作成、セッションに格納
       manager.loggedInAsEmployee(user.getUserId(), em.getPersonalId(), user.getContractCode(), em.getCompanyId(),
               companyCode, em.getEmployeeId(), em.getEmployeeCode());
       //権限（ロール）情報を取得、設定する 
       this.setRoleId(user.getUserId());
   }
   /**

	/**
	 * CCG007-B.セッション生成
	 * @param user ユーザ
	 */
	//EA修正履歴 No.3033
	public void initSession(UserImportNew user) {
		//切替可能な会社一覧を取得する(Có được danh sách công ty có thể chuyển đổi)
		List<String> lstCID = collectComList.getCompanyList(user.getUserId(), user.getContractCode());
		String assePersonId = user.getAssociatePersonId().isPresent() ? user.getAssociatePersonId().get() : null;
		if (lstCID.isEmpty()) {//取得できた場合　会社Listの件数　=　０
			//「ログインユーザコンテキスト」に情報設定（会社情報・社員情報はセットしません。） 
			//個人ID　＝　「ユーザ.紐付け先個人ID」
			manager.loggedInAsUser(user.getUserId(), assePersonId, user.getContractCode(), null, null);
		} else {//取得できた場合　会社Listの件数　＞　０
			Optional<EmployeeImport> opEm = Optional.empty();
			if(assePersonId != null){//ユーザ．紐付先個人ID　≠　Null
				//Imported（GateWay）「社員」を取得する (Imported (GateWay) Acquire 'Employee')
				opEm = employeeAdapter.getByPid(lstCID.get(FIST_COMPANY), assePersonId);
			}
			//Imported（GateWay）「会社情報」を取得する(Imported (GateWay) Acquire "company information")
			CompanyInformationImport comInfo = this.companyInformationAdapter
					.findById(lstCID.get(FIST_COMPANY));
			//「ログインユーザコンテキスト」に会社情報・社員情報をセットします。
			String contractCD = AppContexts.system().isOnPremise() ? "000000000000" : user.getContractCode();
			if(opEm.isPresent()){
				manager.loggedInAsEmployee(user.getUserId(), assePersonId, contractCD, 
						comInfo.getCompanyId(), comInfo.getCompanyCode(), opEm.get().getEmployeeId(), opEm.get().getEmployeeCode());
			}else{
				manager.loggedInAsUser(user.getUserId(), assePersonId, contractCD, comInfo.getCompanyId(), comInfo.getCompanyCode());
			}
		}
		//権限（ロール）情報を取得、設定する(Acquire and set authority (role) information)
		this.setRoleId(user.getUserId());
	}
	/**
	 * ログイン後チェック
	 * @param user
	 * @param oldPassword
	 * @return
	 */
	public CheckChangePassDto checkAfterLogin(UserImportNew user, String oldPassword) {
		//ドメインモデル「ユーザ．パスワード状態」をチェックする
		if(user.getPassStatus() == PassStatus.Reset.value){//「リセット」の場合(reset)
			//画面モデル「パスワード変更ダイアログ．変更理由」に該当したメッセージを設定する
			//(set message tương ứng vào 「ChangePasswordDialog．ChangeReason」 trên screenModel)
//			「社員．パスワード状態=リセット」の場合　⇒　#Msg_283#
			//画面「パスワード変更ダイアログ」を起動する (Khởi động màn hình  "ChangPasswordDialog")
			return new CheckChangePassDto("Msg_283");
		}
		//「リセット以外」の場合
		//imported（GateWay）「パスワードポリシー」を取得する
		Optional<PasswordPolicy> pwPoliOp = pwPolicyRepo.getPasswordPolicy(new ContractCode(user.getContractCode()));
		if (!pwPoliOp.isPresent()) {
			return new CheckChangePassDto(false, null, false);
		}
		PasswordPolicy pwPoli = pwPoliOp.get();
		//「パスワードポリシー.利用する」区分をチェックする (Check「PasswoedPlicy.isUse」)
		if(!pwPoli.isUse()){//利用しない場合(not use)
			return new CheckChangePassDto(false, null, false);
		}
		//利用する場合(use)
		//「パスワードポリシー.初回ログイン時にパスワード変更する」区分をチェックする (Check 「PasswordPolicy.initialPasswordChange」)
		if (pwPoli.isInitialPasswordChange()) {//変更する
			//ドメインモデル「ユーザ．パスワード状態」をチェックする (Check 「User．passwordStatus」)
			if (user.getPassStatus() == PassStatus.InitPassword.value) {//「初期パスワード」の場合(InitialPassword)
				//画面モデル「パスワード変更ダイアログ．変更理由」に該当したメッセージを設定する
				//(set message tương ứng vào 「ChangePasswordDialog．ChangeReason」 trên screenModel)
//				「社員．パスワード状態=初期パスワード」の場合 　⇒　#Msg_282#
				//画面「パスワード変更ダイアログ」を起動する (Khởi động màn hình  "ChangPasswordDialog")
				return new CheckChangePassDto("Msg_282");
			}
		}
		//入力されたパスワードは「パスワードポリシー」を満たしているかをチェックする (Check xem password đã nhập có thỏa mãn  "PasswordPolicy" không)
		CheckBeforeChangePass mess = userAdapter.passwordPolicyCheckForSubmit(user.getUserId(), oldPassword, user.getContractCode());
		if (mess.isError()) {//社員のパスワードがパスワードポリシーを満たさない場合(ko thỏa mãn PasswordPolicy)
			//「パスワードポリシー.ログイン時にパスワードに従っていない場合変更させる」区分をチェックする (Check 「PasswordPolicy.loginCheck」)
			if (pwPoli.isLoginCheck()) {//loginCheck = True
				//画面モデル「パスワード変更ダイアログ．変更理由」に該当したメッセージを設定する
				//(set message tương ứng vào 「ChangePasswordDialog．ChangeReason」 trên screenModel)
//				「社員．パスワード」がパスワードポリシーを満たさない場合 　⇒　#Msg_284#
				//画面「パスワード変更ダイアログ」を起動する (Khởi động màn hình  "ChangPasswordDialog")
				return new CheckChangePassDto("Msg_284");
			}
			return new CheckChangePassDto(false, null, false);
		}
		//パスワードポリシーを満たす場合(Thỏa mãn PasswordPolicy)
		//アルゴリズム「パスワードの期限切れチェック」を実行する (Thực hiện thuật toán 「Check hết hạn password」)
		//エラーあり：パスワードの有効期限が切れた場合
		if(pWPolicyAlg.checkExpiredPass(user.getUserId(), pwPoli.getValidityPeriod().v().intValue())){
			return new CheckChangePassDto("Msg_1516");
		}
		//エラーなし
		//アルゴリズム「パスワード変更通知チェック」を実行する (Thực hiện thuật toán 「Check thông báo thay đổi password」)
		//通知しない(ko thông báo)
		NotifyResult notifyResult = pWPolicyAlg.checkNotifyChangePass(user.getUserId(), pwPoli.getValidityPeriod().v().intValue(), pwPoli.getNotificationPasswordChange().v().intValue());
		if(!notifyResult.isNotifyFlg()){
			return new CheckChangePassDto(false, null, false);
		}
		//通知する(có  thông báo- có check vào mục thông báo)
		//確認メッセージ（Msg_1517）を表示する {0}【残り何日】
		return new CheckChangePassDto("Msg_1517", notifyResult.getSpanDays());
	}

	/**
	 * Check event.
	 *
	 * @param passwordPolicy
	 *            the password policy
	 * @param user
	 *            the user
	 * @param oldPassword
	 *            the old password
	 * @return true, if successful
	 */
	protected boolean checkEvent(PasswordPolicy passwordPolicy, UserImportNew user, String oldPassword) {
		// Check passwordPolicy isUse
		if (passwordPolicy.isUse()) {
			// Check Change Password at first login
			if (passwordPolicy.isInitialPasswordChange()) {
				// Check state
				if (user.getPassStatus() == PassStatus.InitPassword.value) {
					// Math PassPolicy
					return false;
				}
			}

			CheckBeforeChangePass mess = this.userAdapter.passwordPolicyCheckForSubmit(user.getUserId(), oldPassword,
					user.getContractCode());

			if (mess.isError()) {
				if (passwordPolicy.isLoginCheck()) {
					return false;
				}
				return true;
			}
		}
		return true;
	}

	/**
	 * Sets the role id.
	 *
	 * @param userId
	 *            the new role id
	 * @return the check change pass dto
	 */
	// set roll id into login user context
	public CheckChangePassDto setRoleId(String userId) {
		String humanResourceRoleId = this.getRoleId(userId, RoleType.HUMAN_RESOURCE);
		String employmentRoleId = this.getRoleId(userId, RoleType.EMPLOYMENT);
		String salaryRoleId = this.getRoleId(userId, RoleType.SALARY);
		String officeHelperRoleId = this.getRoleId(userId, RoleType.OFFICE_HELPER);
		String companyManagerRoleId = this.getRoleId(userId, RoleType.COMPANY_MANAGER);
		String systemManagerRoleId = this.getRoleId(userId, RoleType.SYSTEM_MANAGER);
		String personalInfoRoleId = this.getRoleId(userId, RoleType.PERSONAL_INFO);
		String groupCompanyManagerRoleId = this.getRoleId(userId, RoleType.GROUP_COMAPNY_MANAGER);
		// 就業
		if (employmentRoleId != null) {
			manager.roleIdSetter().forAttendance(employmentRoleId);
		}
		// 給与
		if (salaryRoleId != null) {
			manager.roleIdSetter().forPayroll(salaryRoleId);
		}
		// 人事
		if (humanResourceRoleId != null) {
			manager.roleIdSetter().forPersonnel(humanResourceRoleId);
		}
		// オフィスヘルパー
		if (officeHelperRoleId != null) {
			manager.roleIdSetter().forOfficeHelper(officeHelperRoleId);
		}
		// 会計
		// マイナンバー
		// グループ会社管理
		if (groupCompanyManagerRoleId != null) {
			manager.roleIdSetter().forGroupCompaniesAdmin(groupCompanyManagerRoleId);
		}
		// 会社管理者
		if (companyManagerRoleId != null) {
			manager.roleIdSetter().forCompanyAdmin(companyManagerRoleId);
		}
		// システム管理者
		if (systemManagerRoleId != null) {
			manager.roleIdSetter().forSystemAdmin(systemManagerRoleId);
		}
		// 個人情報
		if (personalInfoRoleId != null) {
			manager.roleIdSetter().forPersonalInfo(personalInfoRoleId);
		}

		return new CheckChangePassDto(false, null, false);
	}

	/**
	 * Gets the role id.
	 *
	 * @param userId
	 *            the user id
	 * @param roleType
	 *            the role type
	 * @return the role id
	 */
	protected String getRoleId(String userId, RoleType roleType) {
		String roleId = roleFromUserIdAdapter.getRoleFromUser(userId, roleType.value, GeneralDate.today());
		if (roleId == null || roleId.isEmpty()) {
			return null;
		}
		return roleId;
	}
	/**
	 * Compare hash password.
	 *
	 * @param user
	 *            the user
	 * @param password
	 *            the password
	 * @return the string
	 */
	protected String compareHashPassword(UserImportNew user, String password) {
		if (!PasswordHash.verifyThat(password, user.getUserId()).isEqualTo(user.getPassword())) {
			// アルゴリズム「ロックアウト」を実行する ※２次対応
			this.lockOutExecuted(user);
			return "Msg_302";
		}
		return null;
	}

	/**
	 * Lock out executed.
	 *
	 * @param user
	 *            the user
	 */
	@Transactional
	private void lockOutExecuted(UserImportNew user) {
		// ドメインモデル「アカウントロックポリシー」を取得する
		AccountLockPolicy accountLockPolicy = this.accountLockPolicyRepository
				.getAccountLockPolicy(new ContractCode(user.getContractCode())).get();
		if (accountLockPolicy.isUse()) {
			// ロックアウト条件に満たしているかをチェックする (Check whether the lockout condition is
			// satisfied)
			if (this.checkLoginLog(user.getUserId(), accountLockPolicy)) {
				// Add to domain model LockOutData
				LockOutDataDto dto = LockOutDataDto.builder().userId(user.getUserId())
						.contractCode(accountLockPolicy.getContractCode().v()).logoutDateTime(GeneralDateTime.now())
						.lockType(LockType.AUTO_LOCK.value).build();
				LockOutData lockOutData = new LockOutData(dto);
				this.lockOutDataRepository.add(lockOutData);
			}
		}
		// Add to the domain model LoginLog
		LoginLogDto dto = LoginLogDto.builder().userId(user.getUserId())
				.contractCode(accountLockPolicy.getContractCode().v()).processDateTime(GeneralDateTime.now())
				.successOrFail(SuccessFailureClassification.Failure.value).operation(OperationSection.Login.value)
				.programId(AppContexts.programId()).build();
		LoginLog loginLog = new LoginLog(dto);
		this.loginLogRepository.add(loginLog);
	}

	/**
	 * Check login log.
	 *
	 * @param userId
	 *            the user id
	 * @param accountLockPolicy
	 *            the account lock policy
	 * @return true, if successful
	 */
	private boolean checkLoginLog(String userId, AccountLockPolicy accountLockPolicy) {
		GeneralDateTime startTime = GeneralDateTime.now();
		// Check the domain model [Account lock policy. Error interval]
		if (accountLockPolicy.getLockInterval().lessThanOrEqualTo(new LockInterval(0))) {
			startTime = GeneralDateTime.fromString("1901/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss");
		} else {
			startTime = startTime.addMinutes(-1 * accountLockPolicy.getLockInterval().minute());
		}
		// Search the domain model [LoginLog] and acquire [number of failed
		// logs] → [failed times]
		Integer countFailure = this.loginLogRepository.getLoginLogByConditions(userId, startTime);

		// Return LockOut
		if (countFailure < accountLockPolicy.getErrorCount().v().intValue()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Compare account.
	 *
	 * @param context
	 *            the context
	 * @return the windows account
	 */
	// アルゴリズム「アカウント照合」を実行する
	protected WindowsAccount compareAccount(HttpServletRequest context) {
		// Windowsログイン時のアカウントを取得する
		// get UserName and HostName
		String username = AppContexts.windowsAccount().getUserName();
		String domain = AppContexts.windowsAccount().getDomain();

		// cut hostname
		String hostname = domain.substring(0, domain.lastIndexOf(";"));

		// ドメインモデル「Windowsアカウント情報」を取得する
		// ログイン時アカウントとドメインモデル「Windowsアカウント情報」を比較する - get 「Windowsアカウント情報」 from
		// 「Windowsアカウント」
		Optional<WindowsAccount> opWindowAccount = this.windowAccountRepository.findbyUserNameAndHostName(username,
				hostname);

		if (!opWindowAccount.isPresent()) {
			String remarkText = hostname + "\\" + username + " " + TextResource.localize("Msg_876");
			ParamLoginRecord param = new ParamLoginRecord(" ", LoginMethod.SINGLE_SIGN_ON.value, LoginStatus.Fail.value,
					remarkText, null);

			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);

			// エラーメッセージ（#Msg_876）を表示する。
			throw new BusinessException("Msg_876");
		}

		// set List WindowsAccountInfor
		List<WindowsAccountInfo> windows = opWindowAccount.get().getAccountInfos()
				.stream().filter(item -> item.getHostName().v().equals(hostname)
						&& item.getUserName().v().equals(username) && item.getUseAtr().equals(UseAtr.Use))
				.collect(Collectors.toList());

		if (windows.isEmpty()? true : windows.get(0).getUseAtr() == UseAtr.NotUse ? true : false) {
			String remarkText = hostname + " " + username + " " + TextResource.localize("Msg_876");
			ParamLoginRecord param = new ParamLoginRecord(" ", LoginMethod.SINGLE_SIGN_ON.value, LoginStatus.Fail.value,
					remarkText, null);

			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
			throw new BusinessException("Msg_876");
		}
		return opWindowAccount.get();
	}

	/**
	 * Gets the user and check limit time.
	 *
	 * @param windowAccount
	 *            the window account
	 * @return the user and check limit time
	 */
	public UserImportNew getUserAndCheckLimitTime(WindowsAccount windowAccount) {
		String username = AppContexts.windowsAccount().getUserName();
		String domain = AppContexts.windowsAccount().getDomain();
		// cut hostname
		String hostname = domain.substring(0, domain.lastIndexOf(";"));
		
		//EA修正履歴 No.3067、3068、3069
		// get user
		Optional<UserImportNew> optUserImport = this.userAdapter.findUserByEmployeeId(windowAccount.getEmployeeId());

		// Validate limit time
		if (optUserImport.isPresent()) {
			if (optUserImport.get().getExpirationDate().before(GeneralDate.today())) {
				String remarkText = hostname + " " + username + " " + TextResource.localize("Msg_316");
				ParamLoginRecord param = new ParamLoginRecord(" ", LoginMethod.SINGLE_SIGN_ON.value,
						LoginStatus.Fail.value, remarkText, null);
				
				// アルゴリズム「ログイン記録」を実行する１
				this.service.callLoginRecord(param);

				throw new BusinessException("Msg_316");
			}
		}
		return optUserImport.get();
	}

	/**
	 * Error check.
	 *
	 * @param userId
	 *            the user id
	 * @param roleType
	 *            the role type
	 * @param contractCode
	 *            the contract code
	 */
	// アルゴリズム「エラーチェック（形式１）」を実行する
	public void errorCheck(String loginId, String userId, Integer roleType, String contractCode, boolean isSignOn) {

		GeneralDate date = GeneralDate.today();
		// 切替可能な会社一覧を取得する Get list company
		List<String> companyIds = this.getListCompany(userId, date, roleType, contractCode);

		if (companyIds.isEmpty()) {
			Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
			if(isSignOn){
				loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
			}
			String remarkText = loginId + " " + TextResource.localize("Msg_1419");
			ParamLoginRecord param = new ParamLoginRecord(" ", loginMethod, LoginStatus.Fail.value, remarkText, null);
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);

			throw new BusinessException("Msg_1527");
		}

		String message = this.checkAccoutLock(loginId,contractCode, userId, " ", isSignOn).v();

		if (!message.isEmpty()) {
			// return messageError
			throw new BusinessException(message);
		}
	}

	/**
	 * Error check 2.
	 *
	 * @param companyId
	 *            the company id
	 */
	// ルゴリズム「エラーチェック」を実行する (Execute algorithm "error check")
	public void errorCheck2(String companyId, String contractCode, String userId, boolean isSignon, String employeeId) {

		// ドメインモデル「会社」の使用区分をチェックする (Check usage classification of domain model
		// "company")
		CompanyInforImport company = this.companyInformationAdapter.findComById(companyId);

		if (company.getIsAbolition() == Abolition.ABOLISH.value) {
			Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
			if (isSignon){
				loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
			}
			ParamLoginRecord param = new ParamLoginRecord(companyId, loginMethod, LoginStatus.Fail.value,
					TextResource.localize("Msg_281"), employeeId);
			
			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);

			throw new BusinessException("Msg_281");
		}
		if (employeeId != null) {
		//アルゴリズム「在職状態を取得」を実行する
		//Request No.280
		StatusOfEmploymentImport employmentStatus = this.statusEmploymentAdapter.getStatusOfEmployment(employeeId,
				GeneralDate.today());
			int empStatus = employmentStatus.getStatusOfEmployment();
		// check status = before_join
			if (employmentStatus == null || empStatus == StatusOfEmployment.BEFORE_JOINING.value
					|| empStatus == StatusOfEmployment.RETIREMENT.value) {
				
				String errorCode = "";
				if (employmentStatus == null || empStatus == StatusOfEmployment.BEFORE_JOINING.value) {
					errorCode = "Msg_286";
				}
				if (empStatus == StatusOfEmployment.RETIREMENT.value) {
					errorCode = "Msg_287";
				}
			Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
			if (isSignon) {
				loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
			}
			ParamLoginRecord param = new ParamLoginRecord(companyId, loginMethod, LoginStatus.Fail.value,
					TextResource.localize(errorCode), employeeId);

			// アルゴリズム「ログイン記録」を実行する１
			this.service.callLoginRecord(param);
				
			throw new BusinessException(errorCode);
			}
		
		
		}
		//update EA修正履歴 No.3054
		Optional<UserImportNew> opUserImport = userAdapter.findByUserId(userId);
		
		String loginId = "";
		if (opUserImport.isPresent()) {
			loginId = opUserImport.get().getLoginId();
		}
		String message = this.checkAccoutLock(loginId, contractCode, userId, companyId, isSignon).v();
		
		if (!message.isEmpty()) {
			// return messageError
			throw new BusinessException(message);
		}
		
		List<String> lstEmployeeId = new ArrayList<>();
//		DatePeriod periodEmployee = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		
		if (employeeId != null) { // employeeId = null when single sign on
			lstEmployeeId.add(employeeId);
			// 社員所属職位履歴を取得
			List<EmployeeJobHistImport> job = this.syJobTitleAdapter.findBySid(employeeId, GeneralDate.today());
			if (!job.isEmpty()) {

				// 社員所属職場履歴を取得
				List<SWkpHistImport> wkp = this.syWorkplaceAdapter.findBySid(companyId, employeeId,
						GeneralDate.today());
				if (!wkp.isEmpty()) {
					// 社員所属雇用履歴を取得
					Optional<SEmpHistImport> emp = this.syEmploymentAdapter.findSEmpHistBySid(companyId, employeeId,
							GeneralDate.today());
					if (!emp.isPresent()) {
						this.showError(isSignon, companyId, employeeId);
					}
				} else {
					this.showError(isSignon, companyId, employeeId);
				}
			} else {
				this.showError(isSignon, companyId, employeeId);
			}
		}
	}
	
	private void showError(boolean isSignon, String companyId, String employeeId) {
		Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
		if (isSignon) {
			loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
		}
		ParamLoginRecord param = new ParamLoginRecord(companyId, loginMethod, LoginStatus.Fail.value,
				TextResource.localize("Msg_1420"), employeeId);

		// アルゴリズム「ログイン記録」を実行する２ (Execute algorithm "login recording")
		this.service.callLoginRecord(param);

		throw new BusinessException("Msg_1420");
	}
	/**
	 * Gets the list company.
	 *
	 * @param userId
	 *            the user id
	 * @param date
	 *            the date
	 * @param roleType
	 *            the role type
	 * @return the list company
	 */
	// 切替可能な会社一覧を取得する
	private List<String> getListCompany(String userId, GeneralDate date, Integer roleType, String contractCode) {

		// ドメインモデル「ロール個人別付与」を取得する (get List RoleIndividualGrant)
		List<RoleIndividualGrantImport> roles = this.roleIndividualGrantAdapter.getByUserIDDateRoleType(userId, date,
				roleType);

		List<RoleImport> roleImp = new ArrayList<>();

		if (!roles.isEmpty()) {
			// ドメインモデル「ロール」を取得する (Acquire domain model "role"
			roles.stream().map(roleItem -> {
				return roleImp.addAll(this.roleAdapter.getAllById(roleItem.getRoleId()));
			}).collect(Collectors.toList());
		}

		GeneralDate systemDate = GeneralDate.today();

		// ドメインモデル「ユーザ」を取得する get domain "User"
		Optional<UserImportNew> user = this.userAdapter.getByUserIDandDate(userId, systemDate);

		List<EmployeeInfoDtoImport> employees = new ArrayList<>();

		if (!user.get().getAssociatePersonId().get().isEmpty()) {
			employees.addAll(this.employeeInfoAdapter.getEmpInfoByPid(user.get().getAssociatePersonId().get()));
			
			List<String> lstEmployeeId = employees.stream().map(dto -> dto.getEmployeeId()).collect(Collectors.toList());
			DatePeriod periodEmployee = new DatePeriod(GeneralDate.today(), GeneralDate.today());
			
			employees = employees.stream()
				.filter(empItem -> {
					boolean isEmployeeHis; 
					// Imported「社員の履歴情報」 を取得する(get Imported「社員の履歴情報」)
					EmployeeGeneralInfoImport importPub = employeeGeneralInfoAdapter.getEmployeeGeneralInfo(lstEmployeeId, periodEmployee, IS_EMPLOYMENT, IS_CLASSIFICATION, IS_JOBTITLE, IS_WORKPLACE, IS_DEPARTMENT);
					// 職場履歴一覧がEmpty or 雇用履歴一覧がEmpty or 職位履歴一覧がEmpty
					isEmployeeHis = !importPub.isLstWorkplace() && !importPub.isLstEmployment() && !importPub.isLstJobTitle();
					return !this.employeeAdapter.getStatusOfEmployee(empItem.getEmployeeId()).isDeleted() && isEmployeeHis;
				})
				.collect(Collectors.toList());
		}

		// imported（権限管理）「会社」を取得する (imported (authority management) Acquire
		// "company") Request No.51
		List<CompanyBsImport> companys = this.companyBsAdapter.getAllCompany();

		List<String> companyIdAll = companys.stream().map(item -> {
			return item.getCompanyId();
		}).collect(Collectors.toList());

		List<String> lstCompanyId = new ArrayList<>();

		// merge duplicate companyId from lstRole and lstEm
		if (!roleImp.isEmpty()) {
			List<String> lstComp = new ArrayList<>();
			roleImp.forEach(role -> {
				if (role.getCompanyId() != null) {
					lstComp.add(role.getCompanyId());
				}
			});

			lstCompanyId.addAll(lstComp);
		}

		if (!employees.isEmpty()) {
			List<String> lstComp = new ArrayList<>();
			employees.forEach(emp -> {
				if (emp.getCompanyId() != null) {
					lstComp.add(emp.getCompanyId());
				}
			});

			lstCompanyId.addAll(lstComp);
		}

		lstCompanyId = lstCompanyId.stream().distinct().collect(Collectors.toList());

		// 取得した会社（List）から、会社IDのリストを抽出する (Extract the list of company IDs from
		// the acquired company (List))
		List<String> lstCompanyFinal = lstCompanyId.stream().filter(com -> companyIdAll.contains(com))
				.collect(Collectors.toList());
		//EA修正履歴 No.3031
		List<String> lstResult = collectComList.checkStopUse(contractCode, lstCompanyFinal, userId);
		return lstResult;
	}

	/**
	 * Check accout lock.
	 *
	 * @param contractCode
	 *            the contract code
	 * @param userId
	 *            the user id
	 * @return the lock out message
	 */
	private LockOutMessage checkAccoutLock(String loginId, String contractCode, String userId, String companyId, boolean isSignOn) {
		// ドメインモデル「アカウントロックポリシー」を取得する (Acquire the domain model "account lock
		// policy")
		if (this.accountLockPolicyRepository.getAccountLockPolicy(new ContractCode(contractCode)).isPresent()) {
			AccountLockPolicy accountLockPolicy = this.accountLockPolicyRepository
					.getAccountLockPolicy(new ContractCode(contractCode)).get();
			if (accountLockPolicy.isUse()) {
				// ドメインモデル「ロックアウトデータ」を取得する (Acquire domain model "lockout data")
				Optional<LockOutData> lockoutData = this.lockOutDataRepository.findByUserId(userId);

				if (lockoutData.isPresent()) {
					Integer loginMethod = LoginMethod.NORMAL_LOGIN.value;
					if (isSignOn){
						loginMethod = LoginMethod.SINGLE_SIGN_ON.value;
					}
					String remarkText = loginId + " " + accountLockPolicy.getLockOutMessage().v();
					ParamLoginRecord param = new ParamLoginRecord(companyId, loginMethod, LoginStatus.Fail_Lock.value,
							remarkText, null);
					
					// アルゴリズム「ログイン記録」を実行する１
					this.service.callLoginRecord(param);

					// エラーメッセージ（ドメインモデル「アカウントロックポリシー.ロックアウトメッセージ」）を表示する
					// (Display error message (domain model "Account lock
					// policy. Lockout message"))
					return accountLockPolicy.getLockOutMessage();
				}
			}
		}
		return new LockOutMessage(null);
	}
}
