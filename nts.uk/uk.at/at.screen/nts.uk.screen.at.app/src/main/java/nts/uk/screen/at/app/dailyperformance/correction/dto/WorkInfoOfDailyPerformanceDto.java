package nts.uk.screen.at.app.dailyperformance.correction.dto;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;


/** @author lamdt */
@Getter
@Setter
@AllArgsConstructor
public class WorkInfoOfDailyPerformanceDto {
	
	private String employeeId;

	private int calculationState;

	private GeneralDate ymd;
	
	// 勤務実績の勤務情報. 勤務種類コード
	private String recordWorkWorktypeCode;

	// 勤務実績の勤務情報. 就業時間帯コード
	private String recordWorkWorktimeCode;

	// 勤務予定の勤務情報. 勤務種類コード
	private String scheduleWorkWorktypeCode;

	// 勤務予定の勤務情報. 勤務種類コード
	private String scheduleWorkWorktimeCode;

	private Boolean krcdtWorkScheduleTime;

	public WorkInfoOfDailyPerformanceDto(String employeeId, int calculationState, GeneralDate ymd) {
		super();
		this.employeeId = employeeId;
		this.calculationState = calculationState;
		this.ymd = ymd;
	}

	public CalculationState getState() {
		return EnumAdaptor.valueOf(this.calculationState, CalculationState.class);
	}
	
	public boolean isItemNoEmpty() {
		return StringUtils.isEmpty(recordWorkWorktypeCode) && StringUtils.isEmpty(recordWorkWorktimeCode)
				&& StringUtils.isEmpty(scheduleWorkWorktypeCode) && StringUtils.isEmpty(scheduleWorkWorktimeCode)
				&& krcdtWorkScheduleTime;
	}
}