package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 変形労働勤務の月割増対象時間
 * @author shuichi_ishida
 */
@Getter
public class TargetPremiumTimeMonthOfIrregular {

	/** 月割増対象時間 */
	private AttendanceTimeMonth targetPremiumTimeMonth;
	/** 加算した休暇使用時間 */
	private AttendanceTimeMonth addedVacationUseTime;
	
	/**
	 * コンストラクタ
	 */
	public TargetPremiumTimeMonthOfIrregular(){
		
		this.targetPremiumTimeMonth = new AttendanceTimeMonth(0);
		this.addedVacationUseTime = new AttendanceTimeMonth(0);
	}
	
	/**
	 * 変形労働勤務の月割増時間の対象となる時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param isAddVacation 休暇加算　（true=する）
	 * @return 加算した休暇使用時間
	 */
	public void askPremiumTimeMonth(String companyId, String employeeId, DatePeriod datePeriod,
			AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime, boolean isAddVacation){

		// 集計対象時間を取得する
		val legalTime = aggregateTotalWorkingTime.getWorkTime().getAggregateTargetTime(datePeriod);

		// 変形労働勤務の月割増対象時間に集計対象時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(legalTime.v());
		
		// 法定内残業時間を取得する
		val legalOverTime = aggregateTotalWorkingTime.getOverTime().getLegalOverTime(datePeriod);
		
		// 変形労働勤務の月割増対象時間に残業時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(legalOverTime.v());

		// 法定内休出時間を取得する
		val legalHolidayWorkTime = aggregateTotalWorkingTime.getHolidayWorkTime().getLegalHolidayWorkTime(datePeriod);
		
		// 変形労働勤務の月割増対象時間に休出時間を加算する
		this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(legalHolidayWorkTime.v());
		
		// 休暇加算を確認する
		if (isAddVacation){
			
			// 休暇加算時間を取得する
			val vacationAddTime = GetVacationAddTime.getTime(
					datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSet);
			
			// 変形労働勤務の月割増対象時間に休暇加算時間を加算する
			this.targetPremiumTimeMonth = this.targetPremiumTimeMonth.addMinutes(vacationAddTime.v());
			
			// 加算した時間を加算した休暇使用時間に退避しておく
			this.addedVacationUseTime = vacationAddTime;
		}
	}
}
