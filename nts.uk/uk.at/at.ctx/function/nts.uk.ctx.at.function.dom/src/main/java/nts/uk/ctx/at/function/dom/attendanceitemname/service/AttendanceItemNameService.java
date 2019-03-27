package nts.uk.ctx.at.function.dom.attendanceitemname.service;

import java.util.List;

import nts.uk.ctx.at.function.dom.attendanceitemframelinking.AttendanceItemLinking;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;

public interface AttendanceItemNameService {
	/**
	 *  勤怠項目に対応する名称を生成する
	 * @param dailyAttendanceItemIds 勤怠項目ID
	 * @param type 勤怠項目の種類
	 * @return
	 */
	List<AttItemName> getNameOfAttendanceItem(List<Integer> attendanceItemIds, TypeOfItem type);
	
	List<AttItemName> getNameOfAttendanceItem(TypeOfItem type);

	List<AttItemName> getNameOfAttendanceItem(TypeOfItem type, List<AttItemName> attendanceItems);

	List<AttItemName> getNameOfAttendanceItem(List<AttItemName> attendanceItems,
			List<AttendanceItemLinking> attendanceItemAndFrameNos);
}
