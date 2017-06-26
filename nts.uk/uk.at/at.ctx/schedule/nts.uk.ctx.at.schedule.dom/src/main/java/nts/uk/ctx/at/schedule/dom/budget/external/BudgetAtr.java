package nts.uk.ctx.at.schedule.dom.budget.external;

import lombok.AllArgsConstructor;

/*
 * 属性
 */
@AllArgsConstructor
public enum BudgetAtr {

	// 0:時間
	TIME(0, "時間"),
	// 1:人数
	PEOPLE(1, "人数"),
	// 2:金額
	MONEY(2, "金額"),
	// 3:数値
	NUMERICAL(3, "数値"),
	// :単価
	PRICE(4, "単価");

	public final int value;
	public final String name;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "時間";
			break;
		case 1:
			name = "人数";
			break;
		case 2:
			name = "金額";
			break;
		case 3:
			name = "数値";
			break;
		default:
			name = "単価";
			break;
		}
		return name;
	}
}
