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

@Transactional
@Stateless
public class UpdateItemAttendCommandHandler extends CommandHandler<UpdateItemAttendCommand> {

	@Inject
	ItemAttendRespository itemAttendRespository;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemAttendCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		val itemCode = context.getCommand().getItemCode();
		if (!this.itemAttendRespository.find(companyCode, itemCode).isPresent())
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		this.itemAttendRespository.update(companyCode, context.getCommand().toDomain());

	}

}
