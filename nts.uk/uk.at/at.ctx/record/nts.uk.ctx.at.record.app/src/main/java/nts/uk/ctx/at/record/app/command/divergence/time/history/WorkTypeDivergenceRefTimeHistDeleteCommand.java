package nts.uk.ctx.at.record.app.command.divergence.time.history;

import lombok.Data;

/**
 * The Class WorkTypeDivergenceRefTimeHistDeleteCommand.
 */
@Data
public class WorkTypeDivergenceRefTimeHistDeleteCommand {
	/** The history id. */
	private String historyId;
	
	/** The work type code. */
	private String workTypeCode;
	
	/**
	 * Instantiates a new work type divergence ref time hist delete command.
	 */
	public WorkTypeDivergenceRefTimeHistDeleteCommand() {
		super();
	}
}
