package nts.uk.ctx.pr.core.app.command.laborinsurance;

import lombok.Value;

@Value
public class EmpInsurHisCommand {
	private String hisId;
	private int methodEditing;
	private int startMonthYear;
	private int endMonthYear;
}
