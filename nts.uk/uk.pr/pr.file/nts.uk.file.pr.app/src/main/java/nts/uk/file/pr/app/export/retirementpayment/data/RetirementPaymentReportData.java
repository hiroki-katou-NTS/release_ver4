package nts.uk.file.pr.app.export.retirementpayment.data;

import java.util.List;

import lombok.Data;

@Data
public class RetirementPaymentReportData {
	
	private RetirementPaymentDto retirementPaymentDto;
	
	private PersonalBasicDto personalBasicDto;
	
	private CompanyMasterDto companyMasterDto;
}
