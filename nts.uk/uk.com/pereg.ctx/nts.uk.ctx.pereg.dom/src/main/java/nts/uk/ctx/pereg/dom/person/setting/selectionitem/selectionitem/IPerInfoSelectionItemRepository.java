package nts.uk.ctx.pereg.dom.person.setting.selectionitem.selectionitem;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pereg.dom.person.setting.selectionitem.export.PersonSelectionItemExportData;

/**
 * 
 * @author tuannv
 *
 */
public interface IPerInfoSelectionItemRepository {

	void add(PerInfoSelectionItem perInfoSelectionItem);

	void update(PerInfoSelectionItem perInfoSelectionItem);

	void remove(String selectionItemId);

	List<PerInfoSelectionItem> getAllSelectionItemByContractCdAndCID(String contractCd, String companyId);

	Optional<PerInfoSelectionItem> getSelectionItemBySelectionItemId(String selectionItemId);

	Optional<PerInfoSelectionItem> getSelectionItemByName(String contractCode, String selectionItemName,
			String selectionItemId);

	/**
	 * getAllSelection
	 * 
	 * @return List<PerInfoSelectionItem>
	 */
	List<PerInfoSelectionItem> getAllSelection(String contractCode);

	List<PerInfoSelectionItem> getAllSelectionItemByContractCd(String contractCode);

	Optional<PerInfoSelectionItem> getSelectionItemByHistId(String histId);

	// Lanlt

	List<PersonSelectionItemExportData> findAllSelection(String contractCode, String langId);
	
	List<SelectionItemReportData> findByContractCd(String contractCd);

}
