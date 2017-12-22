package nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto.AttendanceLeavingGateOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeSheetDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeStampDto;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;

@Stateless
public class AttendanceLeavingGateOfDailyFinder extends FinderFacade {

	@Inject
	private AttendanceLeavingGateOfDailyRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public AttendanceLeavingGateOfDailyDto find(String employeeId, GeneralDate baseDate) {
		AttendanceLeavingGateOfDailyDto dto = new AttendanceLeavingGateOfDailyDto();
		AttendanceLeavingGateOfDaily domain = this.repo.find(employeeId, baseDate).orElse(null);
		if (domain != null) {
			dto.setAttendanceLeavingGateTime(ConvertHelper.mapTo(domain.getAttendanceLeavingGates(),
					(c) -> new TimeSheetDto(c.getWorkNo().v(),
							new TimeStampDto(c.getAttendance().getTimeWithDay().valueAsMinutes(),
									c.getAttendance().getAfterRoundingTime().valueAsMinutes(),
									c.getAttendance().getLocationCode().v(),
									c.getAttendance().getStampSourceInfo().value),
							new TimeStampDto(c.getAttendance().getTimeWithDay().valueAsMinutes(),
									c.getAttendance().getAfterRoundingTime().valueAsMinutes(),
									c.getAttendance().getLocationCode().v(),
									c.getAttendance().getStampSourceInfo().value)

					)));
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
		}
		return dto;
	}

}
