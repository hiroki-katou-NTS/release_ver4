package nts.uk.ctx.bs.employee.dom.employment.history;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.shr.com.history.DateHistoryItem;

@Stateless
public class EmploymentHistoryService {
	@Inject
	private EmploymentHistoryRepository employmentHistoryRepository;
	
	/**
	 * Add employment history
	 * @param domain
	 * @param salarySegment 
	 */
	public void addToMerge(EmploymentHistory domain, String employmentCode, Integer salarySegment){
		if (domain.getHistoryItems().isEmpty()){
			return;
		}
		// Insert last element
		DateHistoryItem lastItem = domain.getHistoryItems().get(domain.getHistoryItems().size()-1);
		employmentHistoryRepository.addToMerge(domain.getEmployeeId(), lastItem, employmentCode, salarySegment);
		// Update item before and after
		updateItemBefore(domain,lastItem);
	}
	
	/**
	 * Update employment history
	 * @param domain
	 * @param itemToBeUpdated
	 */
	public void update(EmploymentHistory domain, DateHistoryItem itemToBeUpdated){
		employmentHistoryRepository.update(itemToBeUpdated);
		// Update item before
		updateItemBefore(domain,itemToBeUpdated);
	}
	
	/**
	 * Delete employment history
	 * @param domain
	 * @param itemToBeDeleted
	 */
	public void delete(EmploymentHistory domain, DateHistoryItem itemToBeDeleted){
		employmentHistoryRepository.delete(itemToBeDeleted.identifier());
		
		// Update last item
		if (domain.getHistoryItems().size() >0){
			DateHistoryItem itemToBeUpdated = domain.getHistoryItems().get(domain.getHistoryItems().size()-1);
			employmentHistoryRepository.update(itemToBeUpdated);
		}
	}
	
	/**
	 * Update item before
	 * @param domain
	 * @param item
	 */
	private void updateItemBefore(EmploymentHistory domain, DateHistoryItem item){
		Optional<DateHistoryItem> itemToBeUpdated = domain.immediatelyBefore(item);
		if (!itemToBeUpdated.isPresent()){
			return;
		}
		employmentHistoryRepository.update(itemToBeUpdated.get());
	}

}
