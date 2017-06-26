package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday.Payday;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class Qmm005bCommandHandler extends CommandHandler<Qmm005bCommand> {

	@Inject
	private PaydayRepository paydayRepo;

	@Override
	protected void handle(CommandHandlerContext<Qmm005bCommand> context) {
		String companyCode = AppContexts.user().companyCode();

		Qmm005bCommand commands = context.getCommand();

		for (PayDayUpdateCommand command : commands.getPayDays()) {
			Payday domain = command.toDomain(companyCode);
			Payday domainDb = paydayRepo.select3(companyCode, command.getProcessingNo(), command.getPayBonusAtr(),
					command.getProcessingYm(), command.getSparePayAtr());

			// delete bonus record
			if (domain.getPayBonusAtr().value == 0) {
				// select bonus record in db
				Payday deleteBonus = paydayRepo.select3(companyCode, command.getProcessingNo(), 1,
						command.getProcessingYm(), command.getSparePayAtr());
			
				// if record is not null
				if (deleteBonus != null) {
					boolean deleted = !commands.getPayDays().stream().anyMatch(
							m -> m.getProcessingYm() == domain.getProcessingYm().v() && m.getPayBonusAtr() == 1);
					if (deleted) {						
						paydayRepo.delete1(deleteBonus);
					}
				}
			}

			// if db record maping with domain existed.
			if (domainDb != null) {
				Payday update = Payday.createSimpleFromJavaType(companyCode, domainDb.getProcessingNo().v(),
						domainDb.getPayBonusAtr().value, domainDb.getProcessingYm().v(),
						domainDb.getSparePayAtr().value, domain.getPayDate(), domain.getStdDate(),
						domain.getAccountingClosing(), domain.getSocialInsLevyMon().v(), domain.getSocialInsStdDate(),
						domain.getIncomeTaxStdDate(), domain.getNeededWorkDay().v(), domain.getEmpInsStdDate(),
						domain.getStmtOutputMon().v());

				// update existed record in db
				paydayRepo.update1(update);
			} else {
				// insert new record (domain) to db
				paydayRepo.insert1(domain);
			}
		}
	}
}
