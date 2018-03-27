package nts.uk.ctx.at.record.dom.adapter.basicschedule;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Getter
@Setter
public class WorkBreakTimeImport {
	
	/** The schedule break cnt. */
	// 予定休憩回数
	private int scheduleBreakCnt;
	
	/** The scheduled start clock. */
	// 予定休憩開始時刻
	private TimeWithDayAttr  scheduledStartClock;
	
	/** The scheduled end clock. */
	// 予定休憩終了時刻
	private TimeWithDayAttr scheduledEndClock;

	public WorkBreakTimeImport(int scheduleBreakCnt, TimeWithDayAttr scheduledStartClock,
			TimeWithDayAttr scheduledEndClock) {
		super();
		this.scheduleBreakCnt = scheduleBreakCnt;
		this.scheduledStartClock = scheduledStartClock;
		this.scheduledEndClock = scheduledEndClock;
	}

}
