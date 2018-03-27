package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.List;

import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface CompanyDivergenceReferenceTimeHistoryGetMemento.
 */
public interface CompanyDivergenceReferenceTimeHistoryGetMemento {
	
	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();
	
	/**
	 * Gets the history items.
	 *
	 * @return the history items
	 */
	List<DateHistoryItem> getHistoryItems();

}
