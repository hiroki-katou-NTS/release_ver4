package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBD;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBDRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateItemSalaryBDCommandHandler extends CommandHandler<UpdateItemSalaryBDCommand> {

	@Inject
	private ItemSalaryBDRepository itemSalaryBDRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemSalaryBDCommand> context) {
		String itemCode = context.getCommand().getItemCode();
		String itemBreakdownCode = context.getCommand().getItemBreakdownCode();
		String companyCode = AppContexts.user().companyCode();
		ItemSalaryBD itemSalaryBD = context.getCommand().toDomain();
		itemSalaryBD.validate();
		if (!this.itemSalaryBDRepository.find(companyCode, itemCode, itemBreakdownCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemSalaryBDRepository.update(companyCode, itemSalaryBD);
	}

}
