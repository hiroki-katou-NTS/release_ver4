package nts.uk.ctx.basic.app.command.system.bank.linebank;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
public class AddLineBankCommandHandler extends CommandHandler<AddLineBankCommand> {
	@Inject
	private LineBankRepository lineBankRepository;

	@Override
	protected void handle(CommandHandlerContext<AddLineBankCommand> context) {
		// TODO Auto-generated method stub
		String companyCode = AppContexts.user().companyCode();
		LineBank lineBank = context.getCommand().toDomain(companyCode);
		
		lineBank.validate();
		
		// check ton tai lineBankCode
		Optional<LineBank> lineBankOpt = this.lineBankRepository.find(companyCode, lineBank.getLineBankCode().v());
		if (lineBankOpt.isPresent()) {
			throw new BusinessException("���͂������͊��ɑ��݂��Ă��܂��B\r\n �����m�F���Ă��������B");//ER005
		}
		this.lineBankRepository.add(lineBank);
	}
}
