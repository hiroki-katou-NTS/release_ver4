package nts.uk.ctx.at.shared.app.command.workrule.closure;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
public class ClosureEmpAddCommandHandler extends CommandHandler<ClousureEmpAddCommand> {

	@Inject
	private ClosureEmploymentRepository closureEmpRepo;

	@Override
	protected void handle(CommandHandlerContext<ClousureEmpAddCommand> context) {
		// Get user login, companyID.
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();

		// Get command.
		ClousureEmpAddCommand command = context.getCommand();

		// Convert to list ClousureEmploy from command
		List<ClosureEmployment> listClosureEmpDom = command.getEmpCdNameList().stream().map(item -> {
			return new ClosureEmployment(companyId, item.getCode(), item.getClosureId());
		}).collect(Collectors.toList());
		
		//Add lits ClosureEmp to server.
		closureEmpRepo.addListClousureEmp(companyId, listClosureEmpDom);
		
	}

}
