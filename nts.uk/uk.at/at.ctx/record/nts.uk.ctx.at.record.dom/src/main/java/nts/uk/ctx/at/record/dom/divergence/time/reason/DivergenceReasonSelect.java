package nts.uk.ctx.at.record.dom.divergence.time.reason;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

@Getter
@Setter
@AllArgsConstructor
public class DivergenceReasonSelect extends DomainObject {

	/** The reason code. */
	// 乖離理由コード
	DivergenceReasonCode divergenceReasonCode;

	/** The reason. */
	// 乖離理由
	DivergenceReason reason;

	/** Reason required. */
	// 乖離理由の入力を必須とする
	DivergenceInputRequired reasonRequired;

	/**
	 * Instantiates a new divergence reason select.
	 *
	 * @param memento
	 *            the memento
	 */
	public DivergenceReasonSelect(DivergenceReasonSelectGetMemento memento) {
		this.divergenceReasonCode = memento.getDivergenceReasonCode();
		this.reason = memento.getReason();
		this.reasonRequired = memento.getReasonRequired();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(DivergenceReasonSelectSetMemento memento) {
		memento.setDivergenceReasonCode(this.divergenceReasonCode);
		memento.setReason(this.reason);
		memento.setReasonRequired(this.reasonRequired);
	}

}
