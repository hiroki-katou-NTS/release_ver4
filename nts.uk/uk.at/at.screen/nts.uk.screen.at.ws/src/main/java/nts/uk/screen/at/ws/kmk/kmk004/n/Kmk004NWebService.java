package nts.uk.screen.at.ws.kmk.kmk004.n;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetemp.SaveMonthlyWorkTimeSetEmpCommand;
import nts.uk.screen.at.app.command.kmk.kmk004.n.DeleteTransMonthlyWorkTimeSetEmpCommand;
import nts.uk.screen.at.app.command.kmk.kmk004.n.DeleteTransMonthlyWorkTimeSetEmpCommandHandler;
import nts.uk.screen.at.app.command.kmk.kmk004.n.RegisterTransMonthlyWorkTimeSetEmpCommandHandler;
import nts.uk.screen.at.app.command.kmk.kmk004.n.UpdateTransMonthlyWorkTimeSetEmpCommandHandler;
import nts.uk.screen.at.app.kmk004.n.DeforLaborMonthTimeEmpDto;
import nts.uk.screen.at.app.kmk004.n.DisplayDeforBasicSettingByEmployment;
import nts.uk.screen.at.app.kmk004.n.DisplayInitialDeforScreenByEmployment;
import nts.uk.screen.at.app.kmk004.n.DisplayInitialDeforScreenByEmploymentDto;
import nts.uk.screen.at.app.kmk004.n.SelectEmploymentDefor;
import nts.uk.screen.at.app.kmk004.n.SelectEmploymentDeforDto;

/**
 * 
 * @author tutt
 *
 */
@Path("screen/at/kmk004")
@Produces("application/json")
public class Kmk004NWebService {
	
	@Inject
	private RegisterTransMonthlyWorkTimeSetEmpCommandHandler registerHandler;
	
	@Inject
	private UpdateTransMonthlyWorkTimeSetEmpCommandHandler updateHandler;
	
	@Inject
	private DeleteTransMonthlyWorkTimeSetEmpCommandHandler deleteHandler;

	@Inject
	private DisplayDeforBasicSettingByEmployment dislaySetting;
	
	@Inject
	private DisplayInitialDeforScreenByEmployment initScreen;
	
	@Inject
	private SelectEmploymentDefor select;
	
	@POST
	@Path("viewN/monthlyWorkTimeSet/add")
	public void registerMonthlyWorkTimeSet(SaveMonthlyWorkTimeSetEmpCommand command) {
		registerHandler.handle(command);
	}
	
	@POST
	@Path("viewN/monthlyWorkTimeSet/update")
	public void updateMonthlyWorkTimeSet(SaveMonthlyWorkTimeSetEmpCommand command) {
		updateHandler.handle(command);
	}
	
	@POST
	@Path("viewN/monthlyWorkTimeSet/delete")
	public void deleteMonthlyWorkTimeSet(DeleteTransMonthlyWorkTimeSetEmpCommand command) {
		deleteHandler.handle(command);
	}
	
	@POST
	@Path("viewN/getBasicSetting/{empCode}")
	public DeforLaborMonthTimeEmpDto getBasicSetting(@PathParam("empCode") String empCode) {
		return dislaySetting.displayDeforBasicSettingByEmployment(empCode);
	}
	
	@POST
	@Path("viewN/selectEmp/{empCode}")
	public SelectEmploymentDeforDto selectWkp(@PathParam("empCode") String empCode) {
		return select.selectEmploymentDefor(empCode);
	}

	@POST
	@Path("viewN/initScreen")
	public DisplayInitialDeforScreenByEmploymentDto initScreen() {
		return initScreen.displayInitialDeforScreenByEmployment() ;
	}
}
