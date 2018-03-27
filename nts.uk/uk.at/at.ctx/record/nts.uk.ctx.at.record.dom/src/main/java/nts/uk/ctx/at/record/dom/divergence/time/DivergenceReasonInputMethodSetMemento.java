package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;

/**
 * The Interface DivergenceReasonInputMethodSetMemento.
 */
public interface DivergenceReasonInputMethodSetMemento {

	/**
	 * Sets the new divergence time no.
	 *
	 * @param DivergenceTimeNo the new divergence time no
	 */
	void setDivergenceTimeNo(int DivergenceTimeNo);

	/**
	 * Sets the new companyid.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);

	/**
	 * Sets the new divergence reason input.
	 *
	 * @param divergenceReasonInputed the new divergence reason inputed
	 */
	void setDivergenceReasonInputed(boolean divergenceReasonInputed);

	/**
	 * Sets the new divergence reason select.
	 *
	 * @param divergenceReasonSelected the new divergence reason selected
	 */
	void setDivergenceReasonSelected(boolean divergenceReasonSelected);

	/**
	 * Sets the new divergence reasons.
	 *
	 * @param reason the new reasons
	 */
	void setReasons(List<DivergenceReasonSelect> reason);
}
