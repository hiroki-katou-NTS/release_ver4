package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.List;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface WorkTypeDivergenceReferenceTimeHistoryGetMemento.
 */
public interface WorkTypeDivergenceReferenceTimeHistoryGetMemento {
	
	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();
	
	/**
	 * Gets the work type code.
	 *
	 * @return the work type code
	 */
	BusinessTypeCode getWorkTypeCode();

	/**
	 * Gets the history items.
	 *
	 * @return the history items
	 */
	List<DateHistoryItem> getHistoryItems();
}
