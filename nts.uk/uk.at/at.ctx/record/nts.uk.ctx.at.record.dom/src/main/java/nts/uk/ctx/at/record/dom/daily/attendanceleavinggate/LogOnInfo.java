package nts.uk.ctx.at.record.dom.daily.attendanceleavinggate;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.TimeWithDayAttr;

/** ログオン情報*/
@Getter
@NoArgsConstructor
public class LogOnInfo {

	/** 勤務NO: 勤務NO */
	private PCLogOnNo workNo;
	
	/** ログオフ: 勤怠打刻 */
	private Optional<TimeWithDayAttr> logOff;
	
	/** ログオン: 勤怠打刻*/
	private Optional<TimeWithDayAttr> logOn;

	public LogOnInfo(PCLogOnNo workNo, TimeWithDayAttr logOff, TimeWithDayAttr logOn) {
		super();
		this.workNo = workNo;
		this.logOff = Optional.ofNullable(logOff);
		this.logOn = Optional.ofNullable(logOn);
	}
}
