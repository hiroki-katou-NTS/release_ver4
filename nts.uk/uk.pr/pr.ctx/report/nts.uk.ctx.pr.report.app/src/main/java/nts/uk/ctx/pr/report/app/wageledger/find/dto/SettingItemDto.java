/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find.dto;

import lombok.Builder;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemSetMemento;

/**
 * The Class SettingItemDto.
 */
@Builder
public class SettingItemDto implements WLSettingItemSetMemento{
	
	/** The code. */
	public String code;
	
	/** The name. */
	public String name;
	
	/** The is aggregate item. */
	public Boolean isAggregateItem;
	
	/** The order number. */
	public int orderNumber;

	/**
	 * Sets the linkage code.
	 *
	 * @param linkageCode the new linkage code
	 */
	@Override
	public void setLinkageCode(String linkageCode) {
		this.code = linkageCode;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	@Override
	public void setType(WLItemType type) {
		this.isAggregateItem = type == WLItemType.Aggregate;
	}

	/**
	 * Sets the order number.
	 *
	 * @param orderNumber the new order number
	 */
	@Override
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
}
