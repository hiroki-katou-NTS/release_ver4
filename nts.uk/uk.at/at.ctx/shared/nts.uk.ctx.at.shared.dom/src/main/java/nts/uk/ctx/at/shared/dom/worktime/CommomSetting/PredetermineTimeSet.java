package nts.uk.ctx.at.shared.dom.worktime.CommomSetting;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.SiftCode;
import nts.uk.shr.com.time.AttendanceClock;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 所定時間設定
 * @author keisuke_hoshina
 *
 */

@Value
@EqualsAndHashCode(callSuper = false)
public class PredetermineTimeSet extends AggregateRoot {

	private SiftCode siftcode;
	
	private TimeWithDayAttr dateStartTime; 
	
	private AttendanceTime rangeTimeDay;
	
	private PredetermineTimeSheetSetting specifiedTimeSheet;
	
	private SetAdditionToWorkTime additionSet;
	
	public int getPredetermineEndTime() {
		return this.dateStartTime.minute() + (int)this.rangeTimeDay.minute();
	}

}
