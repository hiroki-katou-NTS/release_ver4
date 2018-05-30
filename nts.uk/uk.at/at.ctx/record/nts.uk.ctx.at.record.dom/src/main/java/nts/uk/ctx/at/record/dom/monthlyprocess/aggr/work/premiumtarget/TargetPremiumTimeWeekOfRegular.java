package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 通常勤務の週割増対象時間
 * @author shuichu_ishida
 */
@Getter
public class TargetPremiumTimeWeekOfRegular {

	/** 週割増時間 */
	private AttendanceTimeMonth premiumTimeWeek;
	/** 当月の週割増対象時間 */
	private AttendanceTimeMonth premiumTimeOfCurrentMonth;
	/** 前月の最終週の週割増時間 */
	private AttendanceTimeMonth premiumTimeOfPrevMonth;
	
	/**
	 * 通常勤務の週割増時間の対象となる時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週割増処理期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @return 加算した休暇使用時間
	 */
	public static TargetPremiumTimeWeekOfRegular askPremiumTimeWeek(String companyId, String employeeId,
			DatePeriod weekPeriod, AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime){

		TargetPremiumTimeWeekOfRegular domain = new TargetPremiumTimeWeekOfRegular();
		domain.premiumTimeWeek = new AttendanceTimeMonth(0);
		domain.premiumTimeOfCurrentMonth = new AttendanceTimeMonth(0);
		domain.premiumTimeOfPrevMonth = new AttendanceTimeMonth(0);
		
		// 法定内時間を取得する
		val workTimeOfMonthly = aggregateTotalWorkingTime.getWorkTime();
		val workTime = workTimeOfMonthly.getTimeSeriesTotalLegalActualTime(weekPeriod);

		// 週割増時間に就業時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(workTime.v());
		
		// 法定内残業時間を取得する
		val overTime = aggregateTotalWorkingTime.getOverTime();
		val legalOverTime = overTime.getLegalOverTime(weekPeriod);
		
		// 週割増時間に残業時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(legalOverTime.v());

		// 法定内休出時間を取得する
		val holidayWorkTime = aggregateTotalWorkingTime.getHolidayWorkTime();
		val legalHolidayWorkTime = holidayWorkTime.getLegalHolidayWorkTime(weekPeriod);
		
		// 週割増時間に休出時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(legalHolidayWorkTime.v());
		
		// 加算する休暇時間を取得する
		val vacationUseTime = aggregateTotalWorkingTime.getVacationUseTime();
		val addVacationTime = GetVacationAddTime.getTime(weekPeriod, vacationUseTime, addSet);
		
		// 週割増時間に休暇加算時間を加算する
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(addVacationTime.v());

		// 「当月の週割増対象時間」を求める
		domain.premiumTimeOfCurrentMonth = new AttendanceTimeMonth(domain.premiumTimeWeek.v());

		// 「前月の最終週の週割増対象時間」を加算する
		//*****（未）　前月最終週計算が設計中。2018.5.27 shuichi_ishida
		domain.premiumTimeOfPrevMonth = new AttendanceTimeMonth(0);
		domain.premiumTimeWeek = domain.premiumTimeWeek.addMinutes(domain.premiumTimeOfPrevMonth.v());
		
		return domain;
	}
}
