package nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author sonnh1
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KSCDT_BASIC_SCHEDULE")
public class KscdtBasicSchedule extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KscdpBasicSchedulePK kscdpBSchedulePK;

	@Column(name = "WORKTYPE_CD")
	public String workTypeCd;

	@Column(name = "WORKTIME_CD")
	public String workTimeCd;

	@Override
	protected Object getKey() {
		return this.kscdpBSchedulePK;
	}
}
