package nts.uk.ctx.pr.core.dom.itemmaster;

import java.util.List;
import java.util.Optional;

public interface ItemMasterV1Repository {

	/**
	 * Get all of company
	 * 
	 * @param companyCode
	 * @return
	 */
	List<ItemMasterV1> findAll(String companyCode);

	/**
	 * get All Item Master
	 * 
	 * @param companyCode
	 * @param categoryAtr
	 * @return list Item Master
	 */
	List<ItemMasterV1> findAllByCategory(String companyCode, int categoryAtr);

	/**
	 * get Item Master
	 * 
	 * @param companyCode
	 * @param categoryAtr
	 * @param itemCode
	 * @return list Item Master
	 */
	Optional<ItemMasterV1> getItemMaster(String companyCode, int categoryAtr, String itemCode);
	
	/**
	 * Find item master
	 * @param companyCode company code
	 * @param categoryAtr category attribute
	 * @param itemCode item code
	 * @return item master
	 */
	Optional<ItemMasterV1> find(String companyCode, int categoryAtr, String itemCode);
	
	/**
	 * Find all item master with avePayAtr
	 * @param companyCode company code
	 * @param avePayAtr ave payment attribute
	 * @return
	 */
	List<ItemMasterV1> findAll(String companyCode, int avePayAtr);
	
	/**
	 * Find all item by
	 * @param companyCode company code
	 * @param categoryAtr category attribute
	 * @param itemCode item code
	 * @param fixAtr fix attribute
	 * @return
	 */
	List<ItemMasterV1> findAll(String companyCode, int categoryAtr, String itemCode, int fixAtr);
	/**
	 * Add new item master
	 * @param domain
	 */
	void add(ItemMasterV1 domain);
	
	/**
	 * Update item master
	 * @param domain
	 */
	void update(ItemMasterV1 domain);
}
