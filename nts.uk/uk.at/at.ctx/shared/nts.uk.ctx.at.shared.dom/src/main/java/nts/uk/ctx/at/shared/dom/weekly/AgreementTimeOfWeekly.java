package nts.uk.ctx.at.shared.dom.weekly;

import lombok.Getter;
import lombok.val;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.primitivevalue.LimitWeek;
import nts.uk.ctx.at.shared.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;

/**
 * 週別の36協定時間
 * @author shuichu_ishida
 */
@Getter
public class AgreementTimeOfWeekly implements Cloneable {

	/** 36協定時間 */
	private AttendanceTimeMonth agreementTime;
	/** 限度エラー時間 */
	private LimitWeek limitErrorTime;
	/** 限度アラーム時間 */
	private LimitWeek limitAlarmTime;
	/** 状態 */
	private AgreementTimeStatusOfMonthly status;
	
	/**
	 * コンストラクタ
	 */
	public AgreementTimeOfWeekly(){
		
		this.agreementTime = new AttendanceTimeMonth(0);
		this.limitErrorTime = new LimitWeek(0);
		this.limitAlarmTime = new LimitWeek(0);
		this.status = AgreementTimeStatusOfMonthly.NORMAL;
	}
	
	/**
	 * ファクトリー
	 * @param agreementTime 36協定時間
	 * @param limitErrorTime 限度エラー時間
	 * @param limitAlarmTime 限度アラーム時間
	 * @param status 状態
	 * @return 週別の36協定時間
	 */
	public static AgreementTimeOfWeekly of(
			AttendanceTimeMonth agreementTime,
			LimitWeek limitErrorTime,
			LimitWeek limitAlarmTime,
			AgreementTimeStatusOfMonthly status){
		
		AgreementTimeOfWeekly domain = new AgreementTimeOfWeekly();
		domain.agreementTime = agreementTime;
		domain.limitErrorTime = limitErrorTime;
		domain.limitAlarmTime = limitAlarmTime;
		domain.status = status;
		return domain;
	}
	
	@Override
	public AgreementTimeOfWeekly clone() {
		AgreementTimeOfWeekly cloned = new AgreementTimeOfWeekly();
		try {
			cloned.agreementTime = new AttendanceTimeMonth(this.agreementTime.v());
			cloned.limitErrorTime = new LimitWeek(this.limitErrorTime.v());
			cloned.limitAlarmTime = new LimitWeek(this.limitAlarmTime.v());
			cloned.status = this.status;
		}
		catch (Exception e){
			throw new RuntimeException("AgreementTimeByPeriod clone error.");
		}
		return cloned;
	}
	
	/**
	 * 36協定時間の集計
	 * @param yearMonth 年月（度）
	 * @param weekPeriod 週期間
	 * @param aggregateAtr 集計区分
	 * @param weeklyCalculation 週別の計算
	 * @param companySets 月別集計で必要な会社別設定
	 */
	public void aggregate(RequireM1 require,
			YearMonth yearMonth,
			DatePeriod weekPeriod,
			MonthlyAggregateAtr aggregateAtr,
			WeeklyCalculation weeklyCalculation,
			MonAggrCompanySettings companySets){

		// 年度の確認
		int calcedYear = yearMonth.year();
		if (companySets.getAgreementOperationSet().isPresent()){
			val agreementOpeSet = companySets.getAgreementOperationSet().get();
			int startingMonth = agreementOpeSet.getStartingMonth().value + 1;
			if (yearMonth.month() < startingMonth) calcedYear--;
		}
		
		// 管理期間の36協定時間の作成
		val agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(
				weeklyCalculation.getEmployeeId(), yearMonth);
		agreementTimeOfManagePeriod.aggregateForWeek(require, new Year(calcedYear),
				weekPeriod.end(), aggregateAtr, weeklyCalculation, companySets);
		
		// 週別の36協定時間へ値を移送
		val agreementTimeOfMonthly = agreementTimeOfManagePeriod.getAgreementTime().getAgreementTime();
		this.agreementTime = new AttendanceTimeMonth(agreementTimeOfMonthly.getAgreementTime().v());
		this.limitErrorTime = new LimitWeek(agreementTimeOfMonthly.getLimitErrorTime().v());
		this.limitAlarmTime = new LimitWeek(agreementTimeOfMonthly.getLimitAlarmTime().v());
		this.status = agreementTimeOfMonthly.getStatus();
	}
	
	public static interface RequireM1 extends AgreementTimeOfManagePeriod.RequireM1 {
		
	}
}
