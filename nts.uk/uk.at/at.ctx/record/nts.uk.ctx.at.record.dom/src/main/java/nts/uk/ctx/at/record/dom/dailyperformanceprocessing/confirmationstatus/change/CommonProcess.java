package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 */
public class CommonProcess {

	public static DatePeriod getMaxPeriod(List<DatePeriod> periods) {
		if (periods.isEmpty())
			return null;
		List<GeneralDate> lstStartEnd = periods.stream().flatMap(x -> Arrays.asList(x.start(), x.end()).stream())
				.collect(Collectors.toList());
		lstStartEnd = lstStartEnd.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
		return new DatePeriod(lstStartEnd.get(0), lstStartEnd.get(lstStartEnd.size() - 1));
	}
	
	public static boolean inRange(GeneralDate dateTarget, DatePeriod period) {
		return period.start().beforeOrEquals(dateTarget) && period.end().afterOrEquals(dateTarget);
	}

}
