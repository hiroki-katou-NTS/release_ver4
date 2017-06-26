package nts.uk.ctx.at.record.ws.divergencetime;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergencetime.AddDivergenceReasonCommand;
import nts.uk.ctx.at.record.app.command.divergencetime.AddDivergenceReasonCommandHandler;
import nts.uk.ctx.at.record.app.command.divergencetime.DeleteDivergenceReasonCommand;
import nts.uk.ctx.at.record.app.command.divergencetime.DeleteDivergenceReasonCommandHandler;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceItemSetCommand;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceItemSetCommandHandler;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceReasonCommand;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceReasonCommandHandler;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceTimeCommand;
import nts.uk.ctx.at.record.app.command.divergencetime.UpdateDivergenceTimeCommandHandler;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceItemSetDto;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceItemSetFinder;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceReasonDto;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceReasonFinder;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergencetime.DivergenceTimeFinder;

@Path("at/record/divergencetime")
@Produces("application/json")
public class DivergenceTimeWebService extends WebService{

	@Inject
	private DivergenceTimeFinder getAllDivTime;
	@Inject
	private UpdateDivergenceTimeCommandHandler updateDivTime;
	@Inject
	private DivergenceReasonFinder getAllDivReason;
	@Inject
	private DeleteDivergenceReasonCommandHandler delDivReason;
	@Inject
	private AddDivergenceReasonCommandHandler addDivReason;
	@Inject
	private UpdateDivergenceReasonCommandHandler updateDivReason;
	@Inject
	private DivergenceItemSetFinder getItemSet;
	@Inject
	private UpdateDivergenceItemSetCommandHandler updateItemId;
	/**
	 * get all divergence time
	 * @return
	 */
	@POST
	@Path("getalldivtime")
	public List<DivergenceTimeDto> getAllDivTime(){
		return this.getAllDivTime.getAllDivTime();
	}
	/**
	 * update divergence time
	 * @param command
	 */
	@POST
	@Path("updatedivtime")
	public void updateDivTime(UpdateDivergenceTimeCommand command){
		this.updateDivTime.handle(command);
	}
	/**
	 * get all divergence reason
	 * @param divTimeId
	 * @return
	 */
	@POST
	@Path("getalldivreason/{divTimeId}")
	public List<DivergenceReasonDto> getAllDivReason(@PathParam("divTimeId") String divTimeId){
		return this.getAllDivReason.getAllDivReasonByCode(divTimeId);
	}
	/**
	 * add divergence reason
	 * @param command
	 */
	@POST
	@Path("adddivreason")
	public void addDivReason(AddDivergenceReasonCommand command){
		this.addDivReason.handle(command);
	}
	/**
	 * update divergence reason
	 * @param command
	 */
	@POST
	@Path("updatedivreason")
	public void updateDivReason(UpdateDivergenceReasonCommand command){
		this.updateDivReason.handle(command);
	}
	/**
	 * delete divergence reason
	 * @param command
	 */
	@POST
	@Path("deletedivreason")
	public void deleteDivReason(DeleteDivergenceReasonCommand command){
		this.delDivReason.handle(command);
	}
	/**
	 * get item set
	 * @param divTimeId
	 * @return
	 */
	@POST
	@Path("getitemset/{divTimeId}")
	public List<DivergenceItemSetDto> getItemSet(@PathParam("divTimeId") String divTimeId){
		return this.getItemSet.getAllDivReasonByCode(divTimeId);
	}
	/**
	 * update time item id
	 * @param command
	 */
	@POST
	@Path("updateTimeItemId")
	public void updateTimeItemId(List<UpdateDivergenceItemSetCommand> command){
		this.updateItemId.handle(command);
	}
}
