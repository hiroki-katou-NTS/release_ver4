package nts.uk.ctx.at.function.dom.adapter;

import lombok.Getter;

/**
 * 
 * @author nampt
 *
 */
@Getter
public class DailyAttendanceItemAdapterDto {
	
	private String companyId;

	/* 勤怠項目ID */
	private int attendanceItemId;

	/* 勤怠項目名称 */
	private String attendanceName;

	/* 表示番号 */
	private int displayNumber;

	/* 使用区分 */
	private int userCanUpdateAtr;

	/* 勤怠項目属性 */
	private int dailyAttendanceAtr;

	/* ユーザーが値を変更できる */
	private int nameLineFeedPosition;
	
	private String displayName;

	public DailyAttendanceItemAdapterDto(String companyId, int attendanceItemId, String attendanceName,
			int displayNumber, int userCanUpdateAtr, int dailyAttendanceAtr, int nameLineFeedPosition, String displayName) {
		super();
		this.companyId = companyId;
		this.attendanceItemId = attendanceItemId;
		this.attendanceName = attendanceName;
		this.displayNumber = displayNumber;
		this.userCanUpdateAtr = userCanUpdateAtr;
		this.dailyAttendanceAtr = dailyAttendanceAtr;
		this.nameLineFeedPosition = nameLineFeedPosition;
		this.displayName = displayName;
	}

}
