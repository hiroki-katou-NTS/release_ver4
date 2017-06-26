package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum AdjustmentAtr {
//	0:調整しない
	DO_NOT_ADJUST(0),
//	1:プラス調整
	PLUS_ADJUSTMENT(1),
//	2:マイナス調整　
	MINUS_ADJUSTMENT(2),
//	3:プラスマイナス
	PLUS_OR_MINUS_REVERSAL(3);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "調整しない";
			break;
		case 1:
			name = "プラス調整";
			break;
		case 2:
			name = "マイナス調整";
			break;
		case 3:
			name = "プラスマイナス";
			break;
		default:
			name = "調整しない";
			break;
		}

		return name;
	}
}
