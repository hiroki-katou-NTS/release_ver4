package nts.uk.ctx.at.record.infra.entity.remainingnumber.annlea;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_ANNLEA_BASIC_INFO")
public class KrcmtAnnLeaBasicInfo extends UkJpaEntity{
	
	@Id
    @Column(name = "EMPLOYEE_ID")
    public String employeeId;
	
	@Column(name = "WORK_DAYS_PER_YEAR")
    public int workDaysPerYear;
	
	@Column(name = "WORK_DAYS_BEFORE_INTRO")
    public int workDaysBeforeIntro;
	
	@Column(name = "GRANT_TABLE_CODE")
    public String grantTableCode;
	
	@Column(name = "GRANT_STANDARD_DATE")
    public GeneralDate grantStandardDate;

	@Override
	protected Object getKey() {
		return employeeId;
	}
}
