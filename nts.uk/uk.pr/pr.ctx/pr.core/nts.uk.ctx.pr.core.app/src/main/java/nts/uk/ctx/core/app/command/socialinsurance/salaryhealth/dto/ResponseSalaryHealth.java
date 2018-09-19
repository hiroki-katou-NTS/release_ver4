package nts.uk.ctx.core.app.command.socialinsurance.salaryhealth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseSalaryHealth {
	
	private ResponseHealthInsurancePerGradeFee responseHealthInsurancePerGradeFee;
	private List<ResponseHealthAndMonthly> ResponseHealthAndMonthlies;
	
}
