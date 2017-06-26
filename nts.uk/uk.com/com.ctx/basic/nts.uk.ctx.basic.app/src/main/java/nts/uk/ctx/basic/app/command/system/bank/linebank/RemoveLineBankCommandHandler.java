package nts.uk.ctx.basic.app.command.system.bank.linebank;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
/**
 * remove lineBank. If lineBank is using, not remove
 * 
 * @author sonnh1
 *
 */
public class RemoveLineBankCommandHandler extends CommandHandler<RemoveLineBankCommand> {
	@Inject
	private LineBankRepository lineBankRepository;

	@Inject
	private PersonBankAccountRepository personBankAccountRepository;

	@Override
	protected void handle(CommandHandlerContext<RemoveLineBankCommand> context) {
		RemoveLineBankCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		String lineBankCode = command.getLineBankCode();
		List<String> lineBankCodeList = new ArrayList<String>();
		lineBankCodeList.add(lineBankCode);

		// check exists linebank in database PERSON_BANK_ACCOUNT  
		if (personBankAccountRepository.checkExistsLineBankAccount(companyCode, lineBankCodeList)) {
			throw new BusinessException("ER008");
		}

		lineBankRepository.remove(companyCode, lineBankCode);
	}
}
