/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.common.timerounding;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
//import nts.uk.ctx.at.shared.dom.common.timerounding.Unit.Direction;

/**
 * The Class TimeRoundingSetting.
 */
// 時間丸め設定
@Getter
@NoArgsConstructor
public class TimeRoundingSetting extends DomainObject implements Cloneable{

	/** The rounding time. */
	// 単位
	private Unit roundingTime;

	/** The rounding. */
	// 端数処理
	private Rounding rounding;

	/**
	 * Instantiates a new time rounding setting.
	 *
	 * @param roundingTime
	 *            the rounding time
	 * @param rounding
	 *            the rounding
	 */
	public TimeRoundingSetting(Unit roundingTime, Rounding rounding) {
		this.roundingTime = roundingTime;
		this.rounding = rounding;
	}

	/**
	 * Instantiates a new time rounding setting.
	 *
	 * @param roundingTime
	 *            the rounding time
	 * @param rounding
	 *            the rounding
	 */
	public TimeRoundingSetting(int roundingTime, int rounding) {
		this.roundingTime = EnumAdaptor.valueOf(roundingTime, Unit.class);
		this.rounding = EnumAdaptor.valueOf(rounding, Rounding.class);
	}

//	/**
//	 * Round.
//	 *
//	 * @param timeAsMinutes
//	 *            the time as minutes
//	 * @return the int
//	 */
//	public int round(int timeAsMinutes) {
//
//		int minutesInHour = timeAsMinutes % 60;
//
//		switch (this.rounding) {
//		case ROUNDING_DOWN_OVER:
//			int mod = minutesInHour % (this.roundingTime.asTime() * 2);
//			val direction = mod < this.roundingTime.asTime() ? Direction.TO_BACK : Direction.TO_FORWARD;
//			return collectionUnit().round(timeAsMinutes, direction);
//		case ROUNDING_DOWN:
//			return this.roundingTime.round(timeAsMinutes, Direction.TO_BACK);
//		case ROUNDING_UP:
//			return this.roundingTime.round(timeAsMinutes, Direction.TO_FORWARD);
//
//		default:
//			throw new RuntimeException("invalid case: " + this.rounding);
//		}
//	}

	/**
	 * Correct data.
	 *
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(TimeRoundingSetting oldDomain) {
		this.roundingTime = oldDomain.getRoundingTime();
		this.rounding = oldDomain.getRounding();
	}

	/**
	 * Default data rounding up.
	 */
	public void setDefaultDataRoundingUp() {
		this.roundingTime = Unit.ROUNDING_TIME_1MIN;
		this.rounding = Rounding.ROUNDING_UP;
	}

	/**
	 * Default data rounding down.
	 */
	public void setDefaultDataRoundingDown() {
		this.roundingTime = Unit.ROUNDING_TIME_1MIN;
		this.rounding = Rounding.ROUNDING_DOWN;
	}
	
	
	/**
	 * 未満切捨、以上切上の場合に単位を補正する
	 * @return
	 */
	public Unit collectionUnit() {
		switch (this.roundingTime) {
		case ROUNDING_TIME_15MIN:
			return Unit.valueOf(6);
		case ROUNDING_TIME_30MIN:
			return Unit.valueOf(7);
		case ROUNDING_TIME_1MIN:
		case ROUNDING_TIME_5MIN:
		case ROUNDING_TIME_6MIN:
		case ROUNDING_TIME_10MIN:
		case ROUNDING_TIME_60MIN:
			throw new RuntimeException("invalid case: " + this.roundingTime);
		default:
			throw new RuntimeException("invalid case: " + this.roundingTime);
		}
	}
	
	
	/**
	 * 時間丸め処理
	 * 勤次郎と同様のロジックでの丸め処理
	 * @param timeAsMinutes
	 * @return
	 */
	public int round(int timeAsMinutes) {
		return roundBigDecimal(BigDecimal.valueOf(timeAsMinutes)).intValue();
	}
	
	public BigDecimal roundBigDecimal(BigDecimal timeAsMinutes) {
		//１分単位の場合はそのまま返す
		if(this.roundingTime.equals(Unit.ROUNDING_TIME_1MIN))return timeAsMinutes;
		
		switch (this.rounding) {
		case ROUNDING_DOWN_OVER:
			return this.roundingTime.roundDownOverBigDecimal(timeAsMinutes);
		case ROUNDING_DOWN:
			return this.roundingTime.roundDownBigDecimal(timeAsMinutes);
		case ROUNDING_UP:
			return this.roundingTime.roundUpBigDecimal(timeAsMinutes);

		default:
			throw new RuntimeException("invalid case: " + this.rounding);
		}
	}
	
	/**
	 * 自信を逆丸めに変更する
	 * @return 逆丸めへ変更後の設定
	 */
	public TimeRoundingSetting getReverseRounding() {
		return new TimeRoundingSetting(this.roundingTime, this.rounding.getReverseRounding());
	}
	
	@Override
	public TimeRoundingSetting clone() {
		TimeRoundingSetting cloned = new TimeRoundingSetting();
		try {
			cloned.rounding = Rounding.valueOf(this.rounding.value);
			cloned.roundingTime = Unit.valueOf(this.roundingTime.value);
		}
		catch (Exception e){
			throw new RuntimeException("TimeRoundingSetting clone error.");
		}
		return cloned;
	}
	
}
