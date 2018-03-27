package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeRepository;

/**
 * The Class WorkTypeDivergenceRefTimeHistDeleteCommandHanlder.
 */
@Stateless
public class WorkTypeDivergenceRefTimeHistDeleteCommandHanlder
		extends CommandHandler<WorkTypeDivergenceRefTimeHistDeleteCommand> {

	/** The history repo. */
	@Inject
	private WorkTypeDivergenceReferenceTimeHistoryRepository historyRepo;

	/** The item repo. */
	@Inject
	private WorkTypeDivergenceReferenceTimeRepository itemRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<WorkTypeDivergenceRefTimeHistDeleteCommand> context) {
		// get command
		WorkTypeDivergenceRefTimeHistDeleteCommand command = context.getCommand();

		// find history
		WorkTypeDivergenceReferenceTimeHistory find = this.historyRepo.findByKey(command.getHistoryId());

		if (!find.getHistoryItems().isEmpty()) {
			// delete history
			this.historyRepo.delete(find);

			// find and delete all item by history
			List<WorkTypeDivergenceReferenceTime> listDomain = this.itemRepo.findAll(command.getHistoryId(),
					new BusinessTypeCode(command.getWorkTypeCode()));
			listDomain.stream().forEach(item -> {
				this.itemRepo.delete(item);
			});
		}
	}

}
