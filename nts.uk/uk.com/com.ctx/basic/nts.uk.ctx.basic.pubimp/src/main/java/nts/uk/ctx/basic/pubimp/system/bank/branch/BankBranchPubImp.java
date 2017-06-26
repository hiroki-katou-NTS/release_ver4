package nts.uk.ctx.basic.pubimp.system.bank.branch;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.ctx.basic.pub.system.bank.branch.BankBranchDto;
import nts.uk.ctx.basic.pub.system.bank.branch.BankBranchPub;

@Stateless
public class BankBranchPubImp implements BankBranchPub {

	@Inject
	private BankBranchRepository bankBranchRepo;

	@Override
	public Optional<BankBranchDto> find(String companyCode, String branchId) {
		return bankBranchRepo.find(companyCode, branchId).map(x -> {
				return new BankBranchDto(companyCode, x.getBranchId().toString(), x.getBankCode(), x.getBankBranchCode().toString(), x.getBankBranchName().toString(),
						x.getBankBranchNameKana().toString(), x.getMemo().toString());});
	}

}
