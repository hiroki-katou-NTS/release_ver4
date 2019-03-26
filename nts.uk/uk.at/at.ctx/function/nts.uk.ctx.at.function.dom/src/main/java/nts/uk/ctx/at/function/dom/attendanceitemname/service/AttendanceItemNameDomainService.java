package nts.uk.ctx.at.function.dom.attendanceitemname.service;

import java.util.List;

import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemname.AttendanceItemName;

public interface AttendanceItemNameDomainService {
	
	/**
	 * 勤怠項目に対応する名称を生成する
	 * @param dailyAttendanceItemIds
	 * @param typeOfAttendanceItem
	 * @return AttendanceItemName
	 */
	List<AttendanceItemName> getNameOfAttendanceItem(List<Integer> dailyAttendanceItemIds , int typeOfAttendanceItem);
	
	List<AttendanceItemName> getNameOfAttendanceItem(TypeOfItem typeOfAttendanceItem);

}
