package nts.uk.ctx.sys.portal.app.command.flowmenu;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.portal.dom.flowmenu.DefClassAtr;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenu;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenuRepository;
import nts.uk.ctx.sys.portal.dom.flowmenu.service.FlowMenuService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author hieult
 */
@Stateless
@Transactional
public class DeleteFlowMenuCommandHandler extends CommandHandler<DeleteFlowMenuCommand> {

	@Inject
	private FlowMenuRepository flowMenuRepository;

	@Inject
	private FlowMenuService flowMenuService;
	
	@Override
	protected void handle(CommandHandlerContext<DeleteFlowMenuCommand> context) {		
		String companyID = AppContexts.user().companyId();
		String topPagePartId = context.getCommand().getToppagePartID();
		//check topPagePartId is exit
		Optional<FlowMenu> getFlowMenu = flowMenuRepository.getFlowMenu(companyID, topPagePartId);		
		if(!getFlowMenu.isPresent()){
			throw new BusinessException("ER026");
		}
		if(getFlowMenu.get().getDefClassAtr() == DefClassAtr.Default){
			throw new BusinessException("Msg_76");
		}
		
		flowMenuService.deleteFlowMenu(companyID, topPagePartId);
	}
	
}
