package nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection;

import java.util.List;

/**
 * 
 * @author tuannv
 *
 */

public interface SelectionItemOrderRepository {
	void add(SelectionItemOrder selectionItemOrder);

	void remove(String selectionItemOrder);
	
	List<SelectionItemOrder> getAllOrderSelectionByHistId(String histId);
	
	List<SelectionItemOrder> getAllOrderBySelectionId(String selectionId);
	//hoatt
	/**
	 * update List Selection Item Order
	 * @param lstSelOrder
	 */
	void updateListSelOrder(List<SelectionItemOrder> lstSelOrder);
}
