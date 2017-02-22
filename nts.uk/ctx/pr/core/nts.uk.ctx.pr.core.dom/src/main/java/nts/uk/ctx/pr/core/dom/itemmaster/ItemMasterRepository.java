package nts.uk.ctx.pr.core.dom.itemmaster;

import java.util.List;
import java.util.Optional;

public interface ItemMasterRepository {
	/**
	 * Find item master
	 * @param companyCode company code
	 * @param categoryAtr category attribute
	 * @param itemCode item code
	 * @return item master
	 */
	Optional<ItemMaster> find(String companyCode, int categoryAtr, String itemCode);
	
	/**
	 * Find all item by
	 * @param companyCode company code
	 * @param avePayAtr ave payment attribute
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, int avePayAtr);
	
	/**
	 * Find all item by
	 * 
	 * @param companyCode company code
	 * @param categoryAtr category attribute
	 * @return list Item Master
	 */
	List<ItemMaster> findAllByCategory(String companyCode, int categoryAtr);
	
	/**
	 * Find all item by
	 * @param companyCode company code
	 * @param categoryAtr category attribute
	 * @param itemCode item code
	 * @param fixAtr fix attribute
	 * @return
	 */
	List<ItemMaster> findAll(String companyCode, int categoryAtr, String itemCode, int fixAtr);
}
