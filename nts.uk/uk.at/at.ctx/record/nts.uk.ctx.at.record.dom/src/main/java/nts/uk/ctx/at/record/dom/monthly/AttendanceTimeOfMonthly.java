package nts.uk.ctx.at.record.dom.monthly;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の勤怠時間
 * @author shuichi_ishida
 */
@Getter
public class AttendanceTimeOfMonthly extends AggregateRoot {

	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;

	/** 期間 */
	private DatePeriod datePeriod;
	/** 月の計算 */
	@Setter
	private MonthlyCalculation monthlyCalculation;
	/** 時間外超過 */
	@Setter
	private ExcessOutsideWorkOfMonthly excessOutsideWork;
	/** 縦計 */
	@Setter
	private VerticalTotalOfMonthly verticalTotal;
	/** 集計日数 */
	@Setter
	private AttendanceDaysMonth aggregateDays;
	/** 回数集計 */
	//aggregateTimes

	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 */
	public AttendanceTimeOfMonthly(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.datePeriod = datePeriod;
		this.monthlyCalculation = new MonthlyCalculation();
		this.excessOutsideWork = new ExcessOutsideWorkOfMonthly();
		this.verticalTotal = new VerticalTotalOfMonthly();
		this.aggregateDays = new AttendanceDaysMonth(0.0);
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param monthlyCalculation 月の計算
	 * @param excessOutsideWork 時間外超過
	 * @param verticalTotal 縦計
	 * @param aggregateDays 集計日数
	 * @return 月別実績の勤怠時間
	 */
	public static AttendanceTimeOfMonthly of(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			DatePeriod datePeriod,
			MonthlyCalculation monthlyCalculation,
			ExcessOutsideWorkOfMonthly excessOutsideWork,
			VerticalTotalOfMonthly verticalTotal,
			AttendanceDaysMonth aggregateDays){
		
		val domain = new AttendanceTimeOfMonthly(employeeId, yearMonth, closureId, closureDate, datePeriod);
		domain.monthlyCalculation = monthlyCalculation;
		domain.excessOutsideWork = excessOutsideWork;
		domain.verticalTotal = verticalTotal;
		domain.aggregateDays = aggregateDays;
		return domain;
	}

	/**
	 * 集計準備
	 * @param companyId 会社ID
	 * @param datePeriod 期間
	 * @param workingConditionItem 労働制
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void prepareAggregation(String companyId, DatePeriod datePeriod, WorkingConditionItem workingConditionItem,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		this.monthlyCalculation.prepareAggregation(companyId, this.employeeId, this.yearMonth,
				this.closureId, this.closureDate, datePeriod, workingConditionItem, Optional.empty(), repositories);
	}

	/**
	 * 等しいかどうか
	 * @param target 比較対象
	 * @return true:等しい、false:等しくない
	 */
	public boolean equals(AttendanceTimeOfMonthly target) {
		
		return (this.employeeId == target.employeeId &&
				this.yearMonth.equals(target.yearMonth) &&
				this.closureId.value == target.closureId.value &&
				this.closureDate.getClosureDay().equals(target.closureDate.getClosureDay()) &&
				this.closureDate.getLastDayOfMonth() == target.closureDate.getLastDayOfMonth());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AttendanceTimeOfMonthly target){

		GeneralDate startDate = this.datePeriod.start();
		GeneralDate endDate = this.datePeriod.end();
		if (startDate.after(target.datePeriod.start())) startDate = target.datePeriod.start();
		if (endDate.before(target.datePeriod.end())) endDate = target.datePeriod.end();
		this.datePeriod = new DatePeriod(startDate, endDate);
		
		this.monthlyCalculation.sum(target.monthlyCalculation);
		this.excessOutsideWork.sum(target.excessOutsideWork);
		this.verticalTotal.sum(target.verticalTotal);
		
		this.aggregateDays = this.aggregateDays.addDays(target.aggregateDays.v());
	}
}
