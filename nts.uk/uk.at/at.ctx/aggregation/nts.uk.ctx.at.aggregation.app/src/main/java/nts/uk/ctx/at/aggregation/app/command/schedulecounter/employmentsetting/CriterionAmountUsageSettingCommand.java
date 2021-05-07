package nts.uk.ctx.at.aggregation.app.command.schedulecounter.employmentsetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CriterionAmountUsageSettingCommand {
	/** 会社ID */
	private String cid;

	/** 雇用利用区分 */
	private int employmentUse;
	
}
