package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber;

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
// 特別休暇使用数
public class SpecialLeaveUsedNumber {

	/**
	 * 日数
	 */
	public DayNumberOfUse dayNumberOfUse;
	/**
	 * 時間
	 */
	public Optional<TimeOfUse> timeOfUse;
	/**
	 * 積み崩し日数
	 */
	public Optional<DayNumberOfUse> useSavingDays;
	/**
	 * 上限超過消滅日数
	 */
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
