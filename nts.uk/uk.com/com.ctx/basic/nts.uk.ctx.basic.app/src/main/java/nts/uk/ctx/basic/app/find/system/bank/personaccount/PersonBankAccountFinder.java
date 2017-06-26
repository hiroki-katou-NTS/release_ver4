package nts.uk.ctx.basic.app.find.system.bank.personaccount;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.app.find.system.bank.dto.PersonBankAccountDto;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccount;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * Finder: person bank account
 *
 */
@Stateless
public class PersonBankAccountFinder {
	@Inject
	private PersonBankAccountRepository personBankAccountRepo;
	
	/**
	 * TODO not implement
	 * Find information of person bank 
	 * @param personId person id
	 * @return person bank account dto
	 */
	public PersonBankAccountDto find(String personId) {
		String companyCode = AppContexts.user().companyCode();
		
		Optional<PersonBankAccount> personBankAccOp = personBankAccountRepo.find(companyCode, personId, "0");//TODO
		
		if (!personBankAccOp.isPresent()) {
			return null;
		}
		
		PersonBankAccount personBankAccount = personBankAccOp.get();
		
		return null;
	}
}
