package nts.uk.ctx.at.record.dom.worktime;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
//import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 出退勤
 *
 */
@Getter
@NoArgsConstructor
public class TimeLeavingWork extends DomainObject{
	
	/*
	 * 勤務NO
	 */
	private WorkNo workNo;
	
	@Setter
	private Optional<TimeActualStamp> attendanceStamp;

	@Setter
	private Optional<TimeActualStamp> leaveStamp;
	
	private TimeSpanForCalc timespan;
	


	
	public TimeLeavingWork(WorkNo workNo, TimeActualStamp attendanceStamp, TimeActualStamp leaveStamp) {
		super();
		this.workNo = workNo;
		this.attendanceStamp = Optional.ofNullable(attendanceStamp);
		this.leaveStamp = Optional.ofNullable(leaveStamp);
		
		this.timespan = this.craeteTimeSpan();
	}
	
	/**
	 * PCログオンログオフを打刻へ入れこむ(大塚モード専用処理)
	 * @param pcLogOnInfo
	 */
	public void setStampFromPCLogOn(Optional<LogOnInfo> pcLogOnInfo) {
		if(pcLogOnInfo.isPresent()
		   && pcLogOnInfo.get().getLogOn().isPresent()) {
			if(this.attendanceStamp.isPresent()
				&& this.attendanceStamp.get().getActualStamp().isPresent()) {
				this.attendanceStamp.get().setStampFromPcLogOn(pcLogOnInfo.get().getLogOn().get());
			}
		}
		if(pcLogOnInfo.isPresent()
		   && pcLogOnInfo.get().getLogOff().isPresent()) {
			if(this.leaveStamp.isPresent()) {
				this.leaveStamp.get().setStampFromPcLogOn(pcLogOnInfo.get().getLogOff().get());
			}
		}
	}

	/**
	 * 出勤時刻と退勤時刻から計算用時間帯クラス作成
	 * @return　計算用時間帯クラス
	 */
	private TimeSpanForCalc craeteTimeSpan() {
		//Optional<TimeActualStamp> val = attendanceStamp.orElse(Optional.of(new TimeActualStamp()));
		TimeActualStamp att_myObj = attendanceStamp.orElse(new TimeActualStamp()); //出勤
		WorkStamp att_stamp = att_myObj.getStamp().orElse(new WorkStamp()); //出勤（実じゃない）
		TimeWithDayAttr att_attr = att_stamp.getTimeWithDay(); //出勤時刻
		
		TimeActualStamp lea_myObj = leaveStamp.orElse(new TimeActualStamp()); //退勤
		WorkStamp lea_stamp = lea_myObj.getStamp().orElse(new WorkStamp()); //退勤（実じゃない）                                                                                                                                                                                                 
		TimeWithDayAttr lea_attr = lea_stamp.getTimeWithDay(); //退勤時刻
		
		return new TimeSpanForCalc(att_attr,lea_attr);
		/*
		return new TimeSpanForCalc(attendanceStamp.get().getStamp().get().getTimeWithDay()
								  ,leaveStamp.get().getStamp().get().getTimeWithDay());
		*/
	}

	/**
	 * 出勤時刻と退勤時刻から時間帯クラス作成
	 * @return 時間帯
	 */
	public TimeZone getTimeZone() {
		
		/*
		return new TimeZone(attendanceStamp.get().getStamp().get().getTimeWithDay()
							,leaveStamp.get().getStamp().get().getTimeWithDay());
		*/
		return new TimeZone(this.timespan.getStart(), this.timespan.getEnd());
		
	}
	
	/**
	 * ジャスト遅刻・早退の設定を見て時刻を調整する
	 * @param isJustTimeLateAttendance　ジャスト遅刻とする
	 * @param isJustEarlyLeave　ジャスト早退とする
	 * @return　調整後の処理
	 */
	public TimeLeavingWork correctJustTime(boolean isJustTimeLateAttendance,boolean isJustEarlyLeave) {
		TimeActualStamp newAttendance = attendanceStamp.isPresent()?attendanceStamp.get():null;
		TimeActualStamp newLeave = leaveStamp.isPresent()?leaveStamp.get():null;
		if(isJustTimeLateAttendance&&newAttendance!=null) {
			newAttendance = attendanceStamp.get().moveAheadStampTime(1);
		}
		if(isJustEarlyLeave&&newLeave!=null) {
			newLeave = leaveStamp.get().moveBackStampTime(1);
		}
		return new TimeLeavingWork(this.workNo, newAttendance , newLeave);
	}

	public void setTimeLeavingWork(WorkNo workNo, Optional<TimeActualStamp> attendanceStamp, Optional<TimeActualStamp> leaveStamp){
		this.workNo = workNo;
		this.attendanceStamp = attendanceStamp;
		this.leaveStamp = leaveStamp;
	}

	/**
	 * 打刻漏れをしているかチェックする
	 * @return 打刻漏れをしていない 
	 */
	public boolean checkLeakageStamp() {
		//出勤時刻が無い
		if( this.getAttendanceStamp() == null
			|| !this.getAttendanceStamp().isPresent()
			|| this.getAttendanceStamp().get().getStamp() == null
			|| !this.getAttendanceStamp().get().getStamp().isPresent()
			|| this.getAttendanceStamp().get().getStamp().get().getTimeWithDay() == null) {
			return false;
		}
		if(this.getLeaveStamp() == null
				|| !this.getLeaveStamp().isPresent()
				|| this.getLeaveStamp().get().getStamp() == null
				|| !this.getLeaveStamp().get().getStamp().isPresent()
				|| this.getLeaveStamp().get().getStamp().get().getTimeWithDay() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 打刻順序不正であるかチェックする
	 * @return　順序不正である
	 */
	public boolean isReverseOrder() {
		if(this.getTimespan().getStart().greaterThan(this.getTimespan().getEnd())) {
			return true;
		}
		return false;
	}
	
}
