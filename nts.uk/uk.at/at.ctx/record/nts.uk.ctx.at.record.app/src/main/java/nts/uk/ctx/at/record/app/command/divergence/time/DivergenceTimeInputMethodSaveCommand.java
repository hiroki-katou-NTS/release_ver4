package nts.uk.ctx.at.record.app.command.divergence.time;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class DivergenceTimeInputMethodSaveCommand.
 */
@Getter
@Setter
@AllArgsConstructor
public class DivergenceTimeInputMethodSaveCommand {

	/** The divergence time no. */

	private int divergenceTimeNo;

	/** The c id. */

	private String companyId;

	/** The Use classification. */

	private int divergenceTimeUseSet;

	/** The divergence time name. */

	private String divergenceTimeName;

	/** The divergence type. */

	private String divergenceType;

	/** The divergence time error cancel method. */

	private boolean reasonInput;

	/** The reason select. */

	private boolean reasonSelect;

	/** The divergence reason inputed. */

	private boolean divergenceReasonInputed;

	/** The divergence reason selected. */

	private boolean divergenceReasonSelected;

	/** The attendance id. */
	private List<Integer> targetItems;

	/**
	 * Instantiates a new divergence time input method save command.
	 */
	public DivergenceTimeInputMethodSaveCommand() {
		super();
	}

}
