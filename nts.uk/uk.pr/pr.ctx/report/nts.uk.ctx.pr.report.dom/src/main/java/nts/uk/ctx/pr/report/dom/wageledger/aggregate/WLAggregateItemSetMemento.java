/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import java.util.Set;

/**
 * The Interface WLAggregateItemSetMemento.
 */
public interface WLAggregateItemSetMemento {
	
	/**
	 * Sets the subject.
	 *
	 * @param itemSubject the new subject
	 */
	void setSubject(WLItemSubject itemSubject);
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(WLAggregateItemName name);
	
	/**
	 * Sets the show name zero value.
	 *
	 * @param showNameZeroValue the new show name zero value
	 */
	void setShowNameZeroValue(Boolean showNameZeroValue);
	
	/**
	 * Sets the show value zero value.
	 *
	 * @param showValueZeroValue the new show value zero value
	 */
	void setShowValueZeroValue(boolean showValueZeroValue);
	
	/**
	 * Sets the sub items.
	 *
	 * @param subItems the new sub items
	 */
	void setSubItems(Set<String> subItems);
}
