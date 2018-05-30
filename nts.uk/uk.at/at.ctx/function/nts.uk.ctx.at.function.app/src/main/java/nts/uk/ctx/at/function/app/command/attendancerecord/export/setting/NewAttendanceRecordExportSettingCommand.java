package nts.uk.ctx.at.function.app.command.attendancerecord.export.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.AttendanceRecordAddCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.CalculateAttendanceRecordAddCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.SingleAttendanceRecordAddCommand;

/**
 * @author locph
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewAttendanceRecordExportSettingCommand {
    private AttendanceRecordExportSettingAddCommand cmd;
    private AttendanceRecordAddCommand itemCmd;
}
