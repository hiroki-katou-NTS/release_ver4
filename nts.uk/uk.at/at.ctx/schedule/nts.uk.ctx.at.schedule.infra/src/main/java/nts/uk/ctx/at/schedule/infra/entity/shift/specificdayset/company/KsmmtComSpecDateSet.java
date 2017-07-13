package nts.uk.ctx.at.schedule.infra.entity.shift.specificdayset.company;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSMMT_COM_SPEC_DATE_SET")
public class KsmmtComSpecDateSet extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KsmmtComSpecDateSetPK ksmmtComSpecDateSetPK;

	@Override
	protected Object getKey() {
		return ksmmtComSpecDateSetPK;
	}

}
