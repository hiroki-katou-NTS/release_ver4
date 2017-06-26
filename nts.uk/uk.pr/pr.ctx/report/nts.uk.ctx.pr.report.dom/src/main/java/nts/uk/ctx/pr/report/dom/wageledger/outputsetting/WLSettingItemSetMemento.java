/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

/**
 * The Interface WLSettingItemSetMemento.
 */
public interface WLSettingItemSetMemento {
	
	/**
	 * Sets the linkage code.
	 *
	 * @param linkageCode the new linkage code
	 */
	void setLinkageCode(String linkageCode);
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	void setType(WLItemType type);
	
	/**
	 * Sets the order number.
	 *
	 * @param orderNumber the new order number
	 */
	void setOrderNumber(int orderNumber);
}
