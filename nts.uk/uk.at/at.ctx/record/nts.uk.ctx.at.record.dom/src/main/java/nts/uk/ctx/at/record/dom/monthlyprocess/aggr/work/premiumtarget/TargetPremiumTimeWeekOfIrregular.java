package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 変形労働勤務の週割増対象時間
 * @author shuichi_ishida
 */
@Getter
public class TargetPremiumTimeWeekOfIrregular {

	/** 週割増時間 */
	private AttendanceTimeMonth premiumTimeWeek;
	/** 当月の週割増対象時間 */
	private AttendanceTimeMonth premiumTimeOfCurrentMonth;
	/** 前月の最終週の週割増時間 */
	private AttendanceTimeMonth premiumTimeOfPrevMonth;
	
	/**
	 * 変形労働勤務の週割増時間の対象となる時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週割増処理期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param isAddVacation 休暇加算　（true=する）
	 * @param premiumTimeOfPrevMonLast 前月の最終週の週割増対象時間
	 * @return 変形労働勤務の週割増対象時間
	 */
	public static TargetPremiumTimeWeekOfIrregular askPremiumTimeWeek(String companyId, String employeeId, DatePeriod weekPeriod,
			AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime, boolean isAddVacation,
			AttendanceTimeMonth premiumTimeOfPrevMonLast){

		TargetPremiumTimeWeekOfIrregular domain = new TargetPremiumTimeWeekOfIrregular();
		domain.premiumTimeWeek = new AttendanceTimeMonth(0);
		domain.premiumTimeOfCurrentMonth = new AttendanceTimeMonth(0);
		domain.premiumTimeOfPrevMonth = new AttendanceTimeMonth(premiumTimeOfPrevMonLast.v());
		
		// 集計対象時間を取得する
		val legalTime = aggregateTotalWorkingTime.getWorkTime().getAggregateTargetTime(weekPeriod, addSet);

		// 週割増時間に集計対象時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(legalTime.v());
		
		// 法定内残業時間を取得する
		val legalOverTime = aggregateTotalWorkingTime.getOverTime().calcOverTimeForPremium(
				weekPeriod, aggregateTotalWorkingTime);
		
		// 週割増時間に残業時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(legalOverTime.v());

		// 法定内休出時間を取得する
		val legalHolidayWorkTime = aggregateTotalWorkingTime.getHolidayWorkTime().getLegalHolidayWorkTime(weekPeriod);
		
		// 週割増時間に休出時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(legalHolidayWorkTime.v());
		
		// 休暇加算を確認する
		if (isAddVacation){
			
			// 休暇加算時間を取得する
			val vacationAddTime = GetVacationAddTime.getTime(
					weekPeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSet);
			
			// 週割増時間に休暇加算時間を加算する
			domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(vacationAddTime.v());
		}

		// 「当月の週割増対象時間」を求める
		domain.premiumTimeOfCurrentMonth = new AttendanceTimeMonth(domain.premiumTimeWeek.v());

		// 「前月の最終週の週割増対象時間」を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(domain.premiumTimeOfPrevMonth.v());
		
		return domain;
	}
}
