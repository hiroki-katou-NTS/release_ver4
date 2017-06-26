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
import nts.gul.text.StringUtil;
import nts.uk.ctx.basic.dom.system.bank.Bank;
import nts.uk.ctx.basic.dom.system.bank.BankCode;
import nts.uk.ctx.basic.dom.system.bank.BankRepository;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * remove list bank command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class RemoveListBankCommandHandler extends CommandHandler<RemoveListBankCommand> {

	@Inject
	private BankRepository bankRepository;

	@Inject
	private PersonBankAccountRepository personBankAccountRepository;

	@Inject
	private BankBranchRepository bankBranchRepo;

	@Override
	protected void handle(CommandHandlerContext<RemoveListBankCommand> context) {

		RemoveListBankCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		List<String> bankDeleted = new ArrayList<>();

		command.getBank().forEach(bank -> {
			if (StringUtil.isNullOrEmpty(bank.getBranchId(), true)) {
				Optional<Bank> domain = bankRepository.find(companyCode, bank.getBankCode());
				if (!domain.isPresent()) {
					throw new RuntimeException("銀行を検索できません");
				}
				
				// get all branch by bank
				List<BankBranch> branchAll = bankBranchRepo.findAll(companyCode, new BankCode(bank.getBankCode()));
				if (!branchAll.isEmpty()) {
					// get list of branch id
					List<String> branchIdList = branchAll.stream().map(x -> x.getBranchId().toString())
							.collect(Collectors.toList());

					// check exists person bank account
					if (personBankAccountRepository.checkExistsBranchAccount(companyCode, branchIdList)) {
						throw new BusinessException("ER008"); // ER008
					}
                    // remove list bank branch
						bankBranchRepo.removeAll(companyCode, branchIdList);
				}
				// remove bank
				bankRepository.remove(companyCode, bank.getBankCode());
				bankDeleted.add(bank.getBankCode());
				
			} else if (!bankDeleted.contains(bank.getBankCode())) {
				List<String> branchBranchIdList = new ArrayList<String>();
				branchBranchIdList.add(bank.getBranchId().toString());
				//check exist branch account
				if (personBankAccountRepository.checkExistsBranchAccount(companyCode, branchBranchIdList)) {
					throw new BusinessException("ER008"); // ER008
				}
				bankBranchRepo.remove(companyCode, bank.getBranchId().toString());
			}
		});
	}

}
