package nts.uk.ctx.at.record.app.command.divergence.time.history;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ComDivergenceRefTimeHistSaveCommandHandler.
 */
@Stateless
public class ComDivergenceRefTimeHistSaveCommandHandler extends CommandHandler<ComDivergenceRefTimeHistSaveCommand> {

	private final static int NEW_MOE = 0;

	/** The history repo. */
	@Inject
	private CompanyDivergenceReferenceTimeHistoryRepository historyRepo;

	/** The item repo. */
	@Inject
	private CompanyDivergenceReferenceTimeRepository itemRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<ComDivergenceRefTimeHistSaveCommand> context) {
		// get company Id
		String companyId = AppContexts.user().companyId();

		BundledBusinessException exceptions = BundledBusinessException.newInstance();

		// get command
		ComDivergenceRefTimeHistSaveCommand command = context.getCommand();

		// validate start date , end date
		if (GeneralDate.fromString(command.getStartDate(), "yyyy/MM/dd")
				.compareTo(GeneralDate.fromString(command.getEndDate(), "yyyy/MM/dd")) > 0) {
			exceptions.addMessage("Msg_917");
			exceptions.throwExceptions();
		}

		// find duplicate history
		DatePeriod period = new DatePeriod(GeneralDate.fromString(command.getStartDate(), "yyyy/MM/dd"),
				GeneralDate.fromString(command.getEndDate(), "yyyy/MM/dd"));
		Integer count = this.historyRepo.countByDatePeriod(companyId, period, command.getHistoryId());

		// convert to domain
		CompanyDivergenceReferenceTimeHistory domain = new CompanyDivergenceReferenceTimeHistory(command);

		// find
		CompanyDivergenceReferenceTimeHistory find = this.historyRepo
				.findByHistId(domain.getHistoryItems().get(0).identifier());

		// check and save
		if (find.getHistoryItems().isEmpty()) {
			// validate duplicate history
			if (count.intValue() > 0) {
				exceptions.addMessage("Msg_106");
				exceptions.throwExceptions();
			}

			if (command.getIsCopyData() == NEW_MOE) {
				// create history
				this.historyRepo.add(domain);
				// make default data for Company DivergenceReference Time
				this.itemRepo.addDefaultDataWhenCreateHistory(domain.getHistoryItems().get(0).identifier());
			} else {
				// find latest data to copy
				CompanyDivergenceReferenceTimeHistory latestHist = this.historyRepo.findLatestHist(companyId);
				// create history
				this.historyRepo.add(domain);
				this.itemRepo.copyDataFromLatestHistory(latestHist.getHistoryItems().get(0).identifier(),
						domain.getHistoryItems().get(0).identifier());
			}
		} else {
			// validate duplicate history
			if (count.intValue() > 0) {
				exceptions.addMessage("Msg_107");
				exceptions.throwExceptions();
			}
			// update history
			this.historyRepo.update(domain);
		}

	}

}
