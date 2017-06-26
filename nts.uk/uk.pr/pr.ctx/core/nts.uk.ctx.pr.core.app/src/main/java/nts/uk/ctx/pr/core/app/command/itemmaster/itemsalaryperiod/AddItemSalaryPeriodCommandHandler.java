package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriod;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriodRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class AddItemSalaryPeriodCommandHandler extends CommandHandler<AddItemSalaryPeriodCommand> {
	@Inject
	private ItemSalaryPeriodRepository itemSalaryPeriodRepository;

	@Override
	protected void handle(CommandHandlerContext<AddItemSalaryPeriodCommand> context) {
		String itemCode = context.getCommand().getItemCode();
		String companyCode = AppContexts.user().companyCode();
		ItemSalaryPeriod itemSalaryPeriod = context.getCommand().toDomain();
		itemSalaryPeriod.validate();
		if (this.itemSalaryPeriodRepository.find(companyCode, itemCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemSalaryPeriodRepository.add(companyCode, itemSalaryPeriod);
	}

}
