package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.List;
import java.util.Optional;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：月次集計
 * @author shuichu_ishida
 */
public interface MonthlyAggregationService {

	/**
	 * Managerクラス
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeIds 社員IDリスト
	 * @param datePeriod 期間
	 * @param executionAttr 実行区分　（手動、自動）
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionLog 実行ログ
	 */
	ProcessState manager(AsyncCommandHandlerContext asyncContext,
			String companyId, List<String> employeeIds, DatePeriod datePeriod,
			ExecutionAttr executionAttr, String empCalAndSumExecLogID, Optional<ExecutionLog> executionLog);
}
