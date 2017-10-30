/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.gateway.dom.login.User;
import nts.uk.ctx.sys.gateway.dom.login.adapter.CompanyInformationAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleIndividualGrantAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.RoleType;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.CompanyInformationImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.RoleImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.RoleIndividualGrantImport;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;

/**
 * The Class BaseCommand.
 *
 * @param <T>
 *            the generic type
 */
@Stateless
public abstract class LoginBaseCommand<T> extends CommandHandler<T> {

	/** The employee adapter. */
	@Inject
	private SysEmployeeAdapter employeeAdapter;
	
	/** The company information adapter. */
	@Inject
	private CompanyInformationAdapter companyInformationAdapter;
	
	/** The role individual grant adapter. */
	@Inject
	private RoleIndividualGrantAdapter roleIndividualGrantAdapter;

	@Inject 
	private RoleAdapter roleAdapter;
	
	@Inject
	private LoginUserContextManager manager;
	
	private static final Integer FIST_COMPANY = 0;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<T> context) {
		this.internalHanler(context);
	}

	/**
	 * Internal hanler.
	 *
	 * @param context the context
	 */
	protected abstract void internalHanler(CommandHandlerContext<T> context);

	protected void setLoggedInfo(User user,EmployeeImport em,String companyCode) {
		//set info to session 
		manager.loggedInAsEmployee(user.getUserId(), em.getPersonalId(), user.getContractCode().v(), em.getCompanyId(),
				companyCode, em.getEmployeeId(), em.getEmployeeCode());
	}

//	protected void setLoggedInfo(User user) {
//		EmployeeImport em = this.getEmployeeInfo("",user.getAssociatedPersonId());
//		CompanyInformationImport com = this.getCompanyInfo("");
//		if (em == null || com == null) {
//			throw new BusinessException("");
//		}
//		//set info to session 
//		manager.loggedInAsEmployee(user.getUserId(), em.getPersonalId(), user.getContractCode().v(), em.getCompanyId(),
//				com.getCompanyCode(), em.getEmployeeId(), em.getEmployeeCode());
//	}
	
	protected void initSession(User user) {
		List<String> lstCompanyId = this.getListCompany(user);
		if (lstCompanyId.isEmpty()) {

		} else {
			// get employee
			Optional<EmployeeImport> opEm = this.employeeAdapter.getByPid(lstCompanyId.get(FIST_COMPANY),
					user.getAssociatedPersonId());
			// save to session
			if (opEm.isPresent()) {
				// TODO get company info

				this.setLoggedInfo(user, opEm.get(), "TODO companyCode");
			}
		}
		this.setRoleId(user.getUserId());
	}
	
	protected List<String> getListCompany(User user) {
		List<String> lstCompanyId = new ArrayList<String>();
		// get roleIndividualGrant
		RoleIndividualGrantImport individualGrant = roleIndividualGrantAdapter.getByUser(user.getUserId(),
				GeneralDate.today());
		// get roles by roleId
		List<RoleImport> lstRole = roleAdapter.getAllById(individualGrant.getRoleId());
		// TODO get list employee imported by User associated Id #No.124
		List<EmployeeImport> lstEm = Arrays.asList();

		// merge duplicate companyId from lstRole and lstEm
		for (RoleImport item : lstRole) {
			if (item.getCompanyId() != null) {
				lstCompanyId.add(item.getCompanyId());
			}
		}

		for (EmployeeImport em : lstEm) {
			boolean haveComId = lstCompanyId.stream().anyMatch(item -> {
				return em.getCompanyId().equals(item);
			});
			if (!haveComId) {
				lstCompanyId.add(em.getCompanyId());
			}
		}
		return lstCompanyId;
	}
	protected EmployeeImport getEmployeeInfo(String companyId, String employeeCode) {
		// TODO
		EmployeeImport em = employeeAdapter.getCurrentInfoByScd(companyId, employeeCode).get();
		return em;
//		return null;
	}
	
	protected CompanyInformationImport getCompanyInfo(String companyId) {
		// TODO
		CompanyInformationImport com = companyInformationAdapter.findAll().get(0);
		return com;
	}
	
	protected void setRoleId(String userId)
	{
		// 就業
		if (this.getRoleId(userId, RoleType.EMPLOYMENT) != null) {
			manager.roleIdSetter().forPersonnel(this.getRoleId(userId, RoleType.EMPLOYMENT));
		}
		
		// 給与
		if (this.getRoleId(userId, RoleType.SALARY) != null) {
			manager.roleIdSetter().forPayroll(this.getRoleId(userId, RoleType.SALARY));
		}
		
		//人事
		
		
		// オフィスヘルパー
		if (this.getRoleId(userId, RoleType.OFFICE_HELPER) != null) {
			manager.roleIdSetter().forOfficeHelper(this.getRoleId(userId, RoleType.OFFICE_HELPER));
		}
		
		//会計
		//マイナンバー
		//グループ会社管理
		
		// 会社管理者
		if (this.getRoleId(userId, RoleType.COMPANY_MANAGER) != null) {
			manager.roleIdSetter().forCompanyAdmin(this.getRoleId(userId, RoleType.COMPANY_MANAGER));
		}

		//システム管理者
		if (this.getRoleId(userId, RoleType.SYSTEM_MANAGER) != null) {
			manager.roleIdSetter().forSystemAdmin(this.getRoleId(userId, RoleType.SYSTEM_MANAGER));
		}
		
		//個人情報
		if (this.getRoleId(userId, RoleType.PERSONAL_INFO) != null) {
			manager.roleIdSetter().forPersonalInfo(this.getRoleId(userId, RoleType.PERSONAL_INFO));
		}
	}

	protected String getRoleId(String userId, RoleType roleType) {
		RoleIndividualGrantImport roleImport = roleIndividualGrantAdapter.getByUserAndRole(userId, roleType);
		if (roleImport == null) {
			return null;
		}
		return roleImport.getRoleId();
	}
}
