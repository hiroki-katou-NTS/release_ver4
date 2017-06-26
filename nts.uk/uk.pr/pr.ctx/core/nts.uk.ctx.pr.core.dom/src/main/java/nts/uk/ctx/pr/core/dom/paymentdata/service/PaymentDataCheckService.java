package nts.uk.ctx.pr.core.dom.paymentdata.service;

import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;

public interface PaymentDataCheckService {
	
	/**
	 * Check exists payment data of person.
	 * @param companyCode company code
	 * @param personId person id
	 * @param payBonusAtr payroll bonus attribute
	 * @param processingYearMonth processing year month
	 * @return true if exists payment data of person else false
	 */
	boolean isExists(String companyCode, String personId, PayBonusAtr payBonusAtr, int processingYearMonth);
	
}
