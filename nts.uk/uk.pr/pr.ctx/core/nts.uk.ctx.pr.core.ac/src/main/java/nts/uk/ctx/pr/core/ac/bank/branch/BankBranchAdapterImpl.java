package nts.uk.ctx.pr.core.ac.bank.branch;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.pub.system.bank.branch.BankBranchPub;
import nts.uk.ctx.pr.core.dom.bank.branch.adapter.BankBranchAdapter;
import nts.uk.ctx.pr.core.dom.bank.branch.adapter.BasicBankBranchDto;

@Stateless
public class BankBranchAdapterImpl implements BankBranchAdapter {

	@Inject
	private BankBranchPub bankBranchPub;

	@Override
	public Optional<BasicBankBranchDto> find(String companyCode, String branchId) {
		return bankBranchPub.find(companyCode, branchId).map(x -> {
			return new BasicBankBranchDto(companyCode, x.getBranchId(), x.getBankCode(), x.getBankBranchCode(),
					x.getBankBranchName(), x.getBankBranchNameKana(), x.getMemo());
		});
	}
}
