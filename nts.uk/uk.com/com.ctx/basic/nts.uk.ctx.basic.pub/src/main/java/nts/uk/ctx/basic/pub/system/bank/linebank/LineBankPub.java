package nts.uk.ctx.basic.pub.system.bank.linebank;

import java.util.Optional;

public interface LineBankPub {
	/**
	 * 
	 * @param companyCode
	 * @param lineBankCode
	 * @return
	 */
	Optional<LineBankDto> find(String companyCode, String lineBankCode);
}
