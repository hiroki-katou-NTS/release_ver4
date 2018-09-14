/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ac.employment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.EmpCdNameImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.bs.employee.pub.employment.AffPeriodEmpCdHistExport;
import nts.uk.ctx.bs.employee.pub.employment.EmpCdNameExport;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ShareEmploymentAdapterImpl.
 */
@Stateless
public class ShareEmploymentAdapterImpl implements ShareEmploymentAdapter{
	
	/** The employment. */
	@Inject
	public SyEmploymentPub employment;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter#findAll(java.lang.String)
	 */
	@Override
	public List<EmpCdNameImport> findAll(String companyId) {
		List<EmpCdNameExport> data = employment.findAll(companyId);
		return data.stream().map(x -> {
			return new EmpCdNameImport(x.getCode(), x.getName());
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter
	 * #findByEmpCodes(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<BsEmploymentHistoryImport> findEmploymentHistory(String companyId, String employeeId, GeneralDate baseDate) {
		return employment.findSEmpHistBySid(companyId, employeeId, baseDate).map(empHist -> 
												new BsEmploymentHistoryImport(empHist.getEmployeeId(), empHist.getEmploymentCode(),
													empHist.getEmploymentName(), empHist.getPeriod()));
		
	}

	@Override
	public List<SharedSidPeriodDateEmploymentImport> getEmpHistBySidAndPeriod(List<String> sids, DatePeriod datePeriod) {
		List<SharedSidPeriodDateEmploymentImport> lstEmpHist = employment.getEmpHistBySidAndPeriod(sids, datePeriod)
				.stream()
				.map(x -> {
					List<AffPeriodEmpCodeImport> lstEmpCode = x.getAffPeriodEmpCodeExports().stream()
							.map(y -> {
								return new AffPeriodEmpCodeImport(y.getPeriod(), y.getEmploymentCode());
							}).collect(Collectors.toList());
					return new SharedSidPeriodDateEmploymentImport(x.getEmployeeId(), lstEmpCode);
				}).collect(Collectors.toList());
				
		return lstEmpHist;
	}
	
	
}
