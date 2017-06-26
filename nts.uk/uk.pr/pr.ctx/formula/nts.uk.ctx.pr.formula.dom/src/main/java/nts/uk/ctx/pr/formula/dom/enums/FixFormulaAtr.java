package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum FixFormulaAtr {
	// 0:固定金額を利用する
	USE_FIXED_AMOUNT(0),
	// 1:計算式を利用する
	USE_CALCULATION_FORMULAS(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "固定金額を利用する";
			break;
		case 1:
			name = "計算式を利用する";
			break;
		default:
			name = "固定金額を利用する";
			break;
		}
		return name;
	}
}
