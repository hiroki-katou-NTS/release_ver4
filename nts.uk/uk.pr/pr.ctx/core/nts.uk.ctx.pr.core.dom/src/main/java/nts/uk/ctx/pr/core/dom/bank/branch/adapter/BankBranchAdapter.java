package nts.uk.ctx.pr.core.dom.bank.branch.adapter;

import java.util.Optional;

public interface BankBranchAdapter {
	Optional<BasicBankBranchDto> find(String companyCode, String branchId);
}
