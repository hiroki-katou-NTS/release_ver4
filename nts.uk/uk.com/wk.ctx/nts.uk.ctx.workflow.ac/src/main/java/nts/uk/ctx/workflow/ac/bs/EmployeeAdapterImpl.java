/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.workflow.ac.bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.pub.employee.ConcurrentEmployeeExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoPub;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.sys.auth.pub.grant.RoleSetGrantedEmployeePub;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.ConcurrentEmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.EmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class EmployeeApproveAdapterImpl.
 */
@Stateless
public class EmployeeAdapterImpl implements EmployeeAdapter {

	/** The employee pub. */
	@Inject
	private SyEmployeePub employeePub;

	/** The workplace pub. */
	@Inject
	private SyWorkplacePub workplacePub;

	@Inject
	private PersonAdapter psInfor;
	@Inject
	private EmployeeInfoPub emInfor;
	@Inject
	private RoleSetGrantedEmployeePub roleSetPub;
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.workflow.dom.approvermanagement.workroot.employee.
	 * EmployeeApproveAdapter#findByWpkIds(java.lang.String, java.util.List,
	 * nts.arc.time.GeneralDate)
	 */
	public List<EmployeeImport> findByWpkIds(String companyId, List<String> workplaceIds,
			GeneralDate baseDate) {
		//
		DatePeriod period = new DatePeriod(baseDate,baseDate);
		List<String> lstEmpId = new ArrayList<>();
		List<String> empId = roleSetPub.findEmpGrantedInWkpVer2(workplaceIds, period);
		lstEmpId.addAll(empId);
//		for (String wkpId : workplaceIds) {
//			List<String> empId = roleSetPub.findEmpGrantedInWorkplace(wkpId, period);
//			lstEmpId.addAll(empId);
//		}
		List<EmployeeImport> lstEmpDto = lstEmpId.stream().map(x -> {
			PersonImport perInfo = psInfor.getPersonInfo(x);
			return new EmployeeImport(companyId,
					"",
					x,
					perInfo.getEmployeeCode(),
					perInfo.getEmployeeName(),
					"","","",null,null);
		    }).collect(Collectors.toList());
		return lstEmpDto;
	}

	@Override
	public List<String> findWpkIdsBySid(String companyId, String employeeId, GeneralDate date) {		
		return workplacePub.findWpkIdsBySid(companyId, employeeId, date);
	}

	@Override
	public List<EmployeeImport> getEmployeesAtWorkByBaseDate(String companyId, GeneralDate baseDate) {
		List<EmployeeImport> employeesInfor = emInfor.getEmployeesAtWorkByBaseDate(companyId, baseDate)
				.stream()
				.map(x -> new EmployeeImport(x.getCompanyId(), 
						x.getPersonId(), 
						x.getEmployeeId(), 
						x.getEmployeeCode(), 
						"",
						"", 
						"", 
						"", 
						baseDate,
						baseDate))
				.collect(Collectors.toList());
		return employeesInfor;
	}
	
	/**
	 * 
	 * @param sID
	 * @return
	 */
	@Override
	public String getEmployeeName(String sID) {
		return this.psInfor.getPersonInfo(sID).getEmployeeName();
	}

	@Override
	public List<ConcurrentEmployeeImport> getConcurrentEmployee(String companyId, String jobId, GeneralDate baseDate) {
		List<ConcurrentEmployeeExport> export = employeePub.getConcurrentEmployee(companyId, jobId, baseDate);
		if (CollectionUtil.isEmpty(export)) {
			Collections.emptyList();
		}
		
		return export.stream().map(x -> new ConcurrentEmployeeImport(
				x.getEmployeeId(), 
				x.getEmployeeCd(), 
				x.getPersonName(), 
				x.getJobId(), 
				x.getJobCls().value)).collect(Collectors.toList());
	}
	
	@Override
	public PersonImport getEmployeeInformation(String sID){
		return this.psInfor.getPersonInfo(sID);
	}

	@Override
	public boolean canApprovalOnBaseDate(String companyId , String employeeID , GeneralDate date){
		return this.roleSetPub.canApprovalOnBaseDate(companyId, employeeID, date);
	}
}
