/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.app.find.role.workplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffWorkplaceHistImport;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.SysAuthWorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceInfoImport;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleRepository;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.loginuser.role.LoginUserRoles;

/**
 * The Class RoleWorkplaceIDFinder.
 */
@Stateless
public class RoleWorkplaceIDFinder {

	/** The role repository. */
	@Inject
	private RoleRepository roleRepository;

	/** The workplace adapter. */
	@Inject
	private WorkplaceAdapter workplaceAdapter;

	@Inject
	private SysAuthWorkplaceAdapter sysAuthWorkplaceAdapter;
	
	/** The workplace manager repository. */
	@Inject
	private WorkplaceManagerRepository workplaceManagerRepository;

	/**
	 * Find list wokplace id.
	 *
	 * @param systemType the system type
	 * @return the list
	 */
	public WorkplaceIdDto findListWokplaceId(Integer systemType, GeneralDate referenceDate, boolean oldFlag) {
		String companyId = AppContexts.user().companyId();
		if (systemType == SystemType.ADMINISTRATOR.value) {
			WorkplaceIdDto workplaceIdDto = new WorkplaceIdDto();
			List<String> listWkpId = new ArrayList<>();
			if(oldFlag){
				listWkpId = workplaceAdapter.findListWkpIdByBaseDate(referenceDate);
			}else{
				listWkpId = sysAuthWorkplaceAdapter.getAllActiveWorkplaceInfo(companyId, referenceDate)
						.stream().map(WorkplaceInfoImport::getWorkplaceId).collect(Collectors.toList());
			}
			
			workplaceIdDto.setListWorkplaceIds(listWkpId);
			workplaceIdDto.setIsAllEmp(true);
			return workplaceIdDto;
		}

		String roleId = this.findRoleIdBySystemType(systemType);

		Optional<Role> opRole = roleRepository.findByRoleId(roleId);
		List<String> listWkpId = new ArrayList<>();
		WorkplaceIdDto workplaceIdDto = new WorkplaceIdDto();

		// if role is present
		if (opRole.isPresent()) {
			if (opRole.get().getEmployeeReferenceRange() == EmployeeReferenceRange.ALL_EMPLOYEE) {
				if(oldFlag){
					listWkpId = workplaceAdapter.findListWkpIdByBaseDate(referenceDate);
				}else{
					listWkpId = sysAuthWorkplaceAdapter.getAllActiveWorkplaceInfo(companyId, referenceDate)
							.stream().map(WorkplaceInfoImport::getWorkplaceId).collect(Collectors.toList());
				}
				workplaceIdDto.setListWorkplaceIds(listWkpId);
				workplaceIdDto.setIsAllEmp(true);
			} else {
				listWkpId = this.findListWkpIdByOtherCase(referenceDate, opRole.get());
				workplaceIdDto.setListWorkplaceIds(listWkpId);
				workplaceIdDto.setIsAllEmp(false);
			}
		// if role is not present	
		} else {
			workplaceIdDto.setListWorkplaceIds(listWkpId);
			workplaceIdDto.setIsAllEmp(true);
		}
		return workplaceIdDto;

	}
	
	/**
	 * Find wkp id by algorithm.
	 *
	 * @param referenceDate the reference date
	 * @param role the role
	 * @return the list
	 */
	public List<String> findListWkpIdByOtherCase(GeneralDate referenceDate, Role role) {
		List<String> listWkpId;

		String workplaceId = "";
		String employeeId = AppContexts.user().employeeId();
		String companyId = AppContexts.user().companyId();

		// Including management workplace = null
		// Get workplace manager
		List<WorkplaceManager> listWkpManager = workplaceManagerRepository.findListWkpManagerByEmpIdAndBaseDate(employeeId, referenceDate);
		
		// Map list workplace manager to list workplace id
		listWkpId = listWkpManager.stream().map(WorkplaceManager::getWorkplaceId).collect(Collectors.toList());
				
		// RequestList No.30 get aff workplace history
		Optional<AffWorkplaceHistImport> optAffWorkplaceHistImport = sysAuthWorkplaceAdapter
				.findWkpByBaseDateAndEmpId(referenceDate, employeeId);

		if (optAffWorkplaceHistImport.isPresent()) {
			workplaceId = optAffWorkplaceHistImport.get().getWorkplaceId();
		}

        // check workplace id != null
        if (workplaceId != null) {
            listWkpId.add(workplaceId);
        }

		// [No.567] 職場の下位職場を取得する
		if (role.getEmployeeReferenceRange() == EmployeeReferenceRange.DEPARTMENT_AND_CHILD && !workplaceId.isEmpty()) {
			List<String> wkpIds = sysAuthWorkplaceAdapter.getAllChildrenOfWorkplaceId(companyId, referenceDate, workplaceId);
			listWkpId.addAll(wkpIds);
		}

		return listWkpId.stream().distinct().collect(Collectors.toList());
	}
	
	/**
	 * Find list workplace id.
	 *
	 * @param param the param
	 * @return the list
	 * @author TrangTh
	 */
	public List<String> findListWorkplaceId(WorkplaceParam param) {
		List<String> listWkpId;
		String companyId = AppContexts.user().companyId();
		//check ReferenceRange 
		if (param.getReferenceRange() == EmployeeReferenceRange.ALL_EMPLOYEE.value) {
			//get list WorkplaceId by WorkplaceAdapter
			listWkpId = sysAuthWorkplaceAdapter.getAllActiveWorkplaceInfo(companyId, param.getBaseDate())
					.stream().map(WorkplaceInfoImport::getWorkplaceId).collect(Collectors.toList());
		} else {
			//get list WorkplaceId by function findListWkpId
			listWkpId = this.findListWkpId(param);
		}
		return listWkpId;
	}
	
	/**
	 * Find list wkp id.
	 *
	 * @param param the param
	 * @return the list
	 */
	public List<String> findListWkpId(WorkplaceParam param) {
		List<String> listWkpId = new ArrayList<>();

		String workplaceId = "";
		String employeeId = AppContexts.user().employeeId();
		String companyId = AppContexts.user().companyId();

		// Including management workplace = false

		// RequestList No.30 get aff workplace history
		Optional<AffWorkplaceHistImport> optAffWorkplaceHistImport = sysAuthWorkplaceAdapter
				.findWkpByBaseDateAndEmpId(param.getBaseDate(), employeeId);

		if (optAffWorkplaceHistImport.isPresent()) {
			workplaceId = optAffWorkplaceHistImport.get().getWorkplaceId();
		}

        // check workplace id != null
        if (workplaceId != null) {
            listWkpId.add(workplaceId);
        }

		// [No.567] 職場の下位職場を取得する
		if (param.getReferenceRange() == EmployeeReferenceRange.DEPARTMENT_AND_CHILD.value && !workplaceId.isEmpty()) {
			List<String> wkpIds = sysAuthWorkplaceAdapter.getAllChildrenOfWorkplaceId(companyId, param.getBaseDate(), workplaceId);
			listWkpId.addAll(wkpIds);
		}

		return listWkpId.stream().distinct().collect(Collectors.toList());
	}
		

	/**
	 * Find role id.
	 *
	 * @param systemType the system type
	 * @return the string
	 */
	public String findRoleIdBySystemType(Integer systemType) {
		LoginUserRoles loginUserRoles = AppContexts.user().roles();

		// Mock data
		switch (SystemType.valueOf(systemType)) {
		case PERSONAL_INFORMATION:
			// EmployeeReferenceRange = 0
			return loginUserRoles.forPersonalInfo();

		case EMPLOYMENT:
			// EmployeeReferenceRange = 2
			return loginUserRoles.forAttendance();

		case SALARY:
			// EmployeeReferenceRange = 1
			return loginUserRoles.forPayroll();

		case HUMAN_RESOURCES:
			// EmployeeReferenceRange = 1
			return loginUserRoles.forPersonnel();

		case ADMINISTRATOR:
			// EmployeeReferenceRange = 0
			return loginUserRoles.forCompanyAdmin();

		default:
			break;
		}

		return null;
	}

	/**
	 * The Enum SystemType.
	 */
	public enum SystemType {

		/** The personal information. */
		// システム管理者
		PERSONAL_INFORMATION(1),

		/** The employment. */
		// 就業
		EMPLOYMENT(2),

		/** The salary. */
		// 給与
		SALARY(3),

		/** The human resources. */
		// 人事
		HUMAN_RESOURCES(4),

		/** The administrator. */
		// 管理者
		ADMINISTRATOR(5);

		/** The value. */
		public final int value;

		/** The Constant values. */
		private final static SystemType[] values = SystemType.values();

		/**
		 * Instantiates a new system type.
		 *
		 * @param value the value
		 */
		private SystemType(int value) {
			this.value = value;
		}

		/**
		 * Value of.
		 *
		 * @param value the value
		 * @return the system type
		 */
		public static SystemType valueOf(Integer value) {
			// Invalid object.
			if (value == null) {
				return null;
			}

			// Find value.
			for (SystemType val : SystemType.values) {
				if (val.value == value) {
					return val;
				}
			}
			// Not found.
			return null;
		}
	}
    
	/**
	 * Find list wokplace id.
	 *
	 * @param systemType the system type
	 * @return the list
	 */
	public WorkplaceIdDto findListWokplaceIdNoCheckRole(Integer systemType) {
		GeneralDate referenceDate = GeneralDate.today();
		List<String> listWkpId = new ArrayList<>();
		WorkplaceIdDto workplaceIdDto = new WorkplaceIdDto();
		listWkpId = workplaceAdapter.findListWkpIdByBaseDate(referenceDate);
		workplaceIdDto.setListWorkplaceIds(listWkpId);
		workplaceIdDto.setIsAllEmp(true);
		return workplaceIdDto;

	}
}
