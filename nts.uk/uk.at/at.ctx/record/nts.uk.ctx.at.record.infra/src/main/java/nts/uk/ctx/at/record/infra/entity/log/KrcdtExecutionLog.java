package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workrecord.log.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.log.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.log.PartResetClassification;
import nts.uk.ctx.at.record.dom.workrecord.log.SetInforReflAprResult;
import nts.uk.ctx.at.record.dom.workrecord.log.SettingInforForDailyCreation;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionType;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 * 実行ログ
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_EXECUTION_LOG")
public class KrcdtExecutionLog extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtExecutionLogPK krcdtExecutionLogPK;
	/**
	 * エラーの有無
	 */
	@Column(name = "EXISTENCE_ERROR")
	public int existenceError;

	@Column(name = "EXECUTION_START_DATE")
	public GeneralDateTime executionStartDate;
	
	@Column(name = "EXECUTION_END_DATE")
	public GeneralDateTime executionEndDate;

	@Column(name = "PROCESSING_SITUATION")
	public int processStatus;

	@Column(name = "PERIOD_COVERED_START_DATE")
	public GeneralDate periodCoverdStartDate;

	@Column(name = "PERIOD_COVERED_END_DATE")
	public GeneralDate periodCoverdEndDate;
	
	@Column(name = "CAL_EXECUTION_SET_INFO_ID")
	public String calExecutionSetInfoID;
	
	@ManyToOne
	@JoinColumn(name="EMP_EXECUTION_LOG_ID", referencedColumnName="EMP_EXECUTION_LOG_ID", insertable = false, updatable = false)
	public KrcdtEmpExecutionLog empexecutionlog;
	
	@OneToOne(mappedBy="executionlog", cascade = CascadeType.ALL)
	@JoinTable(name = "KRCDT_CAL_EXE_SET_INFO")
	public KrcdtCalExeSetInfor calExeSetInfor;
	
	public KrcdtExecutionLog(KrcdtExecutionLogPK krcdtExecutionLogPK, int existenceError,
			GeneralDateTime executionStartDate, GeneralDateTime executionEndDate, int processStatus,
			GeneralDate periodCoverdStartDate, GeneralDate periodCoverdEndDate, String calExecutionSetInfoID
			) {
		super();
		this.krcdtExecutionLogPK = krcdtExecutionLogPK;
		this.existenceError = existenceError;
		this.executionStartDate = executionStartDate;
		this.executionEndDate = executionEndDate;
		this.processStatus = processStatus;
		this.periodCoverdStartDate = periodCoverdStartDate;
		this.periodCoverdEndDate = periodCoverdEndDate;
		this.calExecutionSetInfoID = calExecutionSetInfoID;
	}

	public ExecutionLog toDomain() {
		val domain = ExecutionLog.createFromJavaType(
				this.krcdtExecutionLogPK.empCalAndSumExecLogID,
				this.krcdtExecutionLogPK.executionContent, 
				this.existenceError, 
				this.executionStartDate,
				this.executionEndDate, 
				this.processStatus, 
				this.periodCoverdStartDate, 
				this.periodCoverdEndDate,
				this.calExecutionSetInfoID);
		
		if (this.krcdtExecutionLogPK.executionContent == ExecutionContent.DAILY_CREATION.value) {
			domain.setDailyCreationSetInfo(Optional.of(calExeSetInfor.toDomain()));
		} else if(this.krcdtExecutionLogPK.executionContent == ExecutionContent.DAILY_CALCULATION.value ) {
			domain.setDailyCalSetInfo(Optional.of(calExeSetInfor.toDomain()));
		} else if(this.krcdtExecutionLogPK.executionContent == ExecutionContent.REFLRCT_APPROVAL_RESULT.value) {
			domain.setReflectApprovalSetInfo(Optional.of(calExeSetInfor.toDomain()));
		} else {
			domain.setMonlyAggregationSetInfo(Optional.of(calExeSetInfor.toDomain()));
		}
		return domain;
	}
	
	@Override
	protected Object getKey() {
		return this.krcdtExecutionLogPK;
	}
	
	public static KrcdtExecutionLog toEntity(ExecutionLog domain) {
		val entity = new KrcdtExecutionLog(
				 new KrcdtExecutionLogPK(
					 domain.getEmpCalAndSumExecLogID(),
					 domain.getExecutionContent().value
					),
				 domain.getExistenceError().value,
				 domain.getExecutionTime().getStartTime(),
				 domain.getExecutionTime().getEndTime(),
				 domain.getProcessStatus().value,
				 domain.getObjectPeriod().getStartDate(),
				 domain.getObjectPeriod().getEndDate(),
				 domain.getCalExecutionSetInfoID());
		if (domain.getExecutionContent() == ExecutionContent.DAILY_CREATION) {
			entity.calExeSetInfor = KrcdtCalExeSetInfor.toEntity(domain.getDailyCreationSetInfo().get());
		}else if(domain.getExecutionContent() == ExecutionContent.DAILY_CALCULATION) {
			entity.calExeSetInfor = KrcdtCalExeSetInfor.toEntity(domain.getDailyCalSetInfo().get());
		}else if(domain.getExecutionContent() == ExecutionContent.REFLRCT_APPROVAL_RESULT) {
			entity.calExeSetInfor = KrcdtCalExeSetInfor.toEntity(domain.getReflectApprovalSetInfo().get());
		}else {
			entity.calExeSetInfor = KrcdtCalExeSetInfor.toEntity(domain.getMonlyAggregationSetInfo().get());
		}
		return entity;
	}
	
}
