package nts.uk.ctx.pr.core.ws.rule.employment.averagepay;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.employment.averagepay.RegisterAveragePayCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.averagepay.RegisterAveragePayCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.averagepay.UpdateAveragePayCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.averagepay.UpdateAveragePayCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.employment.averagepay.AveragePayFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.averagepay.dto.AveragePayDto;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Path("pr/core/averagepay")
@Produces("application/json")
public class AveragePayWebService extends WebService {
	
	@Inject
	private RegisterAveragePayCommandHandler registerAveragePayCommandHandler;
	
	@Inject
	private UpdateAveragePayCommandHandler updateAveragePayCommandHandler;
	
	@Inject
	private AveragePayFinder averagePayFinder;
	
	@POST
	@Path("register")
	public void register(RegisterAveragePayCommand command) {
		this.registerAveragePayCommandHandler.handle(command);
	}
	
	@POST
	@Path("update")
	public void update(UpdateAveragePayCommand command) {
		this.updateAveragePayCommandHandler.handle(command);
	}
	
	@POST
	@Path("findByCompanyCode")
	public AveragePayDto findByCompanyCode() {
		return this.averagePayFinder.findByCompanyCode();
	}
}
