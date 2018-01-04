package nts.uk.ctx.pereg.dom.person.setting.selectionitem;

import java.util.List;
import java.util.Optional;

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

	Optional<PerInfoSelectionItem> getSelectionItemByName(String selectionItemName);

	/**
	 * getAllSelection
	 * 
	 * @return List<PerInfoSelectionItem>
	 */
	List<PerInfoSelectionItem> getAllSelection(int selectionItemClsAtr);

	List<PerInfoSelectionItem> getAllSelectionItemByContractCd(String contractCode);

	// Lanlt

}
