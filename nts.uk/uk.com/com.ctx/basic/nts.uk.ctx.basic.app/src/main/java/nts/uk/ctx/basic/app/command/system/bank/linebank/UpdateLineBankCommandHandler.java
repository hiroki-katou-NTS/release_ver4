package nts.uk.ctx.basic.app.command.system.bank.linebank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
/**
 * update lineBank
 * 
 * @author sonnh1
 *
 */
public class UpdateLineBankCommandHandler extends CommandHandler<LineBankCommandBase> {
	@Inject
	private LineBankRepository lineBankRepository;

	@Override
	protected void handle(CommandHandlerContext<LineBankCommandBase> context) {
		String companyCode = AppContexts.user().companyCode();
		LineBank lineBank = context.getCommand().toDomain(companyCode);
		
		lineBank.validate();

		this.lineBankRepository.update(lineBank);
	}
}
