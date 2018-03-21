package nts.uk.ctx.at.record.infra.entity.remainingnumber.annlea;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_ANNLEA_REMAIN")
public class KRcmtAnnLeaRemain extends UkJpaEntity{

	@EmbeddedId
    public KRcmtAnnLeaRemainPK key;

	@Column(name = "CID")
    public String CID;
	
	@Column(name = "EMPLOYEE_ID")
    public String employeeId;
	
	@Column(name = "GRANT_DATE")
    public GeneralDate grantDate;
	
	@Column(name = "DEADLINE")
    public GeneralDate deadline;

	@Column(name = "EXP_STATUS")
    public int expStatus;
	
	@Column(name = "REGISTER_TYPE")
    public int registerType;
	
	@Column(name = "GRANT_DAYS")
    public double grantDays;
	
	@Column(name = "GRANT_MINUTES")
    public Integer grantMinutes;
	
	@Column(name = "USED_DAYS")
    public double usedDays;
	
	@Column(name = "USED_MINUTES")
    public Integer usedMinutes;
	
	@Column(name = "STOWAGE_DAYS")
    public Double stowageDays;

	@Column(name = "REMAINING_DAYS")
    public double remainingDays;
	
	@Column(name = "REMAINING_MINUTES")
    public Integer remaningMinutes;
	
	@Column(name = "USED_PERCENT")
    public double usedPercent;

	@Column(name = "PRESCRIBED_DAYS")
    public Double perscribedDays;
	
	@Column(name = "DEDUCTED_DAYS")
    public Double deductedDays;
	
	@Column(name = "WORKING_DAYS")
    public Double workingDays;
	
	@Override
	protected Object getKey() {
		return key;
	}

}
