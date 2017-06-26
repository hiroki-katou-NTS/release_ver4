package nts.uk.ctx.at.shared.app.find.attendance;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceItem;

@Value
public class AttendanceItemDto {
	/* 会社ID */
	private String companyId;
	/* 勤怠項目ID */
	private int attendanceItemId;
	/* 勤怠項目名称 */
	private String attendanceItemName;
	/* 内部ID */
	private int dislayNumber;
	/* 使用区分 */
	private int useAtr;
	/* 勤怠項目属性 */
	private int attendanceAtr;

	public static AttendanceItemDto fromDomain(AttendanceItem domain) {
		return new AttendanceItemDto(domain.getCompanyId(), domain.getAttendanceId(), domain.getAttendanceName().v(),
				domain.getDislayNumber(), domain.getUseAtr().value, domain.getAttendanceAtr().value);
	}

}
