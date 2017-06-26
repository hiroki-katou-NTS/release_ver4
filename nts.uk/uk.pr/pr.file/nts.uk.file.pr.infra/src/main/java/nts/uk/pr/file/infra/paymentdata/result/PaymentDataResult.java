package nts.uk.pr.file.infra.paymentdata.result;

import java.util.List;

import lombok.Data;

@Data
public class PaymentDataResult {

	private PaymentDataHeaderDto paymentHeader;

	private List<LayoutMasterCategoryDto> categories;
	
	private String remarks;
}
