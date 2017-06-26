package nts.uk.ctx.pr.core.dom.enums;

import lombok.AllArgsConstructor;

/** カテゴリ区分 */
@AllArgsConstructor
public enum CategoryAtr {
	// 0:支給
	PAYMENT(0,"支給"),
	// 1:控除
	DEDUCTION(1,"控除"),
	// 2:勤怠
	PERSONAL_TIME(2,"勤怠"),
	// 3:記事
	ARTICLES(3,"記事"),
	// 9:その他
	OTHER(9,"その他"),

	/**
	 * 印字しない
	 */
	DO_NOT_PRINT(-1,"印字しない");

	public final int value;
	public final String name;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "支給";
			break;
		case 1:
			name = "控除";
			break;
		case 2:
			name = "勤怠";
			break;
		case 3:
			name = "記事";
			break;
		default:
			name = "その他";
			break;
		}

		return name;
	}
}
