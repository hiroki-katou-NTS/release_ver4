package nts.uk.ctx.basic.app.command.system.bank.branch;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccount;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonUseSetting;
import nts.uk.shr.com.context.AppContexts;
/**
 * tranfer branch command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class TranferBranchCommandHandler  extends CommandHandler<TranferBranchCommand> {
    
	@Inject
	private PersonBankAccountRepository personBankAccountRepository;
	
	@Inject 
	private LineBankRepository lineBankRepository;
	
	@Override
	protected void handle(CommandHandlerContext<TranferBranchCommand> context) {
		// get company code
		String companyCode = AppContexts.user().companyCode();
		
		// get command
		TranferBranchCommand command = context.getCommand();
		
		// get person bank account by list of branch id
		List<PersonBankAccount> listPersonBankAcc = personBankAccountRepository.findAllBranch(companyCode, command.getBranchId());
		
		// get line bank by list of branch id
		List<LineBank> listLineBank = lineBankRepository.find(companyCode, command.getBranchId());
		if (!listLineBank.isEmpty()) {
			lineBankRepository.updateByBranch(companyCode, command.getBranchId(), command.getBranchNewId());
		}
		
		command.getBranchId().forEach(item -> {
			// get person bank account by branch id
			List<PersonBankAccount> personBankAccList = listPersonBankAcc.stream()
					.filter(x -> filterBranchId(x, item))
					.collect(Collectors.toList());
			
			// perform update person bank account by branch id
			personBankAccList.forEach(x -> {
				PersonBankAccount bankAccount = x;
				PersonUseSetting useSet1 = bankAccount.getUseSet1(); 
				PersonUseSetting useSet2 = bankAccount.getUseSet2();
				PersonUseSetting useSet3 = bankAccount.getUseSet3();
				PersonUseSetting useSet4 = bankAccount.getUseSet4();
				PersonUseSetting useSet5 = bankAccount.getUseSet5(); 
				
				useSet1 = useSet(item, useSet1, command);
				useSet2 = useSet(item, useSet2, command);
				useSet3 = useSet(item, useSet3, command);
				useSet4 = useSet(item, useSet4, command);
				useSet5 = useSet(item, useSet5, command);
				
				PersonBankAccount domain = new PersonBankAccount(
						companyCode,
					    x.getPersonID(),
						x.getHistId(),
						x.getStartYearMonth(),
						x.getEndYearMonth(),
						useSet1,
						useSet2,
						useSet3,
						useSet4,
						useSet5);
				
				personBankAccountRepository.update(domain);
			});
		});
	}
	
	/**
	 * Create useSet
	 * @param branchId
	 * @param bankAccount
	 * @param command
	 * @return
	 */
	private PersonUseSetting useSet(String branchId, PersonUseSetting bankAccount, TranferBranchCommand command) {
		if (bankAccount.getToBranchId().equals(branchId)) {
			bankAccount.setToBranchId(command.getBranchNewId());
		}
		return bankAccount;
	}

	/**
	 * Check update useSet
	 * @param personBankAcc
	 * @param branchId
	 * @return
	 */
	private boolean filterBranchId(PersonBankAccount personBankAcc, String branchId) {
		if (personBankAcc.getUseSet1() != null && branchId.equals(personBankAcc.getUseSet1().getToBranchId())) {
			return true;
		}
		
		if (personBankAcc.getUseSet2() != null && branchId.equals(personBankAcc.getUseSet2().getToBranchId())) {
			return true;
		}
		
		if (personBankAcc.getUseSet3() != null && branchId.equals(personBankAcc.getUseSet3().getToBranchId())) {
			return true;
		}
		
		if (personBankAcc.getUseSet4() != null && branchId.equals(personBankAcc.getUseSet4().getToBranchId())) {
			return true;
		}
		
		return false;
	}
}
