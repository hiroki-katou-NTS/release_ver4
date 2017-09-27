/**
 * 
 */
package nts.uk.ctx.at.record.app.command.workrecord.operationsetting;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.OperationOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.OperationOfDailyPerformanceRepoInterface;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.SettingUnit;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author danpv
 *
 */
@Stateless
public class OperationSettingCommandHandler extends CommandHandler<OperationSettingCommand> {

	@Inject
	private OperationOfDailyPerformanceRepoInterface operationSettingRepo;

	@Override
	protected void handle(CommandHandlerContext<OperationSettingCommand> context) {
		String companyId = AppContexts.user().companyId();
		OperationSettingCommand command = context.getCommand();
		OperationOfDailyPerformance opSetting = operationSettingRepo
				.findOperationOfDailyPerformance(new CompanyId(companyId));
		opSetting.updateBaseInfor(EnumAdaptor.valueOf(command.getSettingUnit(), SettingUnit.class),
				command.getComment());
		operationSettingRepo.registerOperationOfDailyPerformance(opSetting);
	}

}
