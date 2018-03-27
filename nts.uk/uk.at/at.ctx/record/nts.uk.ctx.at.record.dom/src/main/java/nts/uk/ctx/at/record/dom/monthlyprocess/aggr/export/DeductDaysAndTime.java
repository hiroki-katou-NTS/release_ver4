package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 控除の日数と時間
 * @author shuichu_ishida
 */
@Getter
public class DeductDaysAndTime {

	/** 年休控除日数 */
	private AttendanceDaysMonth annualLeaveDeductDays;
	/** 年休控除時間 */
	private AttendanceTimeMonth annualLeaveDeductTime;
	/** 欠勤控除時間 */
	private AttendanceTimeMonth absenceDeductTime;

	/** 所定時間設定（平日時就業） */
	private Optional<PredetemineTimeSetting> predetermineTimeSetOfWeekDay;
	/** エラーメッセージID */
	private List<String> errorMessageIds;
	
	/**
	 * コンストラクタ
	 * @param annualLeaveDeductDays 年休控除日数
	 * @param absenceDeductTime 欠勤控除時間
	 */
	public DeductDaysAndTime(AttendanceDaysMonth annualLeaveDeductDays, AttendanceTimeMonth absenceDeductTime){
		
		this.annualLeaveDeductDays = annualLeaveDeductDays;
		this.annualLeaveDeductTime = new AttendanceTimeMonth(0);
		this.absenceDeductTime = absenceDeductTime;
	}
	
	/**
	 * 年休控除日数を時間換算する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param workingConditionItem 労働条件項目
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void timeConversionOfDeductAnnualLeaveDays(
			String companyId,
			String employeeId,
			DatePeriod period,
			WorkingConditionItem workingConditionItem,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		AttendanceTimeMonth convertTime = new AttendanceTimeMonth(0);
		
		// 平日時就業時間帯コードを取得する
		val workTimeCdOpt = workingConditionItem.getWorkCategory().getWeekdayTime().getWorkTimeCode();
		if (!workTimeCdOpt.isPresent()){
			this.errorMessageIds.add("not exist WorkTimeCode");
			return;
		}
		val workTimeCd = workTimeCdOpt.get().v();
		
		// 「所定時間設定．就業加算時間」を取得する
		this.predetermineTimeSetOfWeekDay =
				repositories.getPredetermineTimeSet().findByWorkTimeCode(companyId, workTimeCd);
		if (!this.predetermineTimeSetOfWeekDay.isPresent()){
			
			// エラー処理
			//ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
			//		new ErrMessageResource("011"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
			//		new ErrMessageContent(TextResource.localize("Msg_1011")));
			this.errorMessageIds.add("not exist PredetermineTimeSetting");
			return;
		}
		val predetermineTimeSet = this.predetermineTimeSetOfWeekDay.get();
		val addTime = predetermineTimeSet.getPredTime().getAddTime();
		
		// 「年休控除日数」の実数部分を時間換算する
		int intDays = this.annualLeaveDeductDays.v().intValue();
		convertTime = convertTime.addMinutes(intDays * addTime.getOneDay().v());
		
		// 小数部分があれば、就業加算時間．午前を加算する
		double fromIntDays = (double)intDays;
		if (this.annualLeaveDeductDays.v().doubleValue() != fromIntDays){
			convertTime = convertTime.addMinutes(addTime.getMorning().v());
		}
		
		// 「年休控除時間」に入れる
		this.annualLeaveDeductTime = convertTime;
	}
	
	/**
	 * 年休控除時間から分を引く
	 * @param minutes 分
	 */
	public void minusMinutesToAnnualLeaveDeductTime(int minutes){
		int applyMinutes = minutes;
		if (applyMinutes > this.annualLeaveDeductTime.v()) applyMinutes = this.annualLeaveDeductTime.v();
		this.annualLeaveDeductTime = this.annualLeaveDeductTime.minusMinutes(applyMinutes);
	}
	
	/**
	 * 欠勤控除時間から分を引く
	 * @param minutes 分
	 */
	public void minusMinutesToAbsenceDeductTime(int minutes){
		int applyMinutes = minutes;
		if (applyMinutes > this.absenceDeductTime.v()) applyMinutes = this.absenceDeductTime.v();
		this.absenceDeductTime = this.absenceDeductTime.minusMinutes(applyMinutes);
	}
}
