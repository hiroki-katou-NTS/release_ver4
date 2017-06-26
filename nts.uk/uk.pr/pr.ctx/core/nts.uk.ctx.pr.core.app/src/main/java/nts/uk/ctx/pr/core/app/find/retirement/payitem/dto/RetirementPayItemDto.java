package nts.uk.ctx.pr.core.app.find.retirement.payitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@AllArgsConstructor
public class RetirementPayItemDto {
	public String companyCode;
	public int category;
	public String itemCode;
	public String itemName;
	public String printName;
	public String englishName;
	public String fullName;
	public String memo;
}	
