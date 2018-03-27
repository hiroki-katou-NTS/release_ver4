package nts.uk.ctx.at.record.dom.monthly.agreement;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 管理期間の36協定時間
 * @author shuichu_ishida
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
	 * 作成
	 * @param companyId 会社ID
	 * @param year 年度
	 * @param criteriaDate 基準日
	 * @param aggregateAtr 集計区分
	 * @param monthlyCalculation 月の計算
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregate(
			String companyId,
			Year year,
			GeneralDate criteriaDate,
			MonthlyAggregateAtr aggregateAtr,
			MonthlyCalculation monthlyCalculation,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 36協定時間の対象を取得
		this.breakdown.getTargetItemOfAgreement(aggregateAtr, monthlyCalculation, repositories);
		
		// 36協定時間内訳の合計時間を36協定時間とする
		this.agreementTime.setAgreementTime(this.breakdown.getTotalTime());
		
		// エラーチェック
		this.agreementTime.errorCheck(companyId, monthlyCalculation.getEmployeeId(), criteriaDate,
				monthlyCalculation.getYearMonth(), monthlyCalculation.getWorkingSystem(), repositories);
	}
}
