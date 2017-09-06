package nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment;

import lombok.Value;

/**
 * 労働契約履歴
 * @author keisuke_hoshina
 *
 */
@Value
public class EmploymentContractHistory {

	private String syainID;
	private WorkingSystem workingSystem;
}
