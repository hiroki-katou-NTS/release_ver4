package nts.uk.ctx.at.function.dom.attendanceitemname.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemname.AttendanceItemName;

/**
 * 
 * @author nampt set name of attendance item with 2 param
 */
@Stateless
public class AttendanceItemNameDomainServiceImpl implements AttendanceItemNameDomainService {

	@Inject
	private AttendanceItemNameService attendanceItemNameService;

	@Override
	public List<AttendanceItemName> getNameOfAttendanceItem(List<Integer> dailyAttendanceItemIds,
			int typeOfAttendanceItem) {
		return attendanceItemNameService.getNameOfAttendanceItem(dailyAttendanceItemIds,
				EnumAdaptor.valueOf(typeOfAttendanceItem, TypeOfItem.class));
	}

}
