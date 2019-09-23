package nts.uk.ctx.at.function.infra.entity.processexecution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
//import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EachProcessPeriod;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KFNMT_PROC_EXEC_LOG")
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtProcessExecutionLog extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KfnmtProcessExecutionLogPK kfnmtProcExecLogPK;

	/* スケジュール作成の期間 */
	@Column(name = "SCH_CREATE_START")
	public GeneralDate schCreateStart;

	/* スケジュール作成の期間 */
	@Column(name = "SCH_CREATE_END")
	public GeneralDate schCreateEnd;

	/* 日別作成の期間 */
	@Column(name = "DAILY_CREATE_START")
	public GeneralDate dailyCreateStart;

	/* 日別作成の期間 */
	@Column(name = "DAILY_CREATE_END")
	public GeneralDate dailyCreateEnd;

	/* 日別計算の期間 */
	@Column(name = "DAILY_CALC_START")
	public GeneralDate dailyCalcStart;

	/* 日別計算の期間 */
	@Column(name = "DAILY_CALC_END")
	public GeneralDate dailyCalcEnd;

	/* 承認結果反映 */
	@Column(name = "RFL_APPR_START")
	public GeneralDate reflectApprovalResultStart;

	/* 承認結果反映 */
	@Column(name = "RFL_APPR_END")
	public GeneralDate reflectApprovalResultEnd;

	@OneToMany(mappedBy = "procExecLogItem", cascade = CascadeType.ALL)
	@JoinTable(name = "KFNMT_EXEC_TASK_LOG")
	public List<KfnmtExecutionTaskLog> taskLogList;

	@Override
	protected Object getKey() {
		return this.kfnmtProcExecLogPK;
	}

	public ProcessExecutionLog toDomain() {
		List<ExecutionTaskLog> taskLogList = this.taskLogList.stream().map(x -> x.toNewDomain())
				.collect(Collectors.toList());
		
		DatePeriod scheduleCreationPeriod = (this.schCreateStart == null || this.schCreateEnd == null) ? null
				: new DatePeriod(this.schCreateStart, this.schCreateEnd);
		DatePeriod dailyCreationPeriod = (this.dailyCreateStart == null || this.dailyCreateEnd == null) ? null
				: new DatePeriod(this.dailyCreateStart, this.dailyCreateEnd);
		DatePeriod dailyCalcPeriod = (this.dailyCalcStart == null || this.dailyCalcEnd == null) ? null
				: new DatePeriod(this.dailyCalcStart, this.dailyCalcEnd);
		DatePeriod reflectApprovalResult = (this.reflectApprovalResultStart == null
				|| this.reflectApprovalResultEnd == null) ? null
						: new DatePeriod(this.reflectApprovalResultStart, this.reflectApprovalResultEnd);
		return new ProcessExecutionLog(new ExecutionCode(this.kfnmtProcExecLogPK.execItemCd),
				this.kfnmtProcExecLogPK.companyId, Optional.ofNullable(new EachProcessPeriod(scheduleCreationPeriod,
						dailyCreationPeriod, dailyCalcPeriod, reflectApprovalResult)),
				taskLogList, this.kfnmtProcExecLogPK.execId);
	}
	
	
	
	public ProcessExecutionLog toDomainMaxDate() {
		List<ExecutionTaskLog> taskLogList = new ArrayList<>();
		if(!this.taskLogList.isEmpty()){
			KfnmtExecutionTaskLog innitExecutionTaskLog = this.taskLogList.get(0);
			int size = this.taskLogList.size();
			for (int i = 1; i < size; i++) {
				if(innitExecutionTaskLog.getUpdDate()!=null && this.taskLogList.get(i).getUpdDate()!=null && innitExecutionTaskLog.getUpdDate().before(this.taskLogList.get(i).getUpdDate())){
					innitExecutionTaskLog = this.taskLogList.get(i);
				}
			}
			for (int i = 0; i < size; i++) {
				if(innitExecutionTaskLog.kfnmtExecTaskLogPK.execId==this.taskLogList.get(i).kfnmtExecTaskLogPK.execId){
					taskLogList.add(this.taskLogList.get(i).toNewDomain());
				}
			}
			//asc
			Collections.sort(taskLogList, new Comparator<ExecutionTaskLog>() {
			    @Override
			    public int compare(ExecutionTaskLog e1, ExecutionTaskLog e2) {
			        return e1.getProcExecTask().value < e2.getProcExecTask().value ?-1 : 1;
			    }
			});
		}
		
		
		DatePeriod scheduleCreationPeriod = (this.schCreateStart == null || this.schCreateEnd == null) ? null
				: new DatePeriod(this.schCreateStart, this.schCreateEnd);
		DatePeriod dailyCreationPeriod = (this.dailyCreateStart == null || this.dailyCreateEnd == null) ? null
				: new DatePeriod(this.dailyCreateStart, this.dailyCreateEnd);
		DatePeriod dailyCalcPeriod = (this.dailyCalcStart == null || this.dailyCalcEnd == null) ? null
				: new DatePeriod(this.dailyCalcStart, this.dailyCalcEnd);
		DatePeriod reflectApprovalResult = (this.reflectApprovalResultStart == null
				|| this.reflectApprovalResultEnd == null) ? null
						: new DatePeriod(this.reflectApprovalResultStart, this.reflectApprovalResultEnd);
		return new ProcessExecutionLog(new ExecutionCode(this.kfnmtProcExecLogPK.execItemCd),
				this.kfnmtProcExecLogPK.companyId, Optional.ofNullable(new EachProcessPeriod(scheduleCreationPeriod,
						dailyCreationPeriod, dailyCalcPeriod, reflectApprovalResult)),
				taskLogList, this.kfnmtProcExecLogPK.execId);
	}
	public ProcessExecutionLog toDomainMaxDate(List<KfnmtExecutionTaskLog> stTaskList) {
		List<ExecutionTaskLog> taskLogList = new ArrayList<>();
		if(!stTaskList.isEmpty()){
			KfnmtExecutionTaskLog innitExecutionTaskLog = stTaskList.get(0);
			int size = stTaskList.size();
			for (int i = 1; i < size; i++) {
				if(innitExecutionTaskLog.getUpdDate()!=null && stTaskList.get(i).getUpdDate()!=null && innitExecutionTaskLog.getUpdDate().before(stTaskList.get(i).getUpdDate())){
					innitExecutionTaskLog = stTaskList.get(i);
				}
			}
			for (int i = 0; i < size; i++) {
				if(innitExecutionTaskLog.kfnmtExecTaskLogPK.execId.equals(stTaskList.get(i).kfnmtExecTaskLogPK.execId)){
					taskLogList.add(stTaskList.get(i).toNewDomain());
				}
			}
			//asc
			Collections.sort(taskLogList, new Comparator<ExecutionTaskLog>() {
			    @Override
			    public int compare(ExecutionTaskLog e1, ExecutionTaskLog e2) {
			        return e1.getProcExecTask().value < e2.getProcExecTask().value ?-1 : 1;
			    }
			});
		}
		
		DatePeriod scheduleCreationPeriod = (this.schCreateStart == null || this.schCreateEnd == null) ? null
				: new DatePeriod(this.schCreateStart, this.schCreateEnd);
		DatePeriod dailyCreationPeriod = (this.dailyCreateStart == null || this.dailyCreateEnd == null) ? null
				: new DatePeriod(this.dailyCreateStart, this.dailyCreateEnd);
		DatePeriod dailyCalcPeriod = (this.dailyCalcStart == null || this.dailyCalcEnd == null) ? null
				: new DatePeriod(this.dailyCalcStart, this.dailyCalcEnd);
		DatePeriod reflectApprovalResult = (this.reflectApprovalResultStart == null
				|| this.reflectApprovalResultEnd == null) ? null
						: new DatePeriod(this.reflectApprovalResultStart, this.reflectApprovalResultEnd);
		return new ProcessExecutionLog(new ExecutionCode(this.kfnmtProcExecLogPK.execItemCd),
				this.kfnmtProcExecLogPK.companyId, Optional.ofNullable(new EachProcessPeriod(scheduleCreationPeriod,
						dailyCreationPeriod, dailyCalcPeriod, reflectApprovalResult)),
				taskLogList, this.kfnmtProcExecLogPK.execId);
	}
	
	
	
	

	public static KfnmtProcessExecutionLog toEntity(ProcessExecutionLog domain) {
		List<KfnmtExecutionTaskLog> taskLogList = domain.getTaskLogList().stream().map(x -> KfnmtExecutionTaskLog
				.toEntity(domain.getCompanyId(), domain.getExecItemCd().v(), domain.getExecId(), x))
				.collect(Collectors.toList());

		GeneralDate schCreateStart = null;
		GeneralDate schCreateEnd = null;
		if (domain.getEachProcPeriod() != null && domain.getEachProcPeriod().isPresent()
				&& domain.getEachProcPeriod().get().getScheduleCreationPeriod() != null
				&& domain.getEachProcPeriod().get().getScheduleCreationPeriod().isPresent()) {
			schCreateStart = domain.getEachProcPeriod().get().getScheduleCreationPeriod().get().start();
			schCreateEnd = domain.getEachProcPeriod().get().getScheduleCreationPeriod().get().end();
		}
		GeneralDate dailyCreateStart = null;
		GeneralDate dailyCreateEnd = null;
		if (domain.getEachProcPeriod() != null && domain.getEachProcPeriod().isPresent()
				&& domain.getEachProcPeriod().get().getDailyCreationPeriod() != null
				&& domain.getEachProcPeriod().get().getDailyCreationPeriod().isPresent()) {
			dailyCreateStart = domain.getEachProcPeriod().get().getDailyCreationPeriod().get().start();
			dailyCreateEnd = domain.getEachProcPeriod().get().getDailyCreationPeriod().get().end();
		}
		GeneralDate dailyCalcStart = null;
		GeneralDate dailyCalcEnd = null;
		if (domain.getEachProcPeriod() != null && domain.getEachProcPeriod().isPresent()
				&& domain.getEachProcPeriod().get().getDailyCalcPeriod() != null
				&& domain.getEachProcPeriod().get().getDailyCalcPeriod().isPresent()) {
			dailyCalcStart = domain.getEachProcPeriod().get().getDailyCalcPeriod().get().start();
			dailyCalcEnd = domain.getEachProcPeriod().get().getDailyCalcPeriod().get().end();
		}
		GeneralDate reflectApprovalResultStart = null;
		GeneralDate reflectApprovalResultEnd = null;
		if (domain.getEachProcPeriod() != null && domain.getEachProcPeriod().isPresent()
				&& domain.getEachProcPeriod().get().getReflectApprovalResult() != null
				&& domain.getEachProcPeriod().get().getReflectApprovalResult().isPresent()) {
			reflectApprovalResultStart = domain.getEachProcPeriod().get().getReflectApprovalResult().get().start();
			reflectApprovalResultEnd = domain.getEachProcPeriod().get().getReflectApprovalResult().get().end();
		}
		return new KfnmtProcessExecutionLog(
				new KfnmtProcessExecutionLogPK(domain.getCompanyId(), domain.getExecItemCd().v(), domain.getExecId()),
				schCreateStart, schCreateEnd, dailyCreateStart, dailyCreateEnd, dailyCalcStart, dailyCalcEnd,
				reflectApprovalResultStart, reflectApprovalResultEnd, taskLogList);
	}

	public KfnmtProcessExecutionLog(GeneralDate schCreateStart, GeneralDate schCreateEnd, GeneralDate dailyCreateStart,
			GeneralDate dailyCreateEnd, GeneralDate dailyCalcStart, GeneralDate dailyCalcEnd,
			List<KfnmtExecutionTaskLog> taskLogList) {
		super();
		this.schCreateStart = schCreateStart;
		this.schCreateEnd = schCreateEnd;
		this.dailyCreateStart = dailyCreateStart;
		this.dailyCreateEnd = dailyCreateEnd;
		this.dailyCalcStart = dailyCalcStart;
		this.dailyCalcEnd = dailyCalcEnd;
		this.taskLogList = taskLogList;
	}
}
