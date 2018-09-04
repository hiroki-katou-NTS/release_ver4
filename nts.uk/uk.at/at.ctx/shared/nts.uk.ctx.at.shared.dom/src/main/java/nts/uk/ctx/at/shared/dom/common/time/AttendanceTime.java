package nts.uk.ctx.at.shared.dom.common.time;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * The Class AttendenceTime.
 */
// 勤怠時間
@TimeRange(max = "48:00", min = "00:00")
public class AttendanceTime extends TimeDurationPrimitiveValue<AttendanceTime> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new attendence time.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public AttendanceTime(Integer rawValue) {
		super(rawValue);
	}
	
	@Override
	protected Integer reviseRawValue(Integer rawValue) {
		if(rawValue > 2880)
			rawValue = 2880;
		if(0 > rawValue)
			rawValue = 0;
		return super.reviseRawValue(rawValue);
	}
}