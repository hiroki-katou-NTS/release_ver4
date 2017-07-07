package nts.uk.ctx.at.shared.app.command;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.SpecBPTimesheetRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class SpecBPTimesheetDeleteCommandHandler extends CommandHandler<List<SpecBPTimesheetDeleteCommand>>  {
	@Inject
	private SpecBPTimesheetRepository specBPTimesheetRepository;
	@Override
	protected void handle(CommandHandlerContext<List<SpecBPTimesheetDeleteCommand>> context) {
		String companyId = AppContexts.user().companyId();
		List<SpecBPTimesheetDeleteCommand> lstSpecBPTimesheetDeleteCommand = context.getCommand();
		 specBPTimesheetRepository.removeListTimesheet(companyId,
				new BonusPaySettingCode(lstSpecBPTimesheetDeleteCommand.get(0).bonusPaySettingCode),
					lstSpecBPTimesheetDeleteCommand.stream().map(c -> toSpecBonusPayTimesheetDomain(c)).collect(Collectors.toList()));
	}
	private SpecBonusPayTimesheet toSpecBonusPayTimesheetDomain(SpecBPTimesheetDeleteCommand specBPTimesheetDeleteCommand) {
		return SpecBonusPayTimesheet.createFromJavaType(specBPTimesheetDeleteCommand.timeSheetNO,
				specBPTimesheetDeleteCommand.useAtr, specBPTimesheetDeleteCommand.timeItemId,
				Long.valueOf(specBPTimesheetDeleteCommand.startTime), Long.valueOf(specBPTimesheetDeleteCommand.endTime),
				specBPTimesheetDeleteCommand.roundingTimeAtr, specBPTimesheetDeleteCommand.roundingAtr,
				specBPTimesheetDeleteCommand.specialDateItemNO);

	}

}
