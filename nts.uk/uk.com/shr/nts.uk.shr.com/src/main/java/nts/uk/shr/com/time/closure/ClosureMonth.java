package nts.uk.shr.com.time.closure;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 締めに基づく年月を厳密に特定するキー情報
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class ClosureMonth {

	private final YearMonth yearMonth;
	private final int closureId;
	private final ClosureDate closureDate;
	
	/**
	 * 年月
	 * @return
	 */
	public YearMonth yearMonth() {
		return yearMonth;
	}
	
	/**
	 * 締めID
	 * @return
	 */
	public int closureId() {
		return closureId;
	}
	
	/**
	 * 締め日
	 * @return
	 */
	public ClosureDate closureDate() {
		return closureDate;
	}
	
	/**
	 * 年月と締め日から算出できるデフォルトの期間
	 * 実際の期間は、途中で締め変更があった場合に変動する（中途半端な期間になったりする）
	 * @return
	 */
	public DatePeriod defaultPeriod() {
		return closureDate.periodOf(yearMonth);
	}
}
