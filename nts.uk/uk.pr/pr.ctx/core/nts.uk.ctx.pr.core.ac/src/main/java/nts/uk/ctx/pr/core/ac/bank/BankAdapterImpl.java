package nts.uk.ctx.pr.core.ac.bank;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.pub.system.bank.BankPub;
import nts.uk.ctx.pr.core.dom.bank.BankAdapter;
import nts.uk.ctx.pr.core.dom.bank.BasicBankDto;

@Stateless
public class BankAdapterImpl implements BankAdapter {

	@Inject
	private BankPub bankPub;

	@Override
	public Optional<BasicBankDto> find(String companyCode, String bankCode) {
		return bankPub.find(companyCode, bankCode).map(x -> {
			return new BasicBankDto(companyCode, x.getBankCode(), x.getBankName(), x.getBankNameKana(), x.getMemo());
		});
	}

}
