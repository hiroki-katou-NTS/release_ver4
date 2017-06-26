package nts.uk.ctx.basic.pubimp.system.bank.personaccount;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonUseSetting;
import nts.uk.ctx.basic.pub.system.bank.personaccount.PersonBankAccountDto;
import nts.uk.ctx.basic.pub.system.bank.personaccount.PersonBankAccountPub;
import nts.uk.ctx.basic.pub.system.bank.personaccount.PersonUseSettingDto;

@Stateless
public class PersonBankAccountPubImp implements PersonBankAccountPub {
	@Inject
	private PersonBankAccountRepository personBankAccountRepo;
	
	@Override
	public Optional<PersonBankAccountDto> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM) {
		return personBankAccountRepo.findBasePIdAndBaseYM(companyCode, personId, baseYM)
				.map(x -> {
					return new PersonBankAccountDto(
							companyCode, 
							x.getPersonID(), 
							x.getHistId(), 
							x.getStartYearMonth(), 
							x.getEndYearMonth(), 
							toUserSetting(x.getUseSet1()), 
							toUserSetting(x.getUseSet2()), 
							toUserSetting(x.getUseSet3()), 
							toUserSetting(x.getUseSet4()), 
							toUserSetting(x.getUseSet5()));
				});
	}

	private PersonUseSettingDto toUserSetting(PersonUseSetting x) {
		return new PersonUseSettingDto(x.getUseSet(), x.getPriorityOrder(), x.getPaymentMethod(), x.getPartialPaySet(), x.getFixAmountMny(),
				x.getFixRate(), x.getFromLineBankCd(), x.getToBranchId(), x.getAccountAtr(), x.getAccountNo(),
				x.getAccountHolderKnName(), x.getAccountHolderName());
	}
}
