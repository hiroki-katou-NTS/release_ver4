package nts.uk.ctx.at.function.infra.enity.alarm.checkcondition;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author HungTT
 *
 */

@NoArgsConstructor
@Entity
@Table(name = "KFNMT_ALCHKTARGET_EMP")
public class KfnmtAlarmCheckTargetEmployment extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtAlarmCheckTargetEmploymentPk pk;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "AL_CHK_TARGET_ID", referencedColumnName = "ID", insertable = false, updatable = false) })
	public KfnmtAlarmCheckTargetCondition targetCondition;

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public KfnmtAlarmCheckTargetEmployment(String targetConditionId, String employmentCode) {
		super();
		this.pk = new KfnmtAlarmCheckTargetEmploymentPk(targetConditionId, employmentCode);
	}

}
