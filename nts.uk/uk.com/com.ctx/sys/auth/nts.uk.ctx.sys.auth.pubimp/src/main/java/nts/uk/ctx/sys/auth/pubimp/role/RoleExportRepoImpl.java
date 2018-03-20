/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.pubimp.role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.app.find.role.workplace.RoleWorkplaceIDFinder;
import nts.uk.ctx.sys.auth.app.find.role.workplace.WorkplaceIdDto;
import nts.uk.ctx.sys.auth.app.find.role.workplace.WorkplaceParam;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleAtr;
import nts.uk.ctx.sys.auth.dom.role.RoleRepository;
import nts.uk.ctx.sys.auth.pub.role.RoleExport;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.sys.auth.pub.role.RoleWhetherLoginPubExport;
import nts.uk.ctx.sys.auth.pub.role.WorkplaceIdExport;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class RoleExportRepoImpl.
 */
@Stateless
public class RoleExportRepoImpl implements RoleExportRepo{

	/** The role repo. */
	@Inject
	private RoleRepository roleRepo;
	
	@Inject
	private RoleWorkplaceIDFinder roleWorkplaceIDFinder;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.pub.role.RoleExportRepo#findByListRoleId(java.lang.String, java.util.List)
	 */
	@Override
	public List<RoleExport> findByListRoleId(String companyId, List<String> lstRoleId) {
		List<Role> lstRole = roleRepo.findByListRoleId(companyId, lstRoleId);
		if (!lstRole.isEmpty()) {
			return lstRole.stream().map(role -> {
				return new RoleExport(role.getRoleId(), role.getRoleCode().v(), role.getName().v());
			}).collect(Collectors.toList());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.pub.role.RoleExportRepo#findWorkPlaceIdByRoleId(java.lang.Integer)
	 */
	@Override
	//ロールIDから参照可能な職場リストを取得する
	public WorkplaceIdExport findWorkPlaceIdByRoleId(Integer systemType) {
		
		WorkplaceIdDto workplaceIdDto = roleWorkplaceIDFinder.findListWokplaceId(systemType);
		
		WorkplaceIdExport workplaceIdExport = new WorkplaceIdExport();
		workplaceIdExport.setIsAllEmp(workplaceIdDto.getIsAllEmp());
		workplaceIdExport.setListWorkplaceIds(workplaceIdDto.getListWorkplaceIds());
		
		return workplaceIdExport;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.sys.auth.pub.role.RoleExportRepo#findById(java.lang.String)
	 */
	@Override
	public List<RoleExport> findById(String roleId) {
		List<Role> lstRole = roleRepo.findById(roleId);
		if (!lstRole.isEmpty()) {
			return lstRole.stream().map(role -> {
				return new RoleExport(role.getRoleId(), role.getRoleCode().v(), role.getName().v());
			}).collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public WorkplaceIdExport findWorkPlaceIdNoRole(Integer systemType) {
		WorkplaceIdDto workplaceIdDto = roleWorkplaceIDFinder.findListWokplaceIdNoCheckRole(systemType);

		WorkplaceIdExport workplaceIdExport = new WorkplaceIdExport();
		workplaceIdExport.setIsAllEmp(workplaceIdDto.getIsAllEmp());
		workplaceIdExport.setListWorkplaceIds(workplaceIdDto.getListWorkplaceIds());

		return workplaceIdExport;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.pub.role.RoleExportRepo
	 * #getWorkPlaceIdByEmployeeReferenceRange(nts.arc.time.GeneralDate, java.lang.Integer)
	 */
	@Override
	public List<String> getWorkPlaceIdByEmployeeReferenceRange(GeneralDate baseDate,
			Integer employeeReferenceRange) {
		WorkplaceParam param = new WorkplaceParam();
		param.setBaseDate(baseDate);
		param.setReferenceRange(employeeReferenceRange);
		return this.roleWorkplaceIDFinder.findListWorkplaceId(param);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.pub.role.RoleExportRepo#findRoleIdBySystemType(java.lang.Integer)
	 */
	@Override
	public String findRoleIdBySystemType(Integer systemType) {
		return this.roleWorkplaceIDFinder.findRoleIdBySystemType(systemType);
	}

	@Override
	public RoleWhetherLoginPubExport getWhetherLoginerCharge() {
		String employmentRoleID = AppContexts.user().roles().forAttendance(); 
		String salaryRoleID = AppContexts.user().roles().forPayroll();
		String humanResourceRoleID = AppContexts.user().roles().forPersonnel(); 
		String officeHelperRoleID = AppContexts.user().roles().forOfficeHelper(); 
		String personalInforRoleID= AppContexts.user().roles().forPersonalInfo();
		
		
		RoleWhetherLoginPubExport outputRole = new RoleWhetherLoginPubExport();
		Optional<Role> roleEmployment =  roleRepo.findByRoleId(employmentRoleID);
		if(roleEmployment.isPresent()) {
			if(roleEmployment.get().getAssignAtr().equals(RoleAtr.INCHARGE)) {
				outputRole.setEmployeeCharge(true);
			}
		}
		Optional<Role> roleSalaryRole =  roleRepo.findByRoleId(salaryRoleID);
		if(roleSalaryRole.isPresent()) {
			if(roleSalaryRole.get().getAssignAtr().equals(RoleAtr.INCHARGE)) {
				outputRole.setSalaryProfessional(true);
			}
		}
		
		Optional<Role> roleHumanResource =  roleRepo.findByRoleId(humanResourceRoleID);
		if(roleHumanResource.isPresent()) {
			if(roleHumanResource.get().getAssignAtr().equals(RoleAtr.INCHARGE)) {
				outputRole.setHumanResOfficer(true);
			}
		}
		
		Optional<Role> roleOfficeHelper =  roleRepo.findByRoleId(officeHelperRoleID);
		if(roleOfficeHelper.isPresent()) {
			if(roleOfficeHelper.get().getAssignAtr().equals(RoleAtr.INCHARGE)) {
				outputRole.setOfficeHelperPersonne(true);
			}
		}
		
		Optional<Role> rolePersonalInfor =  roleRepo.findByRoleId(personalInforRoleID);
		if(rolePersonalInfor.isPresent()) {
			if(rolePersonalInfor.get().getAssignAtr().equals(RoleAtr.INCHARGE)) {
				outputRole.setPersonalInformation(true);
			}
		}
		
		return outputRole;
	}
}

