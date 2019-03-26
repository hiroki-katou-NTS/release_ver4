package nts.uk.ctx.at.function.infra.entity.alarm.extractresult;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KFNMT_ALEX_DATA")
public class KfnmtAlexData extends UkJpaEntity {

	@EmbeddedId
	public KfnmtAlexDataPK pk;

	@Override
	protected Object getKey() {
		return pk;
	}
}
