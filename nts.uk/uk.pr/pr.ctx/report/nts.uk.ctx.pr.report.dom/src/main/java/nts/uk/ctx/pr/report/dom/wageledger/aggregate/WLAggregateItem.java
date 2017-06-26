/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import java.util.Set;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class WageLedgerAggregateItem.
 */
@Getter
public class WLAggregateItem extends DomainObject {
	
	/** The subject. */
	private WLItemSubject subject;
	
	/** The name. */
	private WLAggregateItemName name;
	
	/** The show name zero value. */
	private Boolean showNameZeroValue;
	
	/** The show value zero value. */
	private Boolean showValueZeroValue;
	
	/** The sub items. */
	private Set<String> subItems;
	
	/**
	 * Instantiates a new WL aggregate item.
	 *
	 * @param memento the memento
	 */
	public WLAggregateItem(WLAggregateItemGetMemento memento) {
		super();
		this.subject = memento.getSubject();
		this.name = memento.getName();
		this.showNameZeroValue = memento.getShowNameZeroValue();
		this.showValueZeroValue = memento.getShowValueZeroValue();
		this.subItems = memento.getSubItems();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WLAggregateItemSetMemento memento) {
		memento.setSubject(this.subject);
		memento.setName(this.name);
		memento.setShowNameZeroValue(this.showNameZeroValue);
		memento.setShowValueZeroValue(this.showValueZeroValue);
		memento.setSubItems(this.subItems);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		WLAggregateItem other = (WLAggregateItem) obj;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
}
