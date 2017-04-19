package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBD;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBDRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateItemDeductBDCommandHandler extends CommandHandler<UpdateItemDeductBDCommand> {

	@Inject
	private ItemDeductBDRepository itemDeductBDRepo;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemDeductBDCommand> context) {
		val itemCode = context.getCommand().getItemCode();
		val itemBreakdownCd = context.getCommand().getItemBreakdownCd();
		String companyCode = AppContexts.user().companyCode();
		ItemDeductBD itemDeductBD = context.getCommand().toDomain();
		itemDeductBD.validate();
		// Check if the data no exists
		if (!this.itemDeductBDRepo.find(companyCode, itemCode, itemBreakdownCd).isPresent())
			throw new BusinessException(" 明細書名が入力されていません。");
		this.itemDeductBDRepo.update(companyCode, itemDeductBD);
	}
}
