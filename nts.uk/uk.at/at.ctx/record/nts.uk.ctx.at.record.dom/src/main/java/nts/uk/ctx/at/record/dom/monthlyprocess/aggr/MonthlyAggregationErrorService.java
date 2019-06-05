package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;

/**
 * ドメインサービス：月別集計エラー処理
 * @author shuichi_ishida
 */
public interface MonthlyAggregationErrorService {

	/**
	 * エラー処理（排他エラー用）
	 * @param dataSetter データセッター
	 * @param employeeId 社員ID
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param outYmd 出力年月日
	 */
	void errorProcForOptimisticLock(
			TaskDataSetter dataSetter,
			String employeeId,
			String empCalAndSumExecLogID,
			GeneralDate outYmd);
}
