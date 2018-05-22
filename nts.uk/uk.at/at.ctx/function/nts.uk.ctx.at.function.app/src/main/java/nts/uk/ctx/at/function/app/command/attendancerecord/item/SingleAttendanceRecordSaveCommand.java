package nts.uk.ctx.at.function.app.command.attendancerecord.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SingleAttendanceRecordSaveCommand {

	/** The export setting code. */
	private long exportSettingCode;

	/** The use atr. */
	private boolean useAtr;
	
	/** The export atr. */
	private int exportAtr;

	/** The column index. */
	private int columnIndex;

	/** The position. */
	private int position;

	/** The time time id. */
	private int timeItemId;

	/** The attribute. */
	private int attribute;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new single attendance record save command.
	 */
	public SingleAttendanceRecordSaveCommand() {
		super();
	}

}
