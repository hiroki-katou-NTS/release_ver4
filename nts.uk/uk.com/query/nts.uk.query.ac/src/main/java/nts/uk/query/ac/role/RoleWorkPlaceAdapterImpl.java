/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.ac.role;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.query.model.employee.RoleWorkPlaceAdapter;

/**
 * The Class RoleWorkPlaceAdapterImpl.
 */
@Stateless
public class RoleWorkPlaceAdapterImpl implements RoleWorkPlaceAdapter {

	/** The role export repo. */
	@Inject
	private RoleExportRepo roleExportRepo;

	/*
	 * (non-Javadoc)
	 * @see nts.uk.query.model.employee.RoleWorkPlaceAdapter
	 * #getWorkPlaceIdByEmployeeReferenceRange(nts.arc.time.GeneralDate,
	 * java.lang.Integer)
	 */
	@Override
	public List<String> getWorkPlaceIdByEmployeeReferenceRange(GeneralDate baseDate, Integer employeeReferenceRange, Optional<Boolean> isWkplManager) {
		return this.roleExportRepo.getWorkPlaceIdByEmployeeReferenceRange(baseDate, employeeReferenceRange, isWkplManager);
	}
	
	@Override
	public String findRoleIdBySystemType(Integer systemType) {
		return this.roleExportRepo.findRoleIdBySystemType(systemType);
	}

}
