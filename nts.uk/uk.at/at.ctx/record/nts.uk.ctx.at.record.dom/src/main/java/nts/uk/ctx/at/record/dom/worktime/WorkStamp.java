package nts.uk.ctx.at.record.dom.worktime;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
//import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 勤怠打刻
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkStamp extends DomainObject{
	
	/*
	 * 丸め後の時刻
	 */
	private TimeWithDayAttr AfterRoundingTime;
	
	/*
	 * 時刻
	 */
	private TimeWithDayAttr timeWithDay;
	
	/*
	 * 場所コード
	 */
	private Optional<WorkLocationCD> locationCode;
	
	/*
	 * 打刻元情報
	 */
	private StampSourceInfo stampSourceInfo;

	public WorkStamp(TimeWithDayAttr afterRoundingTime, TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			StampSourceInfo stampSourceInfo) {
		super();
		this.AfterRoundingTime = afterRoundingTime;
		this.timeWithDay = timeWithDay;
		this.locationCode = Optional.ofNullable(locationCode);
		this.stampSourceInfo = stampSourceInfo;
	}
	
	public void setPropertyWorkStamp(TimeWithDayAttr afterRoundingTime, TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			StampSourceInfo stampSourceInfo){
		this.AfterRoundingTime = afterRoundingTime;
		this.timeWithDay = timeWithDay;
		this.locationCode = Optional.ofNullable(locationCode);
		this.stampSourceInfo = stampSourceInfo;
		
	}
	
	public boolean isFromSPR() {
		return stampSourceInfo == StampSourceInfo.SPR;
	}
	
	public void setStampFromPcLogOn(TimeWithDayAttr PcLogOnStamp) {
		this.timeWithDay = PcLogOnStamp;
	}

}
