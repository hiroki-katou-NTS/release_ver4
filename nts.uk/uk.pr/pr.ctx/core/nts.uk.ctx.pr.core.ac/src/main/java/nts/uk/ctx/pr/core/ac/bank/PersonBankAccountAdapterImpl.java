package nts.uk.ctx.pr.core.ac.bank;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.pub.system.bank.personaccount.PersonBankAccountPub;
import nts.uk.ctx.basic.pub.system.bank.personaccount.PersonUseSettingDto;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter.BasicPersonBankAccountDto;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter.BasicPersonUseSettingDto;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter.PersonBankAccountAdapter;

@Stateless
public class PersonBankAccountAdapterImpl implements PersonBankAccountAdapter {
	@Inject
	private PersonBankAccountPub personBankAccountPub;
	
	@Override
	public Optional<BasicPersonBankAccountDto> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM) {
		return personBankAccountPub.findBasePIdAndBaseYM(companyCode, personId, baseYM)
				.map(x -> {
					return new BasicPersonBankAccountDto(
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
	
	private BasicPersonUseSettingDto toUserSetting(PersonUseSettingDto x) {
		return new BasicPersonUseSettingDto(x.getUseSet(), x.getPriorityOrder(), x.getPaymentMethod(), x.getPartialPaySet(), x.getFixAmountMny(),
				x.getFixRate(), x.getFromLineBankCd(), x.getToBranchId(), x.getAccountAtr(), x.getAccountNo(),
				x.getAccountHolderKnName(), x.getAccountHolderName());
	}
}
