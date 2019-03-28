package nts.uk.ctx.at.record.dom.workrecord.export.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@Getter
public class EmployeeClosDate {

	private List<String> empl;
	
	private DatePeriod datePeriod;
}
