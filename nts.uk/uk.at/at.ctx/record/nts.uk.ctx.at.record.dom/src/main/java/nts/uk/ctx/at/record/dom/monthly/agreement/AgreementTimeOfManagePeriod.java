package nts.uk.ctx.at.record.dom.monthly.agreement;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.weekly.WeeklyCalculation;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 管理期間の36協定時間
 * @author shuichi_ishida
 */
@Getter
public class AgreementTimeOfManagePeriod extends AggregateRoot {

	/** 社員ID */
	private String employeeId;
	/** 月度 */
	private YearMonth yearMonth;
	/** 年度 */
	private Year year;
	/** 36協定時間 */
	private AgreementTimeOfMonthly agreementTime;
	/** 内訳 */
	private AgreementTimeBreakdown breakdown;

	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 月度
	 */
	public AgreementTimeOfManagePeriod(String employeeId, YearMonth yearMonth){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.year = new Year(yearMonth.year());
		this.agreementTime = new AgreementTimeOfMonthly();
		this.breakdown = new AgreementTimeBreakdown();
		
		this.errorInfos = new ArrayList<>();
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 月度
	 * @param year 年度
	 * @param agreementTime 36協定時間
	 * @param breakdown 内訳
	 * @return 管理期間の36協定時間
	 */
	public static AgreementTimeOfManagePeriod of(
			String employeeId,
			YearMonth yearMonth,
			Year year,
			AgreementTimeOfMonthly agreementTime,
			AgreementTimeBreakdown breakdown){
	
		AgreementTimeOfManagePeriod domain = new AgreementTimeOfManagePeriod(employeeId, yearMonth);
		domain.year = year;
		domain.agreementTime = agreementTime;
		domain.breakdown = breakdown;
		return domain;
	}
	
	/**
	 * ファクトリー　（エラー出力用）
	 * @param employeeId 社員ID
	 * @param yearMonth 月度
	 * @param errorInfos エラー情報　（月の計算の結果より）
	 * @return 管理期間の36協定時間
	 */
	public static AgreementTimeOfManagePeriod of(
			String employeeId,
			YearMonth yearMonth,
			List<MonthlyAggregationErrorInfo> errorInfos){
	
		AgreementTimeOfManagePeriod domain = new AgreementTimeOfManagePeriod(employeeId, yearMonth);
		domain.errorInfos.addAll(errorInfos);
		return domain;
	}
	
	/**
	 * 作成
	 * @param criteriaDate 基準日
	 * @param aggregateAtr 集計区分
	 * @param monthlyCalculation 月の計算
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregate(
			GeneralDate criteriaDate,
			MonthlyAggregateAtr aggregateAtr,
			MonthlyCalculation monthlyCalculation,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		this.year = monthlyCalculation.getYear();
		
		// 36協定時間の対象を取得
		this.breakdown.getTargetItemOfAgreement(aggregateAtr, monthlyCalculation, repositories);
		
		// 36協定時間内訳の合計時間を36協定時間とする
		this.agreementTime.setAgreementTime(this.breakdown.getTotalTime());
		
		// エラーアラーム値の取得
		this.agreementTime.getErrorAlarmValue(
				monthlyCalculation.getCompanyId(), monthlyCalculation.getEmployeeId(), criteriaDate,
				monthlyCalculation.getYearMonth(), monthlyCalculation.getWorkingSystem(), repositories);
		
		// エラーチェック
		this.agreementTime.errorCheck();
	}
	
	/**
	 * 作成　（週用）
	 * @param year 年度
	 * @param criteriaDate 基準日
	 * @param aggregateAtr 集計区分
	 * @param weeklyCalculation 週別の計算
	 * @param companySets 月別集計で必要な会社別設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateForWeek(
			Year year,
			GeneralDate criteriaDate,
			MonthlyAggregateAtr aggregateAtr,
			WeeklyCalculation weeklyCalculation,
			MonAggrCompanySettings companySets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		this.year = year;
		
		// 36協定時間の対象を取得
		this.breakdown.getTargetItemOfAgreementForWeek(aggregateAtr, weeklyCalculation, companySets);
		
		// 36協定時間内訳の合計時間を36協定時間とする
		this.agreementTime.setAgreementTime(this.breakdown.getTotalTime());
		
		// エラーアラーム値の取得
		this.agreementTime.getErrorAlarmValueForWeek(
				weeklyCalculation.getCompanyId(), weeklyCalculation.getEmployeeId(),
				criteriaDate, weeklyCalculation.getWorkingSystem(), repositories);
		
		// エラーチェック
		this.agreementTime.errorCheck();
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AgreementTimeOfManagePeriod target){
		this.agreementTime.sum(target.agreementTime);
		this.breakdown.sum(target.breakdown);
	}
}