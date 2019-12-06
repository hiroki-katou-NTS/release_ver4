package nts.uk.ctx.at.record.ws.reservation;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.query.reservation.ReservationDateParam;
import nts.uk.ctx.at.record.app.query.reservation.ReservationDto;
import nts.uk.ctx.at.record.app.query.reservation.ReservationQuery;

@Path("at/record/reservation/bento")
@Produces("application/json")
public class ReservationService extends WebService{
	/** The finder.*/
	@Inject 
	private ReservationQuery finder;
	
	@POST
	@Path("find")
	public ReservationDto findOrder(ReservationDateParam param) {
		return this.finder.findAll(param);
	}
}
