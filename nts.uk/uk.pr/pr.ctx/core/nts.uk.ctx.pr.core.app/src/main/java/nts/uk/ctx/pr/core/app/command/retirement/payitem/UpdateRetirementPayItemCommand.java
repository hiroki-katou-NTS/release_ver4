package nts.uk.ctx.pr.core.app.command.retirement.payitem;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@NoArgsConstructor
public class UpdateRetirementPayItemCommand {
	public String companyCode;
	public int category;
	public String itemCode;
	public String itemName;
	public String printName;
	public String englishName;
	public String fullName;
	public String memo;
}
