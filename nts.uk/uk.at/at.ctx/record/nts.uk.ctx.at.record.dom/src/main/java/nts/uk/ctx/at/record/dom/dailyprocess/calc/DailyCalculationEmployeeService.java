package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
//import nts.arc.task.data.TaskDataSetter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.DailyCalculationServiceImpl.StateHolder;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：日別計算　（社員の日別実績を計算）
 * @author shuichu_ishida
 */
public interface DailyCalculationEmployeeService {

	/**
	 * 社員の日別実績を計算
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param counter 
	 * @param counter 
	 * @param reCalcAtr 
	 * @param empCalAndSumExecLogID 
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionType 実行種別　（通常、再実行）
	 * @param companyCommonSetting 
	 * @return 排他エラーが発生したフラグ
	 */
	@SuppressWarnings("rawtypes")
	List<Boolean> calculate(List<String> employeeId,DatePeriod datePeriod, Consumer<ProcessState> counter, ExecutionType reCalcAtr, String empCalAndSumExecLogID);
	
	/**
	 * 社員の日別実績を計算(承認一覧から呼び出す用)
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param counter 
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionType 実行種別　（通常、再実行）
	 * @param companyCommonSetting 
	 */
	@SuppressWarnings("rawtypes")
	ProcessState calculateForOnePerson(String employeeId,DatePeriod datePeriod, Optional<Consumer<ProcessState>> counter,String executeLogId);

	
	/**
	 * 計算状態の更新
	 * @param stateInfo
	 */
	public void upDateCalcState(ManageCalcStateAndResult stateInfo);
}
