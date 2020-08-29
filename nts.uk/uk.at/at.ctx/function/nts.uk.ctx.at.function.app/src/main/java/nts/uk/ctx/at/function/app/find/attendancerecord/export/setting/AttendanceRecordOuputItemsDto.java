package nts.uk.ctx.at.function.app.find.attendancerecord.export.setting;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.function.dom.attendancerecord.export.AttendanceRecordOuputItems;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.AttendanceRecordExportSetting;

/**
 * The Class AttendanceRecordOuputItemsDto.
 */
@Data
public class AttendanceRecordOuputItemsDto implements AttendanceRecordOuputItems.MementoSetter {
	
	/**
	 *	会社ID
	 */
	private String cid;
	
	/**
	 * 	社員ID
	 */
	private String employeeId;

	/**
	 * 項目選択種類
	 */
	private int itemSelectionType;
	
	/**
	 * 	出力項目: 出勤簿の出力項目設定 (List)
	 */
	private List<AttendanceRecordExportSettingDto> attendanceRecordExportSettings;
 
	@Override
	public void setAttendanceRecordExportSettings(List<AttendanceRecordExportSetting> attendanceRecordExportSettings) {

	}
	
}
