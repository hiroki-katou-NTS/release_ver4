package nts.uk.ctx.at.shared.infra.entity.remainingnumber.resvlea;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_RVSLEA_REMAIN")
public class KrcmtReverseLeaRemain extends UkJpaEntity {
	
	@Id
	@Column(name = "RVSLEA_ID")
	public String rvsLeaId;
	
	@Column(name = "SID")
	public String sid;
	
	@Column(name = "CID")
	public String cid;

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

	@Column(name = "USED_DAYS")
	public double usedDays;

	@Column(name = "OVER_LIMIT_DAYS")
	public double overLimitDays;
	
	@Column(name = "REMAINING_DAYS")
	public double remainingDays;

	@Override
	protected Object getKey() {
		return rvsLeaId;
	}

	public ReserveLeaveGrantRemainingData toDomain() {
		return ReserveLeaveGrantRemainingData.createFromJavaType(this.rvsLeaId, this.sid, this.grantDate, this.deadline,
				this.expStatus, this.registerType, this.grantDays, this.usedDays, this.overLimitDays, this.remainingDays);
	}
}
