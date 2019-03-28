package nts.uk.ctx.at.record.ac.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHisOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistImport;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisExport;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisOfEmployee;
import nts.uk.ctx.bs.employee.pub.employment.IEmploymentHistoryPub;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * アダプタ実装：所属雇用履歴を取得する
 * @author shuichu_ishida
 */
@Stateless
public class EmploymentHistAdapterImpl implements EmploymentHistAdapter {

	@Inject
	private IEmploymentHistoryPub employmentHistoryPub;
	
	@Inject
	private SyEmploymentPub syEmploymentPub;
	
	@Override
	public List<EmploymentHistImport> findByEmployeeIdOrderByStartDate(String employeeId) {

		List<EmploymentHisOfEmployee> empHists = this.employmentHistoryPub.getEmploymentHisBySid(employeeId);

		return empHists.stream().map(c -> new EmploymentHistImport(
				c.getSId(), c.getEmploymentCD(), c.getStartDate(), c.getEndDate())).collect(Collectors.toList());
	}

	@Override
	public List<EmploymentHistImport> findBySidDatePeriod(List<String> employeeIds, DatePeriod period) {
		List<EmploymentHisExport> empHistPub = syEmploymentPub.findByListSidAndPeriod(employeeIds, period);
		List<EmploymentHistImport> result = new ArrayList<>();
		empHistPub.stream().forEach(x ->{
			result.addAll(x.getLstEmpCodeandPeriod().stream().map(c -> new EmploymentHistImport(x.getEmployeeId(), c.getEmploymentCode(), c.getDatePeriod())).collect(Collectors.toList()));
		});
		return result;
	}

	@Override
	public Map<String, List<EmploymentHisOfEmployeeImport>> getEmploymentBySidsAndEmploymentCds(List<String> sids,
			List<String> employmentCodes, DatePeriod dateRange) {
		Map<String, List<EmploymentHisOfEmployee>> mapResult = employmentHistoryPub.getEmploymentBySidsAndEmploymentCds(sids, employmentCodes, dateRange);
		return mapResult.entrySet().stream()
				.collect(
						Collectors.toMap(x -> x.getKey(),
								x -> x.getValue().stream().map(y -> new EmploymentHisOfEmployeeImport(y.getSId(),
										y.getStartDate(), y.getEndDate(), y.getEmploymentCD()))
										.collect(Collectors.toList())));
	}
}
