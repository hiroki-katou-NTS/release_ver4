package nts.uk.ctx.at.record.app.find.divergence.time.history;

import lombok.Data;

/**
 * The Class DivergenceReferenceTimeValueDto.
 */
@Data
public class DivergenceReferenceTimeValueDto {
	
	/** The alarm time. */
	private int alarmTime; 
	
	/** The error time. */
	private int errorTime;
	
	/**
	 * Instantiates a new divergence reference time value dto.
	 *
	 * @param alarmTime the alarm time
	 * @param errorTime the error time
	 */
	public DivergenceReferenceTimeValueDto(int alarmTime, int errorTime) {
		this.alarmTime = alarmTime;
		this.errorTime = errorTime;
	}
}
