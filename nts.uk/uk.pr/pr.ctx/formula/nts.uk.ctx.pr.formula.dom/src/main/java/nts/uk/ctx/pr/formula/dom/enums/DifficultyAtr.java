package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum DifficultyAtr {
//	0:簡単設定
	EASY_SETTING(0),
//	1:詳細設定
	ADVANCED_SETTING(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "簡単設定";
			break;
		case 1:
			name = "詳細設定";
			break;
		default:
			name = "簡単設定";
			break;
		}
		return name;
	}
}
