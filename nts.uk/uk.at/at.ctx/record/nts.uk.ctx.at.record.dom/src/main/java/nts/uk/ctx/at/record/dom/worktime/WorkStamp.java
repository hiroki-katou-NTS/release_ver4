package nts.uk.ctx.at.record.dom.worktime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
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
	
	//丸め後の時刻
	private TimeWithDayAttr AfterRoundingTime;
	
	//時刻
	private TimeWithDayAttr timeWithDay;
	
	//場所コード
	private WorkLocationCD locationCode;
	
	//打刻元情報
	private StampSourceInfo stampSourceInfo;

	public WorkStamp(TimeWithDayAttr afterRoundingTime, TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			StampSourceInfo stampSourceInfo) {
		super();
		this.AfterRoundingTime = afterRoundingTime;
		this.timeWithDay = timeWithDay;
		this.locationCode = locationCode;
		this.stampSourceInfo = stampSourceInfo;
	}
}
