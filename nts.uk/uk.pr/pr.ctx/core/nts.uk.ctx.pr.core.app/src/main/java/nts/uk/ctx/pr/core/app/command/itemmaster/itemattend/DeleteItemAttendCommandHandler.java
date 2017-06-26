package nts.uk.ctx.pr.core.app.command.itemmaster.itemattend;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttendRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class DeleteItemAttendCommandHandler extends CommandHandler<DeleteItemAttendCommand> {

	@Inject
	ItemAttendRespository itemAttendRespository;

	@Override
	protected void handle(CommandHandlerContext<DeleteItemAttendCommand> context) {
		val companyCode = AppContexts.user().companyCode();
		if (!this.itemAttendRespository.find(companyCode, context.getCommand().getItemCode()).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemAttendRespository.delete(companyCode, context.getCommand().getItemCode());

	}

}
