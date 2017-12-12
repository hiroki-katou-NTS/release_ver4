package nts.uk.ctx.bs.employee.app.command.employee.deletemanagement;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDeletionAttr;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeCode;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.BusinessName;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.PersonNameGroup;

@Stateless
@Transactional
public class RestoreDataEmpCommandHandler extends CommandHandler<EmployeeDeleteToRestoreCommand> {

	@Inject
	PersonRepository personRepo;

	@Inject
	private EmployeeDataMngInfoRepository empDataMngRepo;

	@Override
	protected void handle(CommandHandlerContext<EmployeeDeleteToRestoreCommand> context) {

		EmployeeDeleteToRestoreCommand command = context.getCommand();

		if (command != null) {
			List<EmployeeDataMngInfo> listEmpData = empDataMngRepo.findByEmployeeId(command.getId());

			if (!listEmpData.isEmpty()) {
				EmployeeDataMngInfo empInfo = listEmpData.get(0);
				empInfo.setEmployeeCode(new EmployeeCode(command.getNewCode().toString()));
				empInfo.setDeletedStatus(EmployeeDeletionAttr.NOTDELETED);
				empInfo.setDeleteDateTemporary(null);
				empInfo.setRemoveReason(null);

				empDataMngRepo.updateRemoveReason(empInfo);

				// get Person
				Person person = personRepo.getByPersonId(empInfo.getPersonId()).get();
				PersonNameGroup nameGroup = person.getPersonNameGroup();
				nameGroup.setBusinessName(new BusinessName(command.getNewName()));
				person.setPersonNameGroup(nameGroup);
				personRepo.update(person);
			}
		}
	}

}
