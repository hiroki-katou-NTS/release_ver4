package nts.uk.ctx.at.record.ac.dailyattendanceitemname;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.pub.dailyattendanceitemname.DailyAttendanceItemPub;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapterDto;

@Stateless
public class DailyAttendanceItemRecFinder implements DailyAttendanceItemNameAdapter {

	@Inject
	private DailyAttendanceItemPub dailyAttendanceItemPub;

	@Override
	public List<DailyAttendanceItemNameAdapterDto> getDailyAttendanceItemName(List<Integer> dailyAttendanceItemIds) {
		return this.dailyAttendanceItemPub.getDailyAttendanceItemName(dailyAttendanceItemIds).stream().map(f -> {
			return new DailyAttendanceItemNameAdapterDto(f.getAttendanceItemId(), f.getAttendanceItemName(),
					f.getAttendanceItemDisplayNumber());
		}).collect(Collectors.toList());
	}

	@Override
	public Map<Integer, String> getDailyAttendanceItemNameAsMapName(List<Integer> itemIds) {
		return this.dailyAttendanceItemPub.getDailyAttendanceItemName(itemIds).stream()
				.collect(Collectors.toMap(c -> c.getAttendanceItemId(), c -> c.getAttendanceItemName()));
	}

}
