package nts.uk.ctx.bs.employee.dom.workplace.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.shr.com.history.DateHistoryItem;

@Stateless
public class AffWorkplaceHistoryService {

	@Inject
	private AffWorkplaceHistoryRepository affWorkplaceHistoryRepository;
	
	/**
	 * ドメインモデル「所属職場」を新規登録する
	 * @param domain
	 * @param histItem 
	 */
	public void addToMerge(AffWorkplaceHistory domain, AffWorkplaceHistoryItem histItem){
		if (domain.getHistoryItems().isEmpty()){
			return;
		}
		// Insert last element
		DateHistoryItem lastItem = domain.getHistoryItems().get(domain.getHistoryItems().size()-1);
		affWorkplaceHistoryRepository.addToMerge(domain.getCompanyId(), domain.getEmployeeId(), lastItem, histItem.getWorkplaceId(), histItem.getNormalWorkplaceId());
		
		// Update item before
		updateItemBefore(domain,lastItem);
	}
	/**
	 * ドメインモデル「所属職場」を削除する
	 * @param domain
	 */
	public void delete(AffWorkplaceHistory domain, DateHistoryItem item){
		affWorkplaceHistoryRepository.delete(item.identifier());
		// Update last item
		if (domain.getHistoryItems().size() >0){
			DateHistoryItem lastItem = domain.getHistoryItems().get(domain.getHistoryItems().size()-1);
			affWorkplaceHistoryRepository.update(lastItem);
		}
	}
	
	/**
	 * ドメインモデル「所属職場」を取得する
	 * @param domain
	 */
	public void update(AffWorkplaceHistory domain, DateHistoryItem item){
		affWorkplaceHistoryRepository.update(item);
		// Update item before
		updateItemBefore(domain,item);
	}
	
	/**
	 * Update item before
	 * @param domain
	 * @param item
	 */
	private void updateItemBefore(AffWorkplaceHistory domain, DateHistoryItem item){
		Optional<DateHistoryItem> itemToBeUpdated = domain.immediatelyBefore(item);
		if (!itemToBeUpdated.isPresent()){
			return;
		}
		affWorkplaceHistoryRepository.update(itemToBeUpdated.get());
	}

}
