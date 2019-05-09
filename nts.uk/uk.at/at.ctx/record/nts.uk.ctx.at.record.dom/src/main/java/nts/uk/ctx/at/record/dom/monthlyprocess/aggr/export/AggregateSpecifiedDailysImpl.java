package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.AggregateMonthlyRecordService;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：指定した日別実績を集計
 * @author shuichi_ishida
 */
@Stateless
public class AggregateSpecifiedDailysImpl implements AggregateSpecifiedDailys {

	/** ドメインサービス：月別実績を集計する */
	@Inject
	private AggregateMonthlyRecordService aggregateMonthlyRecord;
	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	
	/** アルゴリズム */
	@Override
	public Optional<IntegrationOfMonthly> algorithm(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, DatePeriod period, Optional<String> empCalAndSumExecLogID,
			List<IntegrationOfDaily> dailyWorks, Optional<IntegrationOfMonthly> monthlyWork) {

		// 月別集計で必要な会社別設定を取得する
		val companySets = MonAggrCompanySettings.loadSettings(companyId, this.repositories);
		if (companySets.getErrorInfos().size() > 0){
			// エラー発生時
			return Optional.empty();
		}
		
		// 月別集計で必要な社員別設定を取得
		val employeeSets = MonAggrEmployeeSettings.loadSettings(companyId, employeeId, period, this.repositories);
		if (employeeSets.getErrorInfos().size() > 0){
			// エラー発生時
			return Optional.empty();
		}
		
		// 前回集計結果　（年休積立年休の集計結果）
		AggrResultOfAnnAndRsvLeave prevAggrResult = new AggrResultOfAnnAndRsvLeave();
		
		// 月別実績を集計する　（アルゴリズム）
		val value = this.aggregateMonthlyRecord.aggregate(companyId, employeeId,
				yearMonth, closureId, closureDate, period,
				prevAggrResult, Optional.empty(), Optional.empty(), Collections.emptyMap(),
				companySets, employeeSets, Optional.of(dailyWorks), monthlyWork);
		if (value.getErrorInfos().size() > 0) {
			// エラー発生時
			return Optional.empty();
		}

		// 月別実績(Work)を返す
		return Optional.of(value.getIntegration());
	}
}
