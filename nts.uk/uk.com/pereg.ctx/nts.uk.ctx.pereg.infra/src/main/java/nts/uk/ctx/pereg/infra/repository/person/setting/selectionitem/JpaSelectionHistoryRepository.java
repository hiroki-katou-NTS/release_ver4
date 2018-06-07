package nts.uk.ctx.pereg.infra.repository.person.setting.selectionitem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.history.SelectionHistory;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.history.SelectionHistoryRepository;
import nts.uk.ctx.pereg.infra.entity.person.setting.selectionitem.PpemtHistorySelection;
import nts.uk.ctx.pereg.infra.entity.person.setting.selectionitem.PpemtHistorySelectionPK;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author danpv
 *
 */
@Stateless
public class JpaSelectionHistoryRepository extends JpaRepository implements SelectionHistoryRepository {

	private static final String SELECT_ALL = "SELECT si FROM PpemtHistorySelection si";

	private static final String SELECT_ALL_HISTORY = SELECT_ALL
			+ " WHERE si.selectionItemId = :selectionItemId AND si.companyId=:companyId"
			+ " ORDER BY si.startDate";
	
	private static final String SELECT_ALL_HISTORY_SELECTION = SELECT_ALL
			+ " WHERE si.selectionItemId = :selectionItemId";

	@Override
	public Optional<SelectionHistory> get(String selectionItemId, String companyId) {
		List<PpemtHistorySelection> entities = this.queryProxy().query(SELECT_ALL_HISTORY, PpemtHistorySelection.class)
				.setParameter("selectionItemId", selectionItemId).setParameter("companyId", companyId).getList();
		if (entities.isEmpty()) {
			return Optional.empty();
		}
		
		List<DateHistoryItem> dateHistoryItems = entities.stream()
				.map(ent -> new DateHistoryItem(ent.histidPK.histId, new DatePeriod(ent.startDate, ent.endDate)))
				.collect(Collectors.toList());
		
		SelectionHistory selectionHistory = SelectionHistory.createFullHistorySelection(companyId, selectionItemId, dateHistoryItems);
		return Optional.of(selectionHistory);

	}

	@Override
	public void add(SelectionHistory domain) {

		List<DateHistoryItem> dateHistoryItems = domain.getDateHistoryItems();
		if (dateHistoryItems.isEmpty()) {
			return;
		}
		String companyId = domain.getCompanyId();
		String selectionItemId = domain.getSelectionItemId();

		// Insert last element
		DateHistoryItem lastItem = dateHistoryItems.get(dateHistoryItems.size() - 1);

		addDateItem(companyId, selectionItemId, lastItem);

		// Update item before and after
		updateItemBefore(domain, lastItem);

	}
	
	@Override
	public void addAllDomain(SelectionHistory domain) {
		List<DateHistoryItem> dateHistoryItems = domain.getDateHistoryItems();
		if (dateHistoryItems.isEmpty()) {
			return;
		}
		
		String companyId = domain.getCompanyId();
		String selectionItemId = domain.getSelectionItemId();
		
		domain.getDateHistoryItems().forEach(dateHistItem -> {
			addDateItem(companyId, selectionItemId, dateHistItem);
		});
		
	}
	
	@Override
	public void update(SelectionHistory domain, DateHistoryItem itemToBeUpdated) {
		
		updateDateItem(itemToBeUpdated);
		
		updateItemBefore(domain, itemToBeUpdated);
	}
	
	@Override
	public void delete(SelectionHistory domain, DateHistoryItem itemToBeDeleted){
		
		this.commandProxy().remove(PpemtHistorySelection.class, new PpemtHistorySelectionPK(itemToBeDeleted.identifier()));
		
		List<DateHistoryItem> dateHistoryItems = domain.getDateHistoryItems();
		
		// Update last item
		if (dateHistoryItems.size() > 0){
			
			DateHistoryItem itemToBeUpdated = dateHistoryItems.get(dateHistoryItems.size() - 1);
			
			updateDateItem(itemToBeUpdated);
		}
	}
	
	public void removeAllHistoryIds(List<String> historyIds) {
		List<PpemtHistorySelectionPK> keys = historyIds.stream().map(x -> new PpemtHistorySelectionPK(x))
				.collect(Collectors.toList());
		this.commandProxy().removeAll(PpemtHistorySelection.class, keys);
	}

	private void addDateItem(String companyId, String selectionItemId, DateHistoryItem dateItem) {
		PpemtHistorySelectionPK key = new PpemtHistorySelectionPK(dateItem.identifier());

		PpemtHistorySelection entity = new PpemtHistorySelection(key, selectionItemId, companyId, dateItem.start(),
				dateItem.end());
		this.commandProxy().insert(entity);
	}

	private void updateDateItem(DateHistoryItem item) {
		Optional<PpemtHistorySelection> historyItemOpt = this.queryProxy()
				.find(new PpemtHistorySelectionPK(item.identifier()), PpemtHistorySelection.class);
		if (!historyItemOpt.isPresent()) {
			throw new RuntimeException("Invalid KmnmtAffClassHistory");
		}

		PpemtHistorySelection entity = historyItemOpt.get();
		entity.startDate = item.start();
		entity.endDate = item.end();
		this.commandProxy().update(entity);

	}
	
	private void updateItemBefore(SelectionHistory domain, DateHistoryItem item) {
		// Update item before
		Optional<DateHistoryItem> beforeItemOpt = domain.immediatelyBefore(item);

		if (!beforeItemOpt.isPresent()) {
			return;
		}

		updateDateItem(beforeItemOpt.get());

	}

	@Override
	public void removeAllOfSelectionItem(String selectionItemId) {
		List<PpemtHistorySelection> historyList = this.queryProxy()
				.query(SELECT_ALL_HISTORY_SELECTION, PpemtHistorySelection.class)
				.setParameter("selectionItemId", selectionItemId).getList();
		this.commandProxy().removeAll(historyList);
	}

}
