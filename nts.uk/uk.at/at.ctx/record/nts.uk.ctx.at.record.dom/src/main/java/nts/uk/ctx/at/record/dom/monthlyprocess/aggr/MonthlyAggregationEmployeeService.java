package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * ドメインサービス：月別集計　（社員の月別実績を集計する）
 * @author shuichi_ishida
 */
public interface MonthlyAggregationEmployeeService {

	/**
	 * 社員の月別実績を集計する
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionType 実行種別　（通常、再実行）
	 * @return 終了状態
	 */
	@SuppressWarnings("rawtypes")
	ProcessState aggregate(AsyncCommandHandlerContext asyncContext,
			String companyId, String employeeId, GeneralDate criteriaDate,
			String empCalAndSumExecLogID, ExecutionType executionType);

	/**
	 * 社員の月別実績を集計する
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionType 実行種別　（通常、再実行）
	 * @param companySets 月別集計で必要な会社別設定
	 * @return 終了状態
	 */
	@SuppressWarnings("rawtypes")
	MonthlyAggrEmpServiceValue aggregate(AsyncCommandHandlerContext asyncContext,
			String companyId, String employeeId, GeneralDate criteriaDate,
			String empCalAndSumExecLogID, ExecutionType executionType,
			MonAggrCompanySettings companySets);
}
