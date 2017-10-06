package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;

/**
 * 日別実績の出退勤
 * @author keisuke_hoshina
 *
 */
@Value
public class AttendanceLeavingWorkOfDaily {

	private String syainID;
	private WorkTimes workTimes;
	private List<AttendanceLeavingWork> attendanceLeavingWorkTime;
	
	public AttendanceLeavingWork getAttendanceLeavingWork(int workNo) {
		
		List<AttendanceLeavingWork> attendanceLeavingWorkList = this.attendanceLeavingWorkTime.stream()
				.filter(ts -> ts.getWorkNo() == workNo).collect(Collectors.toList());
		if(attendanceLeavingWorkList.size()>1) {
			throw new RuntimeException("Exist duplicate workNo : " + workNo);
		}	
		return attendanceLeavingWorkList.get(0);		
	}
	
	/**
	 * 退勤を返す　　　（勤務回数が2回目の場合は2回目の退勤を返す）
	 * @return
	 */
	public WorkStampWithActualStamp getLeavingWork() {
		AttendanceLeavingWork targetAttendanceLeavingWorkTime = this.getAttendanceLeavingWork(this.workTimes.v());
		return targetAttendanceLeavingWorkTime.getLeaveWork();
	}
	
	/**
	 * 勤務回数が1回か判定する
	 * @return　true:1回　false:2回目
	 */
	public boolean isFirstTimeWork() {
		if(this.workTimes.v() == 1) {
			return true;
		}
		return false;
	}
	
}
