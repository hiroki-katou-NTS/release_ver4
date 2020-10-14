package nts.uk.ctx.sys.gateway.app.command.login.password;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.diagnose.stopwatch.embed.EmbedStopwatch;
import nts.arc.error.BusinessException;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.gateway.app.command.login.LoginCommandHandlerBase;
import nts.uk.ctx.sys.gateway.app.command.loginold.dto.CheckChangePassDto;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.ctx.sys.gateway.dom.login.password.AuthenticateEmployeePassword;
import nts.uk.ctx.sys.shared.dom.user.FindUser;
import nts.uk.ctx.sys.shared.dom.user.User;
import nts.uk.ctx.sys.shared.dom.user.UserRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PasswordAuthenticateCommandHandler 
	extends LoginCommandHandlerBase<PasswordAuthenticateCommand, PasswordAuthenticateCommandHandler.LoginState, CheckChangePassDto>{
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private  SysEmployeeAdapter employeeAdapter;
	
	
	// テナント認証失敗時
	@Override
	protected CheckChangePassDto getResultOnFailTenantAuth() {
		return CheckChangePassDto.failedToAuthTenant();
	}
	
	// 認証処理
	@Override
	protected LoginState processBeforeLogin(PasswordAuthenticateCommand command) {
		
		// 入力チェック
		checkInput(command);
		
		String companyCode = command.getCompanyCode();
		String tenantCode = command.getTenantCode();
		String companyId = tenantCode + "-" + companyCode;
		String employeeCode = command.getEmployeeCode();
		String password = command.getPassword();
		
		val require = EmbedStopwatch.embed(new RequireImpl());
		FindUser.byEmployeeCode(require, companyId, employeeCode);
		
		// パスワード認証
		boolean successPasswordAuth = AuthenticateEmployeePassword.authenticate(require, companyId, employeeCode, password);
		if(!successPasswordAuth) {
			// パスワード認証失敗
			return LoginState.failed();
		}
		// パスワード認証成功
		Optional<EmployeeImport> optEmployeeImport = employeeAdapter.getCurrentInfoByScd(companyId, employeeCode);
		return LoginState.success(optEmployeeImport.get());
	}

	@Override
	protected CheckChangePassDto processSuccess(LoginState state) {
		/* ログインチェック  */
		/* ログインログ  */
		return CheckChangePassDto.successToAuthPassword();
	}

	@Override
	protected CheckChangePassDto processFailure(LoginState state) {
		return CheckChangePassDto.failedToAuthPassword();
	}
	
	// 入力チェック
	public void checkInput(PasswordAuthenticateCommand command) {

		// 会社コードが未入力でないかチェック
		if (StringUtil.isNullOrEmpty(command.getCompanyCode(), true)) {
			throw new BusinessException("Msg_318");
		}
		// 社員コードが未入力でないかチェック
		if (StringUtil.isNullOrEmpty(command.getEmployeeCode(), true)) {
			throw new BusinessException("Msg_312");
		}
		// パスワードが未入力でないかチェック
		if (StringUtil.isNullOrEmpty(command.getPassword(), true)) {
			throw new BusinessException("Msg_310");
		}
	}
	
	static class LoginState implements LoginCommandHandlerBase.LoginState<PasswordAuthenticateCommand>{
			
		private boolean isSuccess;
		
		private EmployeeImport employeeImport;
		
		public LoginState(boolean isSuccess, EmployeeImport employeeImport) {
			this.isSuccess = isSuccess;
			this.employeeImport = employeeImport;
		}
		
		public static LoginState success(EmployeeImport employeeImport) {
			return new LoginState(true, employeeImport);
		}
		
		public static LoginState failed() {
			return new LoginState(false, null);
		}
		
		@Override
		public boolean isSuccess() {
			return isSuccess;
		}

		@Override
		public EmployeeImport getEmployee() {
			return employeeImport;
		}	
	}
	
	public class RequireImpl implements AuthenticateEmployeePassword.Require {

		@Override
		public Optional<String> getPersonalId(String companyId, String employeeCode) {
			val empInfo = employeeAdapter.getCurrentInfoByScd(companyId, employeeCode);
			if(!empInfo.isPresent()) {
				return Optional.empty();
			}
			return Optional.of(empInfo.get().getPersonalId());
		}

		@Override
		public Optional<User> getUser(String personalId) {
			return userRepository.getByAssociatedPersonId(personalId);
		}
	}
}
