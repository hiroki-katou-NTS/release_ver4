package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriodRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateItemSalaryPeriodCommandHandler extends CommandHandler<UpdateItemSalaryPeriodCommand> {

	@Inject
	private ItemSalaryPeriodRepository itemSalaryPeriodRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemSalaryPeriodCommand> context) {
		String itemCd = context.getCommand().getItemCd();
		if (!this.itemSalaryPeriodRepository.find( itemCd).isPresent())
			throw new BusinessException(new RawErrorMessage(" 明細書名が入力されていません。"));
		this.itemSalaryPeriodRepository.update(context.getCommand().toDomain());
	}

}
