/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;

/**
 * The Class SalaryOutputItem.
 */
@Getter
public class SalaryOutputItem extends DomainObject {

	/** The linkage code. */
	private String linkageCode;

	/** The type. */
	private SalaryItemType type;

	/** The order number. */
	private int orderNumber;

	/**
	 * Instantiates a new salary output item.
	 *
	 * @param memento the memento
	 */
	public SalaryOutputItem(SalaryOutputItemGetMemento memento) {
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
	public void saveToMemento(SalaryOutputItemSetMemento memento) {
		memento.setLinkageCode(this.linkageCode);
		memento.setType(this.type);
		memento.setOrderNumber(this.orderNumber);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkageCode == null) ? 0 : linkageCode.hashCode());
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
		SalaryOutputItem other = (SalaryOutputItem) obj;
		if (linkageCode == null) {
			if (other.linkageCode != null)
				return false;
		} else if (!linkageCode.equals(other.linkageCode))
			return false;
		return true;
	}
}
