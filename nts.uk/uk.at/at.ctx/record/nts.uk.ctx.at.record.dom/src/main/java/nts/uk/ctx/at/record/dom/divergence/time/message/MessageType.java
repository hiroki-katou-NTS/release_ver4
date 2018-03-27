package nts.uk.ctx.at.record.dom.divergence.time.message;

/**
 * The Enum MessageType.
 */
public enum MessageType {
	
	/** The alarm. */
	ALARM,
	
	/** The error. */
	ERROR;
	
	/**
	 * Checks if is alarm.
	 *
	 * @return true, if is alarm
	 */
	public boolean isAlarm() {
		return ALARM.equals(this);
	}
	
	/**
	 * Checks if is error.
	 *
	 * @return true, if is error
	 */
	public boolean isError() {
		return ERROR.equals(this);
	}
}
