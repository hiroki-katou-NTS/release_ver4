package nts.uk.ctx.basic.ws.system.bank;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.system.bank.branch.AddBranchCommand;
import nts.uk.ctx.basic.app.command.system.bank.branch.AddBranchCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.branch.RemoveBranchCommand;
import nts.uk.ctx.basic.app.command.system.bank.branch.RemoveBranchCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.branch.TranferBranchCommand;
import nts.uk.ctx.basic.app.command.system.bank.branch.TranferBranchCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.branch.UpdateBranchCommand;
import nts.uk.ctx.basic.app.command.system.bank.branch.UpdateBranchCommandHandler;

@Path("basic/system/bank/branch")
@Produces("application/json")
public class BranchWebService extends WebService {
	@Inject
	private AddBranchCommandHandler addBranchCommandHandler;
	
	@Inject
	private UpdateBranchCommandHandler updateBranchCommandHandler;
	
	@Inject
	private RemoveBranchCommandHandler removeBranchCommandHandler;
	
	@Inject
	private TranferBranchCommandHandler transferBranchCommandHandler; 
    
	/**
	 * add bank branch
	 * @param command
	 */
	@POST
	@Path("add")
	public void add(AddBranchCommand command){
	   this.addBranchCommandHandler.handle(command);
	}
	 /**
	  * update bank branch
	  * @param command
	  */
	@POST
	@Path("update")
	public void update(UpdateBranchCommand command){
	   this.updateBranchCommandHandler.handle(command);
	}
	
	/**
	 * remove bank branch
	 * @param command
	 */
	@POST
	@Path("remove")
	public void remove(RemoveBranchCommand command){
		this.removeBranchCommandHandler.handle(command);		
	}
	
	/**
	 * transfer bank branch
	 * @param command
	 */
	@POST
	@Path("transfer")
	public void update(TranferBranchCommand command){
	   this.transferBranchCommandHandler.handle(command);
	}
}
