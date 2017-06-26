package nts.uk.ctx.basic.app.command.organization.department;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.department.DepartmentRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RemoveHistoryCommandHandler extends CommandHandler<String> {

	@Inject
	private DepartmentRepository departmentRepository;

	@Override
	protected void handle(CommandHandlerContext<String> context) {
		String companyCode = AppContexts.user().companyCode();
		// check isExistHistory
		if (!departmentRepository.isExistHistory(companyCode, context.getCommand().toString())) {
			throw new BusinessException("ER06");
		}
		departmentRepository.removeHistory(companyCode, context.getCommand().toString());
		departmentRepository.removeMemoByHistId(companyCode, context.getCommand().toString());

	}

}
