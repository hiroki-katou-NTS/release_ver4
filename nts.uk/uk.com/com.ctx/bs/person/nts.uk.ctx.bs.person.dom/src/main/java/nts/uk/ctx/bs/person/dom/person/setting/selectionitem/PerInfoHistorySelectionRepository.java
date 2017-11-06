package nts.uk.ctx.bs.person.dom.person.setting.selectionitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface PerInfoHistorySelectionRepository {
	void add(PerInfoHistorySelection perInfoHistorySelection);

	void remove(String histId);

	void update(PerInfoHistorySelection perInfoHistorySelection);

	Optional<PerInfoHistorySelection> getAllHistoryByHistId(String histId);

	List<PerInfoHistorySelection> historySelection(String selectionItemId);

	List<PerInfoHistorySelection> historyStartDateSelection(GeneralDate startDate);

	List<PerInfoHistorySelection> getAllPerInfoHistorySelection(String selectionItemId, String companyId);
	/**
	 * get History Selection Item By Date
	 * @param baseDate
	 * @param lstSelItemId
	 * @return
	 */
	List<PerInfoHistorySelection> getHistorySelItemByDate(GeneralDate baseDate, List<String> lstSelItemId);
	
	/**
	 *get all histId 
	 */
	List<String> getAllHistId(String histId);
}
