package nts.uk.ctx.at.record.dom.standardtime.enums;

/**
 * 
 * @author nampt
 *
 */
public enum TimeOverLimitType {
	
	/*
	 * ３６協定超過上限回数
	 */
	// 0: 0回
	ZERO_TIMES(0),
	// 1: 1回
	ONCE(1),
	// 2: 2回
	TWICE(2),
	// 3: 3回
	THREE_TIMES(3),
	// 4: 4回
	FOUR_TIMES(4),
	// 5: 5回
	FIVE_TIMES(5),
	// 6: 6回
	SIX_TIMES(6),
	// 7: 7回
	SEVEN_TIMES(7),
	// 8: 8回
	EIGHT_TIMES(8),
	// 9: 9回
	NINE_TIMES(9),
	// 10: 10回
	TEN_TIMES(10),
	// 11: 11回
	ELEVEN_TIMES(11),
	// 12: 12回
	TWELVE_TIMES(12);

	public final int value;
	
	private TimeOverLimitType(int type) {
		this.value = type;
	}
	public static TimeOverLimitType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (TimeOverLimitType val : TimeOverLimitType.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
