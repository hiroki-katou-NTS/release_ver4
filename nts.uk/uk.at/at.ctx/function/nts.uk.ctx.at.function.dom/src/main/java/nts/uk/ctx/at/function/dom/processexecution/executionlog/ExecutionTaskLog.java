package nts.uk.ctx.at.function.dom.processexecution.executionlog;

import java.util.Optional;

//import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDateTime;

/**
 * 更新処理自動実行タスクログ
 */
@Getter
@Setter
public class ExecutionTaskLog {
	/* 更新処理 */
	private ProcessExecutionTask procExecTask;
	
	/* 終了状態 */
	private Optional<EndStatus> status;
	
	
	private String execId;
	
	/* 前回実行日時 */
	private GeneralDateTime lastExecDateTime;
	
	/* 前回終了日時*/
	private GeneralDateTime lastEndExecDateTime;
	
	/* 全体のシステムエラー状態*/
	private Boolean errorSystem;
	
	/* 全体の業務エラー状態*/
	private Boolean errorBusiness;

	public ExecutionTaskLog(ProcessExecutionTask procExecTask, Optional<EndStatus> status) {
		super();
		this.procExecTask = procExecTask;
		this.status = status;
	}

	public ExecutionTaskLog(ProcessExecutionTask procExecTask, Optional<EndStatus> status, String execId,
			GeneralDateTime lastExecDateTime, GeneralDateTime lastEndExecDateTime, Boolean errorSystem,
			Boolean errorBusiness) {
		super();
		this.procExecTask = procExecTask;
		this.status = status;
		this.execId = execId;
		this.lastExecDateTime = lastExecDateTime;
		this.lastEndExecDateTime = lastEndExecDateTime;
		this.errorSystem = errorSystem;
		this.errorBusiness = errorBusiness;
	}

//	public ExecutionTaskLog(ProcessExecutionTask procExecTask, Optional<EndStatus> status, String execId) {
//		super();
//		this.procExecTask = procExecTask;
//		this.status = status;
//		this.execId = execId;
//	}
	
	
}
