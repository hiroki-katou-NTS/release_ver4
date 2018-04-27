package nts.uk.ctx.at.function.app.command.annualworkschedule;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.annualworkschedule.SetOutItemsWoSc;
import nts.uk.ctx.at.function.dom.annualworkschedule.repository.SetOutItemsWoScRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateSetOutItemsWoScCommandHandler extends CommandHandler<SetOutItemsWoScCommand>
{
    
    @Inject
    private SetOutItemsWoScRepository repository;
    
    @Override
    protected void handle(CommandHandlerContext<SetOutItemsWoScCommand> context) {
    	String cid = AppContexts.user().companyId();
        SetOutItemsWoScCommand updateCommand = context.getCommand();
        repository.update(new SetOutItemsWoSc(cid, updateCommand.getCd(), updateCommand.getName(), updateCommand.getOutNumExceedTime36Agr(), updateCommand.getDisplayFormat()));
    
    }
}
