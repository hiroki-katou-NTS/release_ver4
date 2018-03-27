package nts.uk.ctx.at.record.dom.divergence.time.history;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * The Class DivergenceReferenceTime.
 */
@TimeRange(max = "99:59", min = "00:00")
public class DivergenceReferenceTime extends TimeDurationPrimitiveValue<DivergenceReferenceTime>{

	/**
	 * Instantiates a new divergence reference time.
	 *
	 * @param timeAsMinutes the time as minutes
	 */
	public DivergenceReferenceTime(int timeAsMinutes) {
		super(timeAsMinutes);
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Gets the in day time with format.
	 *
	 * @return the in day time with format
	 */
	public String getInDayTimeWithFormat(){
		return this.hour() + ":" + (this.minute() < 10 ? "0" + this.minute() : this.minute());
	}

}
