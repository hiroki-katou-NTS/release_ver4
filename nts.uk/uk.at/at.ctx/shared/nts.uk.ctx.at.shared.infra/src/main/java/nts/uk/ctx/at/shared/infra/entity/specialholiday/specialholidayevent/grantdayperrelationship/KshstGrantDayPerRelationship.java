package nts.uk.ctx.at.shared.infra.entity.specialholiday.specialholidayevent.grantdayperrelationship;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_GRANT_DAY_PER_RELP")
// 続柄毎の上限日数
public class KshstGrantDayPerRelationship extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	/* 続柄毎の上限日数ID */
	@Column(name = "GRANT_DAY_PER_RELP_ID")
	private String grantDayPerRelpId;
	
	/* 特別休暇枠NO */
	@Column(name = "S_HOLIDAY_EVENT_NO")
	public int sHolidayEventNo;

	/* 忌引とする */
	@Column(name = "MAKE_INVITATION")
	private int makeInvitation;

	@Override
	protected Object getKey() {
		return this.grantDayPerRelpId;
	}

}
