package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

/**
 * 
 * @author nampt KIF 001
 *  出退勤 temporary - this class created for contain temporary data
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeLeavingWorkOutput {

	/*
	 * 勤務NO
	 */
	private WorkNo workNo;

	private TimeActualStampOutPut attendanceStamp;

	private TimeActualStampOutPut leaveStamp;
}
