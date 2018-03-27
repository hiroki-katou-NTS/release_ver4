package nts.uk.ctx.at.record.ac.attendance;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.pub.attendancetype.AttendanceTypePub;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceTypeAdapter;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceTypeDto;
import nts.uk.ctx.at.record.dom.divergencetime.service.attendance.AttendanceTypeDivergenceAdapterDto;

@Stateless
public class DivergenceAttendanceTypeAdapterImp implements DivergenceAttendanceTypeAdapter {
	@Inject
	private AttendanceTypePub atPub;

	@Override
	public List<DivergenceAttendanceTypeDto> getItemByScreenUseAtr(String companyId, int screenUseAtr) {
		List<DivergenceAttendanceTypeDto> data = atPub.getItemByScreenUseAtr(companyId, screenUseAtr).stream()
				.map(x -> new DivergenceAttendanceTypeDto(x.getAttendanceItemId())).collect(Collectors.toList());
		return data;
	}

}
