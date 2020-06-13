package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.Objects;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public class ClosurePeriodCacheKey {
		private String employeeId;
		private GeneralDate criteriaDate;
		private Optional<YearMonth> yearMonth;
		private Optional<DatePeriod> period;

		/**
		 * コンストラクタ
		 */
		public ClosurePeriodCacheKey(
				String employeeId,
				GeneralDate criteriaDate,
				Optional<YearMonth> yearMonth,
				Optional<DatePeriod> period) {
			this.employeeId = employeeId;
			this.criteriaDate = criteriaDate;
			this.yearMonth = yearMonth;
			this.period = period;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ClosurePeriodCacheKey))  return false;

			ClosurePeriodCacheKey target = (ClosurePeriodCacheKey) obj;
			return this.employeeId.equals(target.employeeId)
					&& this.criteriaDate.equals(target.criteriaDate)
					&& this.yearMonth.equals(target.yearMonth)
					&& this.period.equals(target.period);
		}

		@Override
		public int hashCode() {
	        return Objects.hash(employeeId, criteriaDate, yearMonth, period);
		}
}