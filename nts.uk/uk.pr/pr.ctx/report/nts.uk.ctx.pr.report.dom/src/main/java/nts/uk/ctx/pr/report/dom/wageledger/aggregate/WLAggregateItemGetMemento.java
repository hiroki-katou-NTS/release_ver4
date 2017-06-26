/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import java.util.Set;

/**
 * The Interface WLAggregateItemGetMemento.
 */
public interface WLAggregateItemGetMemento {
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	WLItemSubject getSubject();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	WLAggregateItemName getName();
	
	/**
	 * Gets the show name zero value.
	 *
	 * @return the show name zero value
	 */
	Boolean getShowNameZeroValue();
	
	/**
	 * Gets the show value zero value.
	 *
	 * @return the show value zero value
	 */
	Boolean getShowValueZeroValue();
	
	/**
	 * Gets the sub items.
	 *
	 * @return the sub items
	 */
	Set<String> getSubItems();

}
