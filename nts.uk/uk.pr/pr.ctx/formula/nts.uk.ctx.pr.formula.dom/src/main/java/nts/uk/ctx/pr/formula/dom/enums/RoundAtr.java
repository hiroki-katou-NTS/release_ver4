package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum RoundAtr {
	// 0:四捨五入	
	ROUNDED(0),
	// 1:切り上げ
	ROUND_UP(1),
	// 2:切り捨て
	TRUNCATION(2),
	// 3:端数処理なし
	NO_ROUND(3);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "四捨五入";
			break;
		case 1:
			name = "切り上げ";
			break;
		case 2:
			name = "切り捨て";
			break;
		case 3:
			name = "端数処理なし";
			break;
		default:
			name = "四捨五入";
			break;
		}

		return name;
	}
}
