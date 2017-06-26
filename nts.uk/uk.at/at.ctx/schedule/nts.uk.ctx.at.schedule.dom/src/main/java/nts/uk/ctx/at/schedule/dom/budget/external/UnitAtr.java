package nts.uk.ctx.at.schedule.dom.budget.external;

import lombok.AllArgsConstructor;

/** 単位区分 */
@AllArgsConstructor
public enum UnitAtr {
	// 0:日別
	DAILY(0, "日別"),
	// 1:時間帯別
	BYTIMEZONE(1, "時間帯別");

	public final int value;
	public final String name;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "日別";
			break;
		default:
			name = "時間帯別";
			break;
		}
		return name;
	}

}
