package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.diagnose.stopwatch.Stopwatches;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.parallel.ParallelWithContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.workrule.specific.SpecificWorkRuleRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPUnitUseSettingRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：日別計算
 * @author shuichi_ishida
 */
@Stateless
public class DailyCalculationServiceImpl implements DailyCalculationService {

	/** リポジトリ：就業計算と集計実行ログ */
	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;
	/** リポジトリ：対象者ログ */
	//@Inject
	//private TargetPersonRepository targetPersonRepository;
	
	/** ドメインサービス：日別計算　（社員の日別実績を計算） */
	@Inject
	private DailyCalculationEmployeeService dailyCalculationEmployeeService;

	
	//休暇加算設定
	@Inject
	private HolidayAddtionRepository holidayAddtionRepository;
	//総拘束時間
	@Inject
	private SpecificWorkRuleRepository specificWorkRuleRepository;
	//会社ごとの代休設定
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	//乖離
	@Inject 
	private DivergenceTimeRepository divergenceTimeRepository;
	
	//エラーアラーム設定
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	@Inject
	//加給利用単位
	private BPUnitUseSettingRepository bPUnitUseSettingRepository;
	
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
		this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
				ExecutionStatus.PROCESSING.value);
		
		
		
		// 社員分ループ

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
		this.dailyCalculationEmployeeService.calculate(asyncContext,employeeIds, datePeriod,counter);
		/** end 並列処理、PARALLELSTREAM */
		
		if (stateHolder.isInterrupt()) return ProcessState.INTERRUPTION;
		
		// 完了処理
		this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
				ExecutionStatus.DONE.value);
		dataSetter.updateData("dailyCalculateStatus", ExecutionStatus.DONE.nameId);
		Stopwatches.printAll();
		Stopwatches.STOPWATCHES.clear();
		return ProcessState.SUCCESS;
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
