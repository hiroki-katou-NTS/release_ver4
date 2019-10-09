/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.ac.executionlog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.ScWorkplaceAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.WorkplaceDto;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;

/**
 * The Class ScWorkplaceAdapterImpl.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScWorkplaceAdapterImpl implements ScWorkplaceAdapter {

	/** The workplace pub. */
	@Inject
	private SyWorkplacePub workplacePub;

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.budget.external.actualresult.WorkplaceAdapter#
	 * findWpkIdList(java.lang.String, java.util.Date)
	 */
	@Override
	public List<String> findWpkIdList(String companyId, String wpkCode, Date baseDate) {
		GeneralDate generalDate = GeneralDate.legacyDate(baseDate);
		return this.workplacePub.findWpkIdsByWkpCode(companyId, wpkCode, generalDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.adapter.ScWorkplaceAdapter#findWorkplaceById(
	 * java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkplaceDto> findWorkplaceById(String employeeId, GeneralDate baseDate) {
		return this.workplacePub.findBySid(employeeId, baseDate).map(workplace -> {
			WorkplaceDto dto = new WorkplaceDto();
			dto.setWorkplaceCode(workplace.getWorkplaceCode());
			dto.setWorkplaceId(workplace.getWorkplaceId());
			dto.setWorkplaceName(workplace.getWorkplaceName());
			return dto;
		});

	}

	@Override
	public List<String> findParentWpkIdsByWkpId(String companyId, String workplaceId, GeneralDate date) {
		return this.workplacePub.findParentWpkIdsByWkpId(companyId, workplaceId, date);
	}

	@Override
	public List<String> findWpkIdsBySid(String companyId, String employeeId, GeneralDate date) {
		return this.workplacePub.findWpkIdsBySid(companyId, employeeId, date);
	}
	
	

}
