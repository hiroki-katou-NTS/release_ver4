package nts.uk.ctx.at.function.dom.attendanceitemname.service;

import java.util.List;
import java.util.stream.Collectors;

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
				EnumAdaptor.valueOf(typeOfAttendanceItem, TypeOfItem.class)).stream().map(x -> {
					AttendanceItemName dto = new AttendanceItemName(x.getAttendanceItemId(), x.getAttendanceItemName(),
							x.getAttendanceItemDisplayNumber(), x.getUserCanUpdateAtr(), x.getNameLineFeedPosition(),
							x.getTypeOfAttendanceItem(), x.getFrameCategory());
					return dto;
				}).collect(Collectors.toList());
	}

	@Override
	public List<AttendanceItemName> getNameOfAttendanceItem(TypeOfItem typeOfAttendanceItem) {
		return attendanceItemNameService.getNameOfAttendanceItem(typeOfAttendanceItem).stream().map(x -> {
					AttendanceItemName dto = new AttendanceItemName(x.getAttendanceItemId(), x.getAttendanceItemName(),
							x.getAttendanceItemDisplayNumber(), x.getUserCanUpdateAtr(), x.getNameLineFeedPosition(),
							x.getTypeOfAttendanceItem(), x.getFrameCategory());
					return dto;
				}).collect(Collectors.toList());
	}

}
