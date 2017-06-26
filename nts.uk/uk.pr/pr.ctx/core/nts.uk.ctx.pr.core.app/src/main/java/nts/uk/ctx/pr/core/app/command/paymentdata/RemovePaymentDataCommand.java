package nts.uk.ctx.pr.core.app.command.paymentdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RemovePaymentDataCommand {

	private String personId;
	
	private Integer processingYM;
}
