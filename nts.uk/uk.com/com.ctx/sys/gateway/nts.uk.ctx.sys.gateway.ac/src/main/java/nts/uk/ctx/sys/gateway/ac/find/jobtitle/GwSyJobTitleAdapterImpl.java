/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.jobtitle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.jobtitle.EmployeeJobHistExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;
import nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle.EmployeeJobHistImport;
import nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle.GwSyJobTitleAdapter;

/**
 * The Class SyJobTitleAdapterImpl.
 */
@Stateless
public class GwSyJobTitleAdapterImpl implements GwSyJobTitleAdapter{

	@Inject
	private SyJobTitlePub syJobTitlePub;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle.SyJobTitleAdapter#findBySid(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<EmployeeJobHistImport> findBySid(String employeeId, GeneralDate baseDate) {
		List<EmployeeJobHistExport> lstExport = syJobTitlePub.findSJobHistByListSIdV2(Arrays.asList(employeeId),
				baseDate);
		if (!lstExport.isEmpty()) {
			return lstExport.stream().map(em -> {
				return new EmployeeJobHistImport(em.getEmployeeId(), em.getJobTitleID(), em.getJobTitleName(),
						em.getStartDate(), em.getEndDate());
			}).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}
}
