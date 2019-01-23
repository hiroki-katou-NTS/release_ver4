package nts.uk.ctx.at.function.app.export.holidaysremaining;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@Getter
public class CurrentMonthPeriod {

	//年月
	private YearMonth currentMonthPer;
	
	//期間
	private DatePeriod outPeriodInfo;
	
}
