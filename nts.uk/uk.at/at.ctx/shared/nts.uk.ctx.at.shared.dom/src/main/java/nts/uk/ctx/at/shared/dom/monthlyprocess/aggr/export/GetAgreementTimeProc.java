package nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.export;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.AttendanceTimeOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreMaxTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.work.MonthlyOldDatas;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 処理：36協定時間の取得
 * @author shuichi_ishida
 */
public class GetAgreementTimeProc {

	/** 月別集計で必要な会社別設定 */
	private MonAggrCompanySettings companySets;
	
	/** 会社ID */
	private String companyId;
	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日 */
	private ClosureDate closureDate;
	
	public GetAgreementTimeProc() {
	}
	
	/**
	 * 36協定時間の取得
	 * @param companyId 会社ID
	 * @param employeeIds 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @return 36協定時間一覧
	 */
	public List<AgreementTimeDetail> get(RequireM2 require, CacheCarrier cacheCarrier,
			String companyId, List<String> employeeIds, YearMonth yearMonth, ClosureId closureId) {

		CopyOnWriteArrayList<AgreementTimeDetail> results = new CopyOnWriteArrayList<>();
		this.companyId = companyId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;

		if (employeeIds.size() == 0) return results;
		
		// 月別集計で必要な会社別設定を取得　（36協定時間用）
		this.companySets = MonAggrCompanySettings.loadSettingsForAgreement(require, companyId);
		if (this.companySets.getErrorInfos().size() > 0){
			
			// 会社単位エラーメッセージ　（先頭の社員IDに紐づけて返却）
			val companyError = AgreementTimeDetail.of(employeeIds.get(0), null, null, null, null,
					this.companySets.getErrorInfos().values().stream().findFirst().get().v());
			results.add(companyError);
			return results;
		}
		
		// 年月を集計期間に変換
		val aggrPeriod = this.convertToAggregatePeriod();
		if (aggrPeriod == null) return results;
		
		CopyOnWriteArrayList<String> errorMessages = new CopyOnWriteArrayList<>();
		require.parallelContext().forEach(employeeIds, employeeId -> {
			if (errorMessages.size() > 0) return;
			
			// 月別集計で必要な社員別設定を取得
			MonAggrEmployeeSettings employeeSets = MonAggrEmployeeSettings.loadSettings(require,
					cacheCarrier, companyId, employeeId, aggrPeriod);
			if (employeeSets.getErrorInfos().size() > 0){
				errorMessages.add(employeeSets.getErrorInfos().values().stream().findFirst().get().v());
				return;
			}
			
			// 36協定時間一覧を作成
			AgreementTimeDetail aggrTimeDetail = new AgreementTimeDetail(employeeId);

			// 労働条件項目を取得する
			// ※　必要ないかもしれない。
			//val workConditionItemOpt =
			//		this.repositories.getWorkingConditionItem().getBySidAndStandardDate(employeeId, aggrPeriod.end());
			//if (!workConditionItemOpt.isPresent()) continue;
			//val workConditionItem = workConditionItemOpt.get();
			
			// 「日別実績の勤怠時間」を取得
			val confirmedAttdTimeList = require.dailyAttendanceTimes(employeeId, aggrPeriod);

			// 確定情報の取得
			val confirmedInfo = this.getConfirmedInfo(require, cacheCarrier, employeeId, aggrPeriod, employeeSets, Optional.of(confirmedAttdTimeList));
			val confirmed = confirmedInfo.getAgreementTime().getAgreementTime();
			val confirmedMax = confirmedInfo.getAgreementMaxTime().getAgreementTime();
			
			// エラーがあるか確認する
			if (confirmed.getConfirmedErrorMessage() != null){
				errorMessages.add(confirmed.getConfirmedErrorMessage());
				
				// 36協定時間一覧にエラーメッセージを入れる
				val employeeError = AgreementTimeDetail.of(employeeIds.get(0), null, null, null, null,
						confirmed.getConfirmedErrorMessage());
				results.add(employeeError);
				return;
			}
			
			// 社員の申請を反映　（反映結果の取得）
			//*****（未）　申請反映側処理の完成後、本実装。（永続化でない）「取得モード」で貰う。
			//List<AttendanceTimeOfDailyPerformance> appReflectAttdTimeList = new ArrayList<>();
			
			// 未反映申請反映後情報の取得
			AgreementTimeOfMonthly afterAppReflect = null;
			AgreMaxTimeOfMonthly afterAppReflextMax = null;
			//val afterAppReflect = this.getConfirmed(employeeId, aggrPeriod, workConditionItem,
			//		Optional.of(appReflectAttdTimeList));
			
			aggrTimeDetail = AgreementTimeDetail.of(employeeId, confirmed, afterAppReflect,
					confirmedMax, afterAppReflextMax, null);
			results.add(aggrTimeDetail);
		});
		
		return results;
	}
	
	/**
	 * 年月を集計期間に変換
	 * @return 集計期間
	 */
	private DatePeriod convertToAggregatePeriod(){
		
		// 「締め」を取得
		val closure = this.companySets.getClosureMap().get(this.closureId.value);
		if (closure == null) return null;
		if (closure.getUseClassification() != UseClassification.UseClass_Use) return null;
		
		// 締め日を取得
		val closureHistoryOpt = closure.getHistoryByYearMonth(this.yearMonth);
		if (!closureHistoryOpt.isPresent()) return null;
		val closureHistory = closureHistoryOpt.get();
		this.closureDate = closureHistory.getClosureDate();
		
		// 36協定運用設定を取得
		val agreementOperationSetOpt = this.companySets.getAgreementOperationSet();
		if (!agreementOperationSetOpt.isPresent()) return null;
		val agreementOperationSet = agreementOperationSetOpt.get();
		
		// 年月から集計期間を取得
		val aggrPeriodOpt = agreementOperationSet.getAggregatePeriodByYearMonth(this.yearMonth, closure);
		if (!aggrPeriodOpt.isPresent()) return null;
		
		// 集計期間を返す
		return aggrPeriodOpt.get().getPeriod();
	}
	
	/**
	 * 確定情報の取得
	 * @param employeeId 社員ID
	 * @param aggrPeriod 集計期間
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param attendanceTimeOfDailysOpt 日別実績の勤怠時間リスト
	 * @return 管理期間の36協定時間
	 */
	private AgreementTimeOfManagePeriod getConfirmedInfo(
			RequireM1 require, CacheCarrier cacheCarrier,
			String employeeId,
			DatePeriod aggrPeriod,
			MonAggrEmployeeSettings employeeSets,
			Optional<Map<GeneralDate, AttendanceTimeOfDailyAttendance>> attendanceTimeOfDailysOpt){
		
		// 「月別実績の勤怠時間」を取得
		DatePeriod monthPeriod = null;
		AttendanceTimeOfMonthly attendanceTimeOfMonthly = null;
		val attendanceTimeOfMonthlys = require.attendanceTimeOfMonthly(employeeId, this.yearMonth, this.closureId);
		if (attendanceTimeOfMonthlys.size() == 0) {
			
			// 「締め」を取得する
			val closure = this.companySets.getClosureMap().get(this.closureId.value);
			if (closure == null) return null;
			if (closure.getUseClassification() != UseClassification.UseClass_Use) return null;
			
			// 指定した年月日時点の締め期間を取得する
			val closurePeriodOpt = closure.getClosurePeriodByYmd(aggrPeriod.start());
			if (!closurePeriodOpt.isPresent()) return null;
			val closurePeriod = closurePeriodOpt.get();

			// 「月別実績の勤怠時間」を作成する
			monthPeriod = closurePeriod.getPeriod();
			attendanceTimeOfMonthly = new AttendanceTimeOfMonthly(
					employeeId, this.yearMonth, this.closureId, this.closureDate, monthPeriod);
		}
		else {
			attendanceTimeOfMonthly = attendanceTimeOfMonthlys.get(0);
			monthPeriod = attendanceTimeOfMonthly.getDatePeriod();
		}

		// 集計に必要な日別実績データを取得する
		MonthlyCalculatingDailys monthlyCalcDailys = null;
		if (attendanceTimeOfDailysOpt.isPresent()){
			monthlyCalcDailys = MonthlyCalculatingDailys.loadDataForAgreement(require, employeeId, monthPeriod,
					attendanceTimeOfDailysOpt.get(), employeeSets);
		}
		else {
			monthlyCalcDailys = MonthlyCalculatingDailys.loadDataForAgreement(require, employeeId, monthPeriod,
					employeeSets);
		}
		
		// 集計前の月別実績データを確認する
		MonthlyOldDatas monthlyOldDatas = MonthlyOldDatas.loadData(require, 
				employeeId, this.yearMonth, this.closureId, this.closureDate);
		
		// 36協定時間の集計
		val monthlyCalculation = attendanceTimeOfMonthly.getMonthlyCalculation();
		val agreementTimeOpt = monthlyCalculation.aggregateAgreementTime(require, cacheCarrier,
				this.companyId, employeeId, this.yearMonth, this.closureId, this.closureDate, aggrPeriod,
				Optional.empty(), Optional.empty(), Optional.empty(), this.companySets, employeeSets,
				monthlyCalcDailys, monthlyOldDatas, Optional.empty());
		if (agreementTimeOpt.isPresent()){
			val agreementTime = agreementTimeOpt.get();
			
			// エラーメッセージがあれば、エラーメッセージを入れる
			if (!monthlyCalculation.getErrorInfos().isEmpty()){
				agreementTime.getAgreementTime().getAgreementTime().setConfirmedErrorMessage(
						monthlyCalculation.getErrorInfos().get(0).getMessage().v());
			}
			
			// 管理期間の36協定時間を返す
			return agreementTime;
		}
		
		return null;
	}
	
	public static interface RequireM2 extends MonAggrCompanySettings.RequireM5,
		MonAggrEmployeeSettings.RequireM2, RequireM1{
		
		ManagedParallelWithContext parallelContext();
		
		Map<GeneralDate, AttendanceTimeOfDailyAttendance> dailyAttendanceTimes(String employeeId, DatePeriod datePeriod);
	}
	
	public static interface RequireM1 extends MonthlyCalculatingDailys.RequireM3, 
		MonthlyCalculation.RequireM2, MonthlyOldDatas.RequireM1 {
		
		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthly(String employeeId, YearMonth yearMonth, ClosureId closureId);
	}
}