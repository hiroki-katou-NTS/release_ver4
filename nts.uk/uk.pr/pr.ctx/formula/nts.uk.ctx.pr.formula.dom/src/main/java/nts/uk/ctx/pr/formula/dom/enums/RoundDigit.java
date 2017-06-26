package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum RoundDigit {

//	0:小数点第1位(円単位で丸め)
	FIRST_DECIMAL_PLACE(0),
//	1:1桁(10円単位で丸め)
	ONE_DIGIT(1),
//	2:2桁(100円単位で丸め）
	TWO_DIGITS(2),
//	3:3桁(1000円単位で丸め)
	THREE_DIGITS(3);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "小数点第1位(円単位で丸め)";
			break;
		case 1:
			name = "1桁(10円単位で丸め)";
			break;
		case 2:
			name = "2桁(100円単位で丸め）";
			break;
		case 3:
			name = "3桁(1000円単位で丸め)";
			break;
		default:
			name = "小数点第1位(円単位で丸め)";
			break;
		}

		return name;
	}
	
}
