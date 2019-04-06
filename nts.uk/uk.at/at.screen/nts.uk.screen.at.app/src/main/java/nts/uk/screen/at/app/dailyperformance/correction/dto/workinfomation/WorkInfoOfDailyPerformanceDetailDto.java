package nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
@Data
@NoArgsConstructor

public class WorkInfoOfDailyPerformanceDetailDto {
	private String employeeId;

	private WorkInformationDto recordWorkInformation;

	private WorkInformationDto scheduleWorkInformation;

	private CalculationStateDto calculationState;

	// 直行区分
	private NotUseAttributeDto goStraightAtr;

	// 直帰区分
	private NotUseAttributeDto backStraightAtr;

	private GeneralDate ymd;

	private List<ScheduleTimeSheetDto> scheduleTimeSheets;
	
	private long version;

	public WorkInfoOfDailyPerformanceDetailDto(String employeeId, WorkInformationDto recordWorkInformation,
			WorkInformationDto scheduleWorkInformation, CalculationStateDto calculationState, NotUseAttributeDto goStraightAtr,
			NotUseAttributeDto backStraightAtr, GeneralDate ymd, List<ScheduleTimeSheetDto> scheduleTimeSheets, long version) {
		this.employeeId = employeeId;
		this.recordWorkInformation = recordWorkInformation;
		this.scheduleWorkInformation = scheduleWorkInformation;
		this.calculationState = calculationState;
		this.goStraightAtr = goStraightAtr;
		this.backStraightAtr = backStraightAtr;
		this.ymd = ymd;
		this.scheduleTimeSheets = scheduleTimeSheets; 
		this.version = version;
	}
}
