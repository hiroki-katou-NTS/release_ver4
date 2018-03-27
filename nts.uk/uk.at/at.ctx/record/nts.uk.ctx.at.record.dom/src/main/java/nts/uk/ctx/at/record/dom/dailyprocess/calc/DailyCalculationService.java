package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：日別計算
 * @author shuichu_ishida
 */
public interface DailyCalculationService {

	/**
	 * Managerクラス
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param employeeIds 社員IDリスト
	 * @param datePeriod 期間
	 * @param executionAttr 実行区分　（手動、自動）
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionLog 実行ログ
	 */
	ProcessState manager(AsyncCommandHandlerContext asyncContext,
			List<String> employeeIds, DatePeriod datePeriod,
			ExecutionAttr executionAttr, String empCalAndSumExecLogID, Optional<ExecutionLog> executionLog);
}
