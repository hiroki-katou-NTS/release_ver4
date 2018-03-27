package nts.uk.ctx.at.record.app.command.divergence.time.reason;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceInputRequired;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonCode;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelectGetMemento;

/**
 * The Class DivergenceReasonSelectDeleteCommand.
 */
@Getter
public class DivergenceReasonSelectDeleteCommand implements DivergenceReasonSelectGetMemento {
	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The divergence reason code. */
	private String divergenceReasonCode;

	/** The reason. */
	private String reason;

	/** The reason required. */
	private int reasonRequired;

	/**
	 * Instantiates a new divergence reason select delete command.
	 */
	public DivergenceReasonSelectDeleteCommand() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectGetMemento#getDivergenceReasonCode()
	 */
	@Override
	public DivergenceReasonCode getDivergenceReasonCode() {
		return new DivergenceReasonCode(divergenceReasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectGetMemento#getReason()
	 */
	@Override
	public DivergenceReason getReason() {
		return new DivergenceReason(reason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectGetMemento#getReasonRequired()
	 */
	@Override
	public DivergenceInputRequired getReasonRequired() {
		return DivergenceInputRequired.valueOf(reasonRequired);
	}
}
