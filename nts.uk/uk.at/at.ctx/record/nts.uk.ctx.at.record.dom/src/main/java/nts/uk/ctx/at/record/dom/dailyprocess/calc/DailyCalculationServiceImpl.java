package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.diagnose.stopwatch.Stopwatches;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：日別計算
 * @author shuichi_ishida
 */
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class DailyCalculationServiceImpl implements DailyCalculationService {

	/** リポジトリ：就業計算と集計実行ログ */
	//@Inject
	//private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;
	/** リポジトリ：対象者ログ */
	//@Inject
	//private TargetPersonRepository targetPersonRepository;
	/*リポジトリ：実行ログ*/
	@Inject
	private ExecutionLogRepository executionLogRepository;
	/** ドメインサービス：日別計算　（社員の日別実績を計算） */
	@Inject
	private DailyCalculationEmployeeService dailyCalculationEmployeeService;
	
	
	/**
	 * Managerクラス
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param employeeIds 社員IDリスト
	 * @param datePeriod 期間
	 * @param executionAttr 実行区分　（手動、自動）
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionLog 実行ログ
	 */
	@Override
	public ProcessState manager(AsyncCommandHandlerContext asyncContext, List<String> employeeIds,
			DatePeriod datePeriod, ExecutionAttr executionAttr, String empCalAndSumExecLogID,
			Optional<ExecutionLog> executionLog) {
		
		ProcessState status = ProcessState.SUCCESS;
		// 実行状態　初期設定
		//*****（未）　setDataのID名を、画面PGでの表示値用セションID名と合わせる必要があるので、画面PGの修正と調整要。
		//*****（未）　表示させるエラーが出た時は、HasErrorに任意のタイミングでメッセージを入れて返せばいいｊはず。画面側のエラー表示仕様の確認も要。
		val dataSetter = asyncContext.getDataSetter();
		dataSetter.setData("dailyCalculateCount", 0);
		dataSetter.setData("dailyCalculateHasError", ErrorPresent.NO_ERROR.nameId );

		// 設定情報を取得　（日別計算を実行するかチェックする）
		//　※　実行しない時、終了状態＝正常終了
		if (!executionLog.isPresent()) return status;
		if (executionLog.get().getExecutionContent() != ExecutionContent.DAILY_CALCULATION) return status;
		if (!executionLog.get().getDailyCalSetInfo().isPresent()) return status;
		val executionContent = executionLog.get().getExecutionContent();
		// 実行種別　取得　（通常、再実行）
		ExecutionType reCalcAtr = executionLog.get().getDailyCalSetInfo().get().getExecutionType();
		// ログ情報更新（実行ログ）　→　処理中
		updatelog(empCalAndSumExecLogID, executionContent,ExecutionStatus.PROCESSING);
		
		/** start 並列処理、PARALLELSTREAM */
		StateHolder stateHolder = new StateHolder(employeeIds.size());
		// 社員の日別実績を計算
//		if(stateHolder.isInterrupt()){
//			return;
//		}
		
		Consumer<ProcessState> counter = (cStatus) -> {
			stateHolder.add(cStatus);
			// 状態確認
			if (cStatus == ProcessState.SUCCESS){
				dataSetter.updateData("dailyCalculateCount", stateHolder.count());
			}
			if (cStatus == ProcessState.INTERRUPTION){
				dataSetter.updateData("dailyCalculateStatus", ExecutionStatus.INCOMPLETE.nameId);
			}
		};
		this.dailyCalculationEmployeeService.calculate(asyncContext,employeeIds, datePeriod,counter,reCalcAtr,empCalAndSumExecLogID);
		/** end 並列処理、PARALLELSTREAM */
		
		if (stateHolder.isInterrupt()) return ProcessState.INTERRUPTION;
		
		// 完了処理
		updatelog(empCalAndSumExecLogID,executionContent,ExecutionStatus.DONE);
		//就業計算と集計ログ
		//this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
		//		ExecutionStatus.DONE.value);
		dataSetter.updateData("dailyCalculateStatus", ExecutionStatus.DONE.nameId);
		Stopwatches.printAll();
		Stopwatches.STOPWATCHES.clear();
		return ProcessState.SUCCESS;
	}
	
	/**
	 * 実行ログの更新(transaction管理のため別メソッド実装)
	 * @param empCalAndSumExecLogID 実行ログID
	 * @param executionContent 設定情報（日別計算を実行するか）
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void updatelog(String empCalAndSumExecLogID, ExecutionContent executionContent,ExecutionStatus state) {
		//実行ログ
		this.executionLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,state.value);		
	}
	
	class StateHolder {
		private BlockingQueue<ProcessState> status;
		
		StateHolder(int max){
			status = new ArrayBlockingQueue<ProcessState>(max);
		}
		
		void add(ProcessState status){
			this.status.add(status);
		}
		
		int count(){
			return this.status.size();
		}
		
		boolean isInterrupt(){
			return this.status.stream().filter(s -> s == ProcessState.INTERRUPTION).findFirst().isPresent();
		}
	}
}
