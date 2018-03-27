package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：月次集計
 * @author shuichi_ishida
 */
@Stateless
public class MonthlyAggregationServiceImpl implements MonthlyAggregationService {

	/** リポジトリ：就業計算と集計実行ログ */
	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;
	/** リポジトリ：対象者ログ */
	//@Inject
	//private TargetPersonRepository targetPersonRepository;
	
	/** ドメインサービス：月別集計　（社員の月別実績を集計する） */
	@Inject
	private MonthlyAggregationEmployeeService monthlyAggregationEmployeeService;
	
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
	@Override
	public ProcessState manager(AsyncCommandHandlerContext asyncContext, String companyId, List<String> employeeIds,
			DatePeriod datePeriod, ExecutionAttr executionAttr, String empCalAndSumExecLogID,
			Optional<ExecutionLog> executionLog) {
		
		ProcessState status = ProcessState.SUCCESS;
		
		// 実行状態　初期設定
		val dataSetter = asyncContext.getDataSetter();
		dataSetter.setData("monthlyAggregateCount", 0);
		dataSetter.setData("monthlyAggregateStatus", ExecutionStatus.PROCESSING.nameId);
		dataSetter.setData("monthlyAggregateHasError", " ");

		// 月次集計を実行するかチェックする
		// ※　実行しない時、終了状態＝正常終了
		if (!executionLog.isPresent()) return status;
		if (executionLog.get().getExecutionContent() != ExecutionContent.MONTHLY_AGGREGATION) return status;
		if (!executionLog.get().getMonlyAggregationSetInfo().isPresent()) return status;
		val executionContent = executionLog.get().getExecutionContent();
		
		// 実行種別　取得　（通常、再実行）
		ExecutionType reAggrAtr = executionLog.get().getMonlyAggregationSetInfo().get().getExecutionType();
		
		// ログ情報（実行ログ）を更新する
		this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
				ExecutionStatus.PROCESSING.value);
		
		// システム日付を取得する
		val criteriaDate = GeneralDate.today();
		
		// 対象社員を判断
		List<String> procEmployeeIds = new ArrayList<>();
		if (executionAttr == ExecutionAttr.AUTO){
			
			// 自動実行の時
			//*****（未）　社員IDリストを得るアルゴリズムの引数設計に不整合があるので、確認待ち。以下、仮対応。
			procEmployeeIds = employeeIds;
		}
		else {
			
			// 手動実行の時
			procEmployeeIds = employeeIds;
		}
		
		// 社員の数だけループ
		int aggregatedCount = 0;
		for (val employeeId : procEmployeeIds) {
		
			// 社員1人分の処理　（社員の月別実績を集計する）
			status = this.monthlyAggregationEmployeeService.aggregate(asyncContext,
					companyId, employeeId, criteriaDate, empCalAndSumExecLogID, reAggrAtr);

			if (status == ProcessState.SUCCESS){
				
				// 成功時、ログ情報（実行内容の完了状態(（社員別））を更新する
				//*****（未）　処理が不完全で、日別作成以外で使えない状態になっている。
				//*****（未）　ステータスは、何を設定するかが規定されていない。未処理は、1っぽい。
				//this.targetPersonRepository.update(employeeId, empCalAndSumExecLogID, 0);
				
				aggregatedCount++;
				dataSetter.updateData("monthlyAggregateCount", aggregatedCount);
			}
			if (status == ProcessState.INTERRUPTION){
				
				// 中断時
				dataSetter.updateData("monthlyAggregateStatus", ExecutionStatus.INCOMPLETE.nameId);
				break;
			}
		}
		if (status == ProcessState.INTERRUPTION) return status;
		
		// 処理を完了する
		this.empCalAndSumExeLogRepository.updateLogInfo(empCalAndSumExecLogID, executionContent.value,
				ExecutionStatus.DONE.value);
		dataSetter.updateData("monthlyAggregateStatus", ExecutionStatus.DONE.nameId);
		
		return status;
	}
}
