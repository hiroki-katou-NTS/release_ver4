package nts.uk.ctx.at.record.dom.worktime;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
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
	
	private Optional<TimeActualStamp> attendanceStamp;
	
	private Optional<TimeActualStamp> leaveStamp;
	
	private TimeSpanForCalc timespan;
	


	
	public TimeLeavingWork(WorkNo workNo, Optional<TimeActualStamp> attendanceStamp, Optional<TimeActualStamp> leaveStamp) {
		super();
		this.workNo = workNo;
		this.attendanceStamp = attendanceStamp;
		this.leaveStamp = leaveStamp;
		
		this.timespan = this.craeteTimeSpan();
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
		TimeActualStamp newAttendance = attendanceStamp.get();
		TimeActualStamp newLeave = leaveStamp.get();
		if(isJustEarlyLeave) {
			newAttendance = attendanceStamp.get().moveBackStampTime(1);
		}
		if(isJustTimeLateAttendance) {
			newLeave = leaveStamp.get().moveAheadStampTime(1);
		}
		return new TimeLeavingWork(this.workNo,Optional.ofNullable(newAttendance) , Optional.ofNullable(newLeave));
	}

	public void setTimeLeavingWork(WorkNo workNo, Optional<TimeActualStamp> attendanceStamp, Optional<TimeActualStamp> leaveStamp){
		this.workNo = workNo;
		this.attendanceStamp = attendanceStamp;
		this.leaveStamp = leaveStamp;
	}
	
}
