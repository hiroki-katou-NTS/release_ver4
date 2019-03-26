package nts.uk.ctx.bs.employee.pubimp.employment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistory;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryOfEmployee;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentCodeAndPeriod;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisExport;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisOfEmployee;
import nts.uk.ctx.bs.employee.pub.employment.IEmploymentHistoryPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmploymentHistoryPubImp implements IEmploymentHistoryPub{
	
	/** The employment history item repository. */
	@Inject
	private EmploymentHistoryItemRepository employmentHistoryItemRepository;
	
	/** The employment history repository. */
	@Inject
	private EmploymentHistoryRepository employmentHistoryRepo;
	
	@Override
	public List<EmploymentHisOfEmployee> getEmploymentHisBySid(String sID) {
		// アルゴリズム「社員の雇用履歴を全て取得する」を実行する
		List<EmploymentHistoryOfEmployee> listEmpOfEmployee = employmentHistoryItemRepository.getEmploymentBySID(sID);
		if (!listEmpOfEmployee.isEmpty()){
			return listEmpOfEmployee.stream().map(temp -> {
				return new EmploymentHisOfEmployee(temp.getSId(),temp.getStartDate(),temp.getEndDate(),temp.getEmploymentCD());
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<EmploymentHisExport> getEmploymentHistoryItem(String cid, GeneralDate baseDate) {
		List<EmploymentHistoryItem> historyItems = this.employmentHistoryItemRepository.getEmploymentHistoryItem(cid, baseDate);
		List<EmploymentHisExport> output = historyItems.stream().map(c -> {
			return new EmploymentHisExport(c.getEmployeeId(),
					Arrays.asList(new EmploymentCodeAndPeriod(c.getHistoryId(), null, c.getEmploymentCode().v())));
		}).collect(Collectors.toList());
		return output;
	}

	@Override
	public Optional<EmploymentHisExport> getEmploymentHistory(String historyId, String employmentCode) {
		Optional<EmploymentHistory> optionaldata = this.employmentHistoryRepo.getEmploymentHistory(historyId, employmentCode);
		if(optionaldata.isPresent()) {
			EmploymentHistory history = optionaldata.get();
			List<EmploymentCodeAndPeriod> lst = history.getHistoryItems().stream()
					.map(c -> new EmploymentCodeAndPeriod(c.identifier(), new DatePeriod(c.start(), c.end()),
							employmentCode))
					.collect(Collectors.toList());
			return Optional.of(new EmploymentHisExport(history.getEmployeeId(),lst));
		}
		return Optional.empty();
	}

	@Override
	public Map<String, List<EmploymentHisOfEmployee>> getEmploymentBySidsAndEmploymentCds(List<String> sids,
			List<String> employmentCodes, DatePeriod dateRange) {
		List<EmploymentHistoryOfEmployee>  listEmpOfEmployee = employmentHistoryItemRepository.getEmploymentBySID(sids, employmentCodes, dateRange);
		
		if (!listEmpOfEmployee.isEmpty()){
			List<EmploymentHisOfEmployee>  result = listEmpOfEmployee.stream().map(temp -> {
				return new EmploymentHisOfEmployee(temp.getSId(),temp.getStartDate(),temp.getEndDate(),temp.getEmploymentCD());
			}).collect(Collectors.toList());
			return  result.parallelStream().collect(Collectors.groupingBy(c -> c.getSId()));
		}
		
		return new HashMap<>();
	}
	
	
}
