package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DivideValueSet {
//	0:固定値
	FIXED_VALUE(0),
//	1:基準日数
	REFERENCE_DAYS(1),
//	2:要勤務日数
	WORKING_DAYS(2),
//	3:出勤日数
	ATTENDANCE_DAYS(3),
//	4:出勤日数＋有給使用数
	ATTENDANCE_DAYS_PLUS_PAID_USAGE(4),
//	5:基準日数×基準時間
	REFERENCE_DAYS_MULTI_REFERENCE_TIME(5),
//	6:要勤務日数×基準時間
	WORKING_DAYS_MULTI_REFERENCE_TIME(6),
//	7:出勤日数×基準時間
	ATTENDANCE_DAYS_PLUS_REFERENCE_TIME(7),
//	8:（出勤日数＋有給使用数）×基準時間
	FOURTH_MULTI_REFERENCE_TIME(8),
//	9:出勤時間
	ATTENDANCE_TIME(9);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "固定値";
			break;
		case 1:
			name = "基準日数";
			break;
		case 2:
			name = "要勤務日数";
			break;
		case 3:
			name = "出勤日数";
			break;
		case 4:
			name = "出勤日数＋有給使用数";
			break;
		case 5:
			name = "基準日数×基準時間";
			break;
		case 6:
			name = "要勤務日数×基準時間";
			break;
		case 7:
			name = "出勤日数×基準時間";
			break;
		case 8:
			name = "（出勤日数＋有給使用数）×基準時間";
			break;
		case 9:
			name = "出勤時間";
			break;
		default:
			name = "固定値";
			break;
		}

		return name;
	}
}
