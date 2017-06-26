package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBD;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBDRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author sonnlb
 *
 */
@Stateless
@Transactional
public class AddItemDeductBDCommandHandler extends CommandHandler<AddItemDeductBDCommand> {

	@Inject
	private ItemDeductBDRepository itemDeductBDRepo;

	@Override
	protected void handle(CommandHandlerContext<AddItemDeductBDCommand> context) {
		val itemCode = context.getCommand().getItemCode();
		val itemBreakdownCode = context.getCommand().getItemBreakdownCode();
		String companyCode = AppContexts.user().companyCode();
		ItemDeductBD itemDeductBD = context.getCommand().toDomain();
		itemDeductBD.validate();
		// Check if the data already exists
		if (this.itemDeductBDRepo.find(companyCode, itemCode, itemBreakdownCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemDeductBDRepo.add(companyCode, itemDeductBD);
	}
}
