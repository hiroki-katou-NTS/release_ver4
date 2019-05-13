package nts.uk.ctx.at.function.app.command.processexecution;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;

/**
 * BatchServerAPIによってWebServiceから実行する場合、すぐResponseを返すためにAsyncにしたい。
 * そうしないと、呼び出し元のサーバがずっと待つことになってしまう。
 * それを避けるために、AsyncCommandHandlerを使う。
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AsyncSortingProcessCommandHandler extends AsyncCommandHandler<ScheduleExecuteCommand> {

	@Inject
	private SortingProcessCommandHandler sortingProcessCommandHandler;
	
	@Override
	protected void handle(CommandHandlerContext<ScheduleExecuteCommand> context) {
		this.sortingProcessCommandHandler.handle(context.getCommand());
	}

}
