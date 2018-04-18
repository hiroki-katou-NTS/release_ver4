package nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface SelectionRepository {

	void add(Selection selection);

	void update(Selection selection);

	void remove(String selectionId);

	List<Selection> getAllSelectByHistId(String histId);

	Optional<Selection> getSelectionByHistId(String histId);

	Optional<Selection> getSelectionBySelectionCd(String selectionCD);

	List<String> getAllHistId(String histId);

	List<Selection> getAllSelectionBySelectionCdAndHistId(String selectionCd, String histId);

	List<Selection> getAllSelectionByCompanyId(String companyId, String selectionItemId, GeneralDate baseDate);
	
	List<Selection> getAllSelectionByHistoryId(String selectionItemId, GeneralDate baseDate);

	// Tuan nv:
	List<Selection> getAllSelectionBySelectionID(String selectionId);

}
