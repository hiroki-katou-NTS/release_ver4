package nts.uk.ctx.at.record.infra.entity.daily.remarks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.remarks.RecordRemarks;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_OVERTIMEWORK")
@NoArgsConstructor
public class KrcdtDayRemarksColumn extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KrcdtDayRemarksColumnPK krcdtDayRemarksColumnPK;
	/* 備考 */
	@Column(name = "REMARKS")
	public String remarks;

	@Override
	protected Object getKey() {
		return krcdtDayRemarksColumnPK;
	}

	public KrcdtDayRemarksColumn(KrcdtDayRemarksColumnPK krcdtDayRemarksColumnPK, String remarks) {
		super();
		this.krcdtDayRemarksColumnPK = krcdtDayRemarksColumnPK;
		this.remarks = remarks;
	}

	public RemarksOfDailyPerform toDomain() {
		return new RemarksOfDailyPerform(krcdtDayRemarksColumnPK.sid, krcdtDayRemarksColumnPK.ymd,
				remarks == null ? null : new RecordRemarks(remarks), krcdtDayRemarksColumnPK.remarkNo);
	}

	public static KrcdtDayRemarksColumn toEntity(RemarksOfDailyPerform domain) {
		return new KrcdtDayRemarksColumn(
									new KrcdtDayRemarksColumnPK(domain.getEmployeeId(), domain.getYmd(), domain.getRemarkNo()),
									domain.getRemarks() == null ? null : domain.getRemarks().v());
	}
}