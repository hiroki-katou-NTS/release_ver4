package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

@TimeRange(max="100:30", min = "01:00")
public class SampleTimeDuration extends TimeDurationPrimitiveValue<SampleTimeDuration> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public SampleTimeDuration(int hour, int minute, int second) {
		super(hour, minute, second);
	}

}
