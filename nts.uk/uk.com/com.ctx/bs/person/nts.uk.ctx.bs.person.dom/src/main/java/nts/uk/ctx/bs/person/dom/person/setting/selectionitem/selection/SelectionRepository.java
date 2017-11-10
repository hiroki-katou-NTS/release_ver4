package nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface SelectionRepository {

	void add(Selection selection);

	void update(Selection selection);

	void remove(String selectionId);
	
	//void remowHistory(String selectionId, String histId);

	List<Selection> getAllSelectByHistId(String histId);

	Optional<Selection> getSelectionByHistId(String histId);
	
	Optional<Selection> getSelectionBySelectionCd(String selectionCD);
	
	List<String> getAllHistId(String histId);

	List<Selection> getAllSelectionBySelectionCdAndHistId(String selectionCd, String histId);
	
	//Lanlt
	List<Selection> getAllSelectionByHistoryId(String selectionItemId, GeneralDate baseDate);
	//Lanlt

	//Tuan nv: 
	List<Selection> getAllSelectionBySelectionID(String selectionId);
	
}
