package nts.uk.screen.at.app.dailyperformance.correction.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHisOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 * 社員の締めをチェックする
 */
@Stateless
public class CheckClosingEmployee {
	
	@Inject
	private ClosureEmploymentRepository closureEmpRepo;
	
	@Inject
	private EmploymentHistAdapter employmentHistAdapter;
	
	public Map<String, List<EmploymentHisOfEmployeeImport>> checkClosingEmployee(String companyId, List<String> employeeIds, DatePeriod period, Integer closureId) {
		//締めに紐付く雇用コード一覧を取得
		List<ClosureEmployment> lstClosureEmp = closureEmpRepo.findByClosureId(companyId, closureId);
		if(lstClosureEmp.isEmpty()) return new HashMap<>();
		Map<String, List<EmploymentHisOfEmployeeImport>> lstMapResult = employmentHistAdapter.getEmploymentBySidsAndEmploymentCds(employeeIds,
				lstClosureEmp.stream().map(x -> x.getEmploymentCD()).collect(Collectors.toList()), period);
		return lstMapResult;
	}

}
