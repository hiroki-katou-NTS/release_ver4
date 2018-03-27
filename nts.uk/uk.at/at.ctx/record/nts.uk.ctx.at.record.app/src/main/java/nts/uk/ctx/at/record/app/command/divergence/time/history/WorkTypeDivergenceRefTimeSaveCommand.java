package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.List;

import lombok.Data;

/**
 * The Class WorkTypeDivergenceRefTimeSaveCommand.
 */
@Data
public class WorkTypeDivergenceRefTimeSaveCommand {
	/** The list data setting. */
	private List<WorkTypeDivergenceRefTimeSaveDto> listDataSetting;
	
	/**
	 * Instantiates a new work type divergence ref time save command.
	 */
	public WorkTypeDivergenceRefTimeSaveCommand() {
		super();
	}
}
