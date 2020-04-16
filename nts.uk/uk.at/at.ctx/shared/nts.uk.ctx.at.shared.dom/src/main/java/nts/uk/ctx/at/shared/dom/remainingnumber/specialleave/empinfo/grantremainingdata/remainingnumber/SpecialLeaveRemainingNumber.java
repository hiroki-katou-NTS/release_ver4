package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 特別休暇残数
public class SpecialLeaveRemainingNumber {

	// 日数
	public DayNumberOfRemain dayNumberOfRemain;
	// 時間
	public Optional<TimeOfRemain> timeOfRemain;
	
	private SpecialLeaveRemainingNumber(BigDecimal days, Integer minutes) {
        this.dayNumberOfRemain = new DayNumberOfRemain(days== null? 0.0d: days.doubleValue());
		this.timeOfRemain = minutes != null ? Optional.of(new TimeOfRemain(minutes)) : Optional.empty();
	}

	public static SpecialLeaveRemainingNumber createFromJavaType(BigDecimal days, Integer minutes) {
		return new SpecialLeaveRemainingNumber(days, minutes);
	}
	private SpecialLeaveRemainingNumber(Double days, Integer minutes) {
		this.dayNumberOfRemain = new DayNumberOfRemain(days);
		this.timeOfRemain = minutes != null ? Optional.of(new TimeOfRemain(minutes)) : Optional.empty();
	}

	public static SpecialLeaveRemainingNumber createFromJavaType(Double days, Integer minutes) {
		return new SpecialLeaveRemainingNumber(days, minutes);
	}

}
