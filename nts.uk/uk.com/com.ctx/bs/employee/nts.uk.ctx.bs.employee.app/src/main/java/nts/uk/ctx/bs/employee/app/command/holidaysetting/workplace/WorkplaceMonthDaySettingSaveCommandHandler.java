package nts.uk.ctx.bs.employee.app.command.holidaysetting.workplace;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkplaceMonthDaySettingSaveCommandHandler.
 */
@Stateless
public class WorkplaceMonthDaySettingSaveCommandHandler extends CommandHandler<WorkplaceMonthDaySettingSaveCommand> {
	
	/** The repository. */
	@Inject
	private WorkplaceMonthDaySettingRepository repository;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<WorkplaceMonthDaySettingSaveCommand> context) {
		// Get Company Id
		String companyId = AppContexts.user().companyId();
		
		// Get Command
		WorkplaceMonthDaySettingSaveCommand command = context.getCommand();
		
		// convert to domain
		WorkplaceMonthDaySetting domain = new WorkplaceMonthDaySetting(command);
		
		Optional<WorkplaceMonthDaySetting> optional = this.repository.findByYear(new CompanyId(companyId), command.getWorkplaceID(), new Year(command.getYear()));
	
		// save data
		if(optional.isPresent()){
			this.repository.update(domain);
		}
		this.repository.add(domain);
	}

}
