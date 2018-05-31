package nts.uk.ctx.at.function.dom.annualworkschedule.export;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmployeeData {
	private EmployeeInfo employeeInfo;
	/**
	 * Map<帳表に出力する項目.コード, AnnualWorkScheduleData>
	 */
	private Map<String, AnnualWorkScheduleData> annualWorkSchedule;
}