/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * The Class WtElementSetting.
 */
@Getter
public class ElementSetting {

	/** The demension no. */
	private DemensionNo demensionNo;

	/** The type. */
	private ElementType type;

	/** The item list. */
	@Setter
	private List<? extends Item> itemList;

	/**
	 * Instantiates a new wt element setting.
	 *
	 * @param demensionNo
	 *            the demension no
	 * @param type
	 *            the type
	 * @param itemList
	 *            the item list
	 */
	public ElementSetting(DemensionNo demensionNo, ElementType type, List<? extends Item> itemList) {
		super();
		this.demensionNo = demensionNo;
		this.type = type;
		this.itemList = itemList;
	}
}
