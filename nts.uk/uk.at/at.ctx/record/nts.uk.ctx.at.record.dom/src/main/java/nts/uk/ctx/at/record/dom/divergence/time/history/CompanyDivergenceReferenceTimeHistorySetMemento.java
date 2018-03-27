package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.List;

import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface CompanyDivergenceReferenceTimeHistorySetMemento.
 */
public interface CompanyDivergenceReferenceTimeHistorySetMemento {
	
	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);
	
	/**
	 * Sets the history items.
	 *
	 * @param historyItems the new history items
	 */
	void setHistoryItems(List<DateHistoryItem> historyItems);
}
