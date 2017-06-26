package nts.uk.ctx.at.schedule.infra.entity.budget.external;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KstscExternalBudgetPK implements Serializable {
	/**
	 * serialVersionUID
	 */
	public static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;

	@Basic(optional = false)
	@Column(name = "EXTERNAL_BUDGET_CD")
	public String externalBudgetCd;

}
