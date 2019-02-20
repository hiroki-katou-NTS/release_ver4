/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.employment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.ctx.sys.gateway.dom.adapter.employment.SEmpHistImport;
import nts.uk.ctx.sys.gateway.dom.adapter.employment.GwSyEmploymentAdapter;

/**
 * The Class SyEmploymentAdapterImpl.
 */
@Stateless
public class GwSyEmploymentAdapterImpl implements GwSyEmploymentAdapter {

	/** The Sy employment pub. */
	@Inject
	private SyEmploymentPub SyEmploymentPub;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.employment.SyEmploymentAdapter#findSEmpHistBySid(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<SEmpHistImport> findSEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate) {
		Optional<SEmpHistExport> opEm = this.SyEmploymentPub.findSEmpHistBySid(companyId, employeeId, baseDate);
		if (opEm.isPresent()) {
			SEmpHistExport em = opEm.get();
			return Optional.of(new SEmpHistImport(em.getEmployeeId(), em.getEmploymentCode(), em.getEmploymentName(),
					em.getPeriod()));
		} else {
			return Optional.empty();
		}
	}
	
}
