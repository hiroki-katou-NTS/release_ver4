package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：フレックス不足から年休と欠勤を控除する
 * @author shuichu_ishida
 */
@Stateless
public class DeductFromFlexShortageImpl implements DeductFromFlexShortage {

	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	
	/** フレックス不足から年休と欠勤を控除する */
	@Override
	public DeductFromFlexShortageValue calc(String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod period, AttendanceDaysMonth annualLeaveDeductDays,
			AttendanceTimeMonth absenceDeductTime) {
		
		DeductFromFlexShortageValue returnValue = new DeductFromFlexShortageValue();
		MonthlyCalculation monthlyCalculation = new MonthlyCalculation();
		
		// 労働条件項目を取得する
		val workConditionItemOpt =
				this.repositories.getWorkingConditionItem().getBySidAndStandardDate(employeeId, period.end());
		if (!workConditionItemOpt.isPresent()){
			returnValue.getErrorMessageIds().add("not exist WorkingConditionItem");
			return returnValue;
		}
		val workConditionItem = workConditionItemOpt.get();
		
		// 履歴ごとに月別実績を集計する
		monthlyCalculation.prepareAggregation(companyId, employeeId, yearMonth, closureId, closureDate,
				period, workConditionItem, Optional.empty(), this.repositories);
		monthlyCalculation.aggregate(period, MonthlyAggregateAtr.MONTHLY,
				Optional.of(annualLeaveDeductDays), Optional.of(absenceDeductTime), this.repositories);
		
		// 年休控除日数を時間換算する　→　控除前の年休控除時間
		val flexTime = monthlyCalculation.getFlexTime();
		DeductDaysAndTime beforeDeduct = new DeductDaysAndTime(
				flexTime.getFlexShortDeductTime().getAnnualLeaveDeductDays(), new AttendanceTimeMonth(0));
		beforeDeduct.timeConversionOfDeductAnnualLeaveDays(companyId, employeeId, period,
				workConditionItem, this.repositories);
		if (beforeDeduct.getErrorMessageIds().size() > 0){
			// エラー発生時
			returnValue.getErrorMessageIds().addAll(beforeDeduct.getErrorMessageIds());
			return returnValue;
		}
		if (!beforeDeduct.getPredetermineTimeSetOfWeekDay().isPresent()) return returnValue;
		val predetermineTimeSet = beforeDeduct.getPredetermineTimeSetOfWeekDay().get();
		
		// 控除した時間を求める
		AttendanceTimeMonth deductedTime = beforeDeduct.getAnnualLeaveDeductTime();
		val afterDeduct = flexTime.getDeductDaysAndTime();
		deductedTime = deductedTime.minusMinutes(afterDeduct.getAnnualLeaveDeductTime().v());

		// 控除した時間を年休使用時間に加算する
		val annualLeave = monthlyCalculation.getTotalWorkingTime().getVacationUseTime().getAnnualLeave();
		annualLeave.addMinuteToUseTime(deductedTime.v());

		// 控除時間が余分に入力されていないか確認する
		val predAddTimeAM = predetermineTimeSet.getPredTime().getAddTime().getMorning();
		boolean isExtraTime = false;
		if (afterDeduct.getAnnualLeaveDeductTime().greaterThanOrEqualTo(predAddTimeAM.v())){
			isExtraTime = true;
		}
		else if (afterDeduct.getAbsenceDeductTime().greaterThan(0)){
			isExtraTime = true;
		}
		if (isExtraTime){
			
			// 「余分な控除時間のエラーフラグ」をtrueにする
			flexTime.getFlexShortDeductTime().setErrorAtrOfExtraDeductTime(true);
		}
		
		// 「月別実績の月の計算」を返す
		returnValue.setMonthlyCalculation(monthlyCalculation);
		return returnValue;
	}
}
