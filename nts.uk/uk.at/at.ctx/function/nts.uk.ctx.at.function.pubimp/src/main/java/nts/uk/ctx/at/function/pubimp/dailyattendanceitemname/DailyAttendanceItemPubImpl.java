package nts.uk.ctx.at.function.pubimp.dailyattendanceitemname;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.function.pub.dailyattendanceitemname.DailyAttendanceItemPub;
import nts.uk.ctx.at.function.pub.dailyattendanceitemname.DailyAttendanceItemPubDto;

@Stateless
public class DailyAttendanceItemPubImpl implements DailyAttendanceItemPub{
	
	@Inject
	private DailyAttendanceItemNameDomainService dailyAttendanceItemNameDomainService;

	@Override
	public List<DailyAttendanceItemPubDto> getDailyAttendanceItemName(List<Integer> dailyAttendanceItemIds) {
		return this.dailyAttendanceItemNameDomainService.getNameOfDailyAttendanceItem(dailyAttendanceItemIds).stream().map(f -> {
			return new DailyAttendanceItemPubDto(f.getAttendanceItemId(), f.getAttendanceItemName(), f.getAttendanceItemDisplayNumber());
		}).collect(Collectors.toList());
	}

}
