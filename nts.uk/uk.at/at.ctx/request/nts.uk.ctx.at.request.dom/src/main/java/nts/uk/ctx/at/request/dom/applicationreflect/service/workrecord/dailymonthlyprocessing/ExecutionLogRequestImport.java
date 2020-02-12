package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing;

import java.util.Optional;

public interface ExecutionLogRequestImport {
	/**
	 * 承認結果反映用の設定情報
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID＝パラメータ「実行ID」
	 * @param executionContent 実行内容＝”承認結果の反映”
	 * @return
	 */
	Optional<SetInforReflAprResultImport> optReflectResult(String empCalAndSumExecLogID, int executionContent);
	/**
	 * ドメインモデル「就業計算と集計実行ログ」を取得
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	Optional<ExeStateOfCalAndSumImport> executionStatus(String empCalAndSumExecLogID);
	/**
	 * ドメインモデル「対象者」を更新する
	 * @param empCalAndSumExecLogID
	 * @param executionContent enum ExecutionContent
	 * @param processStatus enum ExecutionStatus
	 */
	void updateLogInfo(String employeeID, String empCalAndSumExecLogId, int executionContent, int state);
	void updateLogInfo(String empCalAndSumExecLogId, int executionContent, int state);
	
	Boolean isCalWhenLock(String empCalAndSumExecLogId,int executionContent);
}
