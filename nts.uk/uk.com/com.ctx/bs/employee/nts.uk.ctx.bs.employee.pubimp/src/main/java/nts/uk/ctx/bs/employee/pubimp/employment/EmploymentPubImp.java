/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.employment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employment.Employment;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentInfo;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.pub.employment.EmpCdNameExport;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.ShEmploymentExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Class EmploymentPubImp.
 */
@Stateless
public class EmploymentPubImp implements SyEmploymentPub {

	/** The Constant FIRST_ITEM_INDEX. */
	private static final int FIRST_ITEM_INDEX = 0;

	/** The employment history repository. */
	@Inject
	private EmploymentHistoryRepository employmentHistoryRepository;

	/** The employment history item repository. */
	@Inject
	private EmploymentHistoryItemRepository employmentHistoryItemRepository;

	/** The employment repository. */
	@Inject
	private EmploymentRepository employmentRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findAll(java.lang.
	 * String)
	 */
	@Override
	public List<EmpCdNameExport> findAll(String companyId) {
		return employmentRepository.findAll(companyId).stream()
				.map(item -> EmpCdNameExport.builder().code(item.getEmploymentCode().v())
						.name(item.getEmploymentName().v()).build())
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findSEmpHistBySid(
	 * java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<SEmpHistExport> findSEmpHistBySid(String companyId, String employeeId,
			GeneralDate baseDate) {

		// Query
		Optional<EmploymentInfo> employmentInfo = employmentHistoryItemRepository
				.getDetailEmploymentHistoryItem(companyId, employeeId, baseDate);

		Optional<DateHistoryItem> optHistoryItem = employmentHistoryRepository
				.getByEmployeeIdAndStandardDate(employeeId, baseDate);

		// Check exist
		if (!employmentInfo.isPresent() || !optHistoryItem.isPresent()) {
			return Optional.empty();
		}

		EmploymentInfo employment = employmentInfo.get();

		// Return
		return Optional.of(SEmpHistExport.builder().employeeId(employeeId)
				.employmentCode(employment.getEmploymentCode())
				.employmentName(employment.getEmploymentName()).period(optHistoryItem.get().span())
				.build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findByEmpCodes(java
	 * .lang.String, java.util.List)
	 */
	@Override
	public List<ShEmploymentExport> findByEmpCodes(String companyId, List<String> empCodes) {
		List<Employment> empList = this.employmentRepository.findByEmpCodes(companyId, empCodes);
		return empList.stream().map(emp -> {
			ShEmploymentExport empExport = new ShEmploymentExport();
			empExport.setCompanyId(emp.getCompanyId().v());
			empExport.setEmploymentCode(emp.getEmploymentCode().v());
			empExport.setEmploymentName(emp.getEmploymentName().v());
			empExport.setEmpExternalCode(emp.getEmpExternalCode().v());
			empExport.setMemo(emp.getMemo().v());
			return empExport;
		}).collect(Collectors.toList());
	}

}
