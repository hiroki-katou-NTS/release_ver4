package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtion;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 当月の変形期間繰越時間
 * @author shuichu_ishida
 */
@Getter
public class IrregularPeriodCarryforwardsTimeOfCurrent {

	/** 時間 */
	private AttendanceTimeMonthWithMinus time;
	/** 加算した休暇使用時間 */
	private AttendanceTimeMonth addedVacationUseTime;
	
	/**
	 * コンストラクタ
	 */
	public IrregularPeriodCarryforwardsTimeOfCurrent(){
		
		this.time = new AttendanceTimeMonthWithMinus(0);
		this.addedVacationUseTime = new AttendanceTimeMonth(0);
	}
	
	/**
	 * 変形期間繰越時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param weeklyTotalPremiumTime 週割増合計時間
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 */
	public void aggregate(String companyId, String employeeId, DatePeriod datePeriod,
			AttendanceTimeMonth weeklyTotalPremiumTime,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeMonth){

		val targetPremiumTimeMonth = new TargetPremiumTimeMonth();
		
		// 加算設定　取得　（割増用）
		val addSet = GetAddSet.get(WorkingSystem.VARIABLE_WORKING_TIME_WORK, PremiumAtr.PREMIUM, holidayAdditionOpt);
		
		// 「変形労働勤務の加算設定」を取得する
		switch (this.getAddMethod(holidayAdditionOpt)){
		
		case ADD_FOR_SHORTAGE:
			// 月割増対象時間（休暇加算前）を求める
			targetPremiumTimeMonth.askTime(companyId, employeeId, datePeriod, weeklyTotalPremiumTime,
					addSet, aggregateTotalWorkingTime, statutoryWorkingTimeMonth, false);
			val beforeAddVacation = targetPremiumTimeMonth.getTime();
			
			// 月割増対象時間（休暇加算前）を〃（休暇加算後）に入れる
			AttendanceTimeMonthWithMinus afterAddVacation = new AttendanceTimeMonthWithMinus(
					beforeAddVacation.v());
			
			// 月割増対象時間（休暇加算後）＜0 の時、不足分を加算する
			if (afterAddVacation.lessThan(0)) {
				
				// 加算設定　取得　（不足時用）
				val addSetWhenShortage = GetAddSet.get(
						WorkingSystem.VARIABLE_WORKING_TIME_WORK, PremiumAtr.WHEN_SHORTAGE, holidayAdditionOpt);
				
				// 加算する休暇時間を取得する
				AttendanceTimeMonth vacationAddTime = GetVacationAddTime.getTime(
						datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenShortage);
				
				// 月割増対象時間（休暇加算後）に、休暇加算時間を加算する
				afterAddVacation = afterAddVacation.addMinutes(vacationAddTime.v());
				
				// 休暇加算後が 0 を超えていたら、0にする　（加算した休暇使用時間を、0 になるまで加算した時間に補正する）
				if (afterAddVacation.greaterThan(0)){
					vacationAddTime = vacationAddTime.minusMinutes(afterAddVacation.v());
					afterAddVacation = new AttendanceTimeMonthWithMinus(0);
				}
				
				// 加算した時間を、加算した休暇使用時間に退避しておく
				this.addedVacationUseTime = new AttendanceTimeMonth(vacationAddTime.v());
			}
	
			// 月割増対象時間（休暇加算後）を当月の変形期間繰越時間に入れる
			this.time = new AttendanceTimeMonthWithMinus(afterAddVacation.v());
			break;
			
		case NOT_ADD:
			// 月割増対象時間（休暇加算後）を求める　（休暇加算しない）
			targetPremiumTimeMonth.askTime(companyId, employeeId, datePeriod, weeklyTotalPremiumTime,
					addSet, aggregateTotalWorkingTime, statutoryWorkingTimeMonth, false);
			this.time = new AttendanceTimeMonthWithMinus(
					targetPremiumTimeMonth.getTime().v());
			this.addedVacationUseTime = new AttendanceTimeMonth(0);
			break;
			
		case ADD:
			// 月割増対象時間（休暇加算後）を求める　（休暇加算する）
			targetPremiumTimeMonth.askTime(companyId, employeeId, datePeriod, weeklyTotalPremiumTime,
					addSet, aggregateTotalWorkingTime, statutoryWorkingTimeMonth, true);
			this.time = new AttendanceTimeMonthWithMinus(
					targetPremiumTimeMonth.getTime().v());
			this.addedVacationUseTime = new AttendanceTimeMonth(
					targetPremiumTimeMonth.getAddedVacationUseTime().v());
			break;
		}
	}

	/**
	 * 加算方法
	 * @author shuichu_ishida
	 */
	private enum ProcAtrAddMethod{
		ADD,
		NOT_ADD,
		ADD_FOR_SHORTAGE;
	}
	
	/**
	 * 変形労働勤務の加算設定（加算方法）を取得する
	 * @param holidayAdditionOpt
	 */
	private ProcAtrAddMethod getAddMethod(Optional<HolidayAddtion> holidayAdditionOpt){
	
		// 変形労働勤務の加算設定を取得する
		if (!holidayAdditionOpt.isPresent()) return ProcAtrAddMethod.ADD;
		val addSetOfIrg = holidayAdditionOpt.get().getIrregularWork();
		
		// 割増計算方法．加算する＝「加算しない」
		if (addSetOfIrg.getAdditionTime1() == 0){
			// 就業時間計算方法．加算する＝「加算する」
			if (addSetOfIrg.getAdditionTime2() == 1){
				// 不足時加算
				return ProcAtrAddMethod.ADD_FOR_SHORTAGE;
			}
			else {
				// 加算しない
				return ProcAtrAddMethod.NOT_ADD;
			}
		}
		// 加算する
		return ProcAtrAddMethod.ADD;
	}
}
