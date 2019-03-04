package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.service.InsertHistoryCmm053Service;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class InsertHistoryCmm053CmdHandler extends CommandHandler<HistoryCmm053Command> {

	@Inject
	private InsertHistoryCmm053Service insertHistoryCmm053Service;
	
	@Override
	protected void handle(CommandHandlerContext<HistoryCmm053Command> context) {
		HistoryCmm053Command command = context.getCommand();
		String companyId             = AppContexts.user().companyId();
		String historyId             = UUID.randomUUID().toString();
		String employeeId            = command.getEmployeeId();
		GeneralDate startDate        = command.getStartDate();
		String departmentApproverId  = command.getDepartmentApproverId();
		String dailyApproverId       = command.getDailyApproverId();
		boolean dailyDisplay = Strings.isNotBlank(dailyApproverId) && !dailyApproverId.equals(departmentApproverId);
		if (!command.isHasAuthority()) {
			dailyApproverId = departmentApproverId;
		}
		this.insertHistoryCmm053Service.insertHistoryByManagerSetting(companyId, historyId, employeeId, startDate, 
				departmentApproverId, 
				Strings.isBlank(dailyApproverId) ? departmentApproverId : dailyApproverId,
				dailyDisplay);
	}
}
