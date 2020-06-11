package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.shr.com.history.DateHistoryItem;

@Stateless
public class AffJobTitleHistoryService {
	@Inject
	private AffJobTitleHistoryRepository affJobTitleHistoryRepository;
	
	/**
	 * ドメインモデル「職務職位」を新規登録する
	 * 
	 * @param domain
	 */
	public void addToMerge(AffJobTitleHistory domain, AffJobTitleHistoryItem histItem){
		if (domain.getHistoryItems().isEmpty()) {
			return;
		}
		DateHistoryItem itemToBeAdded = domain.getHistoryItems().get(domain.getHistoryItems().size() - 1);
		affJobTitleHistoryRepository.addToMerge(domain.getCompanyId(), domain.getEmployeeId(), itemToBeAdded, histItem.getJobTitleId(), histItem.getNote().toString());
		// Update item before
		updateItemBefore(domain, itemToBeAdded);
	}

	/**
	 * 取得した「職務職位」を更新する
	 * 
	 * @param domain
	 */
	public void update(AffJobTitleHistory domain, DateHistoryItem item){
		affJobTitleHistoryRepository.update(item);
		// Update item before
		updateItemBefore(domain, item);
	}

	/**
	 * ドメインモデル「職務職位」を削除する
	 * 
	 * @param jobTitleMainId
	 */
	public void delete(AffJobTitleHistory domain, DateHistoryItem item){
		affJobTitleHistoryRepository.delete(item.identifier());
		if (domain.getHistoryItems().size() > 0) {
			DateHistoryItem itemToBeUpdated = domain.getHistoryItems().get(domain.getHistoryItems().size() - 1);
			affJobTitleHistoryRepository.update(itemToBeUpdated);
		}

	}
	/**
	 * Update item before
	 * @param domain
	 * @param item
	 */
	private void updateItemBefore(AffJobTitleHistory domain, DateHistoryItem item){
		Optional<DateHistoryItem> itemToBeUpdated = domain.immediatelyBefore(item);
		if (!itemToBeUpdated.isPresent()){
			return;
		}
		affJobTitleHistoryRepository.update(itemToBeUpdated.get());
	}

}
