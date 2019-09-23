package nts.uk.ctx.at.function.dom.processexecution.executionlog;

import java.util.List;
import java.util.Optional;

//import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;

/**
 * 更新処理自動実行ログ履歴
 */
@Getter
@Setter
public class ProcessExecutionLogHistory extends AggregateRoot {
	/* コード */
	private ExecutionCode execItemCd;
	
	/* 会社ID */
	private String companyId;
	
	/* 全体のエラー詳細  -> 強制終了の原因*/
	private Optional<OverallErrorDetail>  overallError;
	
	/* 全体の終了状態 */
	private Optional<EndStatus> overallStatus;
	
	/* 前回実行日時 */
	private GeneralDateTime lastExecDateTime;
	
	/* 各処理の期間 */
	private EachProcessPeriod eachProcPeriod;
	
	/* 各処理の終了状態 */
	private List<ExecutionTaskLog> taskLogList;
	
	/* 実行ID */
	private String execId;
	
	/* 前回終了日時*/
	private GeneralDateTime lastEndExecDateTime;
	
	/* 全体のシステムエラー状態*/
	private Boolean errorSystem;
	
	/* 全体の業務エラー状態*/
	private Boolean errorBusiness;

	public ProcessExecutionLogHistory(ExecutionCode execItemCd, String companyId,
			OverallErrorDetail overallError, EndStatus overallStatus,
			GeneralDateTime lastExecDateTime, EachProcessPeriod eachProcPeriod, List<ExecutionTaskLog> taskLogList,
			String execId,GeneralDateTime lastEndExecDateTime,Boolean errorSystem, Boolean errorBusiness) {
		super();
		this.execItemCd = execItemCd;
		this.companyId = companyId;
		this.overallError = Optional.ofNullable(overallError);
		this.overallStatus = Optional.ofNullable(overallStatus);
		this.lastExecDateTime = lastExecDateTime;
		this.eachProcPeriod = eachProcPeriod;
		this.taskLogList = taskLogList;
		this.execId = execId;
		this.lastEndExecDateTime = lastEndExecDateTime;
		this.errorSystem = errorSystem;
		this.errorBusiness = errorBusiness;
		
	}
	
	
}
