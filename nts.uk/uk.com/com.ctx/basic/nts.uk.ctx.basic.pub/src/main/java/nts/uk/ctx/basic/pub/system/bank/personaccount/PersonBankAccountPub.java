package nts.uk.ctx.basic.pub.system.bank.personaccount;

import java.util.Optional;

public interface PersonBankAccountPub {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYM
	 * @return
	 */
	Optional<PersonBankAccountDto> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM);

}
