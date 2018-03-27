package nts.uk.ctx.at.record.app.command.divergence.time.history;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkTypeDivergenceRefTimeHistSaveCommandHandler.
 */
@Stateless
public class WorkTypeDivergenceRefTimeHistSaveCommandHandler
		extends CommandHandler<WorkTypeDivergenceRefTimeHistSaveCommand> {
	
	private final static int NEW_MOE = 0;

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
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.
	 * CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<WorkTypeDivergenceRefTimeHistSaveCommand> context) {
		// get company Id
		String companyId = AppContexts.user().companyId();

		BundledBusinessException exceptions = BundledBusinessException.newInstance();

		// get command
		WorkTypeDivergenceRefTimeHistSaveCommand command = context.getCommand();

		// validate start date , end date
		if (GeneralDate.fromString(command.getStartDate(), "yyyy/MM/dd")
				.compareTo(GeneralDate.fromString(command.getEndDate(), "yyyy/MM/dd")) > 0) {
			exceptions.addMessage("Msg_917");
			exceptions.throwExceptions();
		}

		// find duplicate history
		DatePeriod period = new DatePeriod(GeneralDate.fromString(command.getStartDate(), "yyyy/MM/dd"),
				GeneralDate.fromString(command.getEndDate(), "yyyy/MM/dd"));
		Integer count = this.historyRepo.countByDatePeriod(companyId, new BusinessTypeCode(command.getWorkTypeCodes()),
				period, command.getHistoryId());

		// check and save
		if (StringUtils.isEmpty(command.getHistoryId())) {
			// convert to domain
			WorkTypeDivergenceReferenceTimeHistory domain = new WorkTypeDivergenceReferenceTimeHistory(command);

			// validate start date , end date
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
				WorkTypeDivergenceReferenceTimeHistory latestHist = this.historyRepo.findLatestHist(companyId,
						new BusinessTypeCode(command.getWorkTypeCodes()));
				// create history
				this.historyRepo.add(domain);
				this.itemRepo.copyDataFromLatestHistory(latestHist.getHistoryItems().get(0).identifier(),
						domain.getHistoryItems().get(0).identifier());
			}
		} else {
			// validate start date , end date
			if (count.intValue() > 0) {
				exceptions.addMessage("Msg_107");
				exceptions.throwExceptions();
			}
			// find
			WorkTypeDivergenceReferenceTimeHistory find = this.historyRepo.findByKey(command.getHistoryId());

			// convert to domain
			find.setHistoryItems(command.getHistoryItems());

			// update history
			this.historyRepo.update(find);
		}
	}

}
