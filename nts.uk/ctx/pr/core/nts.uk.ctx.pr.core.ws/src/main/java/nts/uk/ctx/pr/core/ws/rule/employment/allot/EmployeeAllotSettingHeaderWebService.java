package nts.uk.ctx.pr.core.ws.rule.employment.allot;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.command.rule.employment.allot.DeleteEmployeeAllotHeaderCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.DeleteEmployeeAllotHeaderCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.employee.EmployeeAllotSettingHeaderDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.employee.EmployeeAllotSettingHeaderFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("pr/core/allot")
@Produces("application/json")
public class EmployeeAllotSettingHeaderWebService {
	@Inject
	private EmployeeAllotSettingHeaderFinder find;

	@Inject
	private DeleteEmployeeAllotHeaderCommandHandler deleteEAHCommandHandler;

	@POST
	@Path("findallemployeeallotheader")
	public List<EmployeeAllotSettingHeaderDto> GetAllEmployeeAllotSettingHeader() {
		String companyCode = AppContexts.user().companyCode();

		return this.find.getEmployeeAllotSettingHeader(companyCode);
	}

	@POST
	@Path("findallemployeeallotheadermax")
	public Integer getAllotHMax() {
		String companyCode = AppContexts.user().companyCode();

		Optional<Integer> value = this.find.getAllotHMax(companyCode);

		if (value.isPresent()) {
			return value.get();
		} else {
			return -1;
		}
	}

	@POST
	@Path("deleteallotheader")
	public void deleteAllotHeader(DeleteEmployeeAllotHeaderCommand command) {
		deleteEAHCommandHandler.handle(command);
	}
}
