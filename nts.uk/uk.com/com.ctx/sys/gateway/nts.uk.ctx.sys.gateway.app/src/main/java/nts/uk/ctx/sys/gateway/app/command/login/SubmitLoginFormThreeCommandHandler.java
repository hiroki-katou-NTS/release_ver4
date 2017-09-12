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
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.gul.security.hash.password.PasswordHash;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.gateway.dom.login.Contract;
import nts.uk.ctx.sys.gateway.dom.login.ContractRepository;
import nts.uk.ctx.sys.gateway.dom.login.EmployCodeEditType;
import nts.uk.ctx.sys.gateway.dom.login.User;
import nts.uk.ctx.sys.gateway.dom.login.UserRepository;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeCodeSettingAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeCodeSettingImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;

/**
 * The Class SubmitLoginFormThreeCommandHandler.
 */
@Stateless
public class SubmitLoginFormThreeCommandHandler extends CommandHandler<SubmitLoginFormThreeCommand> {

	/** The user repository. */
	@Inject
	private UserRepository userRepository;

	/** The employee code setting adapter. */
	@Inject
	private SysEmployeeCodeSettingAdapter employeeCodeSettingAdapter;

	/** The employee adapter. */
	@Inject
	private SysEmployeeAdapter employeeAdapter;

	/** The contract repository. */
	@Inject
	private ContractRepository contractRepository;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SubmitLoginFormThreeCommand> context) {

		SubmitLoginFormThreeCommand command = context.getCommand();
		String companyCode = command.getCompanyCode();
		String employeeCode = command.getEmployeeCode();
		String password = command.getPassword();
		String contractCode = command.getContractCode();
		String companyId = contractCode+"-"+companyCode;
		// check validate input
		this.checkInput(command);

		//recheck contract
		// pre check contract
		this.checkContractInput(command);
		// contract auth
		this.contractAccAuth(command);
		
		// Edit employee code
		employeeCode = this.employeeCodeEdit(employeeCode, companyId);
		// Get domain 社員
		EmployeeImport em = this.getEmployee(companyId, employeeCode);
		// Get User by associatedPersonId
		User user = this.getUser(em.getEmployeeId().toString());
		// check password
		this.compareHashPassword(user, password);
		// check time limit
		this.checkLimitTime(user);
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
		// check input password
		if (StringUtil.isNullOrEmpty(command.getPassword(), true)) {
			throw new BusinessException("Msg_310");
		}
	}

	/**
	 * Check contract input.
	 *
	 * @param command
	 *            the command
	 */
	private void checkContractInput(SubmitLoginFormThreeCommand command) {
		if (StringUtil.isNullOrEmpty(command.getContractCode(), true)) {
			throw new RuntimeException();
		}
		if (StringUtil.isNullOrEmpty(command.getPassword(), true)) {
			throw new RuntimeException();
		}
	}

	/**
	 * Contract acc auth.
	 *
	 * @param command the command
	 */
	private void contractAccAuth(SubmitLoginFormThreeCommand command) {
		Optional<Contract> contract = contractRepository.getContract(command.getContractCode());
		if (contract.isPresent()) {
			// check contract pass
			if (!PasswordHash.verifyThat(command.getContractPassword(), contract.get().getContractCode().v())
					.isEqualTo(contract.get().getPassword().v())) {
				throw new RuntimeException();
			}
			// check contract time
			if (contract.get().getContractPeriod().getStartDate().after(GeneralDate.today())
					|| contract.get().getContractPeriod().getEndDate().before(GeneralDate.today())) {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
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
		Optional<EmployeeImport> em = employeeAdapter.getByEmployeeCode(companyId, employeeCode);
		if (em.isPresent()) {
			return em.get();
		} else {
			throw new BusinessException("Msg_301");
		}
	}

	/**
	 * Gets the user.
	 *
	 * @param associatedPersonId the associated person id
	 * @return the user
	 */
	private User getUser(String associatedPersonId) {
		Optional<User> user = userRepository.getByAssociatedPersonId(associatedPersonId);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new BusinessException("Msg_301");
		}
	}

	/**
	 * Compare hash password.
	 *
	 * @param user the user
	 * @param password the password
	 */
	private void compareHashPassword(User user, String password) {
		if (!PasswordHash.verifyThat(password, user.getUserId()).isEqualTo(user.getPassword().v())) {
			throw new BusinessException("Msg_302");
		}
	}

	/**
	 * Check limit time.
	 *
	 * @param user the user
	 */
	private void checkLimitTime(User user) {
		if (user.getExpirationDate().before(GeneralDate.today())) {
			throw new BusinessException("Msg_316");
		}
	}
}
