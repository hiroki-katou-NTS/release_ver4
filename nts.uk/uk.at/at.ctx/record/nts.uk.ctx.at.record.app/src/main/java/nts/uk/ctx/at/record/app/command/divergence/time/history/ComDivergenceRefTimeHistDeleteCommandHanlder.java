package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeRepository;

/**
 * The Class ComDivergenceRefTimeHistDeleteCommandHanlder.
 */
@Stateless
public class ComDivergenceRefTimeHistDeleteCommandHanlder extends CommandHandler<ComDivergenceRefTimeHistDeleteCommand>{
	
	/** The history repo. */
	@Inject
	private CompanyDivergenceReferenceTimeHistoryRepository historyRepo;
	
	/** The item repo. */
	@Inject
	private CompanyDivergenceReferenceTimeRepository itemRepo;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<ComDivergenceRefTimeHistDeleteCommand> context) {
		// get command
		ComDivergenceRefTimeHistDeleteCommand command = context.getCommand();
		
		//find history
		CompanyDivergenceReferenceTimeHistory find = this.historyRepo.findByHistId(command.getHistoryId());
		
		if(!find.getHistoryItems().isEmpty()) {
			//delete history
			this.historyRepo.delete(find);
			
			//find and delete all item by history
			List<CompanyDivergenceReferenceTime> listDomain = this.itemRepo.findAll(command.getHistoryId());
			listDomain.stream().forEach(item -> {
				this.itemRepo.delete(item);
			});
		}
	}

}
