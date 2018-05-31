package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 特別休暇使用数
public class SpecialLeaveUsedNumber {


	public DayNumberOfUse dayNumberOfUse;

	public Optional<TimeOfUse> timeOfUse;

	public Optional<DayNumberOfUse> useSavingDays;

	public Optional<SpecialLeaveOverNumber> specialLeaveOverLimitNumber;

	private SpecialLeaveUsedNumber(BigDecimal dayNumberOfUse, Integer timeOfUse, BigDecimal useSavingDays,
			BigDecimal dayNumberOfExeeded, Integer timeOfExeeded) {
		this.dayNumberOfUse = new DayNumberOfUse(dayNumberOfUse == null? 0.0d: dayNumberOfUse.doubleValue());
		this.timeOfUse = timeOfUse != null ? Optional.of(new TimeOfUse(timeOfUse)) : Optional.empty();
		this.useSavingDays = useSavingDays != null ? Optional.of(new DayNumberOfUse( useSavingDays.doubleValue()))
				: Optional.empty();
		if(dayNumberOfExeeded == null && timeOfExeeded  == null) {
			this.specialLeaveOverLimitNumber = Optional.empty();
		}else {
		this.specialLeaveOverLimitNumber = Optional
				.of(SpecialLeaveOverNumber.createFromJavaType(dayNumberOfExeeded == null? 0.0d: dayNumberOfExeeded.doubleValue(), timeOfExeeded));
		}
	}

	public static SpecialLeaveUsedNumber createFromJavaType(BigDecimal dayNumberOfUse, Integer timeOfUse,
			BigDecimal useSavingDays, BigDecimal dayNumberOfExeeded, Integer timeOfExeeded) {
		return new SpecialLeaveUsedNumber(dayNumberOfUse, timeOfUse, useSavingDays, dayNumberOfExeeded,
				timeOfExeeded);
	}
	
	private SpecialLeaveUsedNumber(double dayNumberOfUse, Integer timeOfUse, Double useSavingDays,
			double dayNumberOfExeeded, Integer timeOfExeeded) {
		this.dayNumberOfUse = new DayNumberOfUse(dayNumberOfUse);
		this.timeOfUse = timeOfUse != null ? Optional.of(new TimeOfUse(timeOfUse)) : Optional.empty();
		this.useSavingDays = useSavingDays != null ? Optional.of(new DayNumberOfUse(useSavingDays))
				: Optional.empty();
		this.specialLeaveOverLimitNumber = Optional
				.of(SpecialLeaveOverNumber.createFromJavaType(dayNumberOfExeeded, timeOfExeeded));
	}

	public static SpecialLeaveUsedNumber createFromJavaType(double dayNumberOfUse, Integer timeOfUse,
			Double dayNumberOfUsed, double dayNumberOfExeeded, Integer timeOfExeeded) {
		return new SpecialLeaveUsedNumber(dayNumberOfUse, timeOfUse, dayNumberOfUsed, dayNumberOfExeeded,
				timeOfExeeded);
	}

}
