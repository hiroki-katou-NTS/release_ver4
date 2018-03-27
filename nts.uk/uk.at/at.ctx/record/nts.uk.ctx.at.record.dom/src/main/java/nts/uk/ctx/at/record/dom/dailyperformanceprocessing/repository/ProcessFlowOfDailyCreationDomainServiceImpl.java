package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DailyCalculationService;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationService;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPerson;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ProcessFlowOfDailyCreationDomainServiceImpl implements ProcessFlowOfDailyCreationDomainService{
	
	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;
	
	@Inject
	private TargetPersonRepository targetPersonRepository;
	
	@Inject
	private CreateDailyResultDomainService createDailyResultDomainService;
	
	@Inject
	private DailyCalculationService dailyCalculationService;
	
	@Inject
	private MonthlyAggregationService monthlyAggregationService;
	
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	@Inject
	private ExecutionLogRepository executionLogRepository;
	
//	@Inject
//	private PersonInfoAdapter personInfoAdapter;

	@Override
	public <C> void processFlowOfDailyCreation(AsyncCommandHandlerContext<C> asyncContext, ExecutionAttr executionAttr, DatePeriod periodTime,
			String empCalAndSumExecLogID) {
		
		TaskDataSetter dataSetter = asyncContext.getDataSetter();
		dataSetter.setData("dailyCreateCount", 0);
		dataSetter.setData("dailyCreateStatus", ExecutionStatus.PROCESSING.nameId);
		dataSetter.setData("dailyCreateHasError", " ");
		
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
//		String employeeID = AppContexts.user().employeeId();

		//①実行方法を取得する
		//Optional<ExecutionLog> executionLog = empCalAndSumExeLog.get().getExecutionLogs().stream().filter(item -> item.getExecutionContent() == ExecutionContent.DAILY_CREATION).findFirst();
		//***** 日別作成に限定されていたため、暫定的に、より正確な取得処理に一時調整。（2018.1.16 Shuichi Ishida）
		Optional<EmpCalAndSumExeLog> empCalAndSumExeLog = this.empCalAndSumExeLogRepository.getByEmpCalAndSumExecLogID(empCalAndSumExecLogID);

		//②対象者を取得する
		List<TargetPerson> targetPersons = new ArrayList<>();
		//パラメータ「実行区分」＝手動　の場合 (Manual)
		if(executionAttr == ExecutionAttr.MANUAL){
			targetPersons = this.targetPersonRepository.getTargetPersonById(empCalAndSumExecLogID);
		} 
		//パラメータ「実行区分」＝自動　の場合 (Auto)
		else if(executionAttr == ExecutionAttr.AUTO){
			// アルゴリズム「社員IDから個人社員基本情報を取得」を実行する
			//this.personInfoAdapter.getListPersonInfo(listSid);
		}
		
		// get List personId
		List<String> employeeIdList = targetPersons.stream().map(f -> {
			return f.getEmployeeId();
		}).collect(Collectors.toList());

		dataSetter.setData("dailyCreateTotal", targetPersons.size());
		
		ProcessState finalStatus = ProcessState.SUCCESS;

		// 実行ログを確認する
		
		//val executionLogs = empCalAndSumExeLog.get().getExecutionLogs();
		val executionLogs = this.executionLogRepository.getExecutionLogs(empCalAndSumExeLog.get().getEmpCalAndSumExecLogID());
		Map<ExecutionContent, ExecutionLog> logsMap = new HashMap<>();
		for (val executionLog : executionLogs){
			logsMap.put(executionLog.getExecutionContent(), executionLog);
		}
		
		// 日別実績の作成　実行
		if (logsMap.containsKey(ExecutionContent.DAILY_CREATION)
				&& finalStatus == ProcessState.SUCCESS) {
			
			Optional<ExecutionLog> dailyCreationLog =
					Optional.of(logsMap.get(ExecutionContent.DAILY_CREATION));
			finalStatus = this.createDailyResultDomainService.createDailyResult(asyncContext, employeeIdList,
					periodTime, executionAttr, companyId, empCalAndSumExecLogID, dailyCreationLog);
		}
		
		//***** ↓　以下、仮実装。ログ制御全体を見直して、正確な手順に再修正要。（2018.1.16 Shuichi Ishida）
		// 日別実績の計算　実行
		if (logsMap.containsKey(ExecutionContent.DAILY_CALCULATION)
				&& finalStatus == ProcessState.SUCCESS) {
			
			Optional<ExecutionLog> dailyCalculationLog =
					Optional.of(logsMap.get(ExecutionContent.DAILY_CALCULATION));
			finalStatus = this.dailyCalculationService.manager(asyncContext, employeeIdList,
					periodTime, executionAttr, empCalAndSumExecLogID, dailyCalculationLog);
		}
		
		// 月別実績の集計　実行
		if (logsMap.containsKey(ExecutionContent.MONTHLY_AGGREGATION)
				&& finalStatus == ProcessState.SUCCESS) {
			
			Optional<ExecutionLog> monthlyAggregationLog =
					Optional.of(logsMap.get(ExecutionContent.MONTHLY_AGGREGATION));
			finalStatus = this.monthlyAggregationService.manager(asyncContext, companyId, employeeIdList,
					periodTime, executionAttr, empCalAndSumExecLogID, monthlyAggregationLog);
		}
		//***** ↑
		
		// ドメインモデル「就業計算と修正実行ログ」を更新する
		// 就業計算と集計実行ログ．実行状況　←　実行中止
		if (finalStatus == ProcessState.INTERRUPTION) {
			this.empCalAndSumExeLogRepository.updateStatus(empCalAndSumExecLogID, ExeStateOfCalAndSum.STOPPING.value);
		} else {
			// 完了処理 (Xử lý hoàn thành)
			ExeStateOfCalAndSum executionStatus = this.updateExecutionState(dataSetter, empCalAndSumExecLogID);
			this.empCalAndSumExeLogRepository.updateStatus(empCalAndSumExecLogID, executionStatus.value);
		}
		
		//*****　更新タイミングが悪い。ここで書かずに、日別作成の中で書くべき。（2018.1.16 Shuichi Ishida）
		//***** タイミング調整に関しては、実行ログの監視処理の完了判定も、念のため、確認が必要。
		dataSetter.updateData("dailyCreateStatus", ExecutionStatus.DONE.nameId);
		
	}
	
	private ExeStateOfCalAndSum updateExecutionState(TaskDataSetter dataSetter, String empCalAndSumExecLogID){
		
		// 0 : 完了
		// 1 : 完了（エラーあり）
		ExeStateOfCalAndSum executionStatus = ExeStateOfCalAndSum.DONE;
		
		List<ErrMessageInfo> errMessageInfos = this.errMessageInfoRepository.getAllErrMessageInfoByEmpID(empCalAndSumExecLogID);
		List<String> errorMessage = errMessageInfos.stream().map(error -> {
			return error.getMessageError().v();
		}).collect(Collectors.toList());
		if (errorMessage.isEmpty()) {
			executionStatus = ExeStateOfCalAndSum.DONE;
			dataSetter.updateData("dailyCreateHasError", ErrorPresent.NO_ERROR.nameId);

		} else {
			executionStatus = ExeStateOfCalAndSum.DONE_WITH_ERROR;
			dataSetter.updateData("dailyCreateHasError", ErrorPresent.HAS_ERROR.nameId);
		}
		
		return executionStatus;
	}

	
	
}
