package nts.uk.ctx.at.record.infra.entity.remainingnumber;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Table(name = "KRCMT_ANNLEA_REMAIN")
public class AnnLeaRemainEntity extends UkJpaEntity{

	@Id
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
		return employeeId;
	}

}
