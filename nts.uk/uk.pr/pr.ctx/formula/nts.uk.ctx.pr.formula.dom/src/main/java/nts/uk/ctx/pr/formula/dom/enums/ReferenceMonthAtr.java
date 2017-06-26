package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum ReferenceMonthAtr {
	// 0:当月
	THIS_MONTH(0),
	// 1:1か月前
	ONE_MONTH_AGO(1),
	// 2:2か月前
	TWO_MONTHS_AGO(2),
	// 3:3か月前
	THREE_MONTHS_AGO(3),
	// 4:4か月前
	FOUR_MONTHS_AGO(4),
	// 5:5か月前
	FIVE_MONTHS_AGO(5),
	// 6:6か月前
	SIX_MONTHS_AGO(6),
	// 7:7か月前
	SEVEN_MONTHS_AGO(7),
	// 8:8か月前
	EIGHT_MONTHS_AGO(8),
	// 9:9か月前
	NINE_MONTHS_AGO(9),
	// 10:10か月前
	TEN_MONTHS_AGO(10),
	// 11:11か月前
	ELEVEN_MONTHS_AGO(11),
	// 12:12か月前
	TWELVE_MONTHS_AGO(12);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "当月";
			break;
		case 1:
			name = "1か月前";
			break;
		case 2:
			name = "2か月前";
			break;
		case 3:
			name = "3か月前";
			break;
		case 4:
			name = "4か月前";
			break;
		case 5:
			name = "5か月前";
			break;
		case 6:
			name = "6か月前";
			break;
		case 7:
			name = "7か月前";
			break;
		case 8:
			name = "8か月前";
			break;
		case 9:
			name = "9か月前";
			break;
		case 10:
			name = "10か月前";
			break;
		case 11:
			name = "11か月前";
			break;
		case 12:
			name = "12か月前";
			break;
		default:
			name = "当月";
			break;
		}

		return name;
	}
}
