package nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname;

import lombok.Data;

@Data
public class AttItemNameImport {

	private int attendanceItemId;

	private String attendanceItemName;

	private int attendanceItemDisplayNumber;
	
	private int typeOfAttendanceItem;
	
	private int frameCategory;

	public AttItemNameImport() {
		super();
	}	
}
