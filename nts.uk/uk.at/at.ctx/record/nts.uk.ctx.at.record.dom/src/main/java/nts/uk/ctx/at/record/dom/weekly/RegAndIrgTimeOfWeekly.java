package nts.uk.ctx.at.record.dom.weekly;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByDefo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByReg;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.TargetPremiumTimeWeekOfIrregular;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.TargetPremiumTimeWeekOfRegular;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.WeekStart;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 週別の通常変形時間
 * @author shuichi_ishida
 */
@Getter
public class RegAndIrgTimeOfWeekly implements Cloneable {

	/** 週割増合計時間 */
	private AttendanceTimeMonth weeklyTotalPremiumTime;
	
	/** 週割増処理期間 */
	private DatePeriod weekPremiumProcPeriod;
	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
	/**
	 * コンストラクタ
	 */
	public RegAndIrgTimeOfWeekly(){
		
		this.weeklyTotalPremiumTime = new AttendanceTimeMonth(0);
		
		this.weekPremiumProcPeriod = new DatePeriod(GeneralDate.min(), GeneralDate.min());
		this.errorInfos = new ArrayList<>();
	}
	
	/**
	 * ファクトリー
	 * @param weeklyTotalPremiumTime 週割増合計時間
	 * @return 週別の通常変形時間
	 */
	public static RegAndIrgTimeOfWeekly of(
			AttendanceTimeMonth weeklyTotalPremiumTime){

		RegAndIrgTimeOfWeekly domain = new RegAndIrgTimeOfWeekly();
		domain.weeklyTotalPremiumTime = weeklyTotalPremiumTime;
		return domain;
	}
	
	@Override
	public RegAndIrgTimeOfWeekly clone() {
		RegAndIrgTimeOfWeekly cloned = new RegAndIrgTimeOfWeekly();
		try {
			cloned.weeklyTotalPremiumTime = new AttendanceTimeMonth(this.weeklyTotalPremiumTime.v());
		}
		catch (Exception e){
			throw new RuntimeException("RegAndIrgTimeOfWeekly clone error.");
		}
		return cloned;
	}
	
	/**
	 * 週割増時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週期間
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param settingsByReg 通常勤務が必要とする設定
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param weekStart 週開始
	 * @param premiumTimeOfPrevMonLast 前月の最終週の週割増対象時間
	 */
	public void aggregatePremiumTime(
			String companyId,
			String employeeId,
			DatePeriod weekPeriod,
			WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr,
			SettingRequiredByReg settingsByReg,
			SettingRequiredByDefo settingsByDefo,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			WeekStart weekStart,
			AttendanceTimeMonth premiumTimeOfPrevMonLast){

		// 週割増を集計する期間を求める
		this.weekPremiumProcPeriod = new DatePeriod(weekPeriod.start(), weekPeriod.end());

		// 労働制を確認する
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			
			// 加算設定　取得　（割増用）
			val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByReg.getHolidayAdditionMap());
			if (addSet.getErrorInfo().isPresent()){
				this.errorInfos.add(addSet.getErrorInfo().get());
			}
			
			// 「週割増・月割増を求める」を取得する
			boolean isAskPremium = false;
			if (aggregateAtr == MonthlyAggregateAtr.MONTHLY){
				isAskPremium = settingsByReg.getRegularAggrSet().getAggregateTimeSet().getSurchargeWeekMonth();
			}
			if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK){
				isAskPremium = settingsByReg.getRegularAggrSet().getExcessOutsideTimeSet().getSurchargeWeekMonth();
			}
			if (isAskPremium){
				
				// 通常勤務の週割増時間を集計する
				this.aggregateWeeklyPremiumTimeOfRegular(companyId, employeeId, this.weekPremiumProcPeriod,
						addSet, aggregateTotalWorkingTime, settingsByReg.getStatutoryWorkingTimeWeek(),
						weekStart, premiumTimeOfPrevMonLast);
			}
		}
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){

			// 加算設定　取得　（割増用）
			val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByDefo.getHolidayAdditionMap());
			if (addSet.getErrorInfo().isPresent()){
				this.errorInfos.add(addSet.getErrorInfo().get());
			}
			
			// 変形労働勤務の週割増時間を集計する
			this.aggregateWeeklyPremiumTimeOfIrregular(companyId, employeeId, this.weekPremiumProcPeriod,
					addSet, aggregateTotalWorkingTime, settingsByDefo.getStatutoryWorkingTimeWeek(),
					weekStart, premiumTimeOfPrevMonLast);
		}
	}

	/**
	 * 通常勤務の週割増時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週割増処理期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param weekStart 週開始
	 * @param premiumTimeOfPrevMonLast 前月の最終週の週割増対象時間
	 */
	private void aggregateWeeklyPremiumTimeOfRegular(
			String companyId,
			String employeeId,
			DatePeriod weekPeriod,
			AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			WeekStart weekStart,
			AttendanceTimeMonth premiumTimeOfPrevMonLast){
		
		// 通常勤務の週割増時間の対象となる時間を求める
		val targetPremiumTimeWeekOfReg = TargetPremiumTimeWeekOfRegular.askPremiumTimeWeek(
				companyId, employeeId, weekPeriod, addSet, aggregateTotalWorkingTime, premiumTimeOfPrevMonLast);
		val targetPremiumTimeWeek = targetPremiumTimeWeekOfReg.getPremiumTimeWeek();
		
		AttendanceTimeMonth targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingTimeWeek.v());
		
		// 法定労働時間を按分するか確認する　（期間が7日未満　の時、按分する）
		if (weekStart == WeekStart.TighteningStartDate){
			boolean isDistribute = false;
			val periodDays = weekPeriod.start().daysTo(weekPeriod.end()) + 1;
			if (periodDays < 7) isDistribute = true;
			if (isDistribute){
				int statutoryWorkingMinutesDay = statutoryWorkingTimeWeek.v() / 7;
				targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingMinutesDay * periodDays);
			}
		}
		
		// 週割増対象時間と法定労働時間を比較する
		if (targetPremiumTimeWeek.lessThanOrEqualTo(targetStatutoryWorkingTime)) return;
		
		// 週割増対象時間が法定労働時間を超えた分だけ週単位の週割増時間に入れる
		int weekUnit = targetPremiumTimeWeek.v() - targetStatutoryWorkingTime.v();
		
		// 「週単位の週割増時間」と「当月の週割増対象時間」を比較する
		if (weekUnit > targetPremiumTimeWeekOfReg.getPremiumTimeOfCurrentMonth().v()){
			
			// 「週単位の週割増時間」に「当月の週割増対象時間」を入れる　（当月の週割増対象時間を上限とする）
			weekUnit = targetPremiumTimeWeekOfReg.getPremiumTimeOfCurrentMonth().v();
		}
			
		// 週単位の週割増時間を週割増合計時間に加算する
		this.weeklyTotalPremiumTime = new AttendanceTimeMonth(weekUnit);
	}

	/**
	 * 変形労働勤務の週割増時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param weekStart 週開始
	 * @param premiumTimeOfPrevMonLast 前月の最終週の週割増対象時間
	 */
	private void aggregateWeeklyPremiumTimeOfIrregular(
			String companyId,
			String employeeId,
			DatePeriod weekPeriod,
			AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			WeekStart weekStart,
			AttendanceTimeMonth premiumTimeOfPrevMonLast){
		
		// 変形労働勤務の週割増時間の対象となる時間を求める
		val targetPremiumTimeWeekOfIrg = TargetPremiumTimeWeekOfIrregular.askPremiumTimeWeek(
				companyId, employeeId, weekPeriod, addSet, aggregateTotalWorkingTime, true,
				premiumTimeOfPrevMonLast);
		val targetPremiumTimeWeek = targetPremiumTimeWeekOfIrg.getPremiumTimeWeek();

		// （実績）所定労働時間を取得する
		val prescribedWorkingTime = aggregateTotalWorkingTime.getPrescribedWorkingTime();
		val recordPresctibedWorkingTime = prescribedWorkingTime.getTotalRecordPrescribedWorkingTime(weekPeriod);
		
		AttendanceTimeMonth targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingTimeWeek.v());
		
		// 法定労働時間を按分するか確認する　（期間が7日未満　の時、按分する）
		if (weekStart == WeekStart.TighteningStartDate){
			boolean isDistribute = false;
			val periodDays = weekPeriod.start().daysTo(weekPeriod.end()) + 1;
			if (periodDays < 7) isDistribute = true;
			if (isDistribute){
				int statutoryWorkingMinutesDay = statutoryWorkingTimeWeek.v() / 7;
				targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingMinutesDay * periodDays);
			}
		}
		
		// 法定労働時間と所定労働時間を比較する
		int weekUnit = 0;
		if (targetStatutoryWorkingTime.greaterThanOrEqualTo(recordPresctibedWorkingTime.v())){
			
			// 週割増対象時間と法定労働時間を比較する
			if (targetPremiumTimeWeek.lessThanOrEqualTo(targetStatutoryWorkingTime.v())) return;
			
			// 週割増対象時間が法定労働時間を超えた分だけ週単位の週割増時間に入れる
			weekUnit = targetPremiumTimeWeek.v() - targetStatutoryWorkingTime.v();
		}
		else {
			
			// 週割増対象時間と所定労働時間を比較する
			if (targetPremiumTimeWeek.lessThanOrEqualTo(recordPresctibedWorkingTime.v())) return;
			
			// 週割増対象時間が所定労働時間を超えた分だけ週単位の週割増時間に入れる
			weekUnit = targetPremiumTimeWeek.v() - recordPresctibedWorkingTime.v();
		}
		
		// 「週単位の週割増時間」と「当月の週割増対象時間」を比較する
		if (weekUnit > targetPremiumTimeWeekOfIrg.getPremiumTimeOfCurrentMonth().v()){
			
			// 「週単位の週割増時間」から「前月の最終週の週割増時間」を引く
			weekUnit -= targetPremiumTimeWeekOfIrg.getPremiumTimeOfPrevMonth().v();
		}
			
		// 週単位の週割増時間を週割増合計時間に加算する
		this.weeklyTotalPremiumTime = new AttendanceTimeMonth(weekUnit);
	}
}
