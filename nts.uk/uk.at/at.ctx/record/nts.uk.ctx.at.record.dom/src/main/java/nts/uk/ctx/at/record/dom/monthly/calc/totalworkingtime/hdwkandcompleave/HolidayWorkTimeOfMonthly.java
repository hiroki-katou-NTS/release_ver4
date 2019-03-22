package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalHolidayWorkTransferOrderOfAggrMonthly;
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
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriod;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriodEnum;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.HolidayWorkAndTransferAtr;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の休出時間
 * @author shuichi_ishida
 */
@Getter
public class HolidayWorkTimeOfMonthly implements Cloneable {

	/** 休出合計時間 */
	private TimeMonthWithCalculation totalHolidayWorkTime;
	/** 事前休出時間 */
	private AttendanceTimeMonth beforeHolidayWorkTime;
	/** 振替合計時間 */
	private TimeMonthWithCalculation totalTransferTime;
	/** 集計休出時間 */
	private Map<HolidayWorkFrameNo, AggregateHolidayWorkTime> aggregateHolidayWorkTimeMap;
	
	/**
	 * コンストラクタ
	 */
	public HolidayWorkTimeOfMonthly(){
		
		this.totalHolidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.totalTransferTime = TimeMonthWithCalculation.ofSameTime(0);
		
		this.aggregateHolidayWorkTimeMap = new HashMap<>();
	}

	/**
	 * ファクトリー
	 * @param totalHolidayWorkTime 休出合計時間
	 * @param beforeHolidayWorkTime 事前休出時間
	 * @param totalTransferTime 振替合計時間
	 * @param aggregateHolidayWorkTimeList 集計休出時間
	 * @return 月別実績の休出時間
	 */
	public static HolidayWorkTimeOfMonthly of(
			TimeMonthWithCalculation totalHolidayWorkTime,
			AttendanceTimeMonth beforeHolidayWorkTime,
			TimeMonthWithCalculation totalTransferTime,
			List<AggregateHolidayWorkTime> aggregateHolidayWorkTimeList){

		val domain = new HolidayWorkTimeOfMonthly();
		domain.totalHolidayWorkTime = totalHolidayWorkTime;
		domain.beforeHolidayWorkTime = beforeHolidayWorkTime;
		domain.totalTransferTime = totalTransferTime;
		for (AggregateHolidayWorkTime aggregateHolidayWorkTime : aggregateHolidayWorkTimeList){
			val holidayWorkFrameNo = aggregateHolidayWorkTime.getHolidayWorkFrameNo();
			domain.aggregateHolidayWorkTimeMap.putIfAbsent(holidayWorkFrameNo, aggregateHolidayWorkTime);
		}
		return domain;
	}
	
	@Override
	public HolidayWorkTimeOfMonthly clone() {
		HolidayWorkTimeOfMonthly cloned = new HolidayWorkTimeOfMonthly();
		try {
			cloned.totalHolidayWorkTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.totalHolidayWorkTime.getTime().v()),
					new AttendanceTimeMonth(this.totalHolidayWorkTime.getCalcTime().v()));
			cloned.beforeHolidayWorkTime = new AttendanceTimeMonth(this.beforeHolidayWorkTime.v());
			cloned.totalTransferTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.totalTransferTime.getTime().v()),
					new AttendanceTimeMonth(this.totalTransferTime.getCalcTime().v()));
			for (val aggrHolidayWorkTime : this.aggregateHolidayWorkTimeMap.entrySet()){
				cloned.aggregateHolidayWorkTimeMap.putIfAbsent(
						aggrHolidayWorkTime.getKey(), aggrHolidayWorkTime.getValue().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("HolidayWorkTimeOfMonthly clone error.");
		}
		return cloned;
	}
	
	/**
	 * 対象の集計休出時間を取得する
	 * @param holidayWorkFrameNo 休出枠NO
	 * @return 対象の集計休出時間
	 */
	private AggregateHolidayWorkTime getTargetAggregateHolidayWorkTime(HolidayWorkFrameNo holidayWorkFrameNo){

		this.aggregateHolidayWorkTimeMap.putIfAbsent(holidayWorkFrameNo, new AggregateHolidayWorkTime(holidayWorkFrameNo));
		return this.aggregateHolidayWorkTimeMap.get(holidayWorkFrameNo);
	}
	
	/**
	 * 集計する　（通常・変形労働時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param workInfo 勤務情報
	 * @param legalHolidayWorkTransferOrder 法定内休出振替順
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param roleHolidayWorkFrameMap 休出枠の役割
	 * @param autoExceptHolidayWorkFrames 自動的に除く休出枠
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateForRegAndIrreg(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd, WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr, WorkInformation workInfo,
			LegalHolidayWorkTransferOrderOfAggrMonthly legalHolidayWorkTransferOrder,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			Map<Integer, RoleOfOpenPeriod> roleHolidayWorkFrameMap,
			List<RoleOfOpenPeriod> autoExceptHolidayWorkFrames,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計区分を確認する
		boolean isAggregateHolidayWork = false;
		if (aggregateAtr == MonthlyAggregateAtr.MONTHLY) isAggregateHolidayWork = true;
		if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK){
			
			// 休出を集計するか確認する
			isAggregateHolidayWork = this.confirmAggregateHolidayWork(
					companyId, workInfo, excessOutsideTimeSet, companySets, repositories);
		}
		if (isAggregateHolidayWork){
			
			// 「休出枠の役割」を確認する
			if (roleHolidayWorkFrameMap.size() > 0) {
				
				// 自動計算して休出時間を集計する
				this.aggregateByAutoCalc(attendanceTimeOfDaily, companyId, workplaceId, employmentCd, workingSystem,
						workInfo, legalHolidayWorkTransferOrder, excessOutsideTimeSet, roleHolidayWorkFrameMap,
						companySets, employeeSets, repositories);
			}
		}
		
		// 法定内休出の計算休出を休出時間の計算休出へ移送
		for (val entry : this.aggregateHolidayWorkTimeMap.entrySet()){
			for (val workEntry : entry.getValue().getTimeSeriesWorks().entrySet()){
				workEntry.getValue().addCalcLegalHWTimeToCalcHWTime();
			}
		}
	}

	/**
	 * 休出を集計するか確認する
	 * @param companyId 会社ID
	 * @param workInfo 勤務情報
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param companySets 月別集計で必要な会社別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 * @return true：休出を集計する、false：休出を集計しない
	 */
	private boolean confirmAggregateHolidayWork(
			String companyId,
			WorkInformation workInfo,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			MonAggrCompanySettings companySets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 勤務種類から法定内休日か判断する
		if (workInfo.getWorkTypeCode() == null) return true;
		val workType = companySets.getWorkTypeMap(workInfo.getWorkTypeCode().v(), repositories);
		if (workType != null){
			val workTypeSet = workType.getWorkTypeSet();
			if (workTypeSet != null){
				val holidayAtr = workTypeSet.getHolidayAtr();
				// 法定内休日なら、休出を集計しない
				if (holidayAtr == HolidayAtr.STATUTORY_HOLIDAYS) return false;
			}
		}
		
		// 法定内休日以外なら、休出を集計する
		return true;
	}
	
	/**
	 * 自動計算して集計する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param placeId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param workInfo 勤務情報
	 * @param legalHolidayWorkTransferOrder 法定内休出振替順
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param roleHolidayWorkFrameMap 休出枠の役割
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateByAutoCalc(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String placeId, String employmentCd, WorkingSystem workingSystem,
			WorkInformation workInfo,
			LegalHolidayWorkTransferOrderOfAggrMonthly legalHolidayWorkTransferOrder,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			Map<Integer, RoleOfOpenPeriod> roleHolidayWorkFrameMap,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 法定内休出にできる時間を計算する
		AttendanceTime canLegalHolidayWork = this.calcLegalHolidayWork(attendanceTimeOfDaily,
				companyId, placeId, employmentCd, workingSystem, companySets, employeeSets, repositories);
		
		// 「休出枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val holidayWorkTimeOfDaily = excessOfStatutoryTimeOfDaily.getWorkHolidayTime();
		// 休出時間がない時、集計しない
		if (!holidayWorkTimeOfDaily.isPresent()) return;
		val holidayWorkFrameTimeSrcs = holidayWorkTimeOfDaily.get().getHolidayWorkFrameTime();
			
		// 休出枠時間リストをマップに組み換え　（枠での検索用）
		Map<HolidayWorkFrameNo, HolidayWorkFrameTime> holidayWorkFrameTimes = new HashMap<>();
		for (val holidayWorkFrameTimeSrc : holidayWorkFrameTimeSrcs){
			holidayWorkFrameTimes.putIfAbsent(holidayWorkFrameTimeSrc.getHolidayFrameNo(), holidayWorkFrameTimeSrc);
		}
	
		// 休出・振替の処理順序を取得する
		if (workInfo.getWorkTimeCode() == null) return;
		val workTimeCode = workInfo.getWorkTimeCode().v();
		val holidayWorkAndTransferAtrs = repositories.getHolidayWorkAndTransferOrder().get(
				companyId, companySets.getWorkTimeCommonSetMap(workTimeCode, repositories), false);
		
		// 休出・振替のループ
		for (val holidayWorkAndTransferAtr : holidayWorkAndTransferAtrs){
		
			// 休出枠時間のループ処理
			canLegalHolidayWork = this.holidayWorkFrameTimeProcess(holidayWorkAndTransferAtr,
					legalHolidayWorkTransferOrder, excessOutsideTimeSet, canLegalHolidayWork,
					roleHolidayWorkFrameMap, holidayWorkFrameTimes, attendanceTimeOfDaily.getYmd());
		}
	}
	
	/**
	 * 法定内休出に出来る時間を計算する
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param placeId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private AttendanceTime calcLegalHolidayWork(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String placeId, String employmentCd, WorkingSystem workingSystem,
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
		
		// 法定内休出にできる時間
		AttendanceTime canLegalHolidayWork = new AttendanceTime(dailyUnit.getDailyTime().v());
		return canLegalHolidayWork;
	}
	
	/**
	 * 休出枠時間のループ処理
	 * @param holidayWorkAndTransferAtr 休出振替区分
	 * @param legalHolidayWorkTransferOrderOfAggrMonthly 法定内休出振替順
	 * @param excessOutsideTimeSet 割増集計方法
	 * @param canLegalHolidayWork 法定内休出に出来る時間
	 * @param roleHolidayWorkFrameMap 休出枠の役割
	 * @param holidayWorkFrameTimeMap 休出枠時間　（日別実績より取得）
	 * @param ymd 年月日
	 * @return 法定内休出に出来る時間　（計算後）
	 */
	private AttendanceTime holidayWorkFrameTimeProcess(
			HolidayWorkAndTransferAtr holidayWorkAndTransferAtr,
			LegalHolidayWorkTransferOrderOfAggrMonthly legalHolidayWorkTransferOrderOfAggrMonthly,
			ExcessOutsideTimeSetReg excessOutsideTimeSet,
			AttendanceTime canLegalHolidayWork,
			Map<Integer, RoleOfOpenPeriod> roleHolidayWorkFrameMap,
			Map<HolidayWorkFrameNo, HolidayWorkFrameTime> holidayWorkFrameTimeMap,
			GeneralDate ymd){
		
		AttendanceTime timeAfterCalc = canLegalHolidayWork;
		
		// 休出枠時間分ループ
		for (val legalHolidayWorkTransferOrder : legalHolidayWorkTransferOrderOfAggrMonthly.getLegalHolidayWorkTransferOrders()){
			val holidayWorkFrameNo = legalHolidayWorkTransferOrder.getHolidayWorkFrameNo();
			
			// 該当する（日別実績の）休出枠時間がなければ、次の枠へ
			if (!holidayWorkFrameTimeMap.containsKey(holidayWorkFrameNo)) continue;
			val holidayWorkFrameTime = holidayWorkFrameTimeMap.get(holidayWorkFrameNo);
			
			// 対象の時系列ワークを確認する
			val targetHolidayWorkTime = this.getTargetAggregateHolidayWorkTime(holidayWorkFrameNo);
			val timeSeriesWork = targetHolidayWorkTime.getAndPutTimeSeriesWork(ymd);
			
			// 「法定内休出を含める」を確認
			if (excessOutsideTimeSet.getLegalHoliday() == false){
				
				// 取得した休出枠時間を集計休出時間に入れる
				switch (holidayWorkAndTransferAtr){
				case HOLIDAY_WORK:
					timeSeriesWork.addHolidayWorkTimeInHolidayWorkTime(holidayWorkFrameTime.getHolidayWorkTime().get());
					timeSeriesWork.addBeforeAppTimeInHolidayWorkTime(holidayWorkFrameTime.getBeforeApplicationTime().get());
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInHolidayWorkTime(holidayWorkFrameTime.getTransferTime().get());
					break;
				}
				continue;
			}
			
			// 対象の役割を確認する
			if (!roleHolidayWorkFrameMap.containsKey(holidayWorkFrameNo.v())) continue;
			val roleHolidayWorkFrame = roleHolidayWorkFrameMap.get(holidayWorkFrameNo.v());

			switch (roleHolidayWorkFrame.getRoleOfOpenPeriodEnum()){
			case MIX_WITHIN_OUTSIDE_STATUTORY:
				
				// 取得した休出枠時間を集計休出時間に入れる　（入れた時間分を法定内休出にできる時間から引く）
				switch (holidayWorkAndTransferAtr){
				case HOLIDAY_WORK:
					AttendanceTime legalHolidayWorkTime =
						new AttendanceTime(holidayWorkFrameTime.getHolidayWorkTime().get().getTime().v());
					AttendanceTime holidayWorkTime = new AttendanceTime(0);
					if (legalHolidayWorkTime.lessThanOrEqualTo(timeAfterCalc.v())){
						// 休出時間が法定内休出にできる時間以下の時
						timeAfterCalc = timeAfterCalc.minusMinutes(legalHolidayWorkTime.v());
					}
					else {
						// 休出時間が法定内休出にできる時間を超える時
						holidayWorkTime = new AttendanceTime(legalHolidayWorkTime.v());
						holidayWorkTime = holidayWorkTime.minusMinutes(timeAfterCalc.v());
						legalHolidayWorkTime = new AttendanceTime(timeAfterCalc.v());
						timeAfterCalc = new AttendanceTime(0);
					}
					timeSeriesWork.addHolidayWorkTimeInLegalHolidayWorkTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							legalHolidayWorkTime, new AttendanceTime(0)));
					timeSeriesWork.addHolidayWorkTimeInHolidayWorkTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							holidayWorkTime, holidayWorkFrameTime.getHolidayWorkTime().get().getCalcTime()));
					timeSeriesWork.addBeforeAppTimeInHolidayWorkTime(holidayWorkFrameTime.getBeforeApplicationTime().get());
					break;
				case TRANSFER:
					AttendanceTime legalTransferTimeWork =
						new AttendanceTime(holidayWorkFrameTime.getTransferTime().get().getTime().v());
					AttendanceTime transferTimeWork = new AttendanceTime(0);
					if (legalTransferTimeWork.lessThanOrEqualTo(timeAfterCalc.v())){
						// 振替時間が法定内休出にできる時間以下の時
						timeAfterCalc = timeAfterCalc.minusMinutes(legalTransferTimeWork.v());
					}
					else {
						// 振替時間が法定内休出にできる時間を超える時
						transferTimeWork = new AttendanceTime(legalTransferTimeWork.v());
						transferTimeWork = transferTimeWork.minusMinutes(timeAfterCalc.v());
						legalTransferTimeWork = new AttendanceTime(timeAfterCalc.v());
						timeAfterCalc = new AttendanceTime(0);
					}
					timeSeriesWork.addTransferTimeInLegalHolidayWorkTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							legalTransferTimeWork, new AttendanceTime(0)));
					timeSeriesWork.addTransferTimeInHolidayWorkTime(TimeDivergenceWithCalculation.createTimeWithCalculation(
							transferTimeWork, holidayWorkFrameTime.getTransferTime().get().getCalcTime()));
					break;
				}
				break;
				
			case STATUTORY_HOLIDAYS:
				
				// 取得した休出枠時間を集計休出時間に入れる
				switch (holidayWorkAndTransferAtr){
				case HOLIDAY_WORK:
					timeSeriesWork.addHolidayWorkTimeInHolidayWorkTime(holidayWorkFrameTime.getHolidayWorkTime().get());
					timeSeriesWork.addBeforeAppTimeInHolidayWorkTime(holidayWorkFrameTime.getBeforeApplicationTime().get());
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInHolidayWorkTime(holidayWorkFrameTime.getTransferTime().get());
					break;
				}
				break;
				
			case NON_STATUTORY_HOLIDAYS:
				
				// 取得した休出枠時間を集計休出時間に入れる　（入れた時間分を法定内休出にできる時間から引く）
				switch (holidayWorkAndTransferAtr){
				case HOLIDAY_WORK:
					timeSeriesWork.addHolidayWorkTimeInLegalHolidayWorkTime(holidayWorkFrameTime.getHolidayWorkTime().get());
					timeSeriesWork.addBeforeAppTimeInHolidayWorkTime(holidayWorkFrameTime.getBeforeApplicationTime().get());
					if (timeAfterCalc.lessThanOrEqualTo(holidayWorkFrameTime.getHolidayWorkTime().get().getTime())){
						timeAfterCalc = new AttendanceTime(0);
					}
					else {
						timeAfterCalc =
								timeAfterCalc.minusMinutes(holidayWorkFrameTime.getHolidayWorkTime().get().getTime().v());
					}
					break;
				case TRANSFER:
					timeSeriesWork.addTransferTimeInLegalHolidayWorkTime(holidayWorkFrameTime.getTransferTime().get());
					if (timeAfterCalc.lessThanOrEqualTo(holidayWorkFrameTime.getTransferTime().get().getTime())){
						timeAfterCalc = new AttendanceTime(0);
					}
					else {
						timeAfterCalc =
								timeAfterCalc.minusMinutes(holidayWorkFrameTime.getTransferTime().get().getTime().v());
					}
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
			String companyId, MonthlyAggregateAtr aggregateAtr,
			FlexTime flexTime, SettingRequiredByFlex settingsByFlex){

		val flexAggrSet = settingsByFlex.getFlexAggrSet();
		val roleHolidayWorkFrameMap = settingsByFlex.getRoleHolidayWorkFrameMap();
		
		// 「休出枠時間」を取得する
		val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
		val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
		val excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		val holidayWorkTimeOfDaily = excessOfStatutoryTimeOfDaily.getWorkHolidayTime();
		// 休出時間がない時、集計しない
		if (!holidayWorkTimeOfDaily.isPresent()) return flexTime;
		
		val ymd = attendanceTimeOfDaily.getYmd();
		
		val holidayWorkFrameTimeSrcs = holidayWorkTimeOfDaily.get().getHolidayWorkFrameTime();
		for (val holidayWorkFrameTimeSrc : holidayWorkFrameTimeSrcs){
			val holidayWorkFrameNo = holidayWorkFrameTimeSrc.getHolidayFrameNo();
			
			// 「設定．残業を含める」を確認する
			// ※　本来は、「法定外休出時間をフレックス時間に含める」を確認する仕様だが、クラス属性が未実装のため、仮対応　2018.11.17 shuichi_ishida
			if (flexAggrSet.getIncludeOverTime() == NotUseAtr.USE){

				// 処理中のNOが「法定外休出」に一致するか
				if (roleHolidayWorkFrameMap.containsKey(holidayWorkFrameNo.v())){
					val role = roleHolidayWorkFrameMap.get(holidayWorkFrameNo.v());
					if (role.getRoleOfOpenPeriodEnum() == RoleOfOpenPeriodEnum.NON_STATUTORY_HOLIDAYS){
						
						// 取得した休出枠時間を「フレックス時間」に入れる
						flexTime.addHolidayWorkTimeFrameTime(ymd, holidayWorkFrameTimeSrc);
						
						// 取得した休出枠時間を「集計休出時間」に入れる　（法定内休出時間）　（Redmine#106235）
						val targetHolidayWorkTime = this.getTargetAggregateHolidayWorkTime(holidayWorkFrameNo);
						targetHolidayWorkTime.addLegalHdwkAndTransInTimeSeriesWork(ymd, holidayWorkFrameTimeSrc);
						continue;
					}
				}
			}
				
			// 取得した休出枠時間を「集計休出時間」に入れる
			val targetHolidayWorkTime = this.getTargetAggregateHolidayWorkTime(holidayWorkFrameNo);
			targetHolidayWorkTime.addHolidayWorkTimeInTimeSeriesWork(ymd, holidayWorkFrameTimeSrc);
		}
		
		return flexTime;
	}
	
	/**
	 * 休日出勤の集計　（期間別集計用）
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 * @param roleHolidayWorkFrameMap 休出枠の役割
	 */
	public void aggregateForByPeriod(
			DatePeriod datePeriod,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap,
			Map<Integer, RoleOfOpenPeriod> roleHolidayWorkFrameMap){
		
		// 休出時間を縦計する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyMap.values()) {
			val ymd = attendanceTimeOfDaily.getYmd();
			
			// 期間外はスキップする
			if (!datePeriod.contains(ymd)) continue;
			
			// 「休出枠時間」を取得する
			val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
			val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
			val excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
			val holidayWorkTimeOfDailyOpt = excessOfStatutoryTimeOfDaily.getWorkHolidayTime();
			if (!holidayWorkTimeOfDailyOpt.isPresent()) continue;
			val holidayWorkTimeFrames = holidayWorkTimeOfDailyOpt.get().getHolidayWorkFrameTime();
			
			// 取得した「休出枠時間」を「集計休出時間」に入れる
			for (val holidayWorkTimeFrame : holidayWorkTimeFrames){
				int frameNo = holidayWorkTimeFrame.getHolidayFrameNo().v();
				val target = this.getTargetAggregateHolidayWorkTime(new HolidayWorkFrameNo(frameNo));
				target.addHolidayWorkTimeInTimeSeriesWork(ymd, holidayWorkTimeFrame);
			}
		}
		
		// 休出合計時間を集計する
		this.aggregateTotal(datePeriod);
	}
	
	/**
	 * 法定内休出時間を取得する
	 * @param datePeriod 期間
	 * @return 法定内休出時間
	 */
	public AttendanceTimeMonth getLegalHolidayWorkTime(DatePeriod datePeriod){

		AttendanceTimeMonth returnTime = new AttendanceTimeMonth(0);

		// 法定内休出時間．休出時間　＋　法定内休出時間．振替時間　の合計
		for (val aggregateHolidayWorkTime : this.aggregateHolidayWorkTimeMap.values()){
			for (val timeSeriesWork : aggregateHolidayWorkTime.getTimeSeriesWorks().values()){
				if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
				val legalHolidayWorkTime = timeSeriesWork.getLegalHolidayWorkTime();
				returnTime = returnTime.addMinutes(legalHolidayWorkTime.getHolidayWorkTime().get().getTime().v());
				returnTime = returnTime.addMinutes(legalHolidayWorkTime.getTransferTime().get().getTime().v());
			}
		}
		
		return returnTime;
	}
	
	/**
	 * 休出合計時間を集計する
	 * @param datePeriod 期間
	 */
	public void aggregateTotal(DatePeriod datePeriod){

		this.totalHolidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.totalTransferTime = TimeMonthWithCalculation.ofSameTime(0);

		for (val aggregateHolidayWorkTime : this.aggregateHolidayWorkTimeMap.values()){
			aggregateHolidayWorkTime.aggregate(datePeriod);
			this.totalHolidayWorkTime = this.totalHolidayWorkTime.addMinutes(
					aggregateHolidayWorkTime.getHolidayWorkTime().getTime().v(),
					aggregateHolidayWorkTime.getHolidayWorkTime().getCalcTime().v());
			this.beforeHolidayWorkTime = this.beforeHolidayWorkTime.addMinutes(
					aggregateHolidayWorkTime.getBeforeHolidayWorkTime().v());
			this.totalTransferTime = this.totalTransferTime.addMinutes(
					aggregateHolidayWorkTime.getTransferTime().getTime().v(),
					aggregateHolidayWorkTime.getTransferTime().getCalcTime().v());
		}
	}
	
	/**
	 * 休出合計時間を集計する　（再計算用）
	 */
	public void recalcTotal(){

		this.totalHolidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.totalTransferTime = TimeMonthWithCalculation.ofSameTime(0);

		for (val aggregateHolidayWorkTime : this.aggregateHolidayWorkTimeMap.values()){
			this.totalHolidayWorkTime = this.totalHolidayWorkTime.addMinutes(
					aggregateHolidayWorkTime.getHolidayWorkTime().getTime().v(),
					aggregateHolidayWorkTime.getHolidayWorkTime().getCalcTime().v());
			this.beforeHolidayWorkTime = this.beforeHolidayWorkTime.addMinutes(
					aggregateHolidayWorkTime.getBeforeHolidayWorkTime().v());
			this.totalTransferTime = this.totalTransferTime.addMinutes(
					aggregateHolidayWorkTime.getTransferTime().getTime().v(),
					aggregateHolidayWorkTime.getTransferTime().getCalcTime().v());
		}
	}
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.totalHolidayWorkTime.getTime().v() +
				this.totalTransferTime.getTime().v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(HolidayWorkTimeOfMonthly target){
		
		this.totalHolidayWorkTime = this.totalHolidayWorkTime.addMinutes(
				target.totalHolidayWorkTime.getTime().v(), target.totalHolidayWorkTime.getCalcTime().v());
		this.beforeHolidayWorkTime = this.beforeHolidayWorkTime.addMinutes(target.beforeHolidayWorkTime.v());
		this.totalTransferTime = this.totalTransferTime.addMinutes(
				target.totalTransferTime.getTime().v(), target.totalTransferTime.getCalcTime().v());
		
		for (val aggrHolidayWorkTime : this.aggregateHolidayWorkTimeMap.values()){
			val frameNo = aggrHolidayWorkTime.getHolidayWorkFrameNo();
			if (target.aggregateHolidayWorkTimeMap.containsKey(frameNo)){
				aggrHolidayWorkTime.sum(target.aggregateHolidayWorkTimeMap.get(frameNo));
			}
		}
		for (val targetAggrHolidayWorkTime : target.aggregateHolidayWorkTimeMap.values()){
			val frameNo = targetAggrHolidayWorkTime.getHolidayWorkFrameNo();
			this.aggregateHolidayWorkTimeMap.putIfAbsent(frameNo, targetAggrHolidayWorkTime);
		}
	}
}
