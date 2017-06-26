 package nts.uk.ctx.basic.ws.organization.position;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.organization.position.DeleteHistoryCommand;
import nts.uk.ctx.basic.app.command.organization.position.DeleteHistoryCommandHandler;
import nts.uk.ctx.basic.app.command.organization.position.DeleteJobTitleRefCommand;
import nts.uk.ctx.basic.app.command.organization.position.DeleteJobTitleRefCommandHandler;
import nts.uk.ctx.basic.app.command.organization.position.DeletePositionCommand;
import nts.uk.ctx.basic.app.command.organization.position.DeletePositionCommandHandler;
import nts.uk.ctx.basic.app.command.organization.position.RegistryPositionCommand;
import nts.uk.ctx.basic.app.command.organization.position.RegistryPositionCommandHandler;
import nts.uk.ctx.basic.app.command.organization.position.UpdateHistoryCommand;
import nts.uk.ctx.basic.app.command.organization.position.UpdateHistoryCommandHandler;
import nts.uk.ctx.basic.app.find.organization.position.AuthLevelDto;
import nts.uk.ctx.basic.app.find.organization.position.AuthLevelFinder;
import nts.uk.ctx.basic.app.find.organization.position.JobHistDto;
import nts.uk.ctx.basic.app.find.organization.position.JobHistFinder;
import nts.uk.ctx.basic.app.find.organization.position.JobRefAuthDto;
import nts.uk.ctx.basic.app.find.organization.position.JobRefAuthFinder;
import nts.uk.ctx.basic.app.find.organization.position.JobTitleDto;
import nts.uk.ctx.basic.app.find.organization.position.JobTitleFinder;


@Path("basic/organization/position")
@Produces("application/json")
public class PositionWebService extends WebService {

	@Inject
	private JobTitleFinder positionFinder;
	@Inject
	private JobHistFinder histFinder;
	@Inject
	private DeletePositionCommandHandler deletePosition;
	@Inject
	private DeleteHistoryCommandHandler deleteHistoryCommandHandler;
	@Inject
	private UpdateHistoryCommandHandler updateHistoryCommandHandler;
	@Inject
	private JobRefAuthFinder jobRefAuth;
	@Inject
	private RegistryPositionCommandHandler registryPosition;
	@Inject
	private DeleteJobTitleRefCommandHandler deleteJobTitleRefCommandHandler;
	@Inject
	private AuthLevelFinder authLevelFinder;
	
	@POST
	@Path("findAllJobTitle/{historyId}")
	public List<JobTitleDto> findAllPosition(@PathParam("historyId") String historyId) {
		return this.positionFinder.findAllPosition(historyId);
	}
	/**
	 * Get All History
	 * @return
	 */
	@POST
	@Path("getAllHist")
	public List<JobHistDto> init() {
		
		return this.histFinder.init();
	}
	/**
	 * Delete JobTitle
	 * @param command
	 */
	@POST
	@Path("deleteJobTitle")
	public void deletePosition(DeletePositionCommand command) {
		this.deletePosition.handle(command);
	}
	/**
	 * Delete History
	 * @param command
	 */
	@POST
	@Path("deleteHist")
	public void deleteHistory(DeleteHistoryCommand command) {
		this.deleteHistoryCommandHandler.handle(command);
	}
	/**
	 * Update History
	 * @param command
	 */
	@POST
	@Path("updateHist")
	public void updateHistory(UpdateHistoryCommand command) {
		this.updateHistoryCommandHandler.handle(command);
	}
	/**
	 * Registry Position
	 * @param command
	 */
	@POST
	@Path("registryPosition")
	public void registryPosition(RegistryPositionCommand command) {
		this.registryPosition.handle(command); 
	/**
	 * Add JobTitle Ref
	 */
	}
	@POST
	@Path("addJobTitleRef")
	public void addJobTitleRef(RegistryPositionCommand command){
		this.registryPosition.handle(command);
	}
	/**
	 * Delete JobTitle Ref
	 * @param command
	 */
	@POST
	@Path("deleteJobTitleRef")
	public void deleteJobTitleRef(DeleteJobTitleRefCommand command) {
		this.deleteJobTitleRefCommandHandler.handle(command);
	}
	/**
	 * Get All Job Ref Auth
	 * @param historyId
	 * @param jobCode
	 * @return
	 */
	@POST
	@Path("getAllJobRefAuth/{historyId}/{jobCode}")
	public List<JobRefAuthDto> getAllJobRefAuth(@PathParam("historyId") String historyId,
					@PathParam("jobCode") String jobCode){
		return this.jobRefAuth.getAllRefAuth(historyId, jobCode);
	}
	
	@POST
	@Path("findAuth/{authScopeAtr}")
	public AuthLevelDto getCompanyByUserKtSet(
			@PathParam("authScopeAtr") String authScopeAtr){
		return this.authLevelFinder.getAuthByAuthScopeAtr(authScopeAtr)
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("Not Found")));
	}

}
