package nts.uk.ctx.pr.core.infra.entity.personalinfo.wage;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.AggregateTableEntity;

@Entity
@Table(name = "QPWMT_PERSONAL_WAGE_NAME")
public class QpwmtPersonalWageName extends AggregateTableEntity {
	@EmbeddedId
	public QpwmtPersonalWageNamePK qpwmtPersonalWageNamePK;
	@Column(name = "P_WAGE_NAME")
	public String pWageName;
	@Override
	protected Object getKey() {
		return this.qpwmtPersonalWageNamePK;
	}
}
