package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateItemSalaryCommandHandler extends CommandHandler<UpdateItemSalaryCommand> {

	@Inject
	private ItemSalaryRespository itemSalaryRespository;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemSalaryCommand> context) {
		val companyCode = AppContexts.user().companyCode();
		String itemCode = context.getCommand().getItemCode();
		if (!this.itemSalaryRespository.find(companyCode, itemCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemSalaryRespository.update(companyCode, context.getCommand().toDomain());

	}

}
