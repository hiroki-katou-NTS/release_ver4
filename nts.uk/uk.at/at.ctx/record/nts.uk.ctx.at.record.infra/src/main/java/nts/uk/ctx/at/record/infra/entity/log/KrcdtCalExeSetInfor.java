package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;
import java.util.Optional;

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
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.PartResetClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SetInforReflAprResult;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SettingInforForDailyCreation;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author hieult
 *
 */

@Entity
@NoArgsConstructor
@Table(name = "KRCST_CAL_EXE_SET_INFO")
public class KrcdtCalExeSetInfor extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KrcdtCalExeSetInforPK krcdtCalExeSetInforPK;
	
	/**実行内容*/
	@Column(name = "EXECUTION_CONTENT")
	public int executionContent;

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
	
	@OneToOne
	@JoinColumn(name="CAL_EXECUTION_SET_INFO_ID", referencedColumnName="MONTHLY_AGG_SET_INFOR_ID", insertable = false, updatable = false, nullable=true)
	public KrcstCaseSpecExeContent caseSpecExeContentMonthly;
	
	@OneToOne
	@JoinColumn(name="CAL_EXECUTION_SET_INFO_ID", referencedColumnName="REF_APPROVAL_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	public KrcstCaseSpecExeContent caseSpecExeContentRef;
	
	@OneToOne
	@JoinColumn(name="CAL_EXECUTION_SET_INFO_ID", referencedColumnName="DAILY_CAL_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	public KrcstCaseSpecExeContent caseSpecExeContentCal;
	
	@OneToOne
	@JoinColumn(name="CAL_EXECUTION_SET_INFO_ID", referencedColumnName="DAILY_CREAT_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	public KrcstCaseSpecExeContent caseSpecExeContentCreat;

	
	public KrcdtCalExeSetInfor(KrcdtCalExeSetInforPK krcdtCalExeSetInforPK,int executionContent, int executionType,
			Boolean alsoForciblyReflectEvenIfItIsConfirmed, Integer creationType, Boolean masterReconfiguration,
			Boolean closedHolidays, Boolean resettingWorkingHours, Boolean reflectsTheNumberOfFingerprintChecks,
			Boolean specificDateClassificationResetting, Boolean resetTimeAssignment, Boolean resetTimeChildOrNurseCare,
			Boolean calculationClassificationResetting) {
		super();
		this.krcdtCalExeSetInforPK = krcdtCalExeSetInforPK;
		this.executionContent = executionContent;
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
		if (this.executionContent == ExecutionContent.DAILY_CREATION.value) {
			Optional<PartResetClassification> partResetClassification = Optional.empty();
			
			if (this.creationType == DailyRecreateClassification.PARTLY_MODIFIED.value) {
				partResetClassification = Optional.of(new PartResetClassification(
						this.masterReconfiguration,
						this.closedHolidays, 
						this.resettingWorkingHours, 
						this.reflectsTheNumberOfFingerprintChecks, 
						this.specificDateClassificationResetting, 
						this.resetTimeAssignment, 
						this.resetTimeChildOrNurseCare, 
						this.calculationClassificationResetting));
			}
			SettingInforForDailyCreation settingInforForDailyCreation = 
					new SettingInforForDailyCreation(
						EnumAdaptor.valueOf(this.executionContent, ExecutionContent.class),
						EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
						this.krcdtCalExeSetInforPK.calExecutionSetInfoID, 
						EnumAdaptor.valueOf(this.creationType,DailyRecreateClassification.class),
						partResetClassification);
			return (T) settingInforForDailyCreation;
		} else if(this.executionContent == ExecutionContent.DAILY_CALCULATION.value ) {
			//calculation
			CalExeSettingInfor calExeSettingInfor = new CalExeSettingInfor(
					EnumAdaptor.valueOf(this.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID
					);
			return (T) calExeSettingInfor;
		}else if(this.executionContent == ExecutionContent.REFLRCT_APPROVAL_RESULT.value) {
			//Reflect
			SetInforReflAprResult setInforReflAprResult = new SetInforReflAprResult(
					EnumAdaptor.valueOf(this.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID,
					this.alsoForciblyReflectEvenIfItIsConfirmed
					);
					
			return (T) setInforReflAprResult;
			
		}else {
			//aggregation
			CalExeSettingInfor calExeSettingInfor = new CalExeSettingInfor(
					EnumAdaptor.valueOf(this.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(this.executionType, ExecutionType.class),
					this.krcdtCalExeSetInforPK.calExecutionSetInfoID
					);
			return (T) calExeSettingInfor;
		}
	}

	//create
	public static KrcdtCalExeSetInfor toEntity(SettingInforForDailyCreation domain) {
		Boolean isMasterReconfiguration = null;
		Boolean isClosedHolidays = null;
		Boolean isResettingWorkingHours = null;
		Boolean isReflectsTheNumberOfFingerprintChecks = null;
		Boolean isSpecificDateClassificationResetting = null;
		Boolean isResetTimeAssignment = null;
		Boolean isResetTimeChildOrNurseCare = null;
		Boolean isCalculationClassificationResetting = null;
		
		if (domain.getPartResetClassification().isPresent()) {
			isMasterReconfiguration = domain.getPartResetClassification().get().getMasterReconfiguration();
			isClosedHolidays = domain.getPartResetClassification().get().getClosedHolidays();
			isResettingWorkingHours = domain.getPartResetClassification().get().getResettingWorkingHours();
			isReflectsTheNumberOfFingerprintChecks =  domain.getPartResetClassification().get().getReflectsTheNumberOfFingerprintChecks();
			isSpecificDateClassificationResetting = domain.getPartResetClassification().get().getSpecificDateClassificationResetting();
			isResetTimeAssignment = domain.getPartResetClassification().get().getResetTimeAssignment() ;
			isResetTimeChildOrNurseCare = domain.getPartResetClassification().get().getResetTimeChildOrNurseCare();
			isCalculationClassificationResetting = domain.getPartResetClassification().get().getCalculationClassificationResetting() ;
		}
		return new KrcdtCalExeSetInfor(
					new KrcdtCalExeSetInforPK(
						domain.getCalExecutionSetInfoID()
						),
					domain.getExecutionContent().value,
					domain.getExecutionType().value,
					null,
					domain.getCreationType().value,
					isMasterReconfiguration,
					isClosedHolidays,
					isResettingWorkingHours,
					isReflectsTheNumberOfFingerprintChecks,
					isSpecificDateClassificationResetting,
					isResetTimeAssignment,
					isResetTimeChildOrNurseCare,
					isCalculationClassificationResetting 
				);
	}
	//calculation,aggregation
	public static KrcdtCalExeSetInfor toEntity(CalExeSettingInfor domain) {
		return new KrcdtCalExeSetInfor(
					new KrcdtCalExeSetInforPK(
						domain.getCalExecutionSetInfoID()
						),
					domain.getExecutionContent().value,
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
						domain.getCalExecutionSetInfoID()
						),
					domain.getExecutionContent().value,
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
