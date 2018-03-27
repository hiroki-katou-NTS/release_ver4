package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.math.BigDecimal;

public interface DivergenceReferenceTimeUsageUnitSetMemento {

	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);
	
	/**
	 * Sets the work type use set.
	 *
	 * @param workTypeUseSet the new work type use set
	 */
	void setWorkTypeUseSet(BigDecimal workTypeUseSet);
}
