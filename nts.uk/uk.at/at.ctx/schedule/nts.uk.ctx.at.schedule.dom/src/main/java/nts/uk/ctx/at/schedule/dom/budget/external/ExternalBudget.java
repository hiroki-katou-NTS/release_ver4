package nts.uk.ctx.at.schedule.dom.budget.external;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

@Getter
public class ExternalBudget extends AggregateRoot {

	private final String companyId;

	private final ExternalBudgetCd externalBudgetCd;

	private final ExternalBudgetName externalBudgetName;

	private BudgetAtr budgetAtr;

	private UnitAtr unitAtr;

	public ExternalBudget(String companyId, ExternalBudgetCd externalBudgetCd, ExternalBudgetName externalBudgetName,
			BudgetAtr budgetAtr, UnitAtr unitAtr) {
		super();
		this.companyId = companyId;
		this.externalBudgetCd = externalBudgetCd;
		this.externalBudgetName = externalBudgetName;
		this.budgetAtr = budgetAtr;
		this.unitAtr = unitAtr;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @param companyId
	 *            company ID
	 * @param externalBudgetCd
	 *            externalBudgetCd
	 * @return ExternalBudget
	 */
	public static ExternalBudget createFromJavaType(String companyId, String externalBudgetCd,
			String externalBudgetName, int budgetAtr, int unitAtr) {
		return new ExternalBudget(companyId, new ExternalBudgetCd(externalBudgetCd),
				new ExternalBudgetName(externalBudgetName), EnumAdaptor.valueOf(budgetAtr, BudgetAtr.class),
				EnumAdaptor.valueOf(unitAtr, UnitAtr.class));
	}

}
