package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 前月の最終週の週割増対象時間
 * @author shuichi_ishida
 */
@Getter
public class TargetPrmTimeWeekOfPrevMonLast {

	/** 対象時間 */
	private AttendanceTimeMonth targetTime;
	
	/**
	 * 前月の最終週の週割増対象時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 前月の最終週の期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @return 前月の最終週の週割増対象時間
	 */
	public static TargetPrmTimeWeekOfPrevMonLast askPremiumTimeWeek(String companyId, String employeeId,
			DatePeriod weekPeriod, AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime){

		TargetPrmTimeWeekOfPrevMonLast domain = new TargetPrmTimeWeekOfPrevMonLast();
		domain.targetTime = new AttendanceTimeMonth(0);
		
		// 集計対象時間を取得する
		val workTimeOfMonthly = aggregateTotalWorkingTime.getWorkTime();
		val workTime = workTimeOfMonthly.getAggregateTargetTime(weekPeriod);

		// 対象時間に集計対象時間を加算する
		domain.targetTime = domain.targetTime.addMinutes(workTime.v());
		
		// 法定内残業時間を取得する
		val overTime = aggregateTotalWorkingTime.getOverTime();
		val legalOverTime = overTime.getLegalOverTime(weekPeriod);
		
		// 対象時間に残業時間を加算する
		domain.targetTime = domain.targetTime.addMinutes(legalOverTime.v());

		// 法定内休出時間を取得する
		val holidayWorkTime = aggregateTotalWorkingTime.getHolidayWorkTime();
		val legalHolidayWorkTime = holidayWorkTime.getLegalHolidayWorkTime(weekPeriod);
		
		// 対象時間に休出時間を加算する
		domain.targetTime = domain.targetTime.addMinutes(legalHolidayWorkTime.v());
		
		// 休暇加算時間を取得する
		val vacationUseTime = aggregateTotalWorkingTime.getVacationUseTime();
		val addVacationTime = GetVacationAddTime.getTime(weekPeriod, vacationUseTime, addSet);
		
		// 対象時間に休暇加算時間を加算する
		domain.targetTime = domain.targetTime.addMinutes(addVacationTime.v());

		return domain;
	}
}
