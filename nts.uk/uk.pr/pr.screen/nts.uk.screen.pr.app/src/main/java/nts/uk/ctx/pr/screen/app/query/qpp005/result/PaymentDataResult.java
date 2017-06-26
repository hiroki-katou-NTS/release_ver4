package nts.uk.ctx.pr.screen.app.query.qpp005.result;

import java.util.List;

import lombok.Data;

@Data
public class PaymentDataResult {

	private PaymentDataHeaderDto paymentHeader;

	private List<LayoutMasterCategoryDto> categories;
	
	private String remarks;

}
