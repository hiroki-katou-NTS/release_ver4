package nts.uk.ctx.at.schedule.pub.schedule.basicschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@AllArgsConstructor
public class ScWorkBreakTimeExport {
	/** The schedule break cnt. */
	// 予定休憩回数
	private int scheduleBreakCnt;
	
	/** The scheduled start clock. */
	// 予定休憩開始時刻
	private TimeWithDayAttr  scheduledStartClock;
	
	/** The scheduled end clock. */
	// 予定休憩終了時刻
	private TimeWithDayAttr scheduledEndClock;
}
