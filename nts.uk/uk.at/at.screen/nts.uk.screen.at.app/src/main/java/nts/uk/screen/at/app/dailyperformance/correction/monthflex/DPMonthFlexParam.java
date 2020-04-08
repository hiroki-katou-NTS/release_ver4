package nts.uk.screen.at.app.dailyperformance.correction.monthflex;

import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DPMonthFlexParam {

	private String companyId;
	private String employeeId;
	private GeneralDate date;
	private String employmentCode;
	private OperationOfDailyPerformanceDto dailyPerformanceDto;
	
	private Set<String> formatCode;
	
	private Optional<MonthlyRecordWorkDto> monthOpt = Optional.empty();
	
	//日別実績の修正の状態．対象期間
	private DatePeriod datePeriod;

}
