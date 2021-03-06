package nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainingHistory;
import nts.uk.shr.infra.data.entity.ContractCompanyUkJpaEntity;
/**
 * 
 * @author phongtq
 * 年休付与時点残数履歴データ
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_HDPAID_REM_HIST_GRA")
public class KrcdtAnnLeaTimeRemainHist extends ContractCompanyUkJpaEntity implements Serializable{
	
	@EmbeddedId
	public KrcdtAnnLeaTimeRemainHistPK  krcdtAnnLeaTimeRemainHistPK;  

	@Column(name = "DEADLINE")
	public GeneralDate deadline;

	@Column(name = "EXP_STATUS")
	public int expStatus;

	@Column(name = "REGISTER_TYPE")
	public int registerType;

	@Column(name = "GRANT_DAYS")
	public double grantDays;

	@Column(name = "GRANT_MINUTES")
	@Basic(optional = true)
	public Integer grantMinutes;

	@Column(name = "USED_DAYS")
	public double usedDays;

	@Column(name = "USED_MINUTES")
	@Basic(optional = true)
	public Integer usedMinutes;

	@Column(name = "STOWAGE_DAYS")
	@Basic(optional = true)
	public Double stowageDays;

	@Column(name = "REMAINING_DAYS")
	public double remainingDays;

	@Column(name = "REMAINING_MINUTES")
	@Basic(optional = true)
	public Integer remaningMinutes;

	@Column(name = "USED_PERCENT")
	public double usedPercent;

	@Column(name = "PRESCRIBED_DAYS")
	@Basic(optional = true)
	public Double prescribedDays;

	@Column(name = "DEDUCTED_DAYS")
	@Basic(optional = true)
	public Double deductedDays;

	@Column(name = "WORKING_DAYS")
	@Basic(optional = true)
	public Double workingDays;

	private static final long serialVersionUID = 1L;

	@Override
	protected Object getKey() {
		return krcdtAnnLeaTimeRemainHistPK;
	}

	public KrcdtAnnLeaTimeRemainHist(String sid,
			GeneralDate grantProcessDate, GeneralDate grantDate,
			GeneralDate deadline, int expStatus, int registerType, double grantDays, Integer grantMinutes,
			double usedDays, Integer usedMinutes, Double stowageDays, double remainingDays, Integer remaningMinutes,
			double usedPercent, Double prescribedDays, Double deductedDays, Double workingDays) {
		super();
		this.krcdtAnnLeaTimeRemainHistPK = new KrcdtAnnLeaTimeRemainHistPK(sid, grantProcessDate, grantDate);
		this.deadline = deadline;
		this.expStatus = expStatus;
		this.registerType = registerType;
		this.grantDays = grantDays;
		this.grantMinutes = grantMinutes;
		this.usedDays = usedDays;
		this.usedMinutes = usedMinutes;
		this.stowageDays = stowageDays;
		this.remainingDays = remainingDays;
		this.remaningMinutes = remaningMinutes;
		this.usedPercent = usedPercent;
		this.prescribedDays = prescribedDays;
		this.deductedDays = deductedDays;
		this.workingDays = workingDays;
		
	}

	public static KrcdtAnnLeaTimeRemainHist fromDomain(AnnualLeaveTimeRemainingHistory domain) {
		return new KrcdtAnnLeaTimeRemainHist(domain.getEmployeeId(),
				domain.getGrantProcessDate(),
				domain.getGrantDate(), domain.getDeadline(), domain.getExpirationStatus().value,
				domain.getRegisterType().value, domain.getDetails().getGrantNumber().getDays().v(),
				domain.getDetails().getGrantNumber().getMinutes().isPresent()
						? domain.getDetails().getGrantNumber().getMinutes().get().v() : null,
				domain.getDetails().getUsedNumber().getDays().v(),
				domain.getDetails().getUsedNumber().getMinutes().isPresent()
						? domain.getDetails().getUsedNumber().getMinutes().get().v() : null,
				domain.getDetails().getUsedNumber().getStowageDays().isPresent()
						? domain.getDetails().getUsedNumber().getStowageDays().get().v() : null,
				domain.getDetails().getRemainingNumber().getDays().v(),
				domain.getDetails().getRemainingNumber().getMinutes().isPresent()
						? domain.getDetails().getRemainingNumber().getMinutes().get().v() : null,
				domain.getDetails().getUsedPercent().v().doubleValue(),
				domain.getAnnualLeaveConditionInfo().isPresent()
						? domain.getAnnualLeaveConditionInfo().get().getPrescribedDays().v() : null,
				domain.getAnnualLeaveConditionInfo().isPresent()
						? domain.getAnnualLeaveConditionInfo().get().getDeductedDays().v() : null,
				domain.getAnnualLeaveConditionInfo().isPresent()
						? domain.getAnnualLeaveConditionInfo().get().getWorkingDays().v() : null);
	}

	public AnnualLeaveTimeRemainingHistory toDomain() {
		
		val data = AnnualLeaveGrantRemainingData.createFromJavaType("", krcdtAnnLeaTimeRemainHistPK.sid, krcdtAnnLeaTimeRemainHistPK.grantDate, 
				deadline, this.expStatus,  registerType, grantDays, grantMinutes, usedDays, usedMinutes, stowageDays, this.remainingDays, this.remaningMinutes, 
				usedPercent, prescribedDays, deductedDays, workingDays);
		
		return new AnnualLeaveTimeRemainingHistory(data, krcdtAnnLeaTimeRemainHistPK.grantProcessDate);
	}

}
