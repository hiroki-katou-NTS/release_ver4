package nts.uk.ctx.pr.core.ws.retirement.payitem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.retirement.payitem.UpdateRetirementPayItemCommand;
import nts.uk.ctx.pr.core.app.command.retirement.payitem.UpdateRetirementPayItemCommandHandler;
import nts.uk.ctx.pr.core.app.find.retirement.payitem.RetirementPayItemFinder;
import nts.uk.ctx.pr.core.app.find.retirement.payitem.dto.RetirementPayItemDto;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Path("pr/core/retirement/payitem")
@Produces("application/json")
public class RetirementPayItemWebService extends WebService{
	@Inject
	private RetirementPayItemFinder retirementPayItemFinder;
	
	@Inject
	private UpdateRetirementPayItemCommandHandler updateRetirementPayItemCommandHandler;
	
	@POST
	@Path("findBycompanyCode")
	public List<RetirementPayItemDto> findBycompanyCode() {
		return this.retirementPayItemFinder.findByCompanyCode();
	}
	
	@POST
	@Path("update")
	public void update(UpdateRetirementPayItemCommand command) {
		this.updateRetirementPayItemCommandHandler.handle(command);
	}
}
