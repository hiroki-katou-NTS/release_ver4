package nts.uk.ctx.basic.app.command.organization.department;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.department.DepartmentCode;
import nts.uk.ctx.basic.dom.organization.department.DepartmentRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RemoveDepartmentCommandHandler extends CommandHandler<RemoveDepartmentCommand> {

	@Inject
	private DepartmentRepository departmentRepository;

	@Override
	protected void handle(CommandHandlerContext<RemoveDepartmentCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		// check isExistDepartment
		if (!departmentRepository.isExistDepartment(companyCode, context.getCommand().getHistoryId(),
				new DepartmentCode(context.getCommand().getDepartmentCode()))) {
			throw new BusinessException("ER06");
		}
		departmentRepository.remove(companyCode, context.getCommand().getHistoryId() , context.getCommand().getHierarchyCode());
	}

}
