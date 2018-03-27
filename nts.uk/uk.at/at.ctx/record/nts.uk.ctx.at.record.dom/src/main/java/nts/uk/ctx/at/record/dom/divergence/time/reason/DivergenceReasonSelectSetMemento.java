package nts.uk.ctx.at.record.dom.divergence.time.reason;

/**
 * The Interface DivergenceReasonSelectSetMemento.
 */
public interface DivergenceReasonSelectSetMemento {

	/**
	 * Sets the divergence reason code.
	 *
	 * @param divergenceReasonCode the new divergence reason code
	 * @return the divergence reason code
	 */
	void setDivergenceReasonCode(DivergenceReasonCode divergenceReasonCode);

	/**
	 * Sets the divergence reason.
	 *
	 * @param reason the new reason
	 * @return the divergence reason
	 */
	void setReason (DivergenceReason reason);

	/**
	 * Sets the divergence reason required.
	 *
	 * @param reasonRequired the new reason required
	 * @return the divergence reason required
	 */
	void setReasonRequired(DivergenceInputRequired reasonRequired);
}
