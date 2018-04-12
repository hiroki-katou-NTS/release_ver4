package nts.uk.ctx.at.function.app.command.holidaysremaining;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.holidaysremaining.HolidaysRemainingManagement;

@Stateless
@Transactional
public class RemoveHdRemainManageCommandHandler extends CommandHandler<HdRemainManageCommand>
{
    
    @Inject
    private HolidaysRemainingManagement removeHolidaysRemainingManagement;
    
    @Override
    protected void handle(CommandHandlerContext<HdRemainManageCommand> context) {
    }
}
