package nts.uk.ctx.bs.employee.app.command.empfilemanagement;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.EmpFileManagementRepository;
import nts.uk.ctx.bs.employee.dom.empfilemanagement.EmployeeFileManagement;

@Stateless
@Transactional
public class UpdateCtgDocFileCommandHandler extends CommandHandler<UpdateCtgDocFileDocumentFileCommand>{
	
	@Inject
	private EmpFileManagementRepository empFileManagementRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateCtgDocFileDocumentFileCommand> context) {
		
		UpdateCtgDocFileDocumentFileCommand command = context.getCommand();
		
		Optional<EmployeeFileManagement> domain = empFileManagementRepository.getEmpMana(command.getFileId());
		if (domain.isPresent()) {
			EmployeeFileManagement emp = domain.get();
			emp.setPersonInfoCategoryId(command.getPersonInfoCategoryIdNew());
			empFileManagementRepository.update(emp);
		}
		
		
	}

}
