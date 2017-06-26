/**
 * 
 */
package nts.uk.file.pr.app.export.retirementpayment.data;

import lombok.Value;

/**
 * @author hungnm
 *
 */
@Value
public class RetirePayItemDto {

	private String itemCode;

	private String itemName;

	private String itemAbName;

	public RetirePayItemDto(String itemCode, String itemName, String itemAbName) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemAbName = itemAbName;
	}
	
}
