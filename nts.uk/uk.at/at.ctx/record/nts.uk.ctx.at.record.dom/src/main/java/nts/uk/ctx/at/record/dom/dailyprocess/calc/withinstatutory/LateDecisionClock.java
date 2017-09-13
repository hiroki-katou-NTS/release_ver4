package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 遅刻判断時刻
 * @author ken_takasu
 *
 */
@AllArgsConstructor
@Value
public class LateDecisionClock {
	private  TimeWithDayAttr lateDecisionClock;
	private int workNo;
}
