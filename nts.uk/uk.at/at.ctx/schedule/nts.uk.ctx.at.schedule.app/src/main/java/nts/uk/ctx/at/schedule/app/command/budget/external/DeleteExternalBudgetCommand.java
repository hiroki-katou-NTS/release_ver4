package nts.uk.ctx.at.schedule.app.command.budget.external;

import lombok.Getter;
import nts.uk.ctx.at.schedule.dom.budget.external.ExternalBudget;
import nts.uk.shr.com.context.AppContexts;

@Getter
public class DeleteExternalBudgetCommand {
	private String externalBudgetCode;
	
	private String externalBudgetName;

	private int budgetAtr;

	private int unitAtr;
	
	public ExternalBudget toDomain() {
		return ExternalBudget.createFromJavaType(AppContexts.user().companyId(), this.externalBudgetCode,
				this.externalBudgetName, this.budgetAtr, this.unitAtr);
	}

}
