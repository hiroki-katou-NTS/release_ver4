package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum NumberOfTimeOverLimitType {
	// 0: 0��
	ZERO_TIMES(0),
	// 1: 1��
	ONCE(1),
	// 2: 2��
	TWICE(2),
	// 3: 3��
	THREE_TIMES(3),
	// 4: 4��
	FOUR_TIMES(4),
	// 5: 5��
	FIVE_TIMES(5),
	// 6: 6��
	SIX_TIMES(6),
	// 7: 7��
	SEVEN_TIMES(7),
	// 8: 8��
	EIGHT_TIMES(8),
	// 9: 9��
	NINE_TIMES(9),
	// 10: 10��
	TEN_TIMES(10),
	// 11: 11��
	ELEVEN_TIMES(11),
	// 12: 12��
	TWELVE_TIMES(12);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "0��";
			break;
		case 1:
			name = "1��";
			break;
		case 2:
			name = "2��";
			break;
		case 3:
			name = "3��";
			break;
		case 4:
			name = "4��";
			break;
		case 5:
			name = "5��";
			break;
		case 6:
			name = "6��";
			break;
		case 7:
			name = "7��";
			break;
		case 8:
			name = "8��";
			break;
		case 9:
			name = "9��";
			break;
		case 10:
			name = "10��";
			break;
		case 11:
			name = "11��";
			break;
		case 12:
			name = "12��";
			break;
		default:
			name = "6��";
			break;
		}
		return name;
	}
}
