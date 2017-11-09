package nts.uk.ctx.at.record.dom.actualworkinghours;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktimeset.fluid.AttendanceTime;

/**
 * 
 * @author nampt
 * 総拘束時間
 *
 */
@Getter
public class ConstraintTime {
	
	//深夜拘束時間
	private AttendanceTime lateNightConstraintTime;
	
	//総拘束時間
	private AttendanceTime totalConstraintTime;

}
