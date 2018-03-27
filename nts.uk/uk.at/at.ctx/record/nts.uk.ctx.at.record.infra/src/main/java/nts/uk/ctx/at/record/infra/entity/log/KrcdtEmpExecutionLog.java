package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;
import java.util.List;
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
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 * 就業計算と集計実行ログ
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_EMP_EXECUTION_LOG")
public class KrcdtEmpExecutionLog extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtEmpExecutionLogPK krcdtEmpExecutionLogPK;
	
	@Column(name = "CID")
	public String companyID;
	
	@Column(name = "PROCESSING_MONTH")
	public Integer processingMonth;
	
	@Column(name = "EXECUTED_MENU")
	public int executedMenu;
	
	@Column(name = "EXECUTED_DATE")
	public GeneralDate executedDate;

	@Column(name = "EXECUTED_STATUS")
	public int executedStatus;

	@Column(name = "SID")
	public String employeeID;

	@Column(name = "CLOSURE_ID")
	public int closureID;
	
	@Column(name = "OPERATION_CASE_ID")
	public String caseSpecExeContentID;

//	@OneToMany(mappedBy="empexecutionlog", cascade = CascadeType.ALL)
//	@JoinTable(name = "KRCDT_EXECUTION_LOG")
//	public List<KrcdtExecutionLog> executionLogs;
	
	@Override
	protected Object getKey() {
		return this.krcdtEmpExecutionLogPK;
	}
	
	public static KrcdtEmpExecutionLog toEntity(EmpCalAndSumExeLog domain) {
		return new  KrcdtEmpExecutionLog(
				new KrcdtEmpExecutionLogPK(domain.getEmpCalAndSumExecLogID()),
				domain.getCompanyID(),
				domain.getProcessingMonth().v(),
				domain.getExecutedMenu().value,
				domain.getExecutionDate(),
				domain.getExecutionStatus().value,
				domain.getEmployeeID(),
				domain.getClosureID(),
				domain.getCaseSpecExeContentID()
				//domain.getExecutionLogs().stream().map(c->KrcdtExecutionLog.toEntity(c)).collect(Collectors.toList())
				);
	}
	
	public EmpCalAndSumExeLog toDomain() {
		//List<ExecutionLog> executionLogs = this.executionLogs.stream().map(c -> c.toDomain()).collect(Collectors.toList());
		return EmpCalAndSumExeLog.createFromJavaType(
				this.krcdtEmpExecutionLogPK.empCalAndSumExecLogID,
				this.companyID,
				new YearMonth( this.processingMonth),
				this.executedMenu,
				this.executedDate, 
				this.executedStatus,
				this.employeeID,
				this.closureID, 
				this.caseSpecExeContentID);
	}
}
