package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;

/**
 * The Interface DivergenceReasonInputMethodGetMemento.
 */
public interface DivergenceReasonInputMethodGetMemento {

	/**
	 * Gets the divergence time no.
	 *
	 * @return the divergence time no
	 */
	int getDivergenceTimeNo();

	/**
	 * Gets the companyid.
	 *
	 * @return the companyId
	 */
	String getCompanyId();

	/**
	 * Gets the divergence reason input.
	 *
	 * @return the divergence reason input
	 */
	boolean getDivergenceReasonInputed();

	/**
	 * Gets the divergence reason select.
	 *
	 * @return the divergence reason select
	 */
	boolean getDivergenceReasonSelected();

	/**
	 * Gets the divergence reasons.
	 *
	 * @return the divergence reasons
	 */
	List<DivergenceReasonSelect> getReasons();
}
