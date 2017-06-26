package nts.uk.ctx.at.schedule.infra.entity.budget.external;

import javax.persistence.Basic;
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
@Table(name = "KSCST_EXTERNAL_BUDGET")
public class KscstExternalBudget extends UkJpaEntity{

	@EmbeddedId
	public KstscExternalBudgetPK kscstExternalBudgetPk;

	@Basic(optional = false)
	@Column(name = "EXTERNAL_BUDGET_NAME")
	public String externalBudgetName;

	@Basic(optional = false)
	@Column(name = "BUDGET_ATR")
	public int budgetAtr;

	@Basic(optional = false)
	@Column(name = "UNIT_ATR")
	public int unitAtr;

	@Override
	protected Object getKey() {
		return kscstExternalBudgetPk;
	}

}
