package nts.uk.ctx.at.function.dom.processexecution.executionlog;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;

/**
 * 更新処理自動実行管理
 */
@Getter
@AllArgsConstructor
public class ProcessExecutionLogManage extends AggregateRoot {
	/* コード */
	private ExecutionCode execItemCd;
	
	/* 会社ID */
	private String companyId;
	
	/* 全体のエラー詳細 */
	private OverallErrorDetail overallError;
	
	/* 全体の終了状態 */
	private Optional<EndStatus> overallStatus;
	
	/* 前回実行日時 */
	private GeneralDateTime lastExecDateTime;
	
	
	/* 現在の実行状態 */
	private CurrentExecutionStatus currentStatus;
	
	/* 前回実行日時（即時実行含めない） */
	private GeneralDateTime lastExecDateTimeEx;

	public ProcessExecutionLogManage(ExecutionCode execItemCd, String companyId, EndStatus overallStatus, CurrentExecutionStatus currentStatus) {
		super();
		this.execItemCd = execItemCd;
		this.companyId = companyId;
		this.overallStatus =Optional.ofNullable(overallStatus);
		this.currentStatus = currentStatus;
		this.overallError = null;
		this.lastExecDateTime = null;
		this.lastExecDateTimeEx = null;
	}

	public void setExecItemCd(ExecutionCode execItemCd) {
		this.execItemCd = execItemCd;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setOverallError(OverallErrorDetail overallError) {
		this.overallError = overallError;
	}

	public void setOverallStatus(EndStatus overallStatus) {
		this.overallStatus = Optional.ofNullable(overallStatus);
	}

	public void setLastExecDateTime(GeneralDateTime lastExecDateTime) {
		this.lastExecDateTime = lastExecDateTime;
	}

	public void setCurrentStatus(CurrentExecutionStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public void setLastExecDateTimeEx(GeneralDateTime lastExecDateTimeEx) {
		this.lastExecDateTimeEx = lastExecDateTimeEx;
	}
	
}
