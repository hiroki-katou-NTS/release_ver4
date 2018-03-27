package nts.uk.ctx.at.record.app.command.divergence.time.history;

import lombok.Data;

/**
 * The Class ComDivergenceRefTimeHistDeleteCommand.
 */
@Data
public class ComDivergenceRefTimeHistDeleteCommand {
	
	/** The history id. */
	private String historyId;
	
	/**
	 * Instantiates a new com divergence ref time hist delete command.
	 */
	public ComDivergenceRefTimeHistDeleteCommand() {
		super();
	}
}
