package nts.uk.ctx.pr.core.dom.bank.linebank.adapter;

import java.util.Optional;

public interface LineBankAdapter {
	/**
	 * 
	 * @param companyCode
	 * @param lineBankCode
	 * @return
	 */
	Optional<BasicLineBankDto> find(String companyCode, String lineBankCode);
}
