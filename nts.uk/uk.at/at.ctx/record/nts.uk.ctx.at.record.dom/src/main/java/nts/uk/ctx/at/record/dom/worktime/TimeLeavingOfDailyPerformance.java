package nts.uk.ctx.at.record.dom.worktime;

//import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.JustCorrectionAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

/**
 * 
 * @author nampt 日別実績の出退勤 - root
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class TimeLeavingOfDailyPerformance extends AggregateRoot {

	private String employeeId;

	private WorkTimes workTimes;

	// 1 ~ 2
	private List<TimeLeavingWork> timeLeavingWorks;

	private GeneralDate ymd;
	
	public TimeLeavingOfDailyPerformance(String employeeId, WorkTimes workTimes, List<TimeLeavingWork> timeLeavingWorks,
			GeneralDate ymd) {
		super();
		this.employeeId = employeeId;
		this.workTimes = workTimes;
		this.timeLeavingWorks = timeLeavingWorks;
		this.ymd = ymd;
	}

	public void setProperty(String employeeId, WorkTimes workTimes, List<TimeLeavingWork> timeLeavingWorks,
			GeneralDate ymd) {
		this.employeeId = employeeId;
		this.workTimes = workTimes;
		this.timeLeavingWorks = timeLeavingWorks;
		this.ymd = ymd;

	}
	
	/**
	 * 指定した勤怠Noのデータを取得する
	 * @param workNo 勤怠No
	 * @return　出退勤クラス
	 */
	public Optional<TimeLeavingWork> getAttendanceLeavingWork(WorkNo workNo) {
		return this.timeLeavingWorks.stream().filter(ts -> ts.getWorkNo().v().intValue() == workNo.v().intValue()).findFirst();
	}
	
	/** <<Event>> 実績の出退勤が変更されたイベントを発行する　*/
	public void timeLeaveChangedByNo(int no) {
		timeLeavingWorks.stream().filter(tl -> tl.getWorkNo().v() == no).findFirst().ifPresent(tl -> {
			TimeLeaveChangeEvent.builder().employeeId(employeeId).targetDate(ymd).timeLeave(Arrays.asList(tl)).build().toBePublished();
		});
	}
	
	/** <<Event>> 実績の出退勤が変更されたイベントを発行する　*/
	public void timeLeavesChanged() {
		TimeLeaveChangeEvent.builder().employeeId(employeeId).targetDate(ymd).timeLeave(timeLeavingWorks).build().toBePublished();
	}
	
	/**
	 * 打刻漏れであるか判定する
	 * @return　打刻漏れである
	 */
	public boolean isLeakageStamp(){
		for(TimeLeavingWork timeLeavingWork:this.timeLeavingWorks) {
			//打刻漏れを起こしている(計算できる状態でない)
			if(!timeLeavingWork.checkLeakageStamp())
				return true;
		}
		return false;
	}
	
	/**
	 * 指定した勤怠Noのデータを取得する
	 * @param workNo 勤怠No
	 * @return　出退勤クラス
	 */
	public Optional<TimeLeavingWork> getAttendanceLeavingWork(int workNo) {
		return this.timeLeavingWorks.stream().filter(ts -> ts.getWorkNo().v() == workNo).findFirst();
	}
	
	public Optional<TimeLeavingWork> forceAccessAttendanceLeavingWork(int workNo) {
		Optional<TimeLeavingWork> tlw = this.timeLeavingWorks.stream().filter(ts -> ts.getWorkNo().v() == workNo).findFirst();
		if(tlw.isPresent()){
			return tlw;
		}
		TimeLeavingWork ntlw = new TimeLeavingWork(new WorkNo(workNo), new TimeActualStamp(), new TimeActualStamp());
		this.timeLeavingWorks.add(ntlw);
		return Optional.of(ntlw);
	}
	
	/**
	 * 退勤を返す　　　（勤務回数が2回目の場合は2回目の退勤を返す）
	 * @return
	 */
	public Optional<TimeActualStamp> getLeavingWork() {
		Optional<TimeLeavingWork> targetAttendanceLeavingWorkTime = this.getAttendanceLeavingWork(new WorkNo(this.workTimes.v()));
		return (targetAttendanceLeavingWorkTime.isPresent())?targetAttendanceLeavingWorkTime.get().getAttendanceStamp():Optional.empty();
	}
	
	/**
	 * 勤務回数が1回か判定する
	 * @return　true:1回　false:2回目
	 */
	public boolean isFirstTimeWork() {
		return (this.workTimes.v()) == 1;
	}
	
	

	/**
	 * ジャスト遅刻、ジャスト早退の計算区分を見て時刻調整
	 * @param isJustTimeLateAttendance ジャスト遅刻を計算するか 
	 * @param isJustEarlyLeave　ジャスト早退を計算するか
	 * @return 調整後の日別実績の出退勤クラス
	 */
	public TimeLeavingOfDailyPerformance calcJustTime(boolean isJustTimeLateAttendance,boolean isJustEarlyLeave,JustCorrectionAtr justCorrectionAtr) {
		if(justCorrectionAtr.isNotUse()) return this;
		List<TimeLeavingWork> newAttendanceLeave = new ArrayList<>();
		for(TimeLeavingWork attendanceLeave : timeLeavingWorks) {
			newAttendanceLeave.add(attendanceLeave.correctJustTime(isJustTimeLateAttendance, isJustEarlyLeave));
		}
		
		return new TimeLeavingOfDailyPerformance(this.getEmployeeId(), 
												 this.getWorkTimes(), 
												 newAttendanceLeave, 
												 this.getYmd());
	}

	/**
	 * 出退勤時刻と渡された範囲時間の重複していない部分の取得
	 * @param timeSpan　範囲時間
	 * @return 重複していない時間
	 */
	public List<TimeSpanForCalc> getNotDuplicateSpan(TimeSpanForCalc timeSpan) {
		if(timeSpan == null) return Collections.emptyList();
		List<TimeSpanForCalc> returnList = new ArrayList<>();
		for(TimeLeavingWork tlw : this.timeLeavingWorks ) {
			//notDuplicatedRange = tlw.getTimespan().getNotDuplicationWith(notDuplicatedRange.get());
			returnList.addAll(timeSpan.getNotDuplicationWith(tlw.getTimespan()));
		}
		return returnList;
	}
	
	/**
	 * 打刻順序不正かどうかチェックする
	 * @return 打刻順序不正である
	 */
	public boolean isReverseOrder() {
		for(TimeLeavingWork tl : this.getTimeLeavingWorks()) {
			if(tl.isReverseOrder()) {
				return true;
			}
		}
		return false;
	}
}
