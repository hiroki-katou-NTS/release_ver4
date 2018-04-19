package nts.uk.ctx.at.function.ws.holidaysremaining;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.function.app.command.holidaysremaining.AddHdRemainManageCommandHandler;
import nts.uk.ctx.at.function.app.command.holidaysremaining.HdRemainManageCommand;
import nts.uk.ctx.at.function.app.command.holidaysremaining.RemoveHdRemainManageCommandHandler;
import nts.uk.ctx.at.function.app.command.holidaysremaining.UpdateHdRemainManageCommandHandler;
import nts.uk.ctx.at.function.app.find.holidaysremaining.HdRemainManageDto;
import nts.uk.ctx.at.function.app.find.holidaysremaining.HdRemainManageFinder;

@Path("at/function/holidaysremaining")
@Produces("application/json")
public class HolidaysRemainingWebService extends WebService {

	/* Finder */
	@Inject
	private HdRemainManageFinder hdRemainManageFinder;
	
	@Inject
	private AddHdRemainManageCommandHandler addHdRemainManageCommandHandler;
	
	@Inject
	private UpdateHdRemainManageCommandHandler updateHdRemainManageCommandHandler;
	
	@Inject
	private RemoveHdRemainManageCommandHandler removeHdRemainManageCommandHandler;
	

	@POST
	@Path("findAll")
	public List<HdRemainManageDto> getHdRemainManageList() {
		return this.hdRemainManageFinder.findAll();
	}
	
	@POST
	@Path("add")
	public void addHdRemainManage(HdRemainManageCommand comand) {
		this.addHdRemainManageCommandHandler.handle(comand);
	}
	
	@POST
	@Path("update")
	public void updateHdRemainManage(HdRemainManageCommand comand) {
		this.updateHdRemainManageCommandHandler.handle(comand);
	}
	
	@POST
	@Path("remove")
	public void removerHdRemainManage(HdRemainManageCommand comand) {
		this.removeHdRemainManageCommandHandler.handle(comand);
	}

}
