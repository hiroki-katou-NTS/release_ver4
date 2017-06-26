package nts.uk.ctx.pr.core.dom.itemmaster.itemattend;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;

public interface ItemAttendRespository {
	
	Optional<ItemAttend> find(String companyCode, String itemCode);
	
	/**
	 * Find all item attend by company
	 * @param companyCode
	 * @return
	 */
	List<ItemAttend> findAll(String companyCode);
	
	/**
	 * Find all item attend by company
	 * @param companyCode
	 * @return
	 */
	List<ItemAttend> findAll(String companyCode, AvePayAtr avePayAtr);
	
	/**
	 * Update information of item attend
	 * @param item domain
	 */
	void update(String companyCode, ItemAttend item);

	void add(String companyCode, ItemAttend itemAttend);

	void delete(String companyCode, String itemCode);
	
	/**
	 * Update AvePayAtr multiple items
	 * @param companyCode
	 * @param itemCodeList
	 * @param avePayAtr
	 */
	void updateItems(String companyCode, List<String> itemCodeList, AvePayAtr avePayAtr);
	
}
