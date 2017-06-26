/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import lombok.Getter;

/**
 * The Class WageLedgerSettingItem.
 */
@Getter
public class WLSettingItem {
	
	/** The linkage code. */
	private String linkageCode;
	
	/** The type. */
	private WLItemType type;
	
	/** The order number. */
	private int orderNumber;

	/**
	 * Instantiates a new wage ledger setting item.
	 *
	 * @param linkageCode the linkage code
	 * @param type the type
	 * @param orderNumber the order number
	 */
	public WLSettingItem(WLSettingItemGetMemento memento) {
		super();
		this.linkageCode = memento.getLinkageCode();
		this.type = memento.getType();
		this.orderNumber = memento.getOrderNumber();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WLSettingItemSetMemento memento) {
		memento.setLinkageCode(this.linkageCode);
		memento.setOrderNumber(this.orderNumber);
		memento.setType(this.type);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkageCode == null) ? 0 : linkageCode.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WLSettingItem other = (WLSettingItem) obj;
		if (linkageCode == null) {
			if (other.linkageCode != null)
				return false;
		} else if (!linkageCode.equals(other.linkageCode))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
