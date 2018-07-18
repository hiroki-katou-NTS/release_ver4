package nts.uk.ctx.exio.app.command.exo.condset;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.exio.dom.exo.condset.StandardAttr;
import nts.uk.ctx.exio.dom.exo.condset.StdOutputCondSetService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class OutSetContentCommandHandler extends CommandHandler<StdOutputCondSetCommand>{

	@Inject
	private StdOutputCondSetService stdOutputCondSetService;
	
	@Override
	protected void handle(CommandHandlerContext<StdOutputCondSetCommand> context) {
		String userId = AppContexts.user().userId();
		String condSetCd = context.getCommand().getConditionSetCd();
		String outItemCd = new String ();
		boolean isStandType = false;
		if (context.getCommand().getStandType() == StandardAttr.STANDARD.value) {
			isStandType = true;
		} 
		stdOutputCondSetService.outputAcquisitionItemList(condSetCd, userId, outItemCd, isStandType, false);
	}

}
