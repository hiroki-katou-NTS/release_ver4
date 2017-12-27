package nts.uk.ctx.at.record.infra.repository.daily.workrecord;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.repo.AttendanceTimeByWorkOfDailyRepository;

@Stateless
public class JpaAttendanceTimeByWorkOfDailyRepoImpl extends JpaRepository implements AttendanceTimeByWorkOfDailyRepository {

	@Override
	public void add(AttendanceTimeByWorkOfDaily attendanceTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(AttendanceTimeByWorkOfDaily attendanceTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<AttendanceTimeByWorkOfDaily> find(String employeeId, GeneralDate ymd) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<AttendanceTimeByWorkOfDaily> findAllOf(String employeeId, List<GeneralDate> ymd) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

}
