/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.workflow.ac.bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.pub.company.SyCompanyPub;
import nts.uk.ctx.bs.employee.pub.employee.ConcurrentEmployeeExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoPub;
import nts.uk.ctx.bs.employee.pub.employment.statusemployee.StatusOfEmploymentExport;
import nts.uk.ctx.bs.employee.pub.employment.statusemployee.StatusOfEmploymentPub;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.sys.auth.pub.grant.RoleSetGrantedEmployeePub;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.ConcurrentEmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.EmpInfoRQ18;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.EmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.ResultRequest596Import;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.StatusOfEmpImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.StatusOfEmployment;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.StatusOfEmploymentImport;
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
	
	@Inject
	private StatusOfEmploymentPub statusOfEmploymentPub;
	
	@Inject
	private ManagedParallelWithContext parallel;
	
	@Inject
	private SyCompanyPub syCompanyPub;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.workflow.dom.approvermanagement.workroot.employee.
	 * EmployeeApproveAdapter#findByWpkIds(java.lang.String, java.util.List,
	 * nts.arc.time.GeneralDate)
	 */
	public List<EmployeeImport> findByWpkIds(String companyId, List<String> workplaceIds,
			GeneralDate baseDate) {
		List<String> lstEmpId = new ArrayList<>();
		List<String> empId = roleSetPub.findEmpGrantedInWkpVer2(workplaceIds, baseDate);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.workflow.dom.approvermanagement.workroot.employee.
	 * EmployeeApproveAdapter#findByWpkIds(java.lang.String, java.util.List,
	 * nts.arc.time.GeneralDate)
	 */
	public List<EmployeeImport> findByWpkIdsWithParallel(String companyId, List<String> workplaceIds,
			GeneralDate baseDate) {
		List<String> lstEmpId = new ArrayList<>();
		List<String> empId = roleSetPub.findEmpGrantedInWkpVer2(workplaceIds, baseDate);
		lstEmpId.addAll(empId);

		List<EmployeeImport> lstEmpDto = Collections.synchronizedList(new ArrayList<>());
		this.parallel.forEach(lstEmpId, x -> {
			PersonImport perInfo = psInfor.getPersonInfo(x);
			EmployeeImport emplpyeeImport = new EmployeeImport(companyId,
					"",
					x,
					perInfo.getEmployeeCode(),
					perInfo.getEmployeeName(),
					"","","",null,null);
			lstEmpDto.add(emplpyeeImport);
		});
		
		return new ArrayList<>(lstEmpDto);
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

	@Override
	public boolean isEmployeeDelete(String sid) {
		return employeePub.isEmployeeDelete(sid);
	}

	@Override
	public StatusOfEmploymentImport getStatusOfEmployment(String employeeID, GeneralDate referenceDate) {
		StatusOfEmploymentExport statusOfEmploymentExport = statusOfEmploymentPub.getStatusOfEmployment(employeeID, referenceDate);
		if(statusOfEmploymentExport == null){
			return null;
		}
		return new StatusOfEmploymentImport(
				statusOfEmploymentExport.getEmployeeId(), 
				statusOfEmploymentExport.getRefereneDate(), 
				EnumAdaptor.valueOf(statusOfEmploymentExport.getStatusOfEmployment(), StatusOfEmployment.class), 
				statusOfEmploymentExport.getTempAbsenceFrNo());
	}

	@Override
	public Optional<EmpInfoRQ18> getEmpInfoByScd(String companyId, String employeeCode) {
		return emInfor.getEmployeeInfo(companyId, employeeCode)
				.map(c -> new EmpInfoRQ18(c.getCompanyId(), c.getEmployeeCode(), c.getEmployeeId(), c.getPersonId(), c.getPerName()));
	}

	@Override
	public List<StatusOfEmpImport> getListAffComHistByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {
		return syCompanyPub.GetListAffComHistByListSidAndPeriod(sids, datePeriod).stream()
				.map(x -> new StatusOfEmpImport(x.getEmployeeId(), x.getListPeriod()))
				.collect(Collectors.toList());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ResultRequest596Import> getEmpDeletedLstBySids(List<String> sids) {
		return employeePub.getEmpDeletedLstBySids(sids).stream()
				.map(x -> new ResultRequest596Import(x.getSid(), x.getEmployeeCode(), x.getEmployeeName())).collect(Collectors.toList());
	}
	
	
}
