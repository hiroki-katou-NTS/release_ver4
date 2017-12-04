package nts.uk.ctx.bs.person.dom.person.info.item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.bs.person.dom.person.info.order.PerInfoItemDefOrder;

public interface PerInfoItemDefRepositoty {

	List<PersonInfoItemDefinition> getAllPerInfoItemDefByCategoryId(String perInfoCategoryId, String contractCd);

	Optional<PersonInfoItemDefinition> getPerInfoItemDefById(String perInfoItemDefId, String contractCd);

	List<PersonInfoItemDefinition> getPerInfoItemDefByListId(List<String> listItemDefId, String contractCd);

	List<String> getPerInfoItemsName(String perInfoCtgId, String contractCd);

	String addPerInfoItemDefRoot(PersonInfoItemDefinition perInfoItemDef, String contractCd, String ctgCode);

	void updatePerInfoItemDefRoot(PersonInfoItemDefinition perInfoItemDef, String contractCd);

	String getPerInfoItemCodeLastest(String contractCd, String categoryCd);

	List<String> addPerInfoItemDefByCtgIdList(PersonInfoItemDefinition perInfoItemDef, List<String> perInfoCtgId);

	List<PerInfoItemDefOrder> getPerInfoItemDefOrdersByCtgId(String perInfoCtgId);

	int getItemDispOrderBy(String perInfoCtgId, String perInfoItemDefId);

	List<String> getRequiredIds(String contractCd, String companyId);

	void removePerInfoItemDefRoot(List<String> perInfoCtgIds, String categoryCd, String contractCd, String itemCode);

	boolean checkItemNameIsUnique(String perInfoCtgId, String newItemName, String perInfoItemDefId);

	// Sonnlb Code

	List<PersonInfoItemDefinition> getAllPerInfoItemDefByCategoryIdWithoutAbolition(String perInfoCtgId,
			String contractCd);

	void updatePerInfoItemDef(PersonInfoItemDefinition perInfoItemDef);

	String getItemDefaultName(String categoryCd, String itemCode);

	Optional<PerInfoItemDefOrder> getPerInfoItemDefOrdersByItemId(String perInfoItemDefId);

	void UpdateOrderItem(PerInfoItemDefOrder itemOrder);

	List<PersonInfoItemDefinition> getAllPerInfoItemDefByCategoryIdWithoutSetItem(String perInfoCtgId,
			String contractCd);

	List<PersonInfoItemDefinition> getAllItemFromCodeList(String companyId, String categoryCd,
			List<String> itemCodeList);

	// Sonnlb Code

	// vinhpx start
	int countPerInfoItemDefInCategory(String perInfoCategoryId, String companyId);

	List<PersonInfoItemDefinition> getPerInfoItemByCtgIdAndOrder(String perInfoCategoryId, String companyId,
			String contractCd);


	// vinhpx end
	
	/**
	 * getNotFixedPerInfoItemDefByCategoryId 
	 * @param perInfoCategoryId
	 * @param contractCd
	 * @return
	 */
	List<PersonInfoItemDefinition> getNotFixedPerInfoItemDefByCategoryId(String perInfoCategoryId, String contractCd);
	
	/**
	 * checkExistedSelectionItemId
	 * @param ctgId
	 * @param itemId
	 * @return
	 */
	boolean checkExistedSelectionItemId(String ctgId, String itemId);

	List<PersonInfoItemDefinition> getPerInfoItemByCtgId(String personInfoCategoryId, String companyId,
			String contractCode);
	
}
