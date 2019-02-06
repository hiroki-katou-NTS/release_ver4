package nts.uk.ctx.at.function.app.export.holidaysremaining;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@AllArgsConstructor
@Setter
@Getter
public class OutputPeriodInformation {

	//出力期間
	private YearMonthPeriod yearMonthPeriod;
	
	//過去
	private Optional<YearMonthPeriod> past;
	
	//当月・未来
	private List<YearMonthPeriod> thisMonthFuture;
	
	//当月期間
	private	CurrentMonthPeriod currentMonthPeriod;
}
