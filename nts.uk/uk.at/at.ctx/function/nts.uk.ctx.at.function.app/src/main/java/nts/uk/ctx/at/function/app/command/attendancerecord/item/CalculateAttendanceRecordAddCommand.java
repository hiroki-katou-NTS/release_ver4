package nts.uk.ctx.at.function.app.command.attendancerecord.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author locph
 */
@AllArgsConstructor
@Getter
@Setter
public class CalculateAttendanceRecordAddCommand {
	/** The export setting code. */
	private int exportSettingCode;

	/** The use atr. */
	private boolean useAtr;

	/** The export atr. */
	private int exportAtr;

	/** The column index. */
	private int columnIndex;

	/** The position. */
	private int position;

	/** The time items. */
	private List<TimeItemDto> timeItems;

	/** The attribute. */
	private int attribute;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new calculate attendance record save command.
	 */
	public CalculateAttendanceRecordAddCommand() {
		super();
	}
	
	
}
