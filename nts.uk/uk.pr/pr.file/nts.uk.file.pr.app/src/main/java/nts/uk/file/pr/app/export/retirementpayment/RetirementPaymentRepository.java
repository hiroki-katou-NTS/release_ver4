package nts.uk.file.pr.app.export.retirementpayment;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirePayItemDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentDto;

public interface RetirementPaymentRepository {
	/**
	 * Get retirement payment data.
	 *
	 * @param query the query
	 * @return the payment report data
	 */
	public List<RetirementPaymentDto> getRetirementPayment(String companyCode, List<String> personId, GeneralDate startDate, GeneralDate endDate);
	
	/**
	 * Get retirement payment data.
	 *
	 * @param companyCode
	 * @return the list payment item
	 */
	public List<RetirePayItemDto> getListRetirePayItem(String companyCode);
}
