package nts.uk.ctx.at.record.dom.divergence.time.reason;

/**
 * The Interface DivergenceReasonSelectGetMemento.
 */
public interface DivergenceReasonSelectGetMemento {

	/**
	 * Gets the divergence reason code.
	 *
	 * @return the divergence reason code
	 */
	DivergenceReasonCode getDivergenceReasonCode();

	/**
	 * Gets the divergence reason.
	 *
	 * @return the divergence reason
	 */
	DivergenceReason getReason ();

	/**
	 * Gets the divergence reason required.
	 *
	 * @return the divergence reason required
	 */
	DivergenceInputRequired getReasonRequired();
}
