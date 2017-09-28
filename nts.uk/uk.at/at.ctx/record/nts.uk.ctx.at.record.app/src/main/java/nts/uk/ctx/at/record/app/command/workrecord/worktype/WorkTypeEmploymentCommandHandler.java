/**
 * 
 */
package nts.uk.ctx.at.record.app.command.workrecord.worktype;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.workrecord.workingtype.ChangeableWorktypeGroup;
import nts.uk.ctx.at.record.dom.workrecord.workingtype.WorkingTypeChangedByEmployment;
import nts.uk.ctx.at.record.dom.workrecord.workingtype.WorkingTypeChangedByEmploymentRepoInterface;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author danpv
 *
 */
@Stateless
public class WorkTypeEmploymentCommandHandler extends CommandHandler<WorkTypeEmploymentCommand> {

	@Inject
	private WorkingTypeChangedByEmploymentRepoInterface workTypeRepo;

	@Override
	protected void handle(CommandHandlerContext<WorkTypeEmploymentCommand> context) {
		WorkTypeEmploymentCommand command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		String empCode = AppContexts.user().employeeCode();
		List<ChangeableWorktypeGroup> changeableWorkTypeGroups = command.getGroups().stream()
				.map(group -> new ChangeableWorktypeGroup(group.getNo(), group.getName(), group.getWorkTypeList()))
				.collect(Collectors.toList());
		WorkingTypeChangedByEmployment workingType = new WorkingTypeChangedByEmployment(new CompanyId(companyId),
				new EmploymentCode(empCode), changeableWorkTypeGroups);
		workTypeRepo.saveWorkingTypeChangedByEmployment(workingType);
	}

}
