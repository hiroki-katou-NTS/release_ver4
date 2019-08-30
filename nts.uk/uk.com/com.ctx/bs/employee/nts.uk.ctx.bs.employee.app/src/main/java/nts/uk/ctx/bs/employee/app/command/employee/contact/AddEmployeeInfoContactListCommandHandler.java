package nts.uk.ctx.bs.employee.app.command.employee.contact;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.bs.employee.dom.employee.contact.EmployeeInfoContact;
import nts.uk.ctx.bs.employee.dom.employee.contact.EmployeeInfoContactRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;
@Stateless
public class AddEmployeeInfoContactListCommandHandler extends CommandHandlerWithResult<List<AddEmployeeInfoContactCommand>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddEmployeeInfoContactCommand>{
	@Inject
	private EmployeeInfoContactRepository employeeInfoContactRepository;
	@Override
	public String targetCategoryCd() {
		return "CS00023";
	}

	@Override
	public Class<?> commandClass() {
		return AddEmployeeInfoContactCommand.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(CommandHandlerContext<List<AddEmployeeInfoContactCommand>> context) {
		List<AddEmployeeInfoContactCommand> cmd = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<EmployeeInfoContact> domains = cmd.stream().map(c ->{return new EmployeeInfoContact(cid, c.getSid(), c.getMailAddress(),
				c.getSeatDialIn(), c.getSeatExtensionNo(), c.getPhoneMailAddress(),
				c.getCellPhoneNo());}).collect(Collectors.toList());
		if(!domains.isEmpty()) {
			employeeInfoContactRepository.addAll(domains);
			return cmd.stream().map(c -> {return new PeregAddCommandResult(c.getSid());}).collect(Collectors.toList());
		}
		
		return null;
	}

}
