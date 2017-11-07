package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;



import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workrecord.log.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.log.PartResetClassification;
import nts.uk.ctx.at.record.dom.workrecord.log.SetInforReflAprResult;
import nts.uk.ctx.at.record.dom.workrecord.log.SettingInforForDailyCreation;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionType;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author hieult
 *
 */

@Entity
@NoArgsConstructor
@Table(name = "KRCDT_CAL_EXE_SET_INFO")
public class KrcdtCalExeSetInfor extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KrcdtCalExeSetInforPK krcdtCalExeSetInforPK;

	/** 実行種別 */
	@Column(name = "EXECUTION_TYPE")
	public int executionType;
		
	/** 確定済みの場合にも強制的に反映する */
	@Column(name = "REF_EVEN_CONFIRM", nullable = true)
	public Boolean alsoForciblyReflectEvenIfItIsConfirmed;

	/** 作成区分 */
	@Column(name = "CREATION_TYPE")
	public Integer creationType;

	/** マスタ再設定 */
	@Column(name = "MASTER_RECONFIG", nullable = true)
	public Boolean masterReconfiguration;

	/** 休業再設定 */
	@Column(name = "CLOSED_HOLIDAYS", nullable = true)
	public Boolean closedHolidays;

	/** 就業時間帯再設定 */
	@Column(name = "RESET_WORK_HOURS", nullable = true)
	public Boolean resettingWorkingHours;

	/** 打刻のみ再度反映 */
	@Column(name = "REF_NUMBER_FINGER_CHECK", nullable = true)
	public Boolean reflectsTheNumberOfFingerprintChecks;

	/** 特定日区分再設定 */
	@Column(name = "SPEC_DATE_CLASS_RESET", nullable = true)
	public Boolean specificDateClassificationResetting;

	/** 申し送り時間再設定 */
	@Column(name = "RESET_TIME_ASSIGNMENT", nullable = true)
	public Boolean resetTimeAssignment;

	/** 育児・介護短時間再設定 */
	@Column(name = "RESET_TIME_CHILD_OR_NURCE", nullable = true)
	public Boolean resetTimeChildOrNurseCare;

	/** 計算区分再設定 */
	@Column(name = "CALCULA_CLASS_RESET", nullable = true)
	public Boolean calculationClassificationResetting;

	@Override
	protected Object getKey() {
		return this.krcdtCalExeSetInforPK;
	}
	
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="CAL_EXECUTION_SET_INFO_ID", referencedColumnName="CAL_EXECUTION_SET_INFO_ID", insertable = false, updatable = false),
		@JoinColumn(name="EXECUTION_CONTENT", referencedColumnName="EXECUTION_CONTENT", insertable = false, updatable = false)
	})
	public KrcdtExecutionLog executionlog;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="OPERATION_CASE_ID", referencedColumnName="OPERATION_CASE_ID", insertable = false, updatable = false),
	})
	public KrcstCaseSpecExeContent caseSpecExeContent;

	
	public KrcdtCalExeSetInfor(KrcdtCalExeSetInforPK krcdtCalExeSetInforPK, int executionType,
			Boolean alsoForciblyReflectEvenIfItIsConfirmed, Integer creationType, Boolean masterReconfiguration,
			Boolean closedHolidays, Boolean resettingWorkingHours, Boolean reflectsTheNumberOfFingerprintChecks,
			Boolean specificDateClassificationResetting, Boolean resetTimeAssignment, Boolean resetTimeChildOrNurseCare,
			Boolean calculationClassificationResetting) {
		super();
		this.krcdtCalExeSetInforPK = krcdtCalExeSetInforPK;
		this.executionType = executionType;
		this.alsoForciblyReflectEvenIfItIsConfirmed = alsoForciblyReflectEvenIfItIsConfirmed;
		this.creationType = creationType;
		this.masterReconfiguration = masterReconfiguration;
		this.closedHolidays = closedHolidays;
		this.resettingWorkingHours = resettingWorkingHours;
		this.reflectsTheNumberOfFingerprintChecks = reflectsTheNumberOfFingerprintChecks;
		this.specificDateClassificationResetting = specificDateClassificationResetting;
		this.resetTimeAssignment = resetTimeAssignment;
		this.resetTimeChildOrNurseCare = resetTimeChildOrNurseCare;
		this.calculationClassificationResetting = calculationClassificationResetting;
	}

	public <T> T toDomain() {
		if (this.krcdtCalExeSetInforPK.executionContent == ExecutionContent.DAILY_CREATION.value) {
			PartResetClassification partResetClassification = new PartResetClassification(
					this.masterReconfiguration,
					this.closedHolidays, 
					this.resettingWorkingHours, 
					this.reflectsTheNumberOfFingerprintChecks, 
					this.specificDateClassificationResetting, 
					this.resetTimeAssignment, 
					this.resetTimeChildOrNurseCare, 
					this.calculationClassificationResetting);
			SettingInforForDailyCreation settingInforForDailyCreation = 
					new SettingInforForDailyCreation(
						EnumAdaptor.valueOf(this.krcdtCalExeSetInforPK.executionContent, ExecutionContent.class),
						EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
						this.krcdtCalExeSetInforPK.calExecutionSetInfoID, 
						EnumAdaptor.valueOf(this.creationType,DailyRecreateClassification.class),
						partResetClassification);
			return (T) settingInforForDailyCreation;
		} else if(this.krcdtCalExeSetInforPK.executionContent == ExecutionContent.DAILY_CALCULATION.value ) {
			//calculation
			CalExeSettingInfor calExeSettingInfor = new CalExeSettingInfor(
					EnumAdaptor.valueOf(this.krcdtCalExeSetInforPK.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID
					);
			return (T) calExeSettingInfor;
		}else if(this.krcdtCalExeSetInforPK.executionContent == ExecutionContent.REFLRCT_APPROVAL_RESULT.value) {
			//Reflect
			SetInforReflAprResult setInforReflAprResult = new SetInforReflAprResult(
					EnumAdaptor.valueOf(this.krcdtCalExeSetInforPK.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID,
					this.alsoForciblyReflectEvenIfItIsConfirmed
					);
					
			return (T) setInforReflAprResult;
			
		}else {
			//aggregation
			CalExeSettingInfor calExeSettingInfor = new CalExeSettingInfor(
					EnumAdaptor.valueOf(this.krcdtCalExeSetInforPK.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID
					);
			return (T) calExeSettingInfor;
		}
	}

	//create
	public static KrcdtCalExeSetInfor toEntity(SettingInforForDailyCreation domain) {
		return new KrcdtCalExeSetInfor(
					new KrcdtCalExeSetInforPK(
						domain.getCalExecutionSetInfoID(),
						domain.getExecutionContent().value
						),
					domain.getExecutionType().value,
					null,
					domain.getCreationType().value,
					domain.getPartResetClassification().get().isMasterReconfiguration(),
					domain.getPartResetClassification().get().isClosedHolidays(),
					domain.getPartResetClassification().get().isResettingWorkingHours(),
					domain.getPartResetClassification().get().isReflectsTheNumberOfFingerprintChecks(),
					domain.getPartResetClassification().get().isSpecificDateClassificationResetting(),
					domain.getPartResetClassification().get().isResetTimeAssignment(),
					domain.getPartResetClassification().get().isResetTimeChildOrNurseCare(),
					domain.getPartResetClassification().get().isCalculationClassificationResetting()
				);
	}
	//calculation,aggregation
	public static KrcdtCalExeSetInfor toEntity(CalExeSettingInfor domain) {
		return new KrcdtCalExeSetInfor(
					new KrcdtCalExeSetInforPK(
						domain.getCalExecutionSetInfoID(),
						domain.getExecutionContent().value
						),
					domain.getExecutionType().value,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null
				);
	}
	//Reflect 
	public static KrcdtCalExeSetInfor toEntity(SetInforReflAprResult domain) {
		return new KrcdtCalExeSetInfor(
					new KrcdtCalExeSetInforPK(
						domain.getCalExecutionSetInfoID(),
						domain.getExecutionContent().value
						),
					domain.getExecutionType().value,
					domain.isForciblyReflect(),
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null
				);
	}
	
	
}
