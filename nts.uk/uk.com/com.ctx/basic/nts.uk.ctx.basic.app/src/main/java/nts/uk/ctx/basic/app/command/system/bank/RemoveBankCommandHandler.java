package nts.uk.ctx.basic.app.command.system.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.Bank;
import nts.uk.ctx.basic.dom.system.bank.BankCode;
import nts.uk.ctx.basic.dom.system.bank.BankRepository;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * remove bank command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class RemoveBankCommandHandler extends CommandHandler<RemoveBankCommand> {

	@Inject
	private BankRepository bankRepository;

	@Inject
	private PersonBankAccountRepository personBankAccountRepository;

	@Inject
	private BankBranchRepository bankBranchRepo;

	@Override
	protected void handle(CommandHandlerContext<RemoveBankCommand> context) {

		RemoveBankCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		List<String> bankCodeList = new ArrayList<String>();
		bankCodeList.add(command.getBankCode());

		Optional<Bank> domain = bankRepository.find(companyCode, command.getBankCode());
		if (!domain.isPresent()) {
			throw new RuntimeException("銀行を検索できません");
		}

		// check exists person bank account
		if (personBankAccountRepository.checkExistsBranchAccount(companyCode, bankCodeList)) {
			throw new BusinessException("ER008"); // ER008
		}

		// delete all branch by bank code
		List<BankBranch> branchAll = bankBranchRepo.findAll(companyCode, new BankCode(command.getBankCode()));
		if (!branchAll.isEmpty()) {
			// get list of branch id
			List<String> branchIdList = branchAll.stream().map(x -> x.getBranchId().toString())
					.collect(Collectors.toList());

			// check exists person bank account
			if (personBankAccountRepository.checkExistsBranchAccount(companyCode, branchIdList)) {
				throw new BusinessException("ER008"); // ER008
			}
			bankBranchRepo.removeAll(companyCode, branchIdList);
		}

		// delete bank
		bankRepository.remove(companyCode, command.getBankCode());
	}
}
