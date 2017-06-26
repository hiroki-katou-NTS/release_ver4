package nts.uk.ctx.basic.app.command.system.bank.linebank;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
/**
 * additional lineBank if lineBank has lineBankCode exist, not addition
 * 
 * @author sonnh1
 *
 */
public class AddLineBankCommandHandler extends CommandHandler<LineBankCommandBase> {
	@Inject
	private LineBankRepository lineBankRepository;

	@Override
	protected void handle(CommandHandlerContext<LineBankCommandBase> context) {
		String companyCode = AppContexts.user().companyCode();
		LineBank lineBank = context.getCommand().toDomain(companyCode);
		
		lineBank.validate();
		
		// check exist lineBankCode
		Optional<LineBank> lineBankOpt = this.lineBankRepository.find(companyCode, lineBank.getLineBankCode().v());
		if (lineBankOpt.isPresent()) {
			throw new BusinessException("ER005");
		}
		this.lineBankRepository.add(lineBank);
	}
}
