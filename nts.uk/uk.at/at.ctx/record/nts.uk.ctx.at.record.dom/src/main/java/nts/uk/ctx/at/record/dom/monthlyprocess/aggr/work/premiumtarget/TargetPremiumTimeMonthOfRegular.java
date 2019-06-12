package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 通常勤務の月割増対象時間
 * @author shuichi_ishida
 */
@Getter
public class TargetPremiumTimeMonthOfRegular {

	/** 月割増対象時間 */
	private AttendanceTimeMonth targetPremiumTimeMonth;
	
	/**
	 * コンストラクタ
	 */
	public TargetPremiumTimeMonthOfRegular(){
		
		this.targetPremiumTimeMonth = new AttendanceTimeMonth(0);
	}
	
	/**
	 * 通常勤務の月割増時間の対象となる時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @return 加算した休暇使用時間
	 */
	public AddedVacationUseTime askPremiumTimeMonth(String companyId, String employeeId, DatePeriod datePeriod,
			AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime){

		// 集計対象時間を取得する
		val workTimeOfMonthly = aggregateTotalWorkingTime.getWorkTime();
		val workTime = workTimeOfMonthly.getAggregateTargetTime(datePeriod, addSet);

		// 通常勤務の月割増対象時間に集計対象時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(workTime.v());
		
		// 法定内残業時間を取得する
		val overTime = aggregateTotalWorkingTime.getOverTime();
		val legalOverTime = overTime.calcOverTimeForPremium(datePeriod, aggregateTotalWorkingTime);
		
		// 通常勤務の月割増対象時間に残業時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(legalOverTime.v());

		// 法定内休出時間を取得する
		val holidayWorkTime = aggregateTotalWorkingTime.getHolidayWorkTime();
		val legalHolidayWorkTime = holidayWorkTime.getLegalHolidayWorkTime(datePeriod);
		
		// 通常勤務の月割増対象時間に休出時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(legalHolidayWorkTime.v());
		
		// 休暇加算時間を取得する
		val vacationUseTime = aggregateTotalWorkingTime.getVacationUseTime();
		val addVacationTime = GetVacationAddTime.getTime(datePeriod, vacationUseTime, addSet);
		
		// 通常勤務の月割増対象時間に休暇加算時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(addVacationTime.v());
		
		// 加算した「休暇加算時間」を「加算した休暇使用時間」に退避しておく
		val addedHolidayUseTime = AddedVacationUseTime.of(addVacationTime);
		
		return addedHolidayUseTime;
	}
}
