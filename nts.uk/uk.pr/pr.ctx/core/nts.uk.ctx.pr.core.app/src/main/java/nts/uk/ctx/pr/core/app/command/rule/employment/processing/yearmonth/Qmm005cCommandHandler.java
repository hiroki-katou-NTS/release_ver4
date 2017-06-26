package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.StandardDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.SystemDayRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class Qmm005cCommandHandler extends CommandHandler<Qmm005cCommand> {

	@Inject
	private PaydayRepository paydayRepo;

	@Inject
	private SystemDayRepository systemDayRepo;

	@Inject
	private StandardDayRepository standardDayRepo;

	@Inject
	private PaydayProcessingRepository paydayProcessingRepo;

	@Override
	protected void handle(CommandHandlerContext<Qmm005cCommand> context) {
		Qmm005cCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		try {
			systemDayRepo.insert(command.toSystemDayDomain(companyCode));
			standardDayRepo.insert1(command.toStandardDayDomain(companyCode));

			for (PayDayInsertCommand paydayCommand : command.getPayDays()) {
				paydayRepo.insert1(paydayCommand.toDomain(companyCode));
			}

			paydayProcessingRepo.insert1(command.toPaydayProcessingDomain(companyCode));
		} catch (Exception ex) {
			throw ex;
		}

	}
}
