package nts.uk.ctx.at.function.infra.entity.processexecution;

//import java.io.Serializable;
//import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.EachProcessPeriod;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
//import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name="KFNMT_PRO_EXE_LOG_MANAGE")
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtProcessExecutionLogManage extends UkJpaEntity {
	/* 主キー */
	@EmbeddedId
    public KfnmtProcessExecutionLogManagePK kfnmtProcExecLogPK;
	
	/* 現在の実行状態 */
	@Column(name = "CURRENT_STATUS")
	public Integer currentStatus;
	
	/* 全体の終了状態 */
	@Column(name = "OVERALL_STATUS")
	public Integer overallStatus;
	
	/* 全体のエラー詳細 */
	@Column(name = "ERROR_DETAIL")
	public Integer errorDetail;
	
	/* 前回実行日時 */
	@Column(name = "LAST_EXEC_DATETIME")
	public GeneralDateTime lastExecDateTime;
	
	/* 前回実行日時（即時実行含めない） */
	@Column(name = "LAST_EXEC_DATETIME_EX")
	public GeneralDateTime prevExecDateTimeEx;
	
	@Override
	protected Object getKey() {
		return this.kfnmtProcExecLogPK;
	}
	
	
	public static KfnmtProcessExecutionLogManage toEntity(ProcessExecutionLogManage domain) {
		return new KfnmtProcessExecutionLogManage(
				new KfnmtProcessExecutionLogManagePK(domain.getCompanyId(), domain.getExecItemCd().v()),
				domain.getCurrentStatus() == null ? null : domain.getCurrentStatus().value,
				(domain.getOverallStatus() == null|| !domain.getOverallStatus().isPresent() ) ? null : domain.getOverallStatus().get().value,
				domain.getOverallError() == null ? null : domain.getOverallError().value,
				domain.getLastExecDateTime(),			
				domain.getLastExecDateTimeEx());
	}
	
	public ProcessExecutionLogManage toDomain(){
		return new ProcessExecutionLogManage(new ExecutionCode(this.kfnmtProcExecLogPK.execItemCd),
				this.kfnmtProcExecLogPK.companyId,
				this.errorDetail == null ? null : EnumAdaptor.valueOf(this.errorDetail, OverallErrorDetail.class),
				this.overallStatus == null ? Optional.empty() : Optional.ofNullable(EnumAdaptor.valueOf(this.overallStatus, EndStatus.class)),
				this.lastExecDateTime,
				EnumAdaptor.valueOf(this.currentStatus, CurrentExecutionStatus.class),
				this.prevExecDateTimeEx);
	}
	
	
}
