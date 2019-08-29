package nts.uk.ctx.sys.auth.ws.wkpmanager;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.app.command.wkpmanager.RemoveWorkplaceManagerCommand;
import nts.uk.ctx.sys.auth.app.command.wkpmanager.RemoveWorkplaceManagerCommandHandler;
import nts.uk.ctx.sys.auth.app.command.wkpmanager.SaveWorkplaceManagerCommand;
import nts.uk.ctx.sys.auth.app.command.wkpmanager.SaveWorkplaceManagerCommandHandler;
import nts.uk.ctx.sys.auth.app.find.wkpmanager.WorkplaceManagerFinder;
import nts.uk.ctx.sys.auth.app.find.wkpmanager.dto.WorkplaceManagerDto;

@Path("at/auth/workplace/manager")
@Produces(MediaType.APPLICATION_JSON)
public class WorkplaceManagerWebService extends WebService {
	/** Finder */
	@Inject
	private WorkplaceManagerFinder wkpManagerFinder;
	
	/** Handler */
	@Inject
	private SaveWorkplaceManagerCommandHandler saveWkpMngHandler;
	
	@Inject
	private RemoveWorkplaceManagerCommandHandler removeWkpMngHandler;
	
	@Path("findAll/{wkpId}")
    @POST
    public List<WorkplaceManagerDto> findAllWkpManager(@PathParam("wkpId") String workplaceId) {
        return this.wkpManagerFinder.findAll(workplaceId);
    }
	
	@Path("find/loginnedUser")
    @POST
    public List<WorkplaceManagerDto> getCanManageWpkForLoginUser() {
        return this.wkpManagerFinder.getCanManageWpkForLoginUser();
    }
	
	@Path("save")
    @POST
    public JavaTypeResult<String> saveWorkplaceManager(SaveWorkplaceManagerCommand command) {
        return new JavaTypeResult<String>(this.saveWkpMngHandler.handle(command));
    }
	
	@Path("remove")
    @POST
    public void removeWorkplaceManager(RemoveWorkplaceManagerCommand command) {
        this.removeWkpMngHandler.handle(command);
    }
	
	@Path("findAllWorkplaceId")
    @POST
    public List<String> findAllWorkplaceId(GeneralDate baseDate) {
        return this.wkpManagerFinder.findAllWorkplaceId(baseDate);
    }
}
