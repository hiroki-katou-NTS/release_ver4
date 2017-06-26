package nts.uk.ctx.pr.core.dom.paymentdata.repository;

import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.PaymentCalculationBasicInformation;

/**
 * @author hungnm
 *
 */
public interface PaymentCalculationBasicInformationRepository {
	/**
	 * Find a basic information of payment calculation by company code.
	 * 
	 * @param companyCode
	 *            code
	 * @return PaymentCalculationBasicInformation found
	 */
	Optional<PaymentCalculationBasicInformation> find(String companyCode);
}
