package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.StandardDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.SystemDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.PaydayProcessing;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday.StandardDay;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday.SystemDay;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class Qmm005dCommandHandler extends CommandHandler<Qmm005dCommand> {

	@Inject
	private SystemDayRepository systemDayRepo;

	@Inject
	private StandardDayRepository standardDayRepo;

	@Inject
	private PaydayProcessingRepository paydayProcessingRepo;

	@Override
	protected void handle(CommandHandlerContext<Qmm005dCommand> context) {
		Qmm005dCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		try {
			PaydayProcessing paydayProcessingDomain = command.toPaydayProcessingDomain(companyCode);
			PaydayProcessing paydayProcessingDomainUpdate = paydayProcessingRepo.selectOne(companyCode,
					paydayProcessingDomain.getProcessingNo().v());

			if (paydayProcessingDomainUpdate != null) {
				PaydayProcessing domain = PaydayProcessing.createSimpleFromJavaType(companyCode,
						paydayProcessingDomainUpdate.getProcessingNo().v(),
						paydayProcessingDomain.getProcessingName().v(), paydayProcessingDomain.getDispSet().value,
						paydayProcessingDomainUpdate.getCurrentProcessingYm().v(),
						paydayProcessingDomainUpdate.getBonusAtr().value,
						paydayProcessingDomainUpdate.getBCurrentProcessingYm().v());

				paydayProcessingRepo.update2(domain);
			}

			SystemDay systemDayDomain = command.toSystemDayDomain(companyCode);
			SystemDay systemDayDomainUpdate = systemDayRepo.select1(companyCode, systemDayDomain.getProcessingNo().v());

			if (systemDayDomainUpdate != null) {
				SystemDay domain = SystemDay.createSimpleFromJavaType(companyCode,
						systemDayDomainUpdate.getProcessingNo().v(), systemDayDomain.getSocialInsLevyMonAtr().value,
						systemDayDomain.getPickupStdMonAtr().value, systemDayDomain.getPickupStdDay().v(),
						systemDayDomain.getPayStdDay().v(), systemDayDomain.getAccountDueMonAtr().value,
						systemDayDomain.getAccountDueDay().v(), systemDayDomain.getPayslipPrintMonthAtr().value);
				systemDayRepo.update1(domain);
			}

			StandardDay standardDay = command.toStandardDayDomain(companyCode);
			StandardDay standardDayUpdate = standardDayRepo.select1(companyCode, standardDay.getProcessingNo().v());
			if (standardDayUpdate != null) {
				StandardDay domain = StandardDay.createSimpleFromJavaType(companyCode,
						standardDayUpdate.getProcessingNo().v(), standardDay.getSocialInsStdYearAtr().value,
						standardDay.getSocialInsStdMon().v(), standardDay.getSocialInsStdDay().v(),
						standardDay.getIncometaxStdYearAtr().value, standardDay.getIncometaxStdMon().v(),
						standardDay.getIncometaxStdDay().v(), standardDay.getEmpInsStdMon().v(),
						standardDay.getEmpInsStdDay().v());
				standardDayRepo.update1(domain);
			}

		} catch (Exception ex) {
			throw ex;
		}
	}

}
