package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalOverTimeTransferOrderOfAggrMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByFlex;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.ExcessOutsideTimeSetReg;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.OverTimeAndTransferAtr;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の残業時間
 * @author shuichi_ishida
 */
@Getter
public class OverTimeOfMonthly implements Cloneable {

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
	
	@Override
	public OverTimeOfMonthly clone() {
		OverTimeOfMonthly cloned = new OverTimeOfMonthly();
		try {
			cloned.totalOverTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.totalOverTime.getTime().v()),
					new AttendanceTimeMonth(this.totalOverTime.getCalcTime().v()));
			cloned.beforeOverTime = new AttendanceTimeMonth(this.beforeOverTime.v());
			cloned.totalTransferOverTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.totalTransferOverTime.getTime().v()),
					new AttendanceTimeMonth(this.totalTransferOverTime.getCalcTime().v()));
			for (val aggrOverTime : this.aggregateOverTimeMap.entrySet()){
				cloned.aggregateOverTimeMap.putIfAbsent(aggrOverTime.getKey(), aggrOverTime.getValue().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("OverTimeOfMonthly clone error.");
		}
		return cloned;
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
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param roleOverTimeFrameMap 残業枠の役割
	 * @param autoExceptOverTimeFrames 自動的に除く残業枠
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateForRegAndIrreg(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			WorkInformation workInfo,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrder,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			Map<Integer, RoleOvertimeWork> roleOverTimeFrameMap,
			List<RoleOvertimeWork> autoExceptOverTimeFrames,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){

		if (roleOverTimeFrameMap.values().size() > 0) {
			
			// 自動計算して残業時間を集計する
			this.aggregateByAutoCalc(attendanceTimeOfDaily, companyId, workplaceId, employmentCd, workingSystem,
					workInfo, legalOverTimeTransferOrder, excessOutsideTimeSet, roleOverTimeFrameMap,
					companySets, employeeSets, repositories);
			
		}
		
		// 法定内残業の計算残業を残業時間の計算残業へ移送
		for (val entry : this.aggregateOverTimeMap.entrySet()){
			for (val workEntry : entry.getValue().getTimeSeriesWorks().entrySet()){
				workEntry.getValue().addCalcLegalOverTimeToCalcOverTime();
			}
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
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param roleOverTimeFrameMap 残業枠の役割
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateByAutoCalc(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			WorkInformation workInfo,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrder,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			Map<Integer, RoleOvertimeWork> roleOverTimeFrameMap,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 法定内残業にできる時間を計算する
		AttendanceTime canLegalOverTime = this.calcLegalOverTime(attendanceTimeOfDaily,
				companyId, workplaceId, employmentCd, workingSystem, companySets, employeeSets, repositories);
		
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
		if (workInfo.getWorkTimeCode() == null) return;
		val workTimeCode = workInfo.getWorkTimeCode().v();
		val overTimeAndTransferAtrs = repositories.getOverTimeAndTransferOrder().get(
				companyId, companySets.getWorkTimeCommonSetMap(workTimeCode, repositories), false);
		
		// 残業・振替のループ
		for (val overTimeAndTransferAtr : overTimeAndTransferAtrs){
		
			// 残業枠時間のループ処理
			canLegalOverTime = this.overTimeFrameTimeProcess(overTimeAndTransferAtr,
					legalOverTimeTransferOrder, excessOutsideTimeSet, canLegalOverTime,
					roleOverTimeFrameMap, overTimeFrameTimeMap, attendanceTimeOfDaily.getYmd());
		}
	}
	
	/**
	 * 法定内残業に出来る時間を計算する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private AttendanceTime calcLegalOverTime(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){
	
		// 日の法定労働時間を取得する
		DailyUnit dailyUnit = DailyUnit.zero();
		val workTimeSetOpt = companySets.getWorkingTimeSetting(employmentCd,
				employeeSets.getWorkplacesToRoot(attendanceTimeOfDaily.getYmd()),
				workingSystem, employeeSets, repositories);
		if (workTimeSetOpt.isPresent()){
			if (workTimeSetOpt.get().getDailyTime() != null){
				dailyUnit = workTimeSetOpt.get().getDailyTime();
			}
		}
		
		// 日別実績の法定内時間を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		WithinStatutoryTimeOfDaily legalTimeOfDaily = totalWorkingTime.getWithinStatutoryTimeOfDaily();
		if (legalTimeOfDaily == null){
			legalTimeOfDaily = WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
					new AttendanceTime(0),
					new AttendanceTime(0),
					new AttendanceTime(0),
					new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))),
					new AttendanceTime(0));
		}
		
		// 法定内残業にできる時間を計算する
		AttendanceTime canLegalOverTime = new AttendanceTime(dailyUnit.getDailyTime().v());
		canLegalOverTime = canLegalOverTime.minusMinutes(legalTimeOfDaily.getWorkTime().v());
		if (canLegalOverTime.lessThan(0)) canLegalOverTime = new AttendanceTime(0);
		return canLegalOverTime;
	}
	
	/**
	 * 残業枠時間のループ処理
	 * @param overTimeAndTransferAtr 残業振替区分
	 * @param legalOverTimeTransferOrderOfAggrMonthly 法定内残業振替順
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param canLegalOverTime 法定内残業に出来る時間
	 * @param roleOverTimeFrameMap 残業枠の役割
	 * @param overTimeFrameTimeMap 残業枠時間　（日別実績より取得）
	 * @param ymd 年月日
	 * @return 法定内残業に出来る時間　（計算後）
	 */
	private AttendanceTime overTimeFrameTimeProcess(
			OverTimeAndTransferAtr overTimeAndTransferAtr,
			LegalOverTimeTransferOrderOfAggrMonthly legalOverTimeTransferOrderOfAggrMonthly,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			AttendanceTime canLegalOverTime,
			Map<Integer, RoleOvertimeWork> roleOverTimeFrameMap,
			Map<OverTimeFrameNo, OverTimeFrameTime> overTimeFrameTimeMap,
			GeneralDate ymd){
		
		AttendanceTime timeAfterCalc = canLegalOverTime;
		
		// 残業枠時間分ループ
		for (val legalOverTimeTransferOrder : legalOverTimeTransferOrderOfAggrMonthly.getLegalOverTimeTransferOrders()){
			val overTimeFrameNo = legalOverTimeTransferOrder.getOverTimeFrameNo();
			
			// 該当する（日別実績の）残業枠時間がなければ、次の枠へ
			if (!overTimeFrameTimeMap.containsKey(overTimeFrameNo)) continue;
			val overTimeFrameTime = overTimeFrameTimeMap.get(overTimeFrameNo);
			
			// 対象の時系列ワークを確認する
			val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
			val timeSeriesWork = targetAggregateOverTime.getAndPutTimeSeriesWork(ymd);
			
			// 「法定内残業を含める」を確認
			if (excessOutsideTimeSet.getLegalOverTimeWork() == false){
				
				// 取得した残業枠時間を集計残業時間に入れる
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					timeSeriesWork.addOverTimeInOverTime(overTimeFrameTime.getOverTimeWork());
					timeSeriesWork.addBeforeAppTimeInOverTime(overTimeFrameTime.getBeforeApplicationTime());
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInOverTime(overTimeFrameTime.getTransferTime());
					break;
				}
				continue;
			}
			
			// 対象の役割を確認する
			if (!roleOverTimeFrameMap.containsKey(overTimeFrameNo.v())) continue;
			val roleOverTimeFrame = roleOverTimeFrameMap.get(overTimeFrameNo.v());
			
			switch (roleOverTimeFrame.getRoleOTWorkEnum()){
			case MIX_IN_OUT_STATUTORY:
				
				// 取得した残業枠時間を集計残業時間に入れる　（入れた時間分を法定内残業にできる時間から引く）
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					
					AttendanceTime legalOverTimeWork =
						new AttendanceTime(overTimeFrameTime.getOverTimeWork().getTime().v());
					AttendanceTime overTimeWork = new AttendanceTime(0);
					if (legalOverTimeWork.lessThanOrEqualTo(timeAfterCalc.v())){
						// 残業時間が法定内残業にできる時間以下の時
						timeAfterCalc = timeAfterCalc.minusMinutes(legalOverTimeWork.v());
					}
					else {
						// 残業時間が法定内残業にできる時間を超える時
						overTimeWork = new AttendanceTime(legalOverTimeWork.v());
						overTimeWork = overTimeWork.minusMinutes(timeAfterCalc.v());
						legalOverTimeWork = new AttendanceTime(timeAfterCalc.v());
						timeAfterCalc = new AttendanceTime(0);
					}
					timeSeriesWork.addOverTimeInLegalOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							legalOverTimeWork, new AttendanceTime(0)));
					timeSeriesWork.addOverTimeInOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							overTimeWork, overTimeFrameTime.getOverTimeWork().getCalcTime()));
					timeSeriesWork.addBeforeAppTimeInOverTime(overTimeFrameTime.getBeforeApplicationTime());
					break;
						
				case TRANSFER:
					AttendanceTime legalTransferTimeWork =
						new AttendanceTime(overTimeFrameTime.getTransferTime().getTime().v());
					AttendanceTime transferTimeWork = new AttendanceTime(0);
					if (legalTransferTimeWork.lessThanOrEqualTo(timeAfterCalc.v())){
						// 振替時間が法定内残業にできる時間以下の時
						timeAfterCalc = timeAfterCalc.minusMinutes(legalTransferTimeWork.v());
					}
					else {
						// 振替時間が法定内残業にできる時間を超える時
						transferTimeWork = new AttendanceTime(legalTransferTimeWork.v());
						transferTimeWork = transferTimeWork.minusMinutes(timeAfterCalc.v());
						legalTransferTimeWork = new AttendanceTime(timeAfterCalc.v());
						timeAfterCalc = new AttendanceTime(0);
					}
					timeSeriesWork.addTransferTimeInLegalOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							legalTransferTimeWork, new AttendanceTime(0)));
					timeSeriesWork.addTransferTimeInOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							transferTimeWork, overTimeFrameTime.getTransferTime().getCalcTime()));
					break;
				}
				break;
				
			case OT_STATUTORY_WORK:
				
				// 取得した残業枠時間を集計残業時間に入れる　（入れた時間分を法定内残業にできる時間から引く）
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					timeSeriesWork.addOverTimeInLegalOverTime(overTimeFrameTime.getOverTimeWork());
					timeSeriesWork.addBeforeAppTimeInOverTime(overTimeFrameTime.getBeforeApplicationTime());
					if (timeAfterCalc.lessThanOrEqualTo(overTimeFrameTime.getOverTimeWork().getTime())){
						timeAfterCalc = new AttendanceTime(0);
					}
					else {
						timeAfterCalc =
								timeAfterCalc.minusMinutes(overTimeFrameTime.getOverTimeWork().getTime().v());
					}
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInLegalOverTime(overTimeFrameTime.getTransferTime());
					if (timeAfterCalc.lessThanOrEqualTo(overTimeFrameTime.getTransferTime().getTime())){
						timeAfterCalc = new AttendanceTime(0);
					}
					else {
						timeAfterCalc =
								timeAfterCalc.minusMinutes(overTimeFrameTime.getTransferTime().getTime().v());
					}
					break;
				}
				break;
				
			case OUT_OT_STATUTORY:
				
				// 取得した残業枠時間を集計残業時間に入れる
				switch (overTimeAndTransferAtr){
				case OVER_TIME:
					timeSeriesWork.addOverTimeInOverTime(overTimeFrameTime.getOverTimeWork());
					timeSeriesWork.addBeforeAppTimeInOverTime(overTimeFrameTime.getBeforeApplicationTime());
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInOverTime(overTimeFrameTime.getTransferTime());
					break;
				}
				break;
			}
		}
	
		return timeAfterCalc;
	}
	
	/**
	 * 集計する　（フレックス時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param aggregateAtr 集計区分
	 * @param flexTime フレックス時間
	 * @param settingsByFlex フレックス勤務が必要とする設定
	 */
	public FlexTime aggregateForFlex(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, MonthlyAggregateAtr aggregateAtr, FlexTime flexTime,
			SettingRequiredByFlex settingsByFlex){
		
		val flexAggrSet = settingsByFlex.getFlexAggrSet();
		
		// 「残業枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessPrescribedTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val overTimeOfDaily = excessPrescribedTimeOfDaily.getOverTimeWork();
		// 残業時間がない時、集計しない
		if (!overTimeOfDaily.isPresent()) return flexTime;
		
		val ymd = attendanceTimeOfDaily.getYmd();
		
		val overTimeFrameTimeSrcs = overTimeOfDaily.get().getOverTimeWorkFrameTime();
		for (val overTimeFrameSrc : overTimeFrameTimeSrcs){
			val overTimeFrameNo = overTimeFrameSrc.getOverWorkFrameNo(); 
			
			// 「設定．残業を含める」を確認する
			if (flexAggrSet.getIncludeOverTime() == NotUseAtr.USE){

				// 取得した残業枠時間を「フレックス時間」に入れる
				flexTime.addOverTimeFrameTime(ymd, overTimeFrameSrc);
				
				// 取得した残業枠時間を「集計残業時間」に入れる　（法定内残業時間）　（Redmine#106235）
				val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
				targetAggregateOverTime.addLegalOverAndTransInTimeSeriesWork(ymd, overTimeFrameSrc);
				continue;
			}
				
			// 取得した残業枠時間を「集計残業時間」に入れる
			val targetAggregateOverTime = this.getTargetAggregateOverTime(overTimeFrameNo);
			targetAggregateOverTime.addOverTimeInTimeSeriesWork(ymd, overTimeFrameSrc);
		}
		
		return flexTime;
	}
	
	/**
	 * 残業時間の集計　（期間別集計用）
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 * @param roleOverTimeFrameMap 残業枠の役割
	 */
	public void aggregateForByPeriod(
			DatePeriod datePeriod,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap,
			Map<Integer, RoleOvertimeWork> roleOverTimeFrameMap){
		
		// 残業時間を縦計する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyMap.values()) {
			val ymd = attendanceTimeOfDaily.getYmd();
			
			// 期間外はスキップする
			if (!datePeriod.contains(ymd)) continue;
			
			// 「残業枠時間」を取得する
			val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
			val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
			val excessPrescibedTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
			val overTimeOfDailyOpt = excessPrescibedTimeOfDaily.getOverTimeWork();
			if (!overTimeOfDailyOpt.isPresent()) continue;
			val overTimeFrames = overTimeOfDailyOpt.get().getOverTimeWorkFrameTime();
			
			// 取得した「残業枠時間」を「集計残業時間」に入れる
			for (val overTimeFrame : overTimeFrames){
				int frameNo = overTimeFrame.getOverWorkFrameNo().v();
				val target = this.getTargetAggregateOverTime(new OverTimeFrameNo(frameNo));
				target.addOverTimeInTimeSeriesWork(ymd, overTimeFrame);
			}
		}
		
		// 残業合計時間を集計する
		this.aggregateTotal(datePeriod);
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
	
	/**
	 * 残業合計時間を集計する　（再計算用）
	 */
	public void recalcTotal(){
		
		this.totalOverTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeOverTime = new AttendanceTimeMonth(0);
		this.totalTransferOverTime = TimeMonthWithCalculation.ofSameTime(0);
		
		for (val aggregateOverTime : this.aggregateOverTimeMap.values()){
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
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.totalOverTime.getTime().v() +
				this.totalTransferOverTime.getTime().v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(OverTimeOfMonthly target){
		
		this.totalOverTime = this.totalOverTime.addMinutes(
				target.totalOverTime.getTime().v(), target.totalOverTime.getCalcTime().v());
		this.beforeOverTime = this.beforeOverTime.addMinutes(target.beforeOverTime.v());
		this.totalTransferOverTime = this.totalTransferOverTime.addMinutes(
				target.totalTransferOverTime.getTime().v(), target.totalTransferOverTime.getCalcTime().v());

		for (val aggrOverTime : this.aggregateOverTimeMap.values()){
			val frameNo = aggrOverTime.getOverTimeFrameNo();
			if (target.aggregateOverTimeMap.containsKey(frameNo)){
				aggrOverTime.sum(target.aggregateOverTimeMap.get(frameNo));
			}
		}
		for (val targetAggrOverTime : target.aggregateOverTimeMap.values()){
			val frameNo = targetAggrOverTime.getOverTimeFrameNo();
			this.aggregateOverTimeMap.putIfAbsent(frameNo, targetAggrOverTime);
		}
	}
}
