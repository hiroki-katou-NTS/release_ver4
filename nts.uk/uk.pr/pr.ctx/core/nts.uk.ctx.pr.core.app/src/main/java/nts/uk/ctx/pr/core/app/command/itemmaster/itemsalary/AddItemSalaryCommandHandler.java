package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class AddItemSalaryCommandHandler extends CommandHandler<AddItemSalaryCommand> {

	@Inject
	ItemSalaryRespository itemSalaryRespository;

	@Override
	protected void handle(CommandHandlerContext<AddItemSalaryCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		String itemCode = context.getCommand().getItemCode();
		if (this.itemSalaryRespository.find(companyCode, itemCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemSalaryRespository.add(companyCode, context.getCommand().toDomain());

	}

}
