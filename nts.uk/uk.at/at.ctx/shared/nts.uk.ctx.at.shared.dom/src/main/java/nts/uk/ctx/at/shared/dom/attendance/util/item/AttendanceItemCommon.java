package nts.uk.ctx.at.shared.dom.attendance.util.item;

import java.io.Serializable;

public abstract class AttendanceItemCommon implements ConvertibleAttendanceItem, Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	private boolean haveData = false;

	@Override
	public boolean isHaveData() {
		return this.haveData;
	}

	@Override
	public void exsistData() {
		this.haveData = true;
	}
}
