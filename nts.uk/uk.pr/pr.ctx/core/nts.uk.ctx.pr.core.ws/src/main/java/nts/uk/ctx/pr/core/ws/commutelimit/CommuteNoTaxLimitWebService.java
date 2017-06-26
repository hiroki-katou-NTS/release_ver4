package nts.uk.ctx.pr.core.ws.commutelimit;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.DeleteCommuteNoTaxLimitCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.DeleteCommuteNoTaxLimitCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.InsertCommuteNoTaxLimitCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.InsertCommuteNoTaxLimitCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.UpdateCommuteNoTaxLimitCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit.UpdateCommuteNoTaxLimitCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.commutelimit.CommuteNoTaxLimitDto;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.commutelimit.CommuteNoTaxLimitFinder;

@Path("core/commutelimit")
@Produces("application/json")
public class CommuteNoTaxLimitWebService extends WebService {

	@Inject
	private CommuteNoTaxLimitFinder finder;
	@Inject
	private InsertCommuteNoTaxLimitCommandHandler insertData;
	@Inject
	private UpdateCommuteNoTaxLimitCommandHandler updateDate;
	@Inject
	private DeleteCommuteNoTaxLimitCommandHandler deleteData;

	@POST
	@Path("find/bycompanycode")
	public List<CommuteNoTaxLimitDto> getListCommuteNoTaxLimitByCompanyCode() {
		return finder.getListCommuteNoTaxLimitByCompanyCode();
	}
	
	@POST
	@Path("getCommuteNoTaxLimit/{commuNoTaxLimitCode}")
	public CommuteNoTaxLimitDto getCommuteNoTaxLimit(@PathParam("commuNoTaxLimitCode") String commuNoTaxLimitCode) {
		return finder.getCommuteNoTaxLimit(commuNoTaxLimitCode);
	}
	
	@POST
	@Path("insert")
	public void insert(InsertCommuteNoTaxLimitCommand insertCommand) {
		this.insertData.handle(insertCommand);
	}

	@POST
	@Path("update")
	public void update(UpdateCommuteNoTaxLimitCommand updateCommand) {
		this.updateDate.handle(updateCommand);
	}

	@POST
	@Path("delete")
	public void delete(DeleteCommuteNoTaxLimitCommand deleteCommand) {
		this.deleteData.handle(deleteCommand);
	}
}
