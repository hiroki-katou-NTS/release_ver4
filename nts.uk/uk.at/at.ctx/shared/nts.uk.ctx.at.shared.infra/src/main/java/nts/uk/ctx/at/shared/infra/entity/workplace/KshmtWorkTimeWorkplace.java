package nts.uk.ctx.at.shared.infra.entity.workplace;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author tutk
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KSHMT_WORKTIME_WORKPLACE")
public class KshmtWorkTimeWorkplace extends UkJpaEntity {
	
	@EmbeddedId
	public KshmtWorkTimeWorkplacePK kshmtWorkTimeWorkplacePK;

	@Override
	protected Object getKey() {
		return kshmtWorkTimeWorkplacePK;
	}

}
