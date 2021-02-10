package nts.uk.ctx.at.shared.infra.entity.specialholiday.specialholidayevent.grantdayperrelationship;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHMT_HDSPEV_GRANT_LIMIT")
// 続柄毎の上限日数
public class KshstGrantDayPerRelationship extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KshstGrantDayPerRelationshipPK pk;

	/* 忌引とする */
	@Column(name = "MAKE_INVITATION")
	public int makeInvitation;

	@Override
	protected Object getKey() {
		return this.pk;
	}

}
