package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggrSettingMonthlyOfFlx;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalOverTimeTransferOrderOfAggrMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.regularandirregular.TreatOverTimeOfLessThanCriteriaPerDay;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.workinformation.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.OverTimeAndTransferAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の残業時間
 * @author shuichi_ishida
 */
@Getter
public class OverTimeOfMonthly {

	/** 残業合計時間 */
	@Setter
	private TimeMonthWithCalculation totalOverTime;
	/** 事前残業時間 */
	private AttendanceTimeMonth beforeOverTime;
	/** 振替残業合計時間 */
	private TimeMonthWithCalculation totalTransferOverTime;
	/** 集計残業時間 */
	private Map<OverTimeFrameNo, AggregateOverTime> aggregateOverTimeMap;
	
	/**
	 * コンストラクタ
	 */
	public OverTimeOfMonthly(){
		
		this.totalOverTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeOverTime = new AttendanceTimeMonth(0);
		this.totalTransferOverTime = TimeMonthWithCalculation.ofSameTime(0);
		this.aggregateOverTimeMap = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param totalOverTime 残業合計時間
	 * @param beforeOverTime 事前残業時間
	 * @param totalTransferOverTime 振替残業時間
	 * @param aggregateOverTimeList 集計残業時間
	 * @return 月別実績の残業時間
	 */
	public static OverTimeOfMonthly of(
			TimeMonthWithCalculation totalOverTime,
			AttendanceTimeMonth beforeOverTime,
			TimeMonthWithCalculation totalTransferOverTime,
			List<AggregateOverTime> aggregateOverTimeList){
		
		val domain = new OverTimeOfMonthly();
		domain.totalOverTime = totalOverTime;
		domain.beforeOverTime = beforeOverTime;
		domain.totalTransferOverTime = totalTransferOverTime;
		for (AggregateOverTime aggregateOverTime : aggregateOverTimeList){
			val overTimeFrameNo = aggregateOverTime.getOverTimeFrameNo();
			domain.aggregateOverTimeMap.putIfAbsent(overTimeFrameNo, aggregateOverTime);
		}
		return domain;
	}
	
	/**
	 * 対象の集計残業時間を取得する
	 * @param overTimeFrameNo 残業枠NO
	 * @return 対象の集計残業時間
	 */
	private AggregateOverTime getTargetAggregateOverTime(OverTimeFrameNo overTimeFrameNo){
		
		this.aggregateOverTimeMap.putIfAbsent(overTimeFrameNo, new AggregateOverTime(overTimeFrameNo));
		return this.aggregateOverTimeMap.get(overTimeFrameNo);
	}
	
	/**
	 * 集計する　（通常・変形労働時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param workInfo 勤務情報
	 * @param legalOverTimeTransferOrder 法定内残業振替順
	 * @param treatOverTimeOfLessThanCriteriaPerDay 1日の基準時間未満の残業時間の扱い
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateForRegAndIrreg(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			WorkInformation workInfo,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrder,
			TreatOverTimeOfLessThanCriteriaPerDay treatOverTimeOfLessThanCriteriaPerDay,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 自動的に除く残業枠を確認する
		val autoExcludeOverTimeFrames = treatOverTimeOfLessThanCriteriaPerDay.getAutoExcludeOverTimeFrames();
		if (autoExcludeOverTimeFrames.isEmpty()) {
			// 0件なら、自動計算せず残業時間を集計する
			val legalOverTimeFrames = treatOverTimeOfLessThanCriteriaPerDay.getLegalOverTimeFrames();
			this.aggregateWithoutAutoCalc(attendanceTimeOfDaily, legalOverTimeFrames);
		}
		else {
			// 1件以上なら、自動計算して残業時間を集計する
			this.aggregateByAutoCalc(attendanceTimeOfDaily, companyId, workplaceId, employmentCd, workingSystem,
					workInfo, legalOverTimeTransferOrder, autoExcludeOverTimeFrames, repositories);
		}
	}
	
	/**
	 * 自動計算して集計する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param workInfo 勤務情報
	 * @param legalOverTimeTransferOrder 法定内残業振替順
	 * @param autoExcludeOverTimeFrameList 自動的に除く残業枠
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateByAutoCalc(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			WorkInformation workInfo,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrder,
			List<OverTimeFrameNo> autoExcludeOverTimeFrameList,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 法定内残業にできる時間を計算する
		AttendanceTime canLegalOverTime = this.calcLegalOverTime(attendanceTimeOfDaily,
				companyId, workplaceId, employmentCd, workingSystem, repositories);
		
		// 「残業枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessPrescibedTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val overTimeOfDaily = excessPrescibedTimeOfDaily.getOverTimeWork();
		// 残業時間がない時、集計しない
		if (!overTimeOfDaily.isPresent()) return;
		val overTimeFrameTimeSrcs = overTimeOfDaily.get().getOverTimeWorkFrameTime();
			
		// 残業枠時間リストをマップに組み換え　（枠での検索用）
		Map<OverTimeFrameNo, OverTimeFrameTime> overTimeFrameTimeMap = new HashMap<>();
		for (val overTimeFrameTimeSrc : overTimeFrameTimeSrcs){
			overTimeFrameTimeMap.putIfAbsent(overTimeFrameTimeSrc.getOverWorkFrameNo(), overTimeFrameTimeSrc);
		}
	
		// 残業・振替の処理順序を取得する
		val workTimeCode = workInfo.getWorkTimeCode().v();
		val overTimeAndTransferAtrs = repositories.getOverTimeAndTransferOrder().get(
				companyId, workTimeCode, false);
		
		// 残業・振替のループ
		for (val overTimeAndTransferAtr : overTimeAndTransferAtrs){
		
			// 残業枠時間のループ処理
			canLegalOverTime = this.overTimeFrameTimeProcess(overTimeAndTransferAtr,
					legalOverTimeTransferOrder, canLegalOverTime,
					autoExcludeOverTimeFrameList, overTimeFrameTimeMap, attendanceTimeOfDaily.getYmd());
		}
	}
	
	/**
	 * 自動計算せず集計する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param legalOverTimeFrameList 法定内の残業枠
	 */
	private void aggregateWithoutAutoCalc(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			List<OverTimeFrameNo> legalOverTimeFrameList){
		
		// 「残業枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val overTimeOfDaily = excessOfStatutoryTimeOfDaily.getOverTimeWork();
		// 残業時間がない時、集計しない
		if (!overTimeOfDaily.isPresent()) return;
		val overTimeFrameTimeSrcs = overTimeOfDaily.get().getOverTimeWorkFrameTime();
			
		// 取得した残業枠時間を「集計残業時間」に入れる
		for (val overTimeFrameSrc : overTimeFrameTimeSrcs){
			val overTimeFrameNo = overTimeFrameSrc.getOverWorkFrameNo(); 

			// 対象の集計残業時間を確認する
			val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
			val ymd = attendanceTimeOfDaily.getYmd();
			
			if (legalOverTimeFrameList.contains(overTimeFrameNo)){
				
				// 法定内の残業枠に該当する時、法定内残業時間に入れる
				targetAggregateOverTime.addLegalOverTimeInTimeSeriesWork(ymd, overTimeFrameSrc);
			}
			else {
				
				// 法定内の残業枠に該当しない時、残業時間に入れる
				targetAggregateOverTime.addOverTimeInTimeSeriesWork(ymd, overTimeFrameSrc);
			}
		}
	}
	
	/**
	 * 法定内残業に出来る時間を計算する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private AttendanceTime calcLegalOverTime(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			RepositoriesRequiredByMonthlyAggr repositories){
	
		// 日の法定労働時間を取得する
		//*****（未）　正式な処理の作成待ち。
		//DailyCalculationPersonalInformation dailyCalculationPersonalInformation =
		//		repositories.getGetOfStatutoryWorkTime().getDailyTimeFromStaturoyWorkTime(
		//			workingSystem,
		//			companyId,
		//			workplaceId,
		//			employmentCd,
		//			attendanceTimeOfDaily.getEmployeeId(),
		//			attendanceTimeOfDaily.getYmd());
		
		// 日別実績の法定内時間を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val legalTimeOfDaily = totalWorkingTime.getWithinStatutoryTimeOfDaily();
		
		// 法定内残業にできる時間を計算する
		//*****（未）　正式な処理が出来てから、代入。
		AttendanceTime canLegalOverTime = new AttendanceTime(0);
		//		new AttendanceTime(dailyCalculationPersonalInformation.getStatutoryWorkTime().v());
		canLegalOverTime = canLegalOverTime.minusMinutes(legalTimeOfDaily.getWorkTime().v());
		return canLegalOverTime;
	}
	
	/**
	 * 残業枠時間のループ処理
	 * @param overTimeAndTransferAtr 残業振替区分
	 * @param legalOverTimeTransferOrderOfAggrMonthly 法定内残業振替順
	 * @param canLegalOverTime 法定内残業に出来る時間
	 * @param autoExcludeOverTimeFrameList 自動的に除く残業枠
	 * @param overTimeFrameTimeMap 残業枠時間　（日別実績より取得）
	 * @param ymd 年月日
	 * @return 法定内残業に出来る時間　（計算後）
	 */
	private AttendanceTime overTimeFrameTimeProcess(
			OverTimeAndTransferAtr overTimeAndTransferAtr,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrderOfAggrMonthly,
			AttendanceTime canLegalOverTime,
			List<OverTimeFrameNo> autoExcludeOverTimeFrameList,
			Map<OverTimeFrameNo, OverTimeFrameTime> overTimeFrameTimeMap,
			GeneralDate ymd){
		
		AttendanceTime returnTime = new AttendanceTime(0);
		
		// 残業枠時間分ループ
		for (val legalOverTimeTransferOrder : legalOverTimeTransferOrderOfAggrMonthly.getLegalOverTimeTransferOrders()){
			val overTimeFrameNo = legalOverTimeTransferOrder.getOverTimeFrameNo();
			
			// 該当する（日別実績の）残業枠時間がなければ、次の枠へ
			if (!overTimeFrameTimeMap.containsKey(overTimeFrameNo)) continue;
			val overTimeFrameTime = overTimeFrameTimeMap.get(overTimeFrameNo);
			
			// 対象の時系列ワークを確認する
			val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
			val timeSeriesWork = targetAggregateOverTime.getAndPutTimeSeriesWork(ymd);
			
			// 自動的に除く残業枠か確認する
			if (autoExcludeOverTimeFrameList.contains(overTimeFrameNo)){
				
				// 取得した残業枠時間を集計残業時間に入れる　（入れた時間分を法定内残業にできる時間から引く）
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					AttendanceTime legalOverTimeWork =
						new AttendanceTime(overTimeFrameTime.getOverTimeWork().getTime().v());
					AttendanceTime overTimeWork = new AttendanceTime(0);
					if (legalOverTimeWork.lessThanOrEqualTo(canLegalOverTime.v())){
						// 残業時間が法定内残業にできる時間以下の時
						returnTime = new AttendanceTime(canLegalOverTime.v());
						returnTime = returnTime.minusMinutes(legalOverTimeWork.valueAsMinutes());
					}
					else {
						// 残業時間が法定内残業にできる時間を超える時
						overTimeWork = new AttendanceTime(legalOverTimeWork.v());
						overTimeWork = overTimeWork.minusMinutes(canLegalOverTime.valueAsMinutes());
						legalOverTimeWork = new AttendanceTime(canLegalOverTime.v());
						returnTime = new AttendanceTime(0);
					}
					timeSeriesWork.addOverTimeInLegalOverTime(TimeWithCalculation.sameTime(legalOverTimeWork));
					timeSeriesWork.addOverTimeInOverTime(TimeWithCalculation.sameTime(overTimeWork));
					break;
				case TRANSFER:
					AttendanceTime legalTransferTimeWork =
						new AttendanceTime(overTimeFrameTime.getTransferTime().getTime().v());
					AttendanceTime transferTimeWork = new AttendanceTime(0);
					if (legalTransferTimeWork.lessThanOrEqualTo(canLegalOverTime.v())){
						// 振替時間が法定内残業にできる時間以下の時
						returnTime = new AttendanceTime(canLegalOverTime.v());
						returnTime = returnTime.minusMinutes(legalTransferTimeWork.valueAsMinutes());
					}
					else {
						// 振替時間が法定内残業にできる時間を超える時
						transferTimeWork = new AttendanceTime(legalTransferTimeWork.v());
						transferTimeWork = transferTimeWork.minusMinutes(canLegalOverTime.valueAsMinutes());
						legalTransferTimeWork = new AttendanceTime(canLegalOverTime.v());
						returnTime = new AttendanceTime(0);
					}
					timeSeriesWork.addTransferTimeInLegalOverTime(TimeWithCalculation.sameTime(legalTransferTimeWork));
					timeSeriesWork.addTransferTimeInOverTime(TimeWithCalculation.sameTime(transferTimeWork));
					break;
				}
			}
			else {
				
				// 取得した残業枠時間を集計残業時間に入れる
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					timeSeriesWork.addOverTimeInOverTime(overTimeFrameTime.getOverTimeWork());
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInOverTime(overTimeFrameTime.getTransferTime());
					break;
				}
			}
		}
	
		return returnTime;
	}
	
	/**
	 * 集計する　（フレックス時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param aggregateAtr 集計区分
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 */
	public FlexTime aggregateForFlex(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, MonthlyAggregateAtr aggregateAtr, AggrSettingMonthlyOfFlx aggrSetOfFlex){

		FlexTime returnClass = new FlexTime();
		
		// 「残業枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessPrescribedTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val overTimeOfDaily = excessPrescribedTimeOfDaily.getOverTimeWork();
		// 残業時間がない時、集計しない
		if (!overTimeOfDaily.isPresent()) return returnClass;
		
		val ymd = attendanceTimeOfDaily.getYmd();
		
		val overTimeFrameTimeSrcs = overTimeOfDaily.get().getOverTimeWorkFrameTime();
		for (val overTimeFrameSrc : overTimeFrameTimeSrcs){
			val overTimeFrameNo = overTimeFrameSrc.getOverWorkFrameNo(); 
			
			// 「設定．残業を含める」を確認する
			if (aggrSetOfFlex.isIncludeOverTime()){
				
				// 取得した残業枠時間を「フレックス時間」に入れる
				returnClass.addOverTimeFrameTime(ymd, overTimeFrameSrc);
			}
			else {
				
				// 取得した残業枠時間を「集計残業時間」に入れる
				val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
				targetAggregateOverTime.addOverTimeInTimeSeriesWork(ymd, overTimeFrameSrc);
			}
		}
		
		return returnClass;
	}
	
	/**
	 * 法定内残業時間を取得する
	 * @param datePeriod 期間
	 * @return 法定内残業時間
	 */
	public AttendanceTimeMonth getLegalOverTime(DatePeriod datePeriod){
		
		AttendanceTimeMonth returnTime = new AttendanceTimeMonth(0);

		// 法定内残業時間．残業時間　＋　法定内残業時間．振替時間　の合計
		for (val aggregateOverTime : this.aggregateOverTimeMap.values()){
			for (val timeSeriesWork : aggregateOverTime.getTimeSeriesWorks().values()){
				if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
				val legalOverTime = timeSeriesWork.getLegalOverTime();
				returnTime = returnTime.addMinutes(legalOverTime.getOverTimeWork().getTime().v());
				returnTime = returnTime.addMinutes(legalOverTime.getTransferTime().getTime().v());
			}
		}
		
		return returnTime;
	}
	
	/**
	 * 残業合計時間を集計する
	 * @param datePeriod 期間
	 */
	public void aggregateTotal(DatePeriod datePeriod){
		
		this.totalOverTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeOverTime = new AttendanceTimeMonth(0);
		this.totalTransferOverTime = TimeMonthWithCalculation.ofSameTime(0);
		
		for (val aggregateOverTime : this.aggregateOverTimeMap.values()){
			aggregateOverTime.aggregate(datePeriod);
			this.totalOverTime = this.totalOverTime.addMinutes(
					aggregateOverTime.getOverTime().getTime().v(),
					aggregateOverTime.getOverTime().getCalcTime().v());
			this.beforeOverTime = this.beforeOverTime.addMinutes(
					aggregateOverTime.getBeforeOverTime().v());
			this.totalTransferOverTime = this.totalTransferOverTime.addMinutes(
					aggregateOverTime.getTransferOverTime().getTime().v(),
					aggregateOverTime.getTransferOverTime().getCalcTime().v());
		}
	}
}
