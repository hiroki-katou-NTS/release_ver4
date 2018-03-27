package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * フレックス不足から年休と欠勤を控除する
 * @author shuichu_ishida
 */
public interface DeductFromFlexShortage {

	/**
	 * フレックス不足から年休と欠勤を控除する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param period 期間
	 * @param annualLeaveDeductDays 年休控除日数
	 * @param absenceDeductTime 欠勤控除時間
	 * @return 月別実績の月の計算、エラーメッセージID
	 */
	DeductFromFlexShortageValue calc(String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod period,
			AttendanceDaysMonth annualLeaveDeductDays, AttendanceTimeMonth absenceDeductTime);
}
