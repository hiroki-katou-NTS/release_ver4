package nts.uk.ctx.at.function.infra.entity.processexecution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name="KFNMT_EXEC_TASK_LOG")
@NoArgsConstructor
@Getter
@Setter
public class KfnmtExecutionTaskLog extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
    public KfnmtExecutionTaskLogPK kfnmtExecTaskLogPK;
	
	/* 終了状態 */
	@Column(name = "STATUS")
	public Integer status;
	
	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name="CID", referencedColumnName="CID", insertable = false, updatable = false),
		@JoinColumn(name="EXEC_ITEM_CD", referencedColumnName="EXEC_ITEM_CD", insertable = false, updatable = false),
		@JoinColumn(name="EXEC_ID", referencedColumnName="EXEC_ID", insertable = false, updatable = false)})
	public KfnmtProcessExecutionLog procExecLogItem;
	
	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name="CID", referencedColumnName="CID", insertable = false, updatable = false),
		@JoinColumn(name="EXEC_ITEM_CD", referencedColumnName="EXEC_ITEM_CD", insertable = false, updatable = false),
		@JoinColumn(name="EXEC_ID", referencedColumnName="EXEC_ID", insertable = false, updatable = false)})
	public KfnmtProcessExecutionLogHistory procExecLogHistItem;
	
	@Override
	protected Object getKey() {
		return this.kfnmtExecTaskLogPK;
	}
	
	public KfnmtExecutionTaskLog(KfnmtExecutionTaskLogPK kfnmtExecTaskLogPK, Integer status) {
		super();
		this.kfnmtExecTaskLogPK = kfnmtExecTaskLogPK;
		this.status = status;
	}
	
	public static KfnmtExecutionTaskLog toEntity(String companyId, String execItemCd, String execId, ExecutionTaskLog domain) {
		
		Integer status = null;
		if(domain.getStatus()!=null){
			if(domain.getStatus().isPresent()){
				status =domain.getStatus().get().value;
			}
		}
		
		return new KfnmtExecutionTaskLog(
				new KfnmtExecutionTaskLogPK(companyId, execItemCd, execId, domain.getProcExecTask().value),
				status);
	}
	
public static List<KfnmtExecutionTaskLog> toEntity(String companyId, String execItemCd, String execId, List<ExecutionTaskLog> domains) {
	List<KfnmtExecutionTaskLog> datas = new ArrayList<>();
		for(ExecutionTaskLog executionTaskLog : domains) {
			Integer status = null;
			if(executionTaskLog.getStatus()!=null){
				if(executionTaskLog.getStatus().isPresent()){
					status =executionTaskLog.getStatus().get().value;
				}
			}
			KfnmtExecutionTaskLog kfnmtExecutionTaskLog = new KfnmtExecutionTaskLog(
					new KfnmtExecutionTaskLogPK(companyId, execItemCd, execId, executionTaskLog.getProcExecTask().value),
					status);
			datas.add(kfnmtExecutionTaskLog);
		}
		
		return datas;
	}
	
	public ExecutionTaskLog toDomain() {
		
		return new ExecutionTaskLog(EnumAdaptor.valueOf(this.kfnmtExecTaskLogPK.taskId, ProcessExecutionTask.class),
				(this.status!=null)?Optional.ofNullable(EnumAdaptor.valueOf(this.status.intValue(), EndStatus.class)):Optional.empty());
		
	}
	
	public ExecutionTaskLog toNewDomain() {
		
		return new ExecutionTaskLog(EnumAdaptor.valueOf(this.kfnmtExecTaskLogPK.taskId, ProcessExecutionTask.class),
				(this.status!=null)?Optional.ofNullable(EnumAdaptor.valueOf(this.status.intValue(), EndStatus.class)):Optional.empty(),this.kfnmtExecTaskLogPK.execId);
		
	}
	
	
}
