package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.List;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface WorkTypeDivergenceReferenceTimeHistorySetMemento.
 */
public interface WorkTypeDivergenceReferenceTimeHistorySetMemento {
	
	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);
	
	/**
	 * Sets the work type code.
	 *
	 * @param workTypeCode the new work type code
	 */
	void setWorkTypeCode(BusinessTypeCode workTypeCode);

	/**
	 * Sets the history items.
	 *
	 * @param historyItems the new history items
	 */
	void setHistoryItems(List<DateHistoryItem> historyItems);
}
