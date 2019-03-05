package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime;

import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.VacationUseTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 休暇加算時間を取得する
 * @author shuichi_ishida
 */
public class GetVacationAddTime {

	/**
	 * 休暇加算時間を取得する
	 * @param datePeriod 期間
	 * @param vacationUseTime 休暇使用時間
	 * @param addSet 加算設定
	 * @return 休暇加算時間
	 */
	public static AttendanceTimeMonth getTime(DatePeriod datePeriod,
			VacationUseTimeOfMonthly vacationUseTime, AddSet addSet){
		
		AttendanceTimeMonth vacationAddTime = new AttendanceTimeMonth(0);
		
		if (addSet.isAnnualLeave()){
			
			// 休暇加算時間に年休使用時間を加算する
			vacationAddTime = vacationAddTime.addMinutes(
					vacationUseTime.getAnnualLeave().getTotalUseTime(datePeriod).v());
		}
		
		if (addSet.isRetentionYearly()){
			
			// 休暇加算時間に積立年休使用時間を加算する
			vacationAddTime = vacationAddTime.addMinutes(
					vacationUseTime.getRetentionYearly().getTotalUseTime(datePeriod).v());
		}
		
		// 大塚モードの確認
		if (false) {
			
			if (addSet.isSpecialHoliday()){
				
				// 休暇加算時間に特別休暇使用時間を加算する
				vacationAddTime = vacationAddTime.addMinutes(
						vacationUseTime.getSpecialHoliday().getTotalUseTime(datePeriod).v());
			}
		}

		return vacationAddTime;
	}
}
