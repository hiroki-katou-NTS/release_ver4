package nts.uk.ctx.at.shared.dom.adapter.workschedule;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 勤怠打刻(実打刻付き)
 *
 */
@Getter
public class TimeActualStampSharedImport {
	
	private Optional<WorkStampSharedImport> actualStamp;
	@Setter
	private Optional<WorkStampSharedImport> stamp = Optional.empty();
	
	//打刻反映回数
	private Integer numberOfReflectionStamp;

	public TimeActualStampSharedImport(WorkStampSharedImport actualStamp, WorkStampSharedImport stamp,
			Integer numberOfReflectionStamp) {
		super();
		this.actualStamp = Optional.ofNullable(actualStamp);
		this.stamp = Optional.ofNullable(stamp);
		this.numberOfReflectionStamp = numberOfReflectionStamp;
	}
	
	
}
