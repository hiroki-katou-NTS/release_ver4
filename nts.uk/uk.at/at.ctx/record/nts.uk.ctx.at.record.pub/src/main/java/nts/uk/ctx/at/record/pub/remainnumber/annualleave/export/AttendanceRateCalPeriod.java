package nts.uk.ctx.at.record.pub.remainnumber.annualleave.export;

import java.util.Optional;

import org.eclipse.persistence.internal.xr.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author loivt
 * 年休出勤率計算期間
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRateCalPeriod extends ValueObject {
	
	/**
	 * 入社後期間
	 */
	private DatePeriod afterJoinCompany;
	
	/**
	 * 計算期間
	 */
	private DatePeriod cal;
	
	/**
	 * 入社前期間
	 */
	private Optional<DatePeriod> beforeJoinCompany;

}
