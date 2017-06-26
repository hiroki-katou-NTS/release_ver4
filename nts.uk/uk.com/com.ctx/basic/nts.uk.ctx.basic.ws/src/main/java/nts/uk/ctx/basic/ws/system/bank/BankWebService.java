package nts.uk.ctx.basic.ws.system.bank;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.system.bank.AddBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.AddBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.RemoveBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.RemoveBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.RemoveListBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.RemoveListBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.UpdateBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.UpdateBankCommandHandler;
import nts.uk.ctx.basic.app.find.system.bank.BankFinder;
import nts.uk.ctx.basic.app.find.system.bank.dto.BankDto;

@Path("basic/system/bank")
@Produces("application/json")
public class BankWebService extends WebService {
	@Inject
	private AddBankCommandHandler addBankCommandHandler;

	@Inject
	private UpdateBankCommandHandler updateBankCommandHandler;
	
	@Inject
	private RemoveBankCommandHandler removeBankCommandHandler;
	
	@Inject
	private BankFinder bankFinder;
	
	@Inject
	private RemoveListBankCommandHandler removeListBankCommandHandler;
	
	/**
	 * add bank
	 * @param command
	 */
	@POST
	@Path("add")
	public void add(AddBankCommand command) {
		this.addBankCommandHandler.handle(command);
	}
    
	/**
	 * update bank
	 * @param command
	 */
	@POST
	@Path("update")
	public void update(UpdateBankCommand command) {
		this.updateBankCommandHandler.handle(command);
	}
	
	/**
	 * remove bank
	 * @param command
	 */
	@POST
	@Path("remove")
	public void remove(RemoveBankCommand command) {
		this.removeBankCommandHandler.handle(command);
	}
	
	/**
	 * find all bank
	 * @return
	 */
	@POST
	@Path("find/all")
	public List<BankDto> findAll() {
		return this.bankFinder.findAll();
	}
	
	/**
	 * remove list bank
	 * @param command
	 */
	@POST
	@Path("remove/list")
	public void removeList(RemoveListBankCommand command){
		this.removeListBankCommandHandler.handle(command);
	}
	
	/**
	 * find check exist bank
	 */
	@POST
	@Path("find/check")
	public void check() {
       this.bankFinder.checkExistsBank();
	}
}
