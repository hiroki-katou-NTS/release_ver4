package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Interface WorkTypeDivergenceReferenceTimeSetMemento.
 */
public interface WorkTypeDivergenceReferenceTimeSetMemento {

	/**
	 * Sets the divergence time no.
	 *
	 * @param divergenceTimeNo the new divergence time no
	 */
	void setDivergenceTimeNo(Integer divergenceTimeNo);
	
	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);
	
	/**
	 * Sets the not use atr.
	 *
	 * @param notUseAtr the new not use atr
	 */
	void setNotUseAtr(NotUseAtr notUseAtr);
	
	/**
	 * Sets the work type code.
	 *
	 * @param workTypeCode the new work type code
	 */
	void setWorkTypeCode(BusinessTypeCode workTypeCode);
	
	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	void setHistoryId(String historyId);
	
	/**
	 * Sets the divergence reference time value.
	 *
	 * @param divergenceReferenceTimeValue the new divergence reference time value
	 */
	void setDivergenceReferenceTimeValue(Optional<DivergenceReferenceTimeValue> divergenceReferenceTimeValue);
}
