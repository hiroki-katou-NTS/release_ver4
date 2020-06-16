package nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * process 
 * @author Trung Tran
 *
 */
@Stateless
public class BusinessTypeOfHistoryGeneralRepositoryImpl implements BusinessTypeOfHistoryGeneralRepository {

	@Inject
	private BusinessTypeEmpOfHistoryRepository historyRepos;

	@Override
	public void addBusinessTypeEmpOfHistory(BusinessTypeOfEmployeeHistory bEmployeeHistory, BusinessTypeOfEmployee bEmployee){
		List<DateHistoryItem> history = bEmployeeHistory.getHistory();
		DateHistoryItem item = history.get(history.size() - 1);
		historyRepos.addToMerge(bEmployeeHistory.getCompanyId(), bEmployeeHistory.getEmployeeId(), item.identifier(),
				item.start(), item.end(), bEmployee.getBusinessTypeCode().v());
		updateItemBefore(bEmployeeHistory, item);
	}

	@Override
	public void updateBusinessTypeEmpOfHistory(BusinessTypeOfEmployeeHistory bEmployeeHistory, DateHistoryItem item) {
		historyRepos.update(bEmployeeHistory.getCompanyId(), bEmployeeHistory.getEmployeeId(), item.identifier(),
				item.start(), item.end());
		updateItemBefore(bEmployeeHistory, item);
	}

	public void updateItemBefore(BusinessTypeOfEmployeeHistory bEmployeeHistory, DateHistoryItem item) {
		Optional<DateHistoryItem> optinal = bEmployeeHistory.immediatelyBefore(item);
		if (optinal.isPresent()) {
			DateHistoryItem itemToBeUpdate = optinal.get();
			historyRepos.update(bEmployeeHistory.getCompanyId(), bEmployeeHistory.getEmployeeId(),
					itemToBeUpdate.identifier(), itemToBeUpdate.start(), itemToBeUpdate.end());
		}
	}

	@Override
	public void deleteBusinessTypeEmpOfHistory(BusinessTypeOfEmployeeHistory bEmployeeHistory, DateHistoryItem item) {
		historyRepos.delete(item.identifier());
		if (bEmployeeHistory.getHistory().size() > 0) {
			List<DateHistoryItem> history = bEmployeeHistory.getHistory();
			DateHistoryItem itemToBeUpdate = history.get(history.size() - 1);
			historyRepos.update(bEmployeeHistory.getCompanyId(), bEmployeeHistory.getEmployeeId(),
					itemToBeUpdate.identifier(), itemToBeUpdate.start(), itemToBeUpdate.end());

		}
	}

}
