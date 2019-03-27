package nts.uk.ctx.at.record.dom.daily.attendanceleavinggate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/** 日別実績の入退門 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLeavingGateOfDaily {

	/** 社員ID: 社員ID */
	private String employeeId;
	
	/** 年月日: 年月日 */
	private GeneralDate ymd;
	
	/** 入退門: 入退門 */
	private List<AttendanceLeavingGate> attendanceLeavingGates;
	
	
	/**
	 * 出退勤前時間の計算
	 * @return
	 */
	public AttendanceTimeOfExistMinus calcBeforeAttendanceTime(Optional<TimeLeavingOfDailyPerformance> attendanceLeave,GoLeavingWorkAtr goLeavingWorkAtr) {
		if(!attendanceLeave.isPresent()) return new AttendanceTimeOfExistMinus(0);
		List<AttendanceTimeOfExistMinus> resultList = new ArrayList<>();
		for(AttendanceLeavingGate attendanceLeavingGate : this.attendanceLeavingGates) {
			if(!attendanceLeavingGate.getAttendance().isPresent()){
				continue;
			}
			
			Optional<WorkStamp> attendanceLeaveWorkStamp = goLeavingWorkAtr.isGO_WORK()?attendanceLeavingGate.getAttendance():
																			   			attendanceLeavingGate.getLeaving();
			if(!attendanceLeaveWorkStamp.isPresent()) continue;
			//入門または退門時間の取得
			TimeWithDayAttr gateTime = attendanceLeaveWorkStamp.get().getTimeWithDay();
			
			if(gateTime==null) continue;

			//出勤または退勤時間の取得
			if(!attendanceLeave.isPresent()) continue;
			if(!attendanceLeave.get().getAttendanceLeavingWork(new WorkNo(attendanceLeavingGate.getWorkNo().v())).isPresent()) continue;
					
			Optional<TimeActualStamp> timeActualstamp = goLeavingWorkAtr.isGO_WORK()?attendanceLeave.get().getAttendanceLeavingWork(new WorkNo(attendanceLeavingGate.getWorkNo().v())).get().getAttendanceStamp():
			 	 																	 attendanceLeave.get().getAttendanceLeavingWork(new WorkNo(attendanceLeavingGate.getWorkNo().v())).get().getLeaveStamp();
			if(!timeActualstamp.isPresent()) continue;
			if(!timeActualstamp.get().getStamp().isPresent()) continue;
			Optional<WorkStamp> workStamp = timeActualstamp.get().getStamp();
			if(!workStamp.isPresent()) continue;
			if(workStamp.get().getTimeWithDay()==null) continue;
			
			//出退勤前時間
			int	attendanceLeavingGateTime = gateTime.valueAsMinutes();
			//出勤時刻
			int stamp = workStamp.get().getTimeWithDay().valueAsMinutes();
			
			//出勤なら「出勤-ログオン」、退勤なら「ログオフ-退勤」
			int calcResult = goLeavingWorkAtr.isGO_WORK()?stamp-attendanceLeavingGateTime:attendanceLeavingGateTime-stamp;
			resultList.add(new AttendanceTimeOfExistMinus(calcResult));
		}
		AttendanceTimeOfExistMinus result = new AttendanceTimeOfExistMinus(resultList.stream().filter(t -> t!=null).mapToInt(t->t.valueAsMinutes()).sum());
		return result!=null?result:new AttendanceTimeOfExistMinus(0);
	}
	
	/**
	 * workNoに一致する入門または退門時間を取得する
	 * @param workNo
	 * @param goLeavingWorkAtr
	 * @return
	 */
	public TimeWithDayAttr getAttendanceLeavingGateTime(WorkNo workNo,GoLeavingWorkAtr goLeavingWorkAtr) {
		TimeWithDayAttr result = new TimeWithDayAttr(0);
		if(goLeavingWorkAtr.isGO_WORK()) {
			if(getAttendanceLeavingGate(workNo).isPresent()) {
				if(getAttendanceLeavingGate(workNo).get().getAttendance().isPresent()) {
					result = getAttendanceLeavingGate(workNo).get().getAttendance().get().getTimeWithDay();
				}
			}
		}else {
			if(getAttendanceLeavingGate(workNo).isPresent()) {
				if(getAttendanceLeavingGate(workNo).get().getLeaving().isPresent()) {
					result = getAttendanceLeavingGate(workNo).get().getLeaving().get().getTimeWithDay();
				}
			}
		}
		return result;
	}
	
	/**
	 * workNoに一致する出退勤を取得する
	 * @param workNo
	 * @return
	 */
	public Optional<AttendanceLeavingGate> getAttendanceLeavingGate(WorkNo workNo) {
		if(this.attendanceLeavingGates != null) {
			return this.attendanceLeavingGates.stream().filter(t -> t.getWorkNo().equals(workNo)).findFirst();
		}
		return Optional.empty();
	}
	
	
}
