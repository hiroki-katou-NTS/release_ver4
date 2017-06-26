package nts.uk.ctx.basic.pubimp.system.bank;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.system.bank.BankRepository;
import nts.uk.ctx.basic.pub.system.bank.BankDto;
import nts.uk.ctx.basic.pub.system.bank.BankPub;

@Stateless
public class BankPubImp implements BankPub {

	@Inject
	private BankRepository bankRepo;

	@Override
	public Optional<BankDto> find(String companyCode, String bankCode) {
		return bankRepo.find(companyCode, bankCode).map(x -> {
			return new BankDto(companyCode, x.getBankCode().toString(), x.getBankName().toString(),
					x.getBankNameKana().toString(), x.getMemo().toString());
		});
	}

}
