package nts.uk.ctx.at.shared.ws.calculation.holiday;
/**
 * @author phongtq
 */
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.command.calculation.holiday.roundingmonth.AddRoundingMonthCommand;
import nts.uk.ctx.at.shared.app.command.calculation.holiday.roundingmonth.AddRoundingMonthCommandHandler;
import nts.uk.ctx.at.shared.app.find.calculation.holiday.roundingmonth.RoundingMonthDto;
import nts.uk.ctx.at.shared.app.find.calculation.holiday.roundingmonth.RoundingMonthFinder;

@Path("shared/caculation/holiday/rounding")
@Produces("application/json")
public class RoundingMonthWebService extends WebService{
	@Inject
	private RoundingMonthFinder finder;
	@Inject
	private AddRoundingMonthCommandHandler handler;
	
	@Path("findByCid")
	@POST
	public List<RoundingMonthDto> findByCid(String itemTimeId) {
		return finder.findAllRounding(itemTimeId);
	}
	
	@Path("add")
	@POST
	public void add(List<AddRoundingMonthCommand> command) {
		this.handler.handle(command);
	}
	
	@Path("update")
	@POST
	public void update(List<AddRoundingMonthCommand> command) {
		this.handler.handle(command);
	}
}
