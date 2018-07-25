/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ac.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.adapter.employee.AffComHistItemShareImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffCompanyHistSharedImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.MailAddress;
import nts.uk.ctx.at.shared.dom.adapter.employee.PersonEmpBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.SClsHistImport;
import nts.uk.ctx.bs.employee.pub.classification.SClsHistExport;
import nts.uk.ctx.bs.employee.pub.classification.SyClassificationPub;
import nts.uk.ctx.bs.employee.pub.company.AffCompanyHistExport;
import nts.uk.ctx.bs.employee.pub.company.SyCompanyPub;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeBasicInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.export.PersonEmpBasicInfoPub;
import nts.uk.ctx.bs.employee.pub.employee.export.dto.PersonEmpBasicInfoDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class EmpEmployeeAdapterImpl.
 */
@Stateless
public class EmpEmployeeAdapterImpl implements EmpEmployeeAdapter {

	/** The employee pub. */
	@Inject
	private SyEmployeePub employeePub;
	
	@Inject
	private PersonEmpBasicInfoPub personEmpBasicInfoPub;
	
	@Inject
	private SyCompanyPub syCompanyPub;
	@Inject
	private SyClassificationPub classPub;

	// @Inject
	// private PersonPub personPub;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter#findByEmpId(java.lang.String)
	 */
	@Override
	public EmployeeImport findByEmpId(String empId) {
		// Get Employee Basic Info
		EmployeeBasicInfoExport empExport = employeePub.findBySId(empId);
		// Check Null
		if (empExport != null) {
			// Map to EmployeeImport
			EmployeeImport empDto = EmployeeImport.builder()
					.employeeId(empExport.getEmployeeId())
					.employeeCode(empExport.getEmployeeCode())
					.employeeName(empExport.getPName())
					.employeeMailAddress(
							empExport.getPMailAddr() == null ? null : (new MailAddress(empExport.getPMailAddr().v())))
					.entryDate(empExport.getEntryDate())
					.retiredDate(empExport.getRetiredDate())
					.build();
			return empDto;
		}
		return null;
	}

	@Override
	public List<String> getListEmpByWkpAndEmpt(List<String> wkps, List<String> lstempts, DatePeriod dateperiod) {
		List<String> data = employeePub.getListEmpByWkpAndEmpt(wkps, lstempts, dateperiod);
	if (data.isEmpty()){
		return new ArrayList<>();
	}
		return data;
	}

	@Override
	public List<PersonEmpBasicInfoImport> getPerEmpBasicInfo(List<String> employeeIds) {
		List<PersonEmpBasicInfoDto> export = personEmpBasicInfoPub.getPerEmpBasicInfo(employeeIds);
		List<PersonEmpBasicInfoImport> data = new ArrayList<>();
			if(export.isEmpty()){
				return new ArrayList<>();
			}else{
			data = export.stream().map(c ->{
				return new PersonEmpBasicInfoImport(
						c.getPersonId(),
						c.getEmployeeId(),
						c.getBusinessName(),
						c.getGender(),
						c.getBirthday(),
						c.getEmployeeCode(),
						c.getJobEntryDate(),
						c.getRetirementDate());
						}).collect(Collectors.toList());
					}
		return data;
	}

	@Override
	public List<AffCompanyHistSharedImport> getAffCompanyHistByEmployee(List<String> sids, DatePeriod datePeriod) {
		List<AffCompanyHistSharedImport> importList = this.syCompanyPub.GetAffCompanyHistByEmployee(sids, datePeriod).stream()
				.map(x -> convert(x)).collect(Collectors.toList());
		return importList;
	}
	private AffCompanyHistSharedImport convert(AffCompanyHistExport dataExpprt) {
		List<AffComHistItemShareImport> subListImport = dataExpprt.getLstAffComHistItem().stream()
				.map(x -> new AffComHistItemShareImport(x.getHistoryId(), x.isDestinationData(), x.getDatePeriod()))
				.collect(Collectors.toList());
		return new AffCompanyHistSharedImport(dataExpprt.getEmployeeId(), subListImport);
	}

	@Override
	public EmployeeRecordImport findByAllInforEmpId(String empId) {
		// Get Employee Basic Info
		EmployeeBasicInfoExport empExport = employeePub.findBySId(empId);
		// Check Null
		if (empExport != null) {
			// Map to EmployeeImport
			EmployeeRecordImport empDto = new EmployeeRecordImport(empExport.getPId(),
					empExport.getEmployeeId(),
					empExport.getPName(),
					empExport.getGender(),
					empExport.getBirthDay(),
					empExport.getPMailAddr() == null ? null : (new MailAddress(empExport.getPMailAddr().v())),
					empExport.getEmployeeCode(),
					empExport.getEntryDate(),
					empExport.getRetiredDate(),
					empExport.getPMailAddr() == null ? null : (new MailAddress(empExport.getCompanyMailAddr().v())));
			return empDto;
		}
		return null;
	}

	@Override
	public List<SClsHistImport> lstClassByEmployeeId(String companyId, List<String> employeeIds,
			DatePeriod datePeriod) {
		List<SClsHistExport> lstExport = classPub.findSClsHistBySid(companyId, employeeIds, datePeriod);
		return lstExport.stream().map(x -> new SClsHistImport(x.getPeriod(), x.getEmployeeId(), x.getClassificationCode(), x.getClassificationName()))
				.collect(Collectors.toList());
	}
}
