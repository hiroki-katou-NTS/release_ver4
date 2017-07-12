package nts.uk.ctx.at.schedule.ws.shift.businesscalendar.specificdate;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.schedule.app.find.shift.businesscalendar.specificdate.SpecificDateItemDto;
import nts.uk.ctx.at.schedule.app.find.shift.businesscalendar.specificdate.SpecificDateItemFinder;

@Path("at/schedule/specificdateitem")
@Produces("application/json")
public class SpecificDateItemWebService extends WebService {

	@Inject
	private SpecificDateItemFinder find;
	
	@POST
	@Path("getallspecificdate")
	public List<SpecificDateItemDto> getAllSpecificDateByCompany() {
		return this.find.getAllByCompany();
	}
}