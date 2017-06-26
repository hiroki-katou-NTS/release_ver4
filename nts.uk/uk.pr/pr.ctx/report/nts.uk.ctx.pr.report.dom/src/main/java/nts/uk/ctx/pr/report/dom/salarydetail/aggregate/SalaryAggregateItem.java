/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import java.util.Set;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;

/**
 * The Class SalaryAggregateItem.
 */
@Getter
public class SalaryAggregateItem extends DomainObject {

	
	private SalaryAggregateItemHeader header;

	/** The name. */
	private SalaryAggregateItemName name;

	/** The sub item codes. */
	private Set<SalaryItem> subItemCodes;

	/**
	 * Instantiates a new salary aggregate item.
	 *
	 * @param memento the memento
	 */
	public SalaryAggregateItem(SalaryAggregateItemGetMemento memento) {
		super();
		this.header = memento.getSalaryAggregateItemHeader();
		this.name = memento.getSalaryAggregateItemName();
		this.subItemCodes = memento.getSubItemCodes();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(SalaryAggregateItemSetMemento memento) {
		memento.setSalaryAggregateItemHeader(this.header);
		memento.setSalaryAggregateItemName(this.name);
		memento.setSubItemCodes(this.subItemCodes);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((header == null) ? 0 : header.hashCode());
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
        SalaryAggregateItem other = (SalaryAggregateItem) obj;
        if (header == null) {
            if (other.header != null)
                return false;
        } else if (!header.equals(other.header))
            return false;
        return true;
    }
}
