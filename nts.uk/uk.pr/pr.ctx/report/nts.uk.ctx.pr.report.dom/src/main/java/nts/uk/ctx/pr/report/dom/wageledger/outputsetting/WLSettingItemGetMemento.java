/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

/**
 * The Interface WLSettingItemGetMemento.
 */
public interface WLSettingItemGetMemento {
	
	/**
	 * Gets the linkage code.
	 *
	 * @return the linkage code
	 */
	String getLinkageCode();
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	WLItemType getType();
	
	/**
	 * Gets the order number.
	 *
	 * @return the order number
	 */
	int getOrderNumber();
}
