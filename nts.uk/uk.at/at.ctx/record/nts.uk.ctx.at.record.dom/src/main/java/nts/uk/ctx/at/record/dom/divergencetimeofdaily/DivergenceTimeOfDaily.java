package nts.uk.ctx.at.record.dom.divergencetimeofdaily;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.shared.dom.worktimeset.fluid.AttendanceTime;

/**
 * 
 * @author nampt
 * 日別実績の乖離時間
 *
 */
@Getter
public class DivergenceTimeOfDaily {
	
	//控除後乖離時間
	private AttendanceTime divTimeAfterDeduction;
	
	//控除時間
	private AttendanceTime deductionTime;
	
	//乖離時間
	private AttendanceTime divTime;
	
	//乖離時間NO - primitive value
	private int divTimeId;
	
	//乖離理由
	private DivergenceReasonContent divReason;
	
	//乖離理由コード
	private DiverdenceReasonCode divResonCode;

}
