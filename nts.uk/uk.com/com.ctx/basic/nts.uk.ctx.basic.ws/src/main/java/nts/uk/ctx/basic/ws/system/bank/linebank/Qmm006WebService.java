package nts.uk.ctx.basic.ws.system.bank.linebank;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.system.bank.linebank.AddLineBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.linebank.LineBankCommandBase;
import nts.uk.ctx.basic.app.command.system.bank.linebank.RemoveLineBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.linebank.RemoveLineBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.linebank.TransferLineBankCommand;
import nts.uk.ctx.basic.app.command.system.bank.linebank.TransferLineBankCommandHandler;
import nts.uk.ctx.basic.app.command.system.bank.linebank.UpdateLineBankCommandHandler;
import nts.uk.ctx.basic.app.find.system.bank.linebank.LineBankDto;
import nts.uk.ctx.basic.app.find.system.bank.linebank.LineBankFinder;

@Path("basic/system/bank/linebank")
@Produces("application/json")
public class Qmm006WebService extends WebService {
	@Inject
	private AddLineBankCommandHandler addLineBankCommandHandler;

	@Inject
	private LineBankFinder lineBankFinder;

	@Inject
	private UpdateLineBankCommandHandler updateLineBankCommandHandler;

	@Inject
	private RemoveLineBankCommandHandler removeLineBankCommandHandler;

	@Inject
	private TransferLineBankCommandHandler transferLineBankCommandHandler;

	@POST
	@Path("add")
	public void add(LineBankCommandBase command) {
		this.addLineBankCommandHandler.handle(command);
	}

	@POST
	@Path("findAll")
	public List<LineBankDto> findAll() {
		return this.lineBankFinder.findAll();
	}

	@POST
	@Path("update")
	public void update(LineBankCommandBase command) {
		this.updateLineBankCommandHandler.handle(command);
	}

	@POST
	@Path("remove")
	public void remove(RemoveLineBankCommand command) {
		this.removeLineBankCommandHandler.handle(command);
	}

	@POST
	@Path("transfer")
	public void transfer(TransferLineBankCommand command) {
		this.transferLineBankCommandHandler.handle(command);
	}

	@POST
	@Path("find/{lineBankCode}")
	public LineBankDto find(@PathParam("lineBankCode") String lineBankCode) {
		return this.lineBankFinder.find(lineBankCode).get();
	}

}
