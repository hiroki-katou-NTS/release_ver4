package nts.uk.ctx.at.record.dom.divergence.time;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DivergenceTimeErrorCancelMethod.
 */
@Getter
@Setter
public class DivergenceTimeErrorCancelMethod {

	/** The reason inputed. */
	private boolean reasonInputed;

	/** The reason selected. */
	private boolean reasonSelected;

	/**
	 * Instantiates a new divergence time error cancel method.
	 *
	 * @param reasonInputed
	 *            the reason inputed
	 * @param reasonSelected
	 *            the reason selected
	 */
	public DivergenceTimeErrorCancelMethod(int reasonInputed, int reasonSelected) {

		this.reasonInputed = reasonInputed == 1;

		this.reasonSelected = reasonSelected == 1;

	}

	/**
	 * Instantiates a new divergence time error cancel method.
	 */
	public DivergenceTimeErrorCancelMethod() {

	}

}
