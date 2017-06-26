package nts.uk.ctx.pr.core.dom.itemmaster;

import java.util.List;
import java.util.Optional;

/**
 * @author sonnlb
 *
 */
/**
 * @author sonnlb
 *
 */
/**
 * @author sonnlb
 *
 */
/**
 * @author sonnlb
 *
 */
public interface ItemMasterRepository {
	/**
	 * Find item master
	 * 
	 * @param companyCode
	 *            company code
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCode
	 *            item code
	 * @return item master
	 */
	Optional<ItemMaster> find(String companyCode, int categoryAtr, String itemCode);

	/**
	 * Find all item master
	 * 
	 * @param companyCode
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode);

	/**
	 * Find all item by
	 * 
	 * @param companyCode
	 *            company code
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCode
	 *            item code list
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, int categoryAtr, List<String> itemCode);

	/**
	 * Find all item by
	 * 
	 * @param companyCode
	 *            company code
	 * @param itemCode
	 *            item code list
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, List<String> itemCode);

	/**
	 * Find all item by
	 * 
	 * @param companyCode
	 *            company code
	 * @param avePayAtr
	 *            ave payment attribute
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, int avePayAtr);

	/**
	 * Find all item by
	 * 
	 * @param companyCode
	 *            company code
	 * @param categoryAtr
	 *            category attribute
	 * @return list Item Master
	 */
	List<ItemMaster> findAllByCategory(String companyCode, int categoryAtr);

	/**
	 * Find all item by
	 * 
	 * @param companyCode
	 *            company code
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCode
	 *            item code list
	 * @param fixAtr
	 *            fix attribute
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, int categoryAtr, List<String> itemCode, int fixAtr);

	/**
	 * @param companyCode
	 * @return
	 */

	/**
	 * @param companyCode
	 *            company code
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCode
	 *            item code
	 */
	void remove(String companyCode, int categoryAtr, String itemCode);

	/**
	 * @param itemMaster:
	 *            Object need Add New.
	 */

	void add(ItemMaster itemMaster);

	/**
	 * @param itemMaster:
	 *            Object need update.
	 */
	void update(ItemMaster itemMaster);

	/**
	 * @param companyCode
	 *            company code
	 * @param ctgAtr
	 *            category attribute
	 * @param dispSet
	 *            display set
	 * @return ItemMaster List
	 */
	List<ItemMaster> findAllByDispSetAndCtgAtr(String companyCode, int ctgAtr, int dispSet);

	/**
	 * @param companyCode
	 *            company code
	 * @param dispSet
	 *            display set
	 * @return ItemMaster List
	 */
	List<ItemMaster> findAllByDispSet(String companyCode, int dispSet);

}
