package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.shr.com.history.DateHistoryItem;

@Stateless
public class AffJobTitleHistoryService {
	@Inject
	private AffJobTitleHistoryRepository_ver1 affJobTitleHistoryRepository_ver1;
	
	/**
	 * ドメインモデル「職務職位」を新規登録する
	 * 
	 * @param domain
	 */
	public void add(AffJobTitleHistory_ver1 domain){
		if (domain.getHistoryItems().isEmpty()) {
			return;
		}
		DateHistoryItem itemToBeAdded = domain.getHistoryItems().get(domain.getHistoryItems().size() - 1);
		affJobTitleHistoryRepository_ver1.add(domain.getEmployeeId(), itemToBeAdded);
		// Update item before
		updateItemBefore(domain, itemToBeAdded);
	}

	/**
	 * 取得した「職務職位」を更新する
	 * 
	 * @param domain
	 */
	public void update(AffJobTitleHistory_ver1 domain, DateHistoryItem item){
		affJobTitleHistoryRepository_ver1.update(item);
		// Update item before and after
		updateItemBefore(domain, item);
		updateItemAfter(domain, item);
	}

	/**
	 * ドメインモデル「職務職位」を削除する
	 * 
	 * @param jobTitleMainId
	 */
	public void delete(AffJobTitleHistory_ver1 domain, DateHistoryItem item){
		affJobTitleHistoryRepository_ver1.delete(item.identifier());
		if (domain.getHistoryItems().size() > 0) {
			DateHistoryItem itemToBeUpdated = domain.getHistoryItems().get(domain.getHistoryItems().size() - 1);
			affJobTitleHistoryRepository_ver1.update(itemToBeUpdated);
		}

	}
	/**
	 * Update item before
	 * @param domain
	 * @param item
	 */
	private void updateItemBefore(AffJobTitleHistory_ver1 domain, DateHistoryItem item){
		Optional<DateHistoryItem> itemToBeUpdated = domain.immediatelyBefore(item);
		if (!itemToBeUpdated.isPresent()){
			return;
		}
		affJobTitleHistoryRepository_ver1.update(itemToBeUpdated.get());
	}
	/**
	 * Update item after
	 * @param domain
	 * @param item
	 */
	private void updateItemAfter(AffJobTitleHistory_ver1 domain, DateHistoryItem item){
		Optional<DateHistoryItem> itemToBeUpdated = domain.immediatelyAfter(item);
		if (!itemToBeUpdated.isPresent()){
			return;
		}
		affJobTitleHistoryRepository_ver1.update(itemToBeUpdated.get());
	}
}
