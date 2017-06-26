package nts.uk.ctx.at.shared.dom.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

@Getter
@AllArgsConstructor
public class AttendanceType {
	/*会社ID*/
	private String companyId;
	/*勤怠項目ID*/
	private int attendanceItemId;
	/*勤怠項目名称*/
	private AttendanceItemType attendanceItemType;
	
	public static AttendanceType createSimpleFromJavaType(String companyId,
						int attendanceItemId,
						int attendanceItemType){
		return new AttendanceType(companyId,
				attendanceItemId,
				EnumAdaptor.valueOf(attendanceItemType,AttendanceItemType.class));
	}
}
