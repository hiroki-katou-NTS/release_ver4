
package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.common.timestamp;


import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.shared.dom.worktime.enums.StampSourceInfo;
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
	private TimeWithDayAttr afterRoundingTime;
	
	/*
	 * 時刻
	 */
	private WorkTimeInformation timeDay;
	
	/*
	 * 場所コード
	 */
	private Optional<WorkLocationCD> locationCode;
	

	public WorkStamp(TimeWithDayAttr afterRoundingTime, TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			TimeChangeMeans timeChangeMeans) {
		super();
		this.afterRoundingTime = afterRoundingTime;
		this.timeDay = new WorkTimeInformation(new ReasonTimeChange(timeChangeMeans, null), timeWithDay);
		this.locationCode = Optional.ofNullable(locationCode);
	}
	
	public void setPropertyWorkStamp(TimeWithDayAttr afterRoundingTime, TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			TimeChangeMeans timeChangeMeans){
		this.afterRoundingTime = afterRoundingTime;
		this.timeDay = new WorkTimeInformation(new ReasonTimeChange(timeChangeMeans, null), timeWithDay);
		this.locationCode = Optional.ofNullable(locationCode);
		
	}
	
	public boolean isFromSPR() {
		return this.timeDay.getReasonTimeChange().getTimeChangeMeans() == TimeChangeMeans.SPR_COOPERATION;
	}
	
	public void setStampFromPcLogOn(TimeWithDayAttr PcLogOnStamp) {
		this.timeDay.setTimeWithDay(Optional.ofNullable(PcLogOnStamp));
	}

}
