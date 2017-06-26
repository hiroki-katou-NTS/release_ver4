package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeduct;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeductRespository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author sonnlb
 *
 */
@Stateless
@Transactional
public class DeleteItemDeductCommandHandler extends CommandHandler<DeleteItemDeductCommand> {

	@Inject
	ItemDeductRespository itemDeductRespository;

	@Override
	protected void handle(CommandHandlerContext<DeleteItemDeductCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		val itemCode = context.getCommand().getItemCode();
		if (!this.itemDeductRespository.find(companyCode, itemCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemDeductRespository.delete(companyCode, context.getCommand().getItemCode());

	}

}
