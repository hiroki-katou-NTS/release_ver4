/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.scherec.optitem;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class TimeRange.
 */
// 時間範囲
// 事前条件 : 上限値≧下限値
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange extends DomainObject implements RangeGetter {

    // 日別実績の時間範囲
	private Optional<DailyTimeRange> dailyTimeRange;

	// 月別実績の時間範囲
	private Optional<MonthlyTimeRange> monthlyTimeRange;

	public Optional<BigDecimal> getUpper(PerformanceAtr performanceAtr) {
		if (performanceAtr == PerformanceAtr.DAILY_PERFORMANCE) {
			return dailyTimeRange.flatMap(c -> c.getUpperLimit()).map(c -> BigDecimal.valueOf(c.v()));
		}
		
		return monthlyTimeRange.flatMap(c -> c.getUpperLimit()).map(c -> BigDecimal.valueOf(c.v()));
	}
	
	public Optional<BigDecimal> getLower(PerformanceAtr performanceAtr) {
		if (performanceAtr == PerformanceAtr.DAILY_PERFORMANCE) {
			return dailyTimeRange.flatMap(c -> c.getLowerLimit()).map(c -> BigDecimal.valueOf(c.v()));
		}
		
		return monthlyTimeRange.flatMap(c -> c.getLowerLimit()).map(c -> BigDecimal.valueOf(c.v()));
	}
}
