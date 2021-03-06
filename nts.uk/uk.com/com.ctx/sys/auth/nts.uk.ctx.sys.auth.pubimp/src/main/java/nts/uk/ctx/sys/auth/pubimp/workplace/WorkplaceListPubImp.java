/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.pubimp.workplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.algorithm.AcquireListWorkplaceByEmpIDService;
import nts.uk.ctx.sys.auth.dom.algorithm.AcquireUserIDFromEmpIDService;
import nts.uk.ctx.sys.auth.dom.grant.service.RoleIndividualService;
import nts.uk.ctx.sys.auth.dom.otreferset.OvertimeReferSet;
import nts.uk.ctx.sys.auth.dom.otreferset.OvertimeReferSetRepository;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.ctx.sys.auth.dom.role.RoleType;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.ctx.sys.auth.dom.wplmanagementauthority.WorkPlaceAuthorityRepository;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.sys.auth.pub.wkpmanager.WorkPlaceAuthorityExport;
import nts.uk.ctx.sys.auth.pub.workplace.ReferenceableWorkplaceExport;
import nts.uk.ctx.sys.auth.pub.workplace.WorkplaceInfoExport;
import nts.uk.ctx.sys.auth.pub.workplace.WorkplaceListPub;
import nts.uk.ctx.sys.auth.pub.workplace.WorkplaceManagerExport;
import nts.uk.ctx.sys.auth.pubimp.wkpmanager.GetWorkPlaceRegerence;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkplacePubImp.
 * @author NWS_HoangDD
 */
@Stateless
public class WorkplaceListPubImp implements WorkplaceListPub{

	@Inject
	private AcquireUserIDFromEmpIDService acquireUserIDFromEmpIDService;
	
	@Inject
	private RoleIndividualService roleIndividualService;
	
	@Inject
	private RoleExportRepo roleExportRepo;
	
	@Inject
	private AcquireListWorkplaceByEmpIDService acquireListWorkplace;
	
	@Inject
	private OvertimeReferSetRepository overtimeReferSetRepository;
	
	@Inject
	private WorkplaceManagerRepository workplaceManagerRepository;
	
//	@Inject
//	private SyWorkplacePub syWorkplacePub;
	
	@Inject
	private WorkplacePub workplacePub;
	
	@Inject
	private WorkplaceManagerRepository workplaceManagerRepo;
	
	@Inject
	private WorkPlaceAuthorityRepository workPlaceAuthorityRepository;
	
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	@Inject
	private GetWorkPlaceFromReferenceRange getWorkPlaceFromReferenceRange;
	
	@Inject
	private GetWorkPlaceRegerence getWorkPlaceRegerence;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.auth.pub.workplace.WorkplacePub#getWorkplaceListId(nts.arc
	 * .time.GeneralDate, java.lang.String, boolean)
	 */
	@Override
	public WorkplaceInfoExport getWorkplaceListId(GeneralDate referenceDate, String employeeID, boolean referEmployee) {

		WorkplaceInfoExport workplaceInfoExport = new WorkplaceInfoExport();

		String companyId = AppContexts.user().companyId();
		
		// ??????ID???????????????ID??????????????? (L???y userID t??? employeeID)
		Optional<String> optUserID = acquireUserIDFromEmpIDService.getUserIDByEmpID(employeeID);
		if (optUserID.isPresent()) {
			// ?????????ID?????????????????????????????? (l???y role t??? userID)
			String roleID = roleIndividualService.getRoleFromUserId(optUserID.get(), RoleType.EMPLOYMENT.value, referenceDate);
			
			// ????????????????????????????????? (L???y ph???m vi tham chi???u employee)
			OptionalInt optEmpRange = roleExportRepo.findEmpRangeByRoleID(roleID);
			int empRange;
			if (optEmpRange.isPresent()) {
				// INPUT???????????????????????????????????????
				empRange = optEmpRange.getAsInt();
				workplaceInfoExport.setEmployeeRange(empRange);
				if (!referEmployee && empRange == EmployeeReferenceRange.ALL_EMPLOYEE.value) {
					// ???????????????????????????????????????????????????????????????????????????
					empRange = EmployeeReferenceRange.DEPARTMENT_AND_CHILD.value;
				}
			
				// ???????????????????????????????????????????????????????????????????????????
				OvertimeReferSet overtimeReferSet = overtimeReferSetRepository.getOvertimeReferSet(companyId).get();
				
				// ???????????????????????????????????????
				List<String> listWorkPlaceID = new ArrayList<>();
				if (overtimeReferSet.isReferWkpAdmin()) {
					// ???????????????????????????????????????????????????????????? (L???y list workplace c???a employee ch??? ?????nh)
					listWorkPlaceID = acquireListWorkplace.getListWorkPlaceID(employeeID, empRange, referenceDate);
				} else { // ??????????????????????????????????????????
					listWorkPlaceID = acquireListWorkplace.getListWorkPlaceIDNoWkpAdmin(employeeID, empRange,
							referenceDate);
				}
				workplaceInfoExport.setLstWorkPlaceID(listWorkPlaceID);
			}
		}
		return workplaceInfoExport;
	}

	@Override
	public List<String> getWorkplaceId(GeneralDate baseDate, String employeeId) {
		String companyID = AppContexts.user().companyId();
		List<String> subListWorkPlace = new ArrayList<>();
		// (L???y all domain ?????????????????????)
		List<WorkplaceManager> listWorkplaceManager = workplaceManagerRepository.findListWkpManagerByEmpIdAndBaseDate(employeeId, GeneralDate.today());
		for (WorkplaceManager workplaceManager : listWorkplaceManager) {
			if(!subListWorkPlace.contains(workplaceManager.getWorkplaceId())){
				subListWorkPlace.add(workplaceManager.getWorkplaceId());
				// [No.567]????????????????????????????????????
				subListWorkPlace.addAll(workplacePub.getAllChildrenOfWorkplaceId(companyID, GeneralDate.today(), workplaceManager.getWorkplaceId()));
			}
		}
		return subListWorkPlace.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<String> getListWorkPlaceIDNoWkpAdmin(String employeeID, int empRange, GeneralDate referenceDate) {
		
		List<String> result = acquireListWorkplace.getListWorkPlaceIDNoWkpAdmin(employeeID, empRange, referenceDate);
		return result;
	}

	@Override
	public List<WorkplaceManagerExport> findListWkpManagerByEmpIdAndBaseDate(String employeeId, GeneralDate baseDate) {
		
		List<WorkplaceManager> listDomain =  workplaceManagerRepo.findListWkpManagerByEmpIdAndBaseDate(employeeId, baseDate);
		if (listDomain.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<WorkplaceManagerExport> result = listDomain.stream().map(i -> {
			WorkplaceManagerExport export = new WorkplaceManagerExport(i.getWorkplaceManagerId(), i.getEmployeeId(), i.getWorkplaceId(), i.getHistoryPeriod());
			return export;
		}).collect(Collectors.toList());
		
		return result;
	}
	
	@Override
	public Optional<WorkPlaceAuthorityExport> getWorkPlaceAuthorityById(String companyId, String roleId,
			int functionNo) {
		return workPlaceAuthorityRepository.getWorkPlaceAuthorityById(companyId, roleId, functionNo)
			.map(x -> new WorkPlaceAuthorityExport(
					x.getRoleId(), 
					x.getCompanyId(), 
					x.getFunctionNo().v(), 
					x.isAvailability()));
	}

	@Override
	public ReferenceableWorkplaceExport getWorkPlace(String userID, String employeeID, GeneralDate date) {
		// return ????????????????????????????????????????????????#????????????(require,?????????ID,??????ID,?????????)
		
		return ReferenceableWorkplaceExport.fromDomain(this.getWorkPlaceRegerence.get(userID, employeeID, date));
	}

	@Override
	public ReferenceableWorkplaceExport getWorkPlaceByReference(String employeeID, GeneralDate date) {
		// $???????????? = ??????????????????.?????????

		Integer range = EmployeeReferenceRangeExport.ALL_EMPLOYEE.value;
		// return ???????????????????????????????????????????????????????????????#????????????(require,??????ID,?????????,$????????????)
		
		return ReferenceableWorkplaceExport.fromDomain(this.getWorkPlaceFromReferenceRange.get(new RequireImpl(workplaceAdapter), employeeID, date, range));
	}
	
	@AllArgsConstructor 
	private class RequireImpl implements GetWorkPlaceFromReferenceRange.Require {
		private WorkplaceAdapter workplaceAdapter;

		@Override
		public Map<String, String> getAWorkplace(String employeeID, GeneralDate date) {
			return this.workplaceAdapter.getAWorkplace(employeeID, date);
		}

		@Override
		public Map<String, String> getByListIds(List<String> workPlaceIds, GeneralDate baseDate) {
			return this.workplaceAdapter.getByListIds(workPlaceIds, baseDate);
		}
		
		
	}
}

