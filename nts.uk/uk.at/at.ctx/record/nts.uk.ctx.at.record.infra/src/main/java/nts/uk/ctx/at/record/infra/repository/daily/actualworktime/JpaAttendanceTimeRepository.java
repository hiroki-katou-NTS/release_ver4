package nts.uk.ctx.at.record.infra.repository.daily.actualworktime;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTimePK;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceschedule.KrcdtDayWorkScheTime;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWork;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWorkTs;
import nts.uk.ctx.at.record.infra.entity.daily.latetime.KrcdtDayLateTime;
import nts.uk.ctx.at.record.infra.entity.daily.leaveearlytime.KrcdtDayLeaveEarlyTime;
import nts.uk.ctx.at.record.infra.entity.daily.legalworktime.KrcdtDayPrsIncldTime;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimework;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimeworkTs;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@Stateless
public class JpaAttendanceTimeRepository extends JpaRepository implements AttendanceTimeRepository{

	
	@Override
	public void add(AttendanceTimeOfDailyPerformance attendanceTime) {
		/*勤怠時間*/
		this.commandProxy().insert(KrcdtDayAttendanceTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime));
		if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
			/*残業時間*/
			this.commandProxy().insert(KrcdtDayOvertimework.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()));
			/*残業時間帯*/
			this.commandProxy().insert(KrcdtDayOvertimeworkTs.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()));
		}
		for(LeaveEarlyTimeOfDaily leaveEarlyTime : attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getLeaveEarlyTimeOfDaily()) {
			/*早退時間*/
			this.commandProxy().insert(KrcdtDayLeaveEarlyTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),leaveEarlyTime));
		}
		
		/*予定時間*/
		this.commandProxy().insert(KrcdtDayWorkScheTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getWorkScheduleTimeOfDaily()));
		if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()) {
			/*休出時間*/
			this.commandProxy().insert(KrcdtDayHolidyWork.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get()));
			/*休出時間帯*/
			this.commandProxy().insert(KrcdtDayHolidyWorkTs.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get()));
		}
		for(LateTimeOfDaily lateTime : attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getLateTimeOfDaily()) {
			/*遅刻時間*/
			this.commandProxy().insert(KrcdtDayLateTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),lateTime));
		}
		/*所定時間内時間*/
		this.commandProxy().insert(KrcdtDayPrsIncldTime.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily()));
		
	}

	@Override
	public void update(AttendanceTimeOfDailyPerformance attendanceTime) {
		/*勤怠時間*/
		this.commandProxy().update(KrcdtDayAttendanceTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime));
		/*残業時間*/
		this.commandProxy().update(KrcdtDayOvertimework.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()));
		/*残業時間帯*/
		this.commandProxy().update(KrcdtDayOvertimeworkTs.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()));
		for(LeaveEarlyTimeOfDaily leaveEarlyTime : attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getLeaveEarlyTimeOfDaily()) {
			/*早退時間*/
			this.commandProxy().update(KrcdtDayLeaveEarlyTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),leaveEarlyTime));
		}
		
		/*予定時間*/
		this.commandProxy().update(KrcdtDayWorkScheTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getWorkScheduleTimeOfDaily()));
		/*休出時間*/
		this.commandProxy().update(KrcdtDayHolidyWork.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get()));
		/*休出時間帯*/
		this.commandProxy().update(KrcdtDayHolidyWorkTs.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get()));
		for(LateTimeOfDaily lateTime : attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getLateTimeOfDaily()) {
			/*遅刻時間*/
			this.commandProxy().update(KrcdtDayLateTime.create(attendanceTime.getEmployeeId(),attendanceTime.getYmd(),lateTime));
		}
		/*所定時間内時間*/
		this.commandProxy().update(KrcdtDayPrsIncldTime.create(attendanceTime.getEmployeeId(), attendanceTime.getYmd(), attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily()));
		
	}

	@Override
	public Optional<AttendanceTimeOfDailyPerformance> find(String employeeId, GeneralDate ymd) {
		val pk = new KrcdtDayAttendanceTimePK(employeeId,ymd);
		return this.queryProxy().find(pk, KrcdtDayAttendanceTime.class)
				//find(pk,対象テーブル)
				.map(e -> e.toDomain(ymd,e));
	}

	@Override
	public List<AttendanceTimeOfDailyPerformance> findAllOf(String employeeId, List<GeneralDate> ymd) {
		// TODO Auto-generated method stub
		return null;
	}
}
