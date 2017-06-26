package nts.uk.ctx.basic.pub.system.bank;

import java.util.Optional;

public interface BankPub {
	Optional<BankDto> find(String companyCode, String bankCode);
}
