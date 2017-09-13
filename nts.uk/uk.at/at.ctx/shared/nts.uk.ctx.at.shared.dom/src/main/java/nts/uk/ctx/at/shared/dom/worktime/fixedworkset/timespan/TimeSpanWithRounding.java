package nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan;

import lombok.Getter;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.TimeRoundingSetting;
import nts.uk.shr.com.time.TimeWithDayAttr;

public class TimeSpanWithRounding extends TimeSpanForCalc {

	@Getter
	private final Finally<TimeRoundingSetting> rounding;
	
	public TimeSpanWithRounding(TimeWithDayAttr start, TimeWithDayAttr end, Finally<TimeRoundingSetting> rounding) {
		super(start, end);
		this.rounding = rounding;
	}
	
	public int roundedLengthAsMinutesWithDeductingBy(int deductingTimeAsMinutes) {
		return this.rounding.get().round(this.lengthAsMinutes() - deductingTimeAsMinutes);
	}
}
