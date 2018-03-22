package nts.uk.ctx.at.record.infra.entity.remainingnumber;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Table(name = "KRCMT_SPECIAL_LEAVE_REMA")
public class KrcmtSpecialLeaveReam extends UkJpaEntity{
	
	@Embedded
	public KrcmtSpecialLeaveReamPK key;
	
	@Column(name = "SID")
	public String employeeId;

	@Column(name = "SPECIAL_LEAVE_CD")
	public int specialLeaCode;
	
	@Column(name = "GRANT_DATE")
    public GeneralDate grantDate;
	
	@Column(name = "DEADLINE_DATE")
    public GeneralDate deadlineDate;

	@Column(name = "EXPIRED_STATE")
    public int expStatus;
	
	@Column(name = "REGISTRATION_TYPE")
    public int registerType;
	
	@Column(name = "NUMBER_DAYS_GRANT")
    public int numberDayGrant;
	
	@Column(name = "TIME_GRANT")
    public int timeGrant;
	
	@Column(name = "NUMBER_DAYS_REMAIN")
    public double numberDayRemain;
	
	@Column(name = "TIME_REMAIN")
    public int timeRemain;
	
	@Column(name = "NUMBER_DAYS_USE")
    public double numberDayUse;

	@Column(name = "TIME_USE")
    public int timeUse;
	
	@Column(name = "USED_SAVING_DAYS")
    public double useSavingDays;
	
	@Column(name = "NUMBER_OVER_DAYS")
    public int numberOverDays;

	@Column(name = "TIME_OVER")
    public int timeOver;
	
	@Override
	protected Object getKey() {
		return key;
	}
	
	

}
