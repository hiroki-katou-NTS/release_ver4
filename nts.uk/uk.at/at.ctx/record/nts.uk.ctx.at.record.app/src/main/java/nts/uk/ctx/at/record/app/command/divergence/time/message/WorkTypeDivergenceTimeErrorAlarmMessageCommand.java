package nts.uk.ctx.at.record.app.command.divergence.time.message;

import lombok.Data;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessageCommand.
 */
@Data
public class WorkTypeDivergenceTimeErrorAlarmMessageCommand {

	/** The work type code. */
	private String workTypeCode;

	/** The divergence time no. */
	private Integer divergenceTimeNo;

	/** The divergence time name. */
	private String divergenceTimeName;

	/** The alarm message. */
	private String alarmMessage;

	/** The error message. */
	private String errorMessage;

	/**
	 * Instantiates a new work type divergence time error alarm message command.
	 */
	public WorkTypeDivergenceTimeErrorAlarmMessageCommand() {
		super();
	}
}
