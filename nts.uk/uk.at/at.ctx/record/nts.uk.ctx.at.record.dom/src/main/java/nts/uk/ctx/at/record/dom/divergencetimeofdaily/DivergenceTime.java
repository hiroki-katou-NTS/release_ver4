package nts.uk.ctx.at.record.dom.divergencetimeofdaily;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 乖離時間 */
@Getter
public class DivergenceTime {

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

	public DivergenceTime(AttendanceTime divTimeAfterDeduction, AttendanceTime deductionTime, AttendanceTime divTime,
			int divTimeId, DivergenceReasonContent divReason, DiverdenceReasonCode divResonCode) {
		super();
		this.divTimeAfterDeduction = divTimeAfterDeduction;
		this.deductionTime = deductionTime;
		this.divTime = divTime;
		this.divTimeId = divTimeId;
		this.divReason = divReason;
		this.divResonCode = divResonCode;
	}
}
