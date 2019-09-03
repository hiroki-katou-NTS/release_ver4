/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.ac.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.auth.pub.employee.EmployeePublisher;
import nts.uk.ctx.sys.auth.pub.employee.NarrowEmpByReferenceRange;
import nts.uk.query.model.employee.EmployeeAuthAdapter;

/**
 * The Class EmployeeAuthAdapterImpl.
 */
@Stateless
public class EmployeeAuthAdapterImpl implements EmployeeAuthAdapter {

	/** The employee pub. */
	@Inject
	private EmployeePublisher employeePub;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.query.model.employee.EmployeeAuthAdapter#
	 * narrowEmpListByReferenceRange(java.util.List, java.lang.Integer)
	 */
	@Override
	public List<String> narrowEmpListByReferenceRange(List<String> sIds, Integer roleType, GeneralDate referenceDate) {
		Optional<NarrowEmpByReferenceRange> dto = this.employeePub
				.findByEmpId(CollectionUtil.isEmpty(sIds) ? new ArrayList<String>() : sIds, roleType, referenceDate);

		if (!dto.isPresent()) {
			return Collections.emptyList();
		}
		return dto.get().getEmployeeID();
	}

}
