package nts.uk.ctx.at.schedule.dom.employeeinfo.employeesort;

import lombok.RequiredArgsConstructor;

/**
 * 並び順
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.勤務予定.社員情報.社員並び替え
 * @author HieuLT
 *
 */
@RequiredArgsConstructor
public enum SortType {
	// 0:昇順
	SORT_ASC(0, "昇順"),

	// 1:降順
	SORT_DESC(1, "降順");


	public final int value;

	public final String name;

	/** The Constant values. */
	private final static SortType[] values = SortType.values();

	public static SortType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SortType val : SortType.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}

}
