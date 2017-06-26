package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBDRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author sonnlb
 *
 */
@Stateless
@Transactional
public class DeleteItemDeductBDCommandHandler extends CommandHandler<DeleteItemDeductBDCommand> {
	@Inject
	private ItemDeductBDRepository itemDeductBDRepository;

	@Override
	protected void handle(CommandHandlerContext<DeleteItemDeductBDCommand> context) {
		val itemCode = context.getCommand().getItemCode();
		val itemBreakdownCode = context.getCommand().getItemBreakdownCode();
		String companyCode = AppContexts.user().companyCode();
		// Check if the data no exists
		if (!this.itemDeductBDRepository.find(companyCode, itemCode, itemBreakdownCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemDeductBDRepository.delete(companyCode, itemCode, itemBreakdownCode);
	}
}
