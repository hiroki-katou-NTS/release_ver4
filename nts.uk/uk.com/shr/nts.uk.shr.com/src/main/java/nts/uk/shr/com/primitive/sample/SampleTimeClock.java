package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.TimeClockPrimitiveValue;
import nts.arc.primitive.constraint.TimeMaxValue;
import nts.arc.primitive.constraint.TimeMinValue;

@TimeMaxValue("16:00")
@TimeMinValue("3:00")
public class SampleTimeClock extends TimeClockPrimitiveValue<SampleTimeClock> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public SampleTimeClock(int hour, int minute, int second) {
		super(hour, minute, second);
	}
	
}
