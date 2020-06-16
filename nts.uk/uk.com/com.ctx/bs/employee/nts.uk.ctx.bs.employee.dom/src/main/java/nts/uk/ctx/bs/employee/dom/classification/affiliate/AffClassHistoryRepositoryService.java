package nts.uk.ctx.bs.employee.dom.classification.affiliate;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistoryRepository;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * 
 * @author hop.nt
 *
 */
@Stateless
public class AffClassHistoryRepositoryService {
	
	@Inject
	private AffClassHistoryRepository affClassHistoryRepo;
	/**
	 * add domain history
	 * add last item and update before items
	 * @param history
	 * @param classificationCode 
	 */
	public void addToMerge(AffClassHistory history, String classificationCode){
		if (history.getPeriods().isEmpty()) {
			return;
		}
		List<DateHistoryItem> periods = history.getPeriods();
		DateHistoryItem historyItem = periods.get(periods.size() - 1);
		affClassHistoryRepo.addToMerge(history.getCompanyId(), history.getEmployeeId(), historyItem, classificationCode);

		updateItemBefore(history, historyItem);
	}
	
	/**
	 * update domain history
	 * update item and nearly item
	 * @param history
	 */
	public void update(AffClassHistory history, DateHistoryItem item){
		affClassHistoryRepo.update(item);
		// Update item before and after
		updateItemBefore(history, item);
	}
	
	/**
	 * delete domain history
	 * delete last item and update nearly item
	 * @param history
	 * @param item
	 */
	public void delete(AffClassHistory history, DateHistoryItem item){
		affClassHistoryRepo.delete(item.identifier());
		
		if (!history.getPeriods().isEmpty()) {
			DateHistoryItem lastItem = history.getPeriods().get(history.getPeriods().size() - 1);
			affClassHistoryRepo.update(lastItem);
		}
	}
	
	private void updateItemBefore(AffClassHistory history, DateHistoryItem item) {
		// Update item before
		Optional<DateHistoryItem> beforeItemOpt = history.immediatelyBefore(item);

		if (!beforeItemOpt.isPresent()) {
			return;
		}
		affClassHistoryRepo.update(beforeItemOpt.get());

	}

}
