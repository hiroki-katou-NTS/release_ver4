package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
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
		dataSetter.setData("dailyCalculateStatus", ExecutionStatus.PROCESSING.nameId);
		dataSetter.setData("dailyCalculateHasError", " ");

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
		int calculatedCount = 0;
		for (val employeeId : employeeIds) {
		
			// 社員の日別実績を計算
			status = this.dailyCalculationEmployeeService.calculate(asyncContext,
					employeeId, datePeriod, empCalAndSumExecLogID, reCalcAtr);

			// 状態確認
			if (status == ProcessState.SUCCESS){
				
				// ログ情報更新（実行内容の完了状態）
				// ※　成功時、社員別ログ情報（社員別完了状態）を更新する
				//*****（未）　処理が不完全で、日別作成以外で使えない状態になっている。
				//*****（未）　ステータスは、何を設定するかが規定されていない。未処理は、1っぽい。
				//this.targetPersonRepository.update(employeeId, empCalAndSumExecLogID, 0);
				
				calculatedCount++;
				dataSetter.updateData("dailyCalculateCount", calculatedCount);
			}
			if (status == ProcessState.INTERRUPTION){
				
				// 中断処理
				dataSetter.updateData("dailyCalculateStatus", ExecutionStatus.INCOMPLETE.nameId);
				break;
			}
		}
		if (status == ProcessState.INTERRUPTION) return status;
		
		// 完了処理
		this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
				ExecutionStatus.DONE.value);
		dataSetter.updateData("dailyCalculateStatus", ExecutionStatus.DONE.nameId);
		
		return status;
	}
}
