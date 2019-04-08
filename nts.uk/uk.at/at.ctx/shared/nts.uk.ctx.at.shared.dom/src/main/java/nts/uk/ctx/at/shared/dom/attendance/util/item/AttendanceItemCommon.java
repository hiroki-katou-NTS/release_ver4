package nts.uk.ctx.at.shared.dom.attendance.util.item;

public abstract class AttendanceItemCommon implements ConvertibleAttendanceItem {

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
