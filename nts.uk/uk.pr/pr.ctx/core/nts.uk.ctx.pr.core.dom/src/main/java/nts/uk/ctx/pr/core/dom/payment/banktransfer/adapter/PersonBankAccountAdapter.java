package nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter;

import java.util.Optional;

public interface PersonBankAccountAdapter {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYM
	 * @return
	 */
	Optional<BasicPersonBankAccountDto> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM);
}
