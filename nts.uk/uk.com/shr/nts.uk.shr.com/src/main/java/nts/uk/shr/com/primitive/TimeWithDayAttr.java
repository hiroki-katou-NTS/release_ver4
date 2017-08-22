package nts.uk.shr.com.primitive;

import org.apache.http.annotation.Obsolete;

import nts.arc.primitive.TimeClockPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;
import nts.gul.util.Time;
import nts.uk.shr.com.enumcommon.DayAttr;

@TimeRange(min = "-12:00", max = "71:59")
public class TimeWithDayAttr extends TimeClockPrimitiveValue<TimeWithDayAttr>{

	private static final int MAX_MINUTES_IN_DAY = Time.MAX_HOUR * Time.MAX_MS;

	/** 12:00 at the previous day */
	public static final TimeWithDayAttr THE_PREVIOUS_DAY_1200 = new TimeWithDayAttr(-12 * Time.MAX_MS);
	
	/** 00:00 at the present day */
	public static final TimeWithDayAttr THE_PRESENT_DAY_0000 = new TimeWithDayAttr(0);
	
	/** 00:00 at the next day */
	public static final TimeWithDayAttr THE_NEXT_DAY_0000 = new TimeWithDayAttr(1 * MAX_MINUTES_IN_DAY);
	
	/** 00:00 at two days later */
	public static final TimeWithDayAttr TWO_DAYS_LATER_0000 = new TimeWithDayAttr(2 * MAX_MINUTES_IN_DAY);
	
	/** 23:59 at two days later */
	public static final TimeWithDayAttr TWO_DAYS_LATER_2359 = new TimeWithDayAttr(3 * MAX_MINUTES_IN_DAY - 1);
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs.
	 * @param minutesFromZeroOClock elapsed time as minutes from zero o'clock
	 */
	public TimeWithDayAttr(int minutesFromZeroOClock) {
		super(minutesFromZeroOClock);
	}

	/**
	 * OBSOLETE: use dayAttr() instead
	 * @return
	 */
	public DayAttr getDayDivision() {
		return this.dayAttr();
	}
	
	/**
	 * Returns day attribute.
	 * @return day attribute
	 */
	public DayAttr dayAttr(){
		
		switch (this.daysOffset()) {
		case 0:
			return DayAttr.THE_PRESENT_DAY;
		case 1:
			return DayAttr.THE_NEXT_DAY;
		case 2:
			return DayAttr.TWO_DAY_LATER;
		case -1:
			return DayAttr.THE_PREVIOUS_DAY;
		default:
			throw new RuntimeException("not supported day attr: " + this.v());
		}
	}
	
	public int getDayTime(){
		return (this.v() + MAX_MINUTES_IN_DAY) % MAX_MINUTES_IN_DAY;
	}
	
	/**
	 * OBSOLETE: use timeAsMinutes() instead.
	 * @return value as minutes integer
	 */
	@Obsolete
	public int valueAsMinutes() {
		return this.v();
	}
	
	public String getInDayTimeWithFormat(){
		return this.hour() + ":" + this.minute();
	}
	
	public String getRawTimeWithFormat(){
		return (this.v() / 60) + ":" + (this.v() % 60);
	}
	
	/**
	 * Returns shifted time instance but min="12:00 at previous day" max="23:59 at two days later".
	 * @param minutesToShift
	 * @return shifted time instance
	 */
	public TimeWithDayAttr shiftWithLimit(int minutesToShift) {
		int newValue = this.v() + minutesToShift;
		newValue = Math.max(newValue, THE_PREVIOUS_DAY_1200.timeAsMinutes());
		newValue = Math.min(newValue, TWO_DAYS_LATER_2359.timeAsMinutes());
		return new TimeWithDayAttr(newValue);
	}
}
