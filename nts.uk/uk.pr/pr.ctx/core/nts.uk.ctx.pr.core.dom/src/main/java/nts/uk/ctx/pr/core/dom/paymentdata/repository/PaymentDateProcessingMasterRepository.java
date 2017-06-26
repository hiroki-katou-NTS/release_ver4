package nts.uk.ctx.pr.core.dom.paymentdata.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateProcessingMaster;

public interface PaymentDateProcessingMasterRepository {
	/**
	 * 
	 * @param companyCode
	 * @param paymentBonusAtribute
	 * @param processingNo
	 * @return PaymentDateProcessingMaster
	 */
	Optional<PaymentDateProcessingMaster> find(String companyCode, int paymentBonusAtribute, int processingNo);

	/**
	 * Get list payment date processing with condition display = 1
	 * @param companyCode
	 * @param paymentBonusAtribute
	 * @return PaymentDateProcessingMaster
	 */
	List<PaymentDateProcessingMaster> findAll(String companyCode, int paymentBonusAtribute);
	
	//========================================//
	
	/**
	 * Find pay day processing
	 * @param companyCode companyCode
	 * @param processingNo processingNo
	 * @return PaymentDateProcessingMaster
	 */
	Optional<PaymentDateProcessingMaster> find(String companyCode, int processingNo);
}
