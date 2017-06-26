package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum EasyFormulaTypeAtr {
//	0:計算式1
	CALCULATION_FORMULA_1(0),
//	1:計算式2
	CALCULATION_FORMULA_2(1),
//	2:計算式3
	CALCULATION_FORMULA_3(2);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "計算式1";
			break;
		case 1:
			name = "計算式2";
			break;
		case 2:
			name = "計算式3";
			break;
		default:
			name = "計算式1";
			break;
		}

		return name;
	}
}
