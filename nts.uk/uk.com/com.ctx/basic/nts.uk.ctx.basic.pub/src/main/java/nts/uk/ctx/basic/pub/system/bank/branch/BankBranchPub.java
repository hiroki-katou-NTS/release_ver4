package nts.uk.ctx.basic.pub.system.bank.branch;

import java.util.Optional;

public interface BankBranchPub {
	Optional<BankBranchDto> find(String companyCode, String branchId);
}
