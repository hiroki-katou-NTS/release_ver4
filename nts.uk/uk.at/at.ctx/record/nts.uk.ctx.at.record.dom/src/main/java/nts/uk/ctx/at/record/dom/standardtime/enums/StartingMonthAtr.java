package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum StartingMonthAtr {

	// 0: 1��
	JANUARY(0),
	// 1: 2��
	FEBRUARY(1),
	// 2: 3��
	MARCH(2),
	// 3: 4��
	APRIL(3),
	// 4: 5��
	MAY(4),
	// 5: 6��
	JUNE(5),
	// 6: 7��
	JULY(6),
	// 7: 8��
	AUGUST(7),
	// 8: 9��
	SEPTEMBER(8),
	// 9: 10��
	OCTOBER(9),
	// 10: 11��
	NOVEMBER(10),
	// 11: 12��
	DECEMBER(11);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "1��";
			break;
		case 1:
			name = "2��";
			break;
		case 2:
			name = "3��";
			break;
		case 3:
			name = "4��";
			break;
		case 4:
			name = "5��";
			break;
		case 5:
			name = "6��";
			break;
		case 6:
			name = "7��";
			break;
		case 7:
			name = "8��";
			break;
		case 8:
			name = "9��";
			break;
		case 9:
			name = "10��";
			break;
		case 10:
			name = "11��";
			break;
		case 11:
			name = "12��";
			break;
		default:
			name = "1��";
			break;
		}
		return name;
	}

}
