package nts.uk.ctx.at.record.ac.attendance;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.pub.dailyattendanceitemname.DailyAttendanceItemPub;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceNameAdapter;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceNameDto;

@Stateless
public class DivergenceAttendanceNameAdapterImpl implements DivergenceAttendanceNameAdapter {

	@Inject
	private DailyAttendanceItemPub itemPub;

	@Override
	public List<DivergenceAttendanceNameDto> getDailyAttendanceItemName(List<Integer> dailyAttendanceItemIds) {
		List<DivergenceAttendanceNameDto> data = itemPub.getDailyAttendanceItemName(dailyAttendanceItemIds).stream()
				.map(x -> new DivergenceAttendanceNameDto(x.getAttendanceItemId(), x.getAttendanceItemName(),
						x.getAttendanceItemDisplayNumber()))
				.collect(Collectors.toList());
		return data;
	}

}
