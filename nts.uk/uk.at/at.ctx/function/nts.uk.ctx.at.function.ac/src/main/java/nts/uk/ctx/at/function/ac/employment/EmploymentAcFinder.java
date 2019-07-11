package nts.uk.ctx.at.function.ac.employment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.employment.EmploymentAdapter;
import nts.uk.ctx.at.function.dom.adapter.employment.EmploymentHistoryImported;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisOfEmployee;
import nts.uk.ctx.bs.employee.pub.employment.IEmploymentHistoryPub;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmploymentAcFinder implements EmploymentAdapter {

	@Inject
	private SyEmployeePub syEmployeePub;
	
	/** The emp pub. */
	@Inject
	private SyEmploymentPub empPub;

	@Inject
	private IEmploymentHistoryPub employmentHistoryPub;
	
	@Override
	public String getClosure(String employeeId, GeneralDate baseDate) {
		
		List<String> employmentCodes = syEmployeePub.getEmployeeCode(employeeId, baseDate);
		
		if (employmentCodes == null || employmentCodes.isEmpty())
			throw new BusinessException("Msg_1143"); 
		return employmentCodes.get(0);
	}

	@Override
	public Optional<EmploymentHistoryImported> getEmpHistBySid(String companyId, String employeeId,
			GeneralDate baseDate) {
			return this.empPub.findSEmpHistBySid(companyId, employeeId, baseDate)
					.map(f -> new EmploymentHistoryImported(f.getEmployeeId(), f.getEmploymentCode(), f.getPeriod()));
	}

	@Override
	public List<String> getEmploymentBySidsAndEmploymentCds(List<String> sids,
			List<String> employmentCodes, DatePeriod dateRange) {
		return employmentHistoryPub.getEmploymentBySidsAndEmploymentCds(sids, employmentCodes, dateRange)
				.keySet()
				.stream()
				.collect(Collectors.toList());
	}
	

}
