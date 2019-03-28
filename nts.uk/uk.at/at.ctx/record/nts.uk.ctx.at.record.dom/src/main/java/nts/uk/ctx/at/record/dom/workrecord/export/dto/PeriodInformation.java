package nts.uk.ctx.at.record.dom.workrecord.export.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Getter
@AllArgsConstructor
public class PeriodInformation {

	private DatePeriod datePeriod;
	
	private YearMonthPeriod yearMonthPeriod;
}
