package nts.uk.ctx.at.function.infra.entity.processexecution;

import java.io.Serializable;
import java.util.List;
//import java.util.Optional;
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
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EachProcessPeriod;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name="KFNMT_PROC_EXEC_LOG_HIST")
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtProcessExecutionLogHistory extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
    public KfnmtProcessExecutionLogHistoryPK kfnmtProcExecLogHstPK;
	
	/* 全体の終了状態 */
	@Column(name = "OVERALL_STATUS")
	public Integer overallStatus;
	
	/* 全体のエラー詳細 */
	@Column(name = "ERROR_DETAIL")
	public Integer errorDetail;
	
	/* 前回実行日時 */
	@Column(name = "LAST_EXEC_DATETIME")
	public GeneralDateTime prevExecDateTime;
	
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
	
	@Column(name = "LAST_END_EXEC_DATETIME")
	public GeneralDateTime lastEndExecDateTime;

	@Column(name = "ERROR_SYSTEM")
	public Integer errorSystem;
	
	@Column(name = "ERROR_BUSINESS")
	public Integer errorBusiness;
	
	@OneToMany(mappedBy = "procExecLogHistItem", cascade = CascadeType.ALL)
    @JoinTable(name = "KFNMT_EXEC_TASK_LOG")
    public List<KfnmtExecutionTaskLog> taskLogList;
	
	@Override
	protected Object getKey() {
		return this.kfnmtProcExecLogHstPK;
	}
	
	public ProcessExecutionLogHistory toDomain() {
		List<ExecutionTaskLog> taskLogList =
				this.taskLogList.stream().map(x -> x.toNewDomain()).collect(Collectors.toList());
		return new ProcessExecutionLogHistory(new ExecutionCode(this.kfnmtProcExecLogHstPK.execItemCd),
				this.kfnmtProcExecLogHstPK.companyId,
				this.errorDetail != null? EnumAdaptor.valueOf(this.errorDetail, OverallErrorDetail.class): null,
				this.overallStatus!=null? EnumAdaptor.valueOf(this.overallStatus, EndStatus.class):null,
				this.prevExecDateTime,
				new EachProcessPeriod(new DatePeriod(this.schCreateStart, this.schCreateEnd),
						new DatePeriod(this.dailyCreateStart, this.dailyCreateEnd),
						new DatePeriod(this.dailyCalcStart, this.dailyCalcEnd),new DatePeriod(this.reflectApprovalResultStart, this.reflectApprovalResultEnd) ),
				taskLogList,
				this.kfnmtProcExecLogHstPK.execId,
				this.lastEndExecDateTime,
				this.errorSystem == null?null:(this.errorSystem==1?true:false),
				this.errorBusiness == null?null:(this.errorBusiness==1?true:false)
				);
	}
	
	public static KfnmtProcessExecutionLogHistory toEntity(ProcessExecutionLogHistory domain) {
		GeneralDate schCreateStart = null;
		GeneralDate schCreateEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod().isPresent()) {
			schCreateStart = domain.getEachProcPeriod().getScheduleCreationPeriod().get().start();
			schCreateEnd = domain.getEachProcPeriod().getScheduleCreationPeriod().get().end();
		}
		GeneralDate dailyCreateStart = null;
		GeneralDate dailyCreateEnd = null;
		if (domain.getEachProcPeriod() != null 
				&& domain.getEachProcPeriod().getDailyCreationPeriod() != null
				&& domain.getEachProcPeriod().getDailyCreationPeriod().isPresent()) {
			dailyCreateStart = domain.getEachProcPeriod().getDailyCreationPeriod().get().start();
			dailyCreateEnd = domain.getEachProcPeriod().getDailyCreationPeriod().get().end();
		}
		GeneralDate dailyCalcStart = null;
		GeneralDate dailyCalcEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod().isPresent()) {
			dailyCalcStart = domain.getEachProcPeriod().getDailyCalcPeriod().get().start();
			dailyCalcEnd = domain.getEachProcPeriod().getDailyCalcPeriod().get().end();
		}
		GeneralDate reflectApprovalResultStart = null;
		GeneralDate reflectApprovalResultEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult().isPresent()) {
			reflectApprovalResultStart = domain.getEachProcPeriod().getReflectApprovalResult().get().start();
			reflectApprovalResultEnd = domain.getEachProcPeriod().getReflectApprovalResult().get().end();
		}
		
		
		return new KfnmtProcessExecutionLogHistory(
				new KfnmtProcessExecutionLogHistoryPK(domain.getCompanyId(), domain.getExecItemCd().v(), domain.getExecId()),
				(domain.getOverallStatus() != null && domain.getOverallStatus().isPresent() ) ? domain.getOverallStatus().get().value : null,
				(domain.getOverallError() != null && domain.getOverallError().isPresent() ) ? domain.getOverallError().get().value : null,
				domain.getLastExecDateTime(),
				schCreateStart,
				schCreateEnd,
				dailyCreateStart,
				dailyCreateEnd,
				dailyCalcStart,
				dailyCalcEnd,
				reflectApprovalResultStart,
				reflectApprovalResultEnd,
				domain.getLastEndExecDateTime(),
				domain.getErrorSystem()== null?null:(domain.getErrorSystem().booleanValue()?1:0),
				domain.getErrorBusiness()==null?null:(domain.getErrorBusiness().booleanValue() ?1:0),
				null);
	}
	
	public static KfnmtProcessExecutionLogHistory toEntity2(ProcessExecutionLogHistory domain) {
		GeneralDate schCreateStart = null;
		GeneralDate schCreateEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod().isPresent()) {
			schCreateStart = domain.getEachProcPeriod().getScheduleCreationPeriod().get().start();
			schCreateEnd = domain.getEachProcPeriod().getScheduleCreationPeriod().get().end();
		}
		GeneralDate dailyCreateStart = null;
		GeneralDate dailyCreateEnd = null;
		if (domain.getEachProcPeriod() != null 
				&& domain.getEachProcPeriod().getDailyCreationPeriod() != null
				&& domain.getEachProcPeriod().getDailyCreationPeriod().isPresent()) {
			dailyCreateStart = domain.getEachProcPeriod().getDailyCreationPeriod().get().start();
			dailyCreateEnd = domain.getEachProcPeriod().getDailyCreationPeriod().get().end();
		}
		GeneralDate dailyCalcStart = null;
		GeneralDate dailyCalcEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod().isPresent()) {
			dailyCalcStart = domain.getEachProcPeriod().getDailyCalcPeriod().get().start();
			dailyCalcEnd = domain.getEachProcPeriod().getDailyCalcPeriod().get().end();
		}
		GeneralDate reflectApprovalResultStart = null;
		GeneralDate reflectApprovalResultEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult().isPresent()) {
			reflectApprovalResultStart = domain.getEachProcPeriod().getReflectApprovalResult().get().start();
			reflectApprovalResultEnd = domain.getEachProcPeriod().getReflectApprovalResult().get().end();
		}
		
		
		return new KfnmtProcessExecutionLogHistory(
				new KfnmtProcessExecutionLogHistoryPK(domain.getCompanyId(), domain.getExecItemCd().v(), domain.getExecId()),
				(domain.getOverallStatus() != null && domain.getOverallStatus().isPresent() ) ? domain.getOverallStatus().get().value : null,
				(domain.getOverallError() != null && domain.getOverallError().isPresent() ) ? domain.getOverallError().get().value : null,
				domain.getLastExecDateTime(),
				schCreateStart,
				schCreateEnd,
				dailyCreateStart,
				dailyCreateEnd,
				dailyCalcStart,
				dailyCalcEnd,
				reflectApprovalResultStart,
				reflectApprovalResultEnd,
				domain.getLastEndExecDateTime(),
				domain.getErrorSystem()==null?null:( domain.getErrorSystem().booleanValue()?1:0),
				domain.getErrorBusiness()==null?null:(domain.getErrorBusiness().booleanValue()?1:0),
				KfnmtExecutionTaskLog.toEntity(domain.getCompanyId(), domain.getExecItemCd().v(), domain.getExecId(), domain.getTaskLogList()));
	}
}

