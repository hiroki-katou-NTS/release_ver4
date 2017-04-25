package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBDRepository;
@Stateless
@Transactional
public class DeleteItemSalaryBDCommandHandler extends CommandHandler<DeleteItemSalaryBDCommand> {

	@Inject
	private ItemSalaryBDRepository itemSalaryBDRepository;

	@Override
	protected void handle(CommandHandlerContext<DeleteItemSalaryBDCommand> context) {
		
		val itemCode = context.getCommand().getItemCode();
		val itemBreakdownCode = context.getCommand().getItemBreakdownCode();
		if(!this.itemSalaryBDRepository.find(itemCode,itemBreakdownCode).isPresent())
			throw new BusinessException("明細書名が入力されていません。");
		this.itemSalaryBDRepository.delete(itemCode,itemBreakdownCode);
	}

}
