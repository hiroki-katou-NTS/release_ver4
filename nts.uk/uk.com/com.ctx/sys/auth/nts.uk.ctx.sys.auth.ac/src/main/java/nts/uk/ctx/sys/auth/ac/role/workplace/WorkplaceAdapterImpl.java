/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.ac.role.workplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.workplace.AffAtWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffWorkplaceHistImport;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffWorkplaceImport;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffiliationWorkplace;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkplaceAdapterImpl.
 */
@Stateless
public class WorkplaceAdapterImpl implements WorkplaceAdapter {

	/** The sy workplace pub. */
	@Inject
	private SyWorkplacePub syWorkplacePub;
	
	@Inject
	private WorkplaceManagerRepository workplaceManagerRepository;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter#findListWkpIdByBaseDate(nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findListWkpIdByBaseDate(GeneralDate baseDate) {
		return syWorkplacePub.findListWorkplaceIdByBaseDate(baseDate);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter#findWkpByBaseDateAndEmployeeId(nts.arc.time.GeneralDate, java.lang.String)
	 */
	@Override
	public Optional<AffWorkplaceHistImport> findWkpByBaseDateAndEmployeeId(GeneralDate baseDate, String employeeId) {

		AffWorkplaceHistImport affWorkplaceHistImport = new AffWorkplaceHistImport();

		Optional<SWkpHistExport> opSWkpHistExport = syWorkplacePub.findBySid(employeeId, baseDate);
		if (opSWkpHistExport.isPresent()) {			
			affWorkplaceHistImport.setWorkplaceId(opSWkpHistExport.get().getWorkplaceId());			
		}
		return Optional.ofNullable(affWorkplaceHistImport);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter#findListWorkplaceIdByCidAndWkpIdAndBaseDate(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findListWorkplaceIdByCidAndWkpIdAndBaseDate(String companyId, String workplaceId,
			GeneralDate baseDate) {	
		return syWorkplacePub.findListWorkplaceIdByCidAndWkpIdAndBaseDate(companyId, workplaceId, baseDate);
	}

	@Override
	public List<AffWorkplaceImport> findListSIdByCidAndWkpIdAndPeriod(String workplaceId, GeneralDate startDate,
			GeneralDate endDate) {
		return syWorkplacePub.findListSIdByCidAndWkpIdAndPeriod(workplaceId, startDate, endDate).stream().map(
				item -> new AffWorkplaceImport(item.getEmployeeId(), item.getJobEntryDate(), item.getRetirementDate()))
				.collect(Collectors.toList());
	}

	private AffiliationWorkplace toImport (AffAtWorkplaceExport ex){
		return new AffiliationWorkplace(ex.getHistoryID(), ex.getEmployeeId(), ex.getWorkplaceId(), ex.getNormalWorkplaceID());
	}
	
	@Override
	public List<AffiliationWorkplace> findByListEmpIDAndDate(List<String> listEmployeeID, GeneralDate baseDate) {
		// TODO Auto-generated method stub
		return syWorkplacePub.findBySIdAndBaseDate(listEmployeeID, baseDate).stream().map(c -> toImport(c)).collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceImport> findListSIdWkpIdAndPeriod(List<String> lstWkpId, GeneralDate startDate,
			GeneralDate endDate) {
		return syWorkplacePub.getByLstWkpIdAndPeriod(lstWkpId, startDate, endDate).stream().map(
				item -> new AffWorkplaceImport(item.getEmployeeId(), item.getJobEntryDate(), item.getRetirementDate()))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getWorkplaceId(GeneralDate baseDate, String employeeId) {
		String companyID = AppContexts.user().companyId();
		List<String> subListWorkPlace = new ArrayList<>();
		// (Lấy all domain 「職場管理者」)
		List<WorkplaceManager> listWorkplaceManager = workplaceManagerRepository.findListWkpManagerByEmpIdAndBaseDate(employeeId, GeneralDate.today());
		for (WorkplaceManager workplaceManager : listWorkplaceManager) {
			if(!subListWorkPlace.contains(workplaceManager.getWorkplaceId())){
				subListWorkPlace.addAll(syWorkplacePub.findListWorkplaceIdByCidAndWkpIdAndBaseDate(companyID, workplaceManager.getWorkplaceId(), GeneralDate.today()));
			}
		}
		return subListWorkPlace.stream().distinct().collect(Collectors.toList());
	}

	

}
