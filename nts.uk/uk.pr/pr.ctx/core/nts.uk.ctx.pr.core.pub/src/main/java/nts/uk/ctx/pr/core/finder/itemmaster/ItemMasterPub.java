package nts.uk.ctx.pr.core.finder.itemmaster;

import java.util.List;

public interface ItemMasterPub {
	
	List<ItemMasterSEL_3_Dto> find_SEL_3(int categoryAtr);

	/**
	 * Find all item master
	 * @param companyCode company code
	 * @return list of item master
	 */ 
	List<ItemMasterDto> findAll(String companyCode);
	
	/**
	 * Find all item master 
	 * @param companyCode company code
	 * @param itemCodes list of item code
	 * @return list of item master
	 */
	List<ItemMasterDto> findBy(String companyCode, List<String> itemCodes);
	
}
