package nts.uk.ctx.at.record.dom.monthly.agreement;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 36協定時間内訳
 * @author shuichu_ishida
 */
@Getter
public class AgreementTimeBreakdown {

	/** 残業時間 */
	private AttendanceTimeMonth overTime;
	/** 振替残業時間 */
	private AttendanceTimeMonth transferOverTime;
	/** 休出時間 */
	private AttendanceTimeMonth holidayWorkTime;
	/** 振替時間 */
	private AttendanceTimeMonth transferTime;
	/** フレックス超過時間 */
	private AttendanceTimeMonth flexExcessTime;
	/** 所定内割増時間 */
	private AttendanceTimeMonth withinPrescribedPremiumTime;
	/** 週割増時間 */
	private AttendanceTimeMonth weeklyPremiumTime;
	/** 月割増時間 */
	private AttendanceTimeMonth monthlyPremiumTime;
	
	/**
	 * コンストラクタ
	 */
	public AgreementTimeBreakdown(){
		
		this.overTime = new AttendanceTimeMonth(0);
		this.transferOverTime = new AttendanceTimeMonth(0);
		this.holidayWorkTime = new AttendanceTimeMonth(0);
		this.transferTime = new AttendanceTimeMonth(0);
		this.flexExcessTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		this.weeklyPremiumTime = new AttendanceTimeMonth(0);
		this.monthlyPremiumTime = new AttendanceTimeMonth(0);
	}
	
	/**
	 * ファクトリー
	 * @param overTime 残業時間
	 * @param transferOverTime 振替残業時間
	 * @param holidayWorkTime 休出時間
	 * @param transferTime 振替時間
	 * @param flexExcessTime フレックス超過時間
	 * @param withinPrescribedPremiumTime 所定内割増時間
	 * @param weeklyPremiumTime 週割増時間
	 * @param monthlyPremiumTime 月割増時間
	 * @return 36協定時間内訳
	 */
	public static AgreementTimeBreakdown of(
			AttendanceTimeMonth overTime,
			AttendanceTimeMonth transferOverTime,
			AttendanceTimeMonth holidayWorkTime,
			AttendanceTimeMonth transferTime,
			AttendanceTimeMonth flexExcessTime,
			AttendanceTimeMonth withinPrescribedPremiumTime,
			AttendanceTimeMonth weeklyPremiumTime,
			AttendanceTimeMonth monthlyPremiumTime){
		
		AgreementTimeBreakdown domain = new AgreementTimeBreakdown();
		domain.overTime = overTime;
		domain.transferOverTime = transferOverTime;
		domain.holidayWorkTime = holidayWorkTime;
		domain.transferTime = transferTime;
		domain.flexExcessTime = flexExcessTime;
		domain.withinPrescribedPremiumTime = withinPrescribedPremiumTime;
		domain.weeklyPremiumTime = weeklyPremiumTime;
		domain.monthlyPremiumTime = monthlyPremiumTime;
		return domain;
	}
	
	/**
	 * 36協定時間の対象項目を取得
	 * @param aggregateAtr 集計区分
	 * @param monthlyCalculation 月別実績の月の計算
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void getTargetItemOfAgreement(
			MonthlyAggregateAtr aggregateAtr,
			MonthlyCalculation monthlyCalculation,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		val companyId = monthlyCalculation.getCompanyId();
		
		// 集計結果　初期化
		this.overTime = new AttendanceTimeMonth(0);
		this.transferOverTime = new AttendanceTimeMonth(0);
		this.holidayWorkTime = new AttendanceTimeMonth(0);
		this.transferTime = new AttendanceTimeMonth(0);
		this.flexExcessTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		this.weeklyPremiumTime = new AttendanceTimeMonth(0);
		this.monthlyPremiumTime = new AttendanceTimeMonth(0);
		
		// 丸め設定取得
		RoundingSetOfMonthly roundingSet = new RoundingSetOfMonthly(companyId);
		val roundingSetOpt = repositories.getRoundingSetOfMonthly().find(companyId);
		if (roundingSetOpt.isPresent()) roundingSet = roundingSetOpt.get();
		
		// 「時間帯超過設定」を取得
		val outsideOTSetOpt = repositories.getOutsideOTSet().findById(companyId);
		if (!outsideOTSetOpt.isPresent()) return;
		val outsideOTSet = outsideOTSetOpt.get();
		for (val attendanceItemId : outsideOTSet.getAllAttendanceItemIds()){
			
			// 対象項目の時間を取得　と　丸め処理
			val targetItemTime = monthlyCalculation.getTimeOfAttendanceItemId(attendanceItemId, roundingSet);
			
			// 勤怠項目IDに対応する時間を加算する
			this.addTimeByAttendanceItemId(attendanceItemId, targetItemTime);
		}
	}
	
	/**
	 * 勤怠項目IDに対応する時間を加算する
	 * @param attendanceItemId 勤怠項目ID
	 * @param targetItemTime 対象項目の時間
	 */
	private void addTimeByAttendanceItemId(int attendanceItemId, AttendanceTimeMonth targetItemTime){
		
		// 残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.OVER_TIME_10.value){
			this.overTime = this.overTime.addMinutes(targetItemTime.v());
		}
		
		// 計算残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_OVER_TIME_10.value){
			this.overTime = this.overTime.addMinutes(targetItemTime.v());
		}
		
		// 振替残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_10.value){
			this.transferOverTime = this.transferOverTime.addMinutes(targetItemTime.v());
		}
		
		// 計算振替残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_10.value){
			this.transferOverTime = this.transferOverTime.addMinutes(targetItemTime.v());
		}
		
		// 休出時間
		if (attendanceItemId >= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_10.value){
			this.holidayWorkTime = this.holidayWorkTime.addMinutes(targetItemTime.v());
		}
		
		// 計算休出時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_10.value){
			this.holidayWorkTime = this.holidayWorkTime.addMinutes(targetItemTime.v());
		}
		
		// 振替時間
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_TIME_10.value){
			this.transferTime = this.transferTime.addMinutes(targetItemTime.v());
		}
		
		// 計算振替時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_10.value){
			this.transferTime = this.transferTime.addMinutes(targetItemTime.v());
		}
		
		// フレックス超過時間
		if (attendanceItemId == AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value){
			this.flexExcessTime = this.flexExcessTime.addMinutes(targetItemTime.v());
		}
		
		// 所定内割増時間
		if (attendanceItemId == AttendanceItemOfMonthly.WITHIN_PRESCRIBED_PREMIUM_TIME.value){
			this.withinPrescribedPremiumTime = this.withinPrescribedPremiumTime.addMinutes(targetItemTime.v());
		}
		
		// 週割増合計時間
		if (attendanceItemId == AttendanceItemOfMonthly.WEEKLY_TOTAL_PREMIUM_TIME.value){
			this.weeklyPremiumTime = this.weeklyPremiumTime.addMinutes(targetItemTime.v());
		}
		
		// 月割増合計時間
		if (attendanceItemId == AttendanceItemOfMonthly.MONTHLY_TOTAL_PREMIUM_TIME.value){
			this.monthlyPremiumTime = this.monthlyPremiumTime.addMinutes(targetItemTime.v());
		}
	}
	
	/**
	 * 合計時間を取得
	 * @return 合計時間
	 */
	public AttendanceTimeMonth getTotalTime(){
		
		AttendanceTimeMonth totalTime = new AttendanceTimeMonth(0);
		totalTime = totalTime.addMinutes(this.overTime.v());
		totalTime = totalTime.addMinutes(this.transferOverTime.v());
		totalTime = totalTime.addMinutes(this.holidayWorkTime.v());
		totalTime = totalTime.addMinutes(this.transferTime.v());
		totalTime = totalTime.addMinutes(this.flexExcessTime.v());
		totalTime = totalTime.addMinutes(this.withinPrescribedPremiumTime.v());
		totalTime = totalTime.addMinutes(this.weeklyPremiumTime.v());
		totalTime = totalTime.addMinutes(this.monthlyPremiumTime.v());
		return totalTime;
	}
}
