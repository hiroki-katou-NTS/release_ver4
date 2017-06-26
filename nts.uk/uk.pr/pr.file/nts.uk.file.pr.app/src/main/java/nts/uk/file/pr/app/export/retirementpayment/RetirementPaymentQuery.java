package nts.uk.file.pr.app.export.retirementpayment;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Setter
@Getter
public class RetirementPaymentQuery {
	private List<String> lstPersonId;
	
	private GeneralDate startDate;
	
	private GeneralDate endDate;
}
