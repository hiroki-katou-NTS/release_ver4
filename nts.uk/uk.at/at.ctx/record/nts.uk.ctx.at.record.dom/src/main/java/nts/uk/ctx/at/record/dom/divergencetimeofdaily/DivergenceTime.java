package nts.uk.ctx.at.record.dom.divergencetimeofdaily;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/** 乖離時間 */
@Getter
public class DivergenceTime {

	//控除後乖離時間
	private AttendanceTimeOfExistMinus divTimeAfterDeduction;
	
	//控除時間
	private AttendanceTimeOfExistMinus deductionTime;
	
	//乖離時間
	private AttendanceTimeOfExistMinus divTime;
	
	//乖離時間NO - primitive value
	private int divTimeId;
	
	//乖離理由
	private Optional<DivergenceReasonContent> divReason;
	
	//乖離理由コード
	private Optional<DiverdenceReasonCode> divResonCode;

	public DivergenceTime(AttendanceTimeOfExistMinus divTimeAfterDeduction, AttendanceTimeOfExistMinus deductionTime, AttendanceTimeOfExistMinus divTime,
			int divTimeId, DivergenceReasonContent divReason, DiverdenceReasonCode divResonCode) {
		super();
		this.divTimeAfterDeduction = divTimeAfterDeduction;
		this.deductionTime = deductionTime;
		this.divTime = divTime;
		this.divTimeId = divTimeId;
		this.divReason = Optional.ofNullable(divReason);
		this.divResonCode = Optional.ofNullable(divResonCode);
	}
	
	public DivergenceTime(AttendanceTimeOfExistMinus divTimeAfterDeduction, AttendanceTimeOfExistMinus deductionTime, AttendanceTimeOfExistMinus divTime,
			int divTimeId, Optional<DivergenceReasonContent> divReason, Optional<DiverdenceReasonCode> divResonCode) {
		super();
		this.divTimeAfterDeduction = divTimeAfterDeduction;
		this.deductionTime = deductionTime;
		this.divTime = divTime;
		this.divTimeId = divTimeId;
		this.divReason = (divReason);
		this.divResonCode = (divResonCode);
	}
	
	/**
	 * 乖離時間のチェック
	 * @return
	 */
	public Optional<EmployeeDailyPerError> checkDivergenceTime(){
		return Optional.empty();
	}
}
