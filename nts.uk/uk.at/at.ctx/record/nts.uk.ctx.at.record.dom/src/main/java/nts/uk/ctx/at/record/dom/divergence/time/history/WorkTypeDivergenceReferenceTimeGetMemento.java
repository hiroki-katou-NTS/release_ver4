package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Interface WorkTypeDivergenceReferenceTimeGetMemento.
 */
public interface WorkTypeDivergenceReferenceTimeGetMemento {
	/**
	 * Gets the divergence time no.
	 *
	 * @return the divergence type
	 */
	Integer getDivergenceTimeNo();
	
	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();
	
	/**
	 * Gets the not use atr.
	 *
	 * @return the not use atr
	 */
	NotUseAtr getNotUseAtr();
	
	/**
	 * Gets the work type code.
	 *
	 * @return the work type code
	 */
	BusinessTypeCode getWorkTypeCode();
	
	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	String getHistoryId();
	
	/**
	 * Gets the divergence reference time value.
	 *
	 * @return the divergence reference time value
	 */
	Optional<DivergenceReferenceTimeValue> getDivergenceReferenceTimeValue();
}
