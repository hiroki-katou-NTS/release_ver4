package nts.uk.ctx.at.shared.dom.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceItem extends AggregateRoot {
	/* 会社ID */
	private String companyId;
	/* 勤怠項目ID */
	private int attendanceId;
	/* 勤怠項目名称 */
	private AttendanceName attendanceName;
	/* 表示番号 */
	private int dislayNumber;
	/* 使用区分 */
	private UseSetting useAtr;
	/* 勤怠項目属性 */
	private AttendanceAtr attendanceAtr;

	public static AttendanceItem createSimpleFromJavaType(String companyId, int attendanceId, String attendanceName,
			int dislayNumber, int useAtr, int attendanceAtr) {
		return new AttendanceItem(companyId, attendanceId, new AttendanceName(attendanceName), dislayNumber,
				EnumAdaptor.valueOf(useAtr, UseSetting.class), EnumAdaptor.valueOf(attendanceAtr, AttendanceAtr.class));
	}

}
