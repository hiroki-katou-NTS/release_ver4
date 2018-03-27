/*
 * 
 */
package nts.uk.ctx.at.record.app.command.divergence.time.message;

import lombok.Data;

/**
 * The Class DivergenceTimeErrorAlarmMessageCommand.
 */
@Data
public class DivergenceTimeErrorAlarmMessageCommand {

	/** The divergence time no. */
	private Integer divergenceTimeNo;

	/** The divergence time name. */
	private String divergenceTimeName;

	/** The alarm message. */
	private String alarmMessage;

	/** The error message. */
	private String errorMessage;

	/**
	 * Instantiates a new divergence time error alarm command.
	 */
	public DivergenceTimeErrorAlarmMessageCommand() {
		super();
	}
}
