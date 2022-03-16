package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.weekly;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.scherec.byperiod.AnyItemByPeriod;
import nts.uk.ctx.at.shared.dom.scherec.byperiod.ExcessOutsideByPeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.totalcount.TotalCountByPeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 週別実績の勤怠時間
 * @author shuichi_ishida
 */
@Getter
public class AttendanceTimeOfWeekly extends AggregateRoot {

	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;
	/** 週NO */
	private final int weekNo;

	/** 期間 */
	private DatePeriod period;
	/** 週別の計算 */
	private WeeklyCalculation weeklyCalculation;
	/** 時間外超過 */
	private ExcessOutsideByPeriod excessOutside;
	/** 縦計 */
	private VerticalTotalOfMonthly verticalTotal;
	/** 回数集計 */
	private TotalCountByPeriod totalCount;
	/** 任意項目 */
	private AnyItemByPeriod anyItem;
	/** 集計日数 */
	private AttendanceDaysMonth aggregateDays;

	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param weekNo 週NO
	 * @param period 期間
	 */
	public AttendanceTimeOfWeekly(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, int weekNo, DatePeriod period){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.weekNo = weekNo;
		
		this.period = period;
		this.weeklyCalculation = new WeeklyCalculation();
		this.excessOutside = new ExcessOutsideByPeriod();
		this.verticalTotal = new VerticalTotalOfMonthly();
		this.totalCount = new TotalCountByPeriod();
		this.anyItem = new AnyItemByPeriod();
		this.aggregateDays = new AttendanceDaysMonth((double)(period.start().daysTo(period.end()) + 1));
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param weekNo 週NO
	 * @param period 期間
	 * @param weeklyCalculation 週別の計算
	 * @param excessOutside 時間外超過
	 * @param verticalTotal 縦計
	 * @param totalCount 回数集計
	 * @param anyItem 任意項目
	 * @param aggregateDays 集計日数
	 * @return 週別実績の勤怠時間
	 */
	public static AttendanceTimeOfWeekly of(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			int weekNo,
			DatePeriod period,
			WeeklyCalculation weeklyCalculation,
			ExcessOutsideByPeriod excessOutside,
			VerticalTotalOfMonthly verticalTotal,
			TotalCountByPeriod totalCount,
			AnyItemByPeriod anyItem,
			AttendanceDaysMonth aggregateDays){
		
		AttendanceTimeOfWeekly domain =
				new AttendanceTimeOfWeekly(employeeId, yearMonth, closureId, closureDate, weekNo, period);
		domain.weeklyCalculation = weeklyCalculation;
		domain.excessOutside = excessOutside;
		domain.verticalTotal = verticalTotal;
		domain.totalCount = totalCount;
		domain.anyItem = anyItem;
		domain.aggregateDays = aggregateDays;
		return domain;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttendanceTimeOfWeekly other = (AttendanceTimeOfWeekly) obj;
		
		if ((this.employeeId == null && other.employeeId != null) 
				|| !this.employeeId.equals(other.employeeId)) 
				return false;
		if ((this.yearMonth == null && other.yearMonth != null) 
				|| !this.yearMonth.equals(other.yearMonth)) 
				return false;
		if ((this.closureId == null && other.closureId != null) 
				|| !this.closureId.equals(other.closureId)) 
				return false;
		if ((this.closureDate == null && other.closureDate != null) 
				|| !this.closureDate.equals(other.closureDate)) 
				return false;
		
		return this.weekNo == other.weekNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.employeeId == null) ? 0 : this.employeeId.hashCode());
		result = prime * result + ((this.yearMonth == null) ? 0 : this.yearMonth.hashCode());
		result = prime * result + ((this.closureId == null) ? 0 : this.closureId.hashCode());
		result = prime * result + ((this.closureDate == null) ? 0 : this.closureDate.hashCode());
		result = prime * result + this.weekNo;
		return result;
	}
}
