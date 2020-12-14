package nts.uk.screen.at.ws.kmk.kmk004.j;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.screen.at.app.command.kmk.kmk004.j.DeleteFlexMonthlyWorkTimeSetShaCommand;
import nts.uk.screen.at.app.command.kmk.kmk004.j.DeleteFlexMonthlyWorkTimeSetShaCommandHandler;
import nts.uk.screen.at.app.command.kmk.kmk004.j.RegisterFlexMonthlyWorkTimeSetShaCommandHandler;
import nts.uk.screen.at.app.command.kmk.kmk004.j.UpdateFlexMonthlyWorkTimeSetShaCommandHandler;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetsha.SaveMonthlyWorkTimeSetShaCommand;
import nts.uk.screen.at.app.kmk004.g.GetFlexPredWorkTimeDto;
import nts.uk.screen.at.app.kmk004.j.AfterChangeFlexEmployeeSetting;
import nts.uk.screen.at.app.kmk004.j.AfterChangeFlexEmployeeSettingDto;
import nts.uk.screen.at.app.kmk004.j.AfterCopyFlexMonthlyWorkTimeSetSha;
import nts.uk.screen.at.app.kmk004.j.DisplayInitialFlexScreenByEmployee;
import nts.uk.screen.at.app.kmk004.j.SelectEmployeeFlex;
import nts.uk.screen.at.app.kmk004.j.SelectEmployeeFlexDto;
import nts.uk.screen.at.app.kmk004.j.SelectFlexYearByEmployee;
import nts.uk.screen.at.app.query.kmk004.common.DisplayMonthlyWorkingDto;
import nts.uk.screen.at.app.query.kmk004.common.EmployeeIdDto;

@Path("screen/at/kmk004/j")
@Produces("application/json")
public class Kmk004JWebService {

	@Inject
	private DisplayInitialFlexScreenByEmployee displayInitialFlexScreenByEmployee;

	@Inject
	private SelectEmployeeFlex selectShaFlex;

	@Inject
	private SelectFlexYearByEmployee selectFlexYearByEmployee;

	@Inject
	private RegisterFlexMonthlyWorkTimeSetShaCommandHandler registerHandler;

	@Inject
	private UpdateFlexMonthlyWorkTimeSetShaCommandHandler updateHandler;

	@Inject
	private DeleteFlexMonthlyWorkTimeSetShaCommandHandler deleteHandler;

	@Inject
	private AfterCopyFlexMonthlyWorkTimeSetSha afterCopyFlexMonthlyWorkTimeSetSha;

	@Inject
	private AfterChangeFlexEmployeeSetting afterChangeFlexEmployeeSetting;

	@POST
	@Path("init-screen")
	public GetFlexPredWorkTimeDto initScreen() {
		return this.displayInitialFlexScreenByEmployee.displayInitialFlexScreenByEmployee();
	}

	@POST
	@Path("change-year/{sId}/{year}")
	public List<DisplayMonthlyWorkingDto> changeYear(@PathParam("sId") String sId, @PathParam("sId") int year) {
		return this.selectFlexYearByEmployee.selectFlexYearByEmployee(sId, year);
	}

	@POST
	@Path("change-sId/{sId}")
	public SelectEmployeeFlexDto changeShaCd(@PathParam("sId") String sId) {
		return this.selectShaFlex.selectEmployeeFlex(sId);
	}

	@POST
	@Path("register")
	public List<EmployeeIdDto> register(SaveMonthlyWorkTimeSetShaCommand command) {
		return this.registerHandler.handle(command);
	}

	@POST
	@Path("update")
	public void update(SaveMonthlyWorkTimeSetShaCommand command) {
		this.updateHandler.handle(command);
	}

	@POST
	@Path("delete")
	public List<EmployeeIdDto> delete(DeleteFlexMonthlyWorkTimeSetShaCommand command) {
		return this.deleteHandler.handle(command);
	}

	@POST
	@Path("after-copy")
	public List<EmployeeIdDto> afterCopy() {
		return this.afterCopyFlexMonthlyWorkTimeSetSha.afterCopyFlexMonthlyWorkTimeSetSha();
	}

	@POST
	@Path("change-setting")
	public AfterChangeFlexEmployeeSettingDto changeSetting(String ShaCd) {
		return this.afterChangeFlexEmployeeSetting.afterChangeFlexEmployeeSetting(ShaCd);
	}

}
