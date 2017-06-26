package nts.uk.ctx.pr.core.dom.bank;

import java.util.Optional;

public interface BankAdapter {
	Optional<BasicBankDto> find(String companyCode, String bankCode);
}	
