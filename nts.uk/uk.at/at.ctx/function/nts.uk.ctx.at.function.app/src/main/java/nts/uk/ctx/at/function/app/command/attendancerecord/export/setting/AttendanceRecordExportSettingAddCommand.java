package nts.uk.ctx.at.function.app.command.attendancerecord.export.setting;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class AttendanceRecordExportSettingAddCommand.
 */
@Getter
@Setter
@AllArgsConstructor
public class AttendanceRecordExportSettingAddCommand {

	/** The code. */
	String code;

	/** The name. */
	String name;

	/** The seal stamp. */
	List<String> sealStamp;
	
	/** The seal use atr. */
	Boolean sealUseAtr;

	/** The name use atr. */
	Integer nameUseAtr;
	
	/** The once update. */
	boolean onceUpdate;
	
	/** The font size. */
	Integer exportFontSize;
	
	/** The monthly display. */
	Integer monthlyDisplay;
	
	/** The item sel type. */
	Integer itemSelType;
	
	/**
	 * Instantiates a new attendance record export setting add command.
	 */
	public AttendanceRecordExportSettingAddCommand() {
		super();
	}
}
