package nts.uk.ctx.at.record.app.find.divergence.time.reason;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceInputRequired;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonCode;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelectSetMemento;

/**
 * The Class DivergenceReasonSelectDto.
 */
@Getter
@Setter
@AllArgsConstructor
public class DivergenceReasonSelectDto implements DivergenceReasonSelectSetMemento {

	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The divergence reason code. */
	private String divergenceReasonCode;

	/** The reason. */
	private String reason;

	/** The reason required. */
	private int reasonRequired;

	/**
	 * Instantiates a new divergence reason select dto.
	 */
	public DivergenceReasonSelectDto() {
		super();
	}

	@Override
	public void setDivergenceReasonCode(DivergenceReasonCode divergenceReasonCode) {
		this.divergenceReasonCode = divergenceReasonCode.toString();

	}

	@Override
	public void setReason(DivergenceReason reason) {
		this.reason = reason.toString();

	}

	@Override
	public void setReasonRequired(DivergenceInputRequired reasonRequired) {

		this.reasonRequired = reasonRequired.value;
	}

}
