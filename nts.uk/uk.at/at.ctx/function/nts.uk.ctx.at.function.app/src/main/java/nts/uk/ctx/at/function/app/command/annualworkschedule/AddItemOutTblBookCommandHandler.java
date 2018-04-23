package nts.uk.ctx.at.function.app.command.annualworkschedule;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBookRepository;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBook;

@Stateless
@Transactional
public class AddItemOutTblBookCommandHandler extends CommandHandler<ItemOutTblBookCommand>
{
    
    @Inject
    private ItemOutTblBookRepository repository;
    
    @Override
    protected void handle(CommandHandlerContext<ItemOutTblBookCommand> context) {
        ItemOutTblBookCommand addCommand = context.getCommand();
        repository.add(new ItemOutTblBook(addCommand.getCid(), addCommand.getCd(), addCommand.getSetOutCd(), addCommand.getItemOutCd(), addCommand.getUseClass(), addCommand.getValOutFormat()));
    
    }
}
