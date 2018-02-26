package nts.uk.ctx.exio.app.command.exi.execlog;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.exio.dom.exi.execlog.ExacErrorLogRepository;
import nts.uk.ctx.exio.dom.exi.execlog.ExacErrorLog;

@Stateless
@Transactional
public class UpdateExacErrorLogCommandHandler extends CommandHandler<ExacErrorLogCommand>
{
    
    @Inject
    private ExacErrorLogRepository repository;
    
    @Override
    protected void handle(CommandHandlerContext<ExacErrorLogCommand> context) {
        ExacErrorLogCommand updateCommand = context.getCommand();
        repository.update(ExacErrorLog.createFromJavaType(0L, updateCommand.getLogSeqNumber(), updateCommand.getCid(), updateCommand.getExternalProcessId(), updateCommand.getCsvErrorItemName(), updateCommand.getCsvAcceptedValue(), updateCommand.getErrorContents(), updateCommand.getRecordNumber(), updateCommand.getLogRegDateTime(), updateCommand.getItemName(), updateCommand.getErrorAtr()));
    
    }
}
