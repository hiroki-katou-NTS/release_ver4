package nts.uk.ctx.at.shared.infra.entity.remainingnumber.subhdmana;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_LEAVE_MANA_DATA")
public class KrcmtLeaveManaData extends UkJpaEntity {
	@Id
	@Column(name="LEAVE_MANA_ID")
	public String leaveID;
	
	@Column(name="CID")
	public String cID;
	
	@Column(name="SID")
	public String sID;
	
	@Column(name= "UNKNOWN_DATE")
	public boolean unknownDate;

	@Column(name = "DAYOFF_DATE")
	public GeneralDate dayOff;
	
	@Column(name="EXPIRED_DATE")
	public GeneralDate expiredDate;
	
	@Column(name="OCCURRED_DAYS")
	public Double occurredDays;
	
	@Column(name="OCCURRED_TIMES")
	public int occurredTimes;
	
	@Column(name="UNUSED_DAYS")
	public Double unUsedDays;
	
	@Column(name="UNUSED_TIMES")
	public int unUsedTimes;
	
	@Column(name="SUB_HD_ATR")
	public int subHDAtr;
	
	@Column(name="FULL_DAY_TIME")
	public int fullDayTime;
	
	@Column(name="HALF_DAY_TIME")
	public int halfDayTime;
	
	@Column(name="DISAPEAR_DATE")
	public GeneralDate disapearDate;

	@Override
	protected Object getKey() {
		return this.leaveID;
	}

}
