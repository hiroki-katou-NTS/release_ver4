package nts.uk.ctx.pr.core.app.command.paymentdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Command: add payment data
 * @author chinhbv
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class CreatePaymentDataCommand {
	private String personId;
	
	private String personName;
	
	private int processingNo;
	
	/**
	 * Current processing year month
	 */
	private int processingYearMonth;
	
	public CreatePaymentDataCommand() {}
}
