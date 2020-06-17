package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class ConfirmStatusMonthlyHelper {

	public static ClosureMonth closureMonthHelper(int closureId, int yearmonth) {
		new ClosureMonth(new YearMonth(yearmonth), closureId, new ClosureDate(1, true));
		return null;
	}

	public static List<String> employeesHelper() {
		return Arrays.asList(
				"ba7f7d44-1f82-42bd-b551-0153a1c972b1",
				"362da6e6-a893-46e4-bdf9-22cd9441f17c",
				"0168730d-47ec-44da-86c5-760db4fd6371",
				"8dc540a4-0581-4387-b16a-d3943641e5fc",
				"fccb8f4f-2fde-4c14-bf68-4d7bc605863c",
				"6d57123b-c7db-4efb-9976-12b92f7a3aa5",
				"f784ad8f-54ee-4fc3-ad3b-2e319f629b16",
				"6a6fbd3a-0882-4883-829c-1e34b8bc8fa4",
				"4d442a4c-36a7-403d-9dfd-3dd0b713c4e8");
	}

	public static Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cacheHelper(ClosureMonth cm, List<String> employees) {
		GeneralDate criteriaDate = GeneralDate.ymd(cm.yearMonth().year(), cm.yearMonth().month(), cm.closureDate().getClosureDay().v());
		DatePeriod period = cm.defaultPeriod();
		AggrPeriodEachActualClosure actualClosure = AggrPeriodEachActualClosure.of(
				ClosureId.valueOf(cm.closureId()), cm.closureDate(), cm.yearMonth(), period, period);
		List<ClosurePeriod> closurePeriod = Arrays.asList(ClosurePeriod.of(
				ClosureId.valueOf(cm.closureId()), cm.closureDate(), cm.yearMonth(), criteriaDate, Arrays.asList(actualClosure)));
		return employees.stream()
			.map(e -> new ClosurePeriodCacheKey(e, criteriaDate, Optional.of(cm.yearMonth()), Optional.empty()))
			.collect(Collectors.toMap(key -> key, value -> closurePeriod));
	}

}
