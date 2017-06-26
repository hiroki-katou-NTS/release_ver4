package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;

public interface ItemSalaryRespository {
	Optional<ItemSalary> find(String companyCode, String itemCode);

	/**
	 * Find all item salary by company
	 * 
	 * @param companyCode
	 * @return
	 */
	List<ItemSalary> findAll(String companyCode);

	/**
	 * Find all item salary by company
	 * 
	 * @param companyCode
	 * @param avePayAtr
	 * @return
	 */
	List<ItemSalary> findAll(String companyCode, AvePayAtr avePayAtr);

	/**
	 * update information of item salary
	 * 
	 * @param item
	 *            domain ItemSalary
	 */
	void update(String companyCode, ItemSalary item);

	void add(String companyCode, ItemSalary item);

	void delete(String companyCode, String itemCode);

	/**
	 * Update avePayAtr multiple item
	 * 
	 * @param companyCode
	 * @param itemCodeList
	 * @param avePayAtr
	 */
	void updateItems(String companyCode, List<String> itemCodeList, AvePayAtr avePayAtr);
}
