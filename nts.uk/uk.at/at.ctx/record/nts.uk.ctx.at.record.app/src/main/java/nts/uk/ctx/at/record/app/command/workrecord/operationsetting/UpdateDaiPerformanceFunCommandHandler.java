package nts.uk.ctx.at.record.app.command.workrecord.operationsetting;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFunRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;

@Stateless
@Transactional
public class UpdateDaiPerformanceFunCommandHandler extends CommandHandler<DaiPerformanceFunCommand>
{
    
    @Inject
    private DaiPerformanceFunRepository repository;
    
    @Override
    protected void handle(CommandHandlerContext<DaiPerformanceFunCommand> context) {
        DaiPerformanceFunCommand updateCommand = context.getCommand();
        repository.update(DaiPerformanceFun.createFromJavaType(updateCommand.getCid(), updateCommand.getComment(), 
        		updateCommand.isDisp36Atr() ? 1 : 0, 
        		updateCommand.isFlexDispAtr() ? 1 : 0, 
				updateCommand.isCheckErrRefDisp()? 1 : 0));
    
    }
}
