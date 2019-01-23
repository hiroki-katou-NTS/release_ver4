package nts.uk.ctx.at.record.app.command.dailyimport;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyimport.DailyDataImportTempRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyDataImportTruncateDataHandler extends CommandHandler<Void>{

	@Inject
	private DailyDataImportTempRepository tempRepo;
	
	@Override
	protected void handle(CommandHandlerContext<Void> context) {
		tempRepo.truncateTable();
	}

}
